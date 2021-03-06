/*
 * Scriptographer
 *
 * This file is part of Scriptographer, a Scripting Plugin for Adobe Illustrator
 * http://scriptographer.org/
 *
 * Copyright (c) 2002-2010, Juerg Lehni
 * http://scratchdisk.com/
 *
 * All rights reserved. See LICENSE file for details.
 *
 * File created on 24.03.2005.
 */

package com.scriptographer.ai;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.scratchdisk.util.SoftIntMap;
import com.scriptographer.ScriptographerException;

/**
 * @author lehni
 */
abstract class NativeObject implements ValidationObject {
	// used for storing the native handle for this object
	protected int handle;
	
	protected NativeObject() {
		handle = 0;
	}
	
	protected NativeObject(int handle) {
		this.handle = handle;
	}

	protected static NativeObject wrapHandle(Class cls, int handle,
			Document document) {
		if (handle == 0)
			return null;
		WrapFactory factory = factories.get(cls);
		if (factory == null) {
			factory = new WrapFactory(cls);
			factories.put(cls, factory);
		}
		return factory.wrapHandle(handle, document);
	}

	protected static NativeObject wrapHandle(Class cls, int handle) {
		return wrapHandle(cls, handle, null);
	}

	protected boolean nativeRemove() {
		return false;
	}

	/**
	 * protected scaffold function for removing. subclasses that want to use it
	 * need to override nativeRemove and make remove public
	 * 
	 */
	protected boolean remove() {
		boolean ret = false;
		if (handle != 0) {
			ret = nativeRemove();
			if (ret) {
				WrapFactory factory = factories.get(getClass());
				if (factory != null)
					factory.wrappers.remove(handle);
				handle = 0;
			}
		}
		return ret;
	}

	public int hashCode() {
		// Return the native handle here as hashCode, as we use equals() in
		// quite a few places to see if wrapper objects are actually
		// representing the same native object. For example this is used when
		// reusing live effects and menu groups / items after the plug-in was
		// reloaded.
		return handle != 0 ? handle : super.hashCode();
	}

	public boolean isValid() {
		return handle != 0;
	}
	
	public boolean equals(Object obj) {
		// Some objects subclass NativeObject and do not use handle,
		// use a fallback scenario for these!
		if (handle == 0) {
			return this == obj;
		} else if (obj instanceof NativeObject) {
			return this == obj || 
				handle == ((NativeObject) obj).handle
					&& getClass().equals(obj.getClass());
		}
		return false;
	}

	/**
	 * @jshide
	 */
	public Object getId() {
		return "@" + Integer.toHexString(hashCode());
	}

	public String toString() {
		return getClass().getSimpleName() + " "
				+ (isValid() ? getId() : "<invalid>");
	}

	// Cache the factories for the various wrapper classes which use this base class
	private static HashMap<Class, WrapFactory> factories
			= new HashMap<Class, WrapFactory>();

	private static class WrapFactory {
		// Use a SoftIntMap to keep track of already wrapped objects per factory:
		SoftIntMap<NativeObject> wrappers = new SoftIntMap<NativeObject>();

		boolean hasDocument;
		Constructor ctor;

		WrapFactory(Class<?> cls) {
			// Get the constructor for this class
			try {
				// Use the (int, Document) constructor for DocumentObjects,
				// (int) otherwise
				hasDocument = DocumentObject.class.isAssignableFrom(cls);
				ctor = cls.getDeclaredConstructor(hasDocument
						? new Class[] { Integer.TYPE, Document.class }
						: new Class[] { Integer.TYPE });
			} catch (Exception e) {
				throw new ScriptographerException(e);
			}
		}

		NativeObject wrapHandle(int handle, Document document) {
			if (handle == 0)
				return null;
			NativeObject obj = wrappers.get(handle);
			if (obj == null) {
				try {
					// Now create a new instance, passing the handle as a
					// parameter
					obj = (NativeObject) ctor.newInstance(hasDocument
							? new Object[] { handle, document != null 
									? document 
									: Document.getWorkingDocument() }
							: new Object[] { handle });
					wrappers.put(handle, obj);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return obj;
		}
	}
}
