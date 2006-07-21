#include "StdHeaders.h"
#include "ScriptographerEngine.h"
#include "aiGlobals.h"
#include "com_scriptographer_ai_TextStory.h"

/*
 * com.scriptographer.ai.TextStory
 */

using namespace ATE;

/*
 * void finailze()
 */
JNIEXPORT void JNICALL Java_com_scriptographer_ai_TextStory_finailze(JNIEnv *env, jobject obj) {
	try {
		StoryRef story = (StoryRef) gEngine->getIntField(env, obj, gEngine->fid_AIObject_handle);
		if (story != NULL)
			sStory->Release(story);
	} EXCEPTION_CONVERT(env)
}

/*
 * int getIndex()
 */
JNIEXPORT jint JNICALL Java_com_scriptographer_ai_TextStory_getIndex(JNIEnv *env, jobject obj) {
	try {
		StoryRef story = gEngine->getStoryRef(env, obj);
		ASInt32 index;
		if (!sStory->GetIndex(story, &index))
			return index;
	} EXCEPTION_CONVERT(env)
	return -1;
}

/*
 * com.scriptographer.ai.TextRange nativeGetRange()
 */
JNIEXPORT jobject JNICALL Java_com_scriptographer_ai_TextStory_nativeGetRange(JNIEnv *env, jobject obj) {
	try {
		StoryRef story = gEngine->getStoryRef(env, obj);
		TextRangeRef range;
		if (!sStory->GetTextRange_ForThisStory(story, &range))
			return gEngine->wrapTextRangeRef(env, range);
	} EXCEPTION_CONVERT(env)
	return NULL;
}

/*
 * com.scriptographer.ai.TextRange getRange(int start, int end)
 */
JNIEXPORT jobject JNICALL Java_com_scriptographer_ai_TextStory_getRange(JNIEnv *env, jobject obj, jint start, jint end) {
	try {
		StoryRef story = gEngine->getStoryRef(env, obj);
		TextRangeRef range;
		if (!sStory->GetTextRange(story, start, end, &range))
			return gEngine->wrapTextRangeRef(env, range);
	} EXCEPTION_CONVERT(env)
	return NULL;
}

/*
 * com.scriptographer.ai.TextRange getSelection()
 */
JNIEXPORT jobject JNICALL Java_com_scriptographer_ai_TextStory_getSelection(JNIEnv *env, jobject obj) {
	try {
		StoryRef story = gEngine->getStoryRef(env, obj);
		TextRangesRef ranges;
		if (!sStory->GetTextSelection(story, &ranges))
			return TextRange_convertTextRanges(env, ranges);
	} EXCEPTION_CONVERT(env)
	return NULL;
}

/*
 * boolean equals(java.lang.Object story)
 */
JNIEXPORT jboolean JNICALL Java_com_scriptographer_ai_TextStory_equals(JNIEnv *env, jobject obj, jobject story) {
	try {
		if (env->IsInstanceOf(story, gEngine->cls_TextStory)) {
			StoryRef story1 = gEngine->getStoryRef(env, obj);
			StoryRef story2 = gEngine->getStoryRef(env, story);
			if (story2 != NULL) {
				bool ret;
				if (!sStory->IsEqual(story1, story2, &ret))
					return ret;
			}
		}
	} EXCEPTION_CONVERT(env)
	return JNI_FALSE;
}

/*
 * int nativeGetTexListLength(int handle)
 */
JNIEXPORT jint JNICALL Java_com_scriptographer_ai_TextStory_nativeGetTexListLength(JNIEnv *env, jobject obj, jint handle) {
	try {
		// determine amount of TextFrames by looping through the iterator
		TextFramesIteratorRef framesRef;
		if (!sStory->GetTextFramesIterator((StoryRef) handle, &framesRef)) {
			ITextFramesIterator frames(framesRef);
			int length = 0;
			while (frames.IsNotDone()) {
				frames.Next();
				length++;
			}
			return length;
		}
	} EXCEPTION_CONVERT(env)
	return 0;
}

/*
 * com.scriptographer.ai.Text nativeGetTextFrame(int handle, int index)
 */
JNIEXPORT jobject JNICALL Java_com_scriptographer_ai_TextStory_nativeGetTextFrame(JNIEnv *env, jobject obj, jint handle, jint index) {
	try {
		TextFrameRef textRef;
		AIArtHandle textHandle;
		if (!sStory->GetFrame((StoryRef) handle, index, &textRef) && !sAITextFrame->GetAITextFrame(textRef, &textHandle))
			return gEngine->wrapArtHandle(env, textHandle);
	} EXCEPTION_CONVERT(env)
	return NULL;
}

/*
 * int getLength()
 */
JNIEXPORT jint JNICALL Java_com_scriptographer_ai_TextStory_getLength(JNIEnv *env, jobject obj) {
	try {
		StoryRef story = gEngine->getStoryRef(env, obj);
		ASInt32 length;
		if (!sStory->GetSize(story, &length))
			return length;
	} EXCEPTION_CONVERT(env)
	return 0;
}

/*
 * void nativeSuspendReflow(int handle)
 */
JNIEXPORT void JNICALL Java_com_scriptographer_ai_TextStory_nativeSuspendReflow(JNIEnv *env, jclass cls, jint handle) {
	try {
		sStory->SuspendReflow((StoryRef) handle);
	} EXCEPTION_CONVERT(env)
}

/*
 * void nativeResumeReflow(int handle)
 */
JNIEXPORT void JNICALL Java_com_scriptographer_ai_TextStory_nativeResumeReflow(JNIEnv *env, jclass cls, jint handle) {
	try {
		sStory->ResumeReflow((StoryRef) handle);
	} EXCEPTION_CONVERT(env)
}