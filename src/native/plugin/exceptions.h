/*
 * Scriptographer
 *
 * This file is part of Scriptographer, a Plugin for Adobe Illustrator.
 *
 * Copyright (c) 2002-2010 Juerg Lehni, http://www.scratchdisk.com.
 * All rights reserved.
 *
 * Please visit http://scriptographer.org/ for updates and contact.
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
 */

#define kExceptionErr 'EXPT';

class ScriptographerException: public std::exception {
public:
	virtual void convert(JNIEnv *env);
	virtual char *toString(JNIEnv *env);
	void report(JNIEnv *env);
};

class StringException: public ScriptographerException {
protected:
	char *m_message;
	
public:
	StringException(const char *message, ...);
	void convert(JNIEnv *env);
	char *toString(JNIEnv *env);
	
	~StringException() throw() {
		delete m_message;
	}
};

class JObjectException: public StringException {
public:
	JObjectException(JNIEnv *env, const char *message, jobject object);
};

class ASErrException: public ScriptographerException {
private:
	ASErr m_error;
	
public:
	ASErrException(ASErr error);
	void convert(JNIEnv *env);
	char *toString(JNIEnv *env);
};

class JThrowableException: public ScriptographerException {
private:
	jthrowable m_throwable;	
	
public:
	JThrowableException(jthrowable throwable);
	void convert(JNIEnv *env);
	char *toString(JNIEnv *env);
};

class JThrowableClassException: public ScriptographerException {
private:
	jclass m_class;
		
public:
	JThrowableClassException(jclass cls);
	JThrowableClassException(JNIEnv *env, const char *name);
	void convert(JNIEnv *env);
	char *toString(JNIEnv *env);
};
