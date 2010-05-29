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

#include "stdHeaders.h"
#include "ScriptographerEngine.h"
#include "com_scriptographer_ui_Tracker.h"

/*
 * com.scriptographer.ui.Tracker
 */

/*
 * int getCurrentModifiers()
 */
JNIEXPORT jint JNICALL Java_com_scriptographer_ui_Tracker_getCurrentModifiers(JNIEnv *env, jclass cls) {
	try {
		return (jint)sADMTracker->GetModifiers(NULL);
	} EXCEPTION_CONVERT(env);
	return 0;
}

/*
 * void abort()
 */
JNIEXPORT void JNICALL Java_com_scriptographer_ui_Tracker_abort(JNIEnv *env, jobject obj) {
	try {
		ADMTrackerRef tracker = gEngine->getTrackerHandle(env, obj);
		sADMTracker->Abort(tracker);
	} EXCEPTION_CONVERT(env);
}

/*
 * void releaseMouseCapture()
 */
JNIEXPORT void JNICALL Java_com_scriptographer_ui_Tracker_releaseMouseCapture(JNIEnv *env, jobject obj) {
	try {
		ADMTrackerRef tracker = gEngine->getTrackerHandle(env, obj);
		sADMTracker->ReleaseMouseCapture(tracker);
	} EXCEPTION_CONVERT(env);
}
