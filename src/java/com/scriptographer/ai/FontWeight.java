/*
 * Scriptographer
 * 
 * This file is part of Scriptographer, a Plugin for Adobe Illustrator.
 * 
 * Copyright (c) 2002-2008 Juerg Lehni, http://www.scratchdisk.com.
 * All rights reserved.
 *
 * Please visit http://scriptographer.com/ for updates and contact.
 * 
 * -- GPL LICENSE NOTICE --
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * -- GPL LICENSE NOTICE --
 * 
 * File created on 04.11.2005.
 * 
 * $Id$
 */

package com.scriptographer.ai;

/**
 * @author lehni
 */
public class FontWeight extends NativeObject {

	public static final FontWeight NONE = new FontWeight(0);

	protected FontWeight(int handle) {
		super(handle);
	}

	private native String nativeGetName(int handle);

	public String getName() {
		return handle == 0 ? "None" : nativeGetName(handle);
	}

	private native int nativeGetFamily(int handle);

	public FontFamily getFamily() {
		return FontFamily.wrapHandle(nativeGetFamily(handle));
	}

	private native int nativeGetIndex(int handle);

	public int getIndex() {
		return handle == 0 ? -1 : nativeGetIndex(handle);
	}

	protected static FontWeight wrapHandle(int handle) {
		return (FontWeight) (handle == 0 ? null : wrapHandle(FontWeight.class, handle));
	}

	private native boolean nativeIsValid(int handle);

	public boolean isValid() {
		return handle == 0 ? false : nativeIsValid(handle);
	}

	public String toString() {
		return getFamily() + " " + getName();
	}
}
