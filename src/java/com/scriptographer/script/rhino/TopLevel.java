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
 * 
 * File created on 06.03.2005.
 * 
 * $Id$
 */

package com.scriptographer.script.rhino;

import java.io.File;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

import com.scratchdisk.script.Scope;
import com.scratchdisk.script.Script;
import com.scratchdisk.script.ScriptEngine;
import com.scratchdisk.script.ScriptException;
import com.scratchdisk.script.rhino.ExtendedJavaClass;
import com.scriptographer.ai.Annotator;
import com.scriptographer.ai.AreaText;
import com.scriptographer.ai.Artboard;
import com.scriptographer.ai.CMYKColor;
import com.scriptographer.ai.CharacterStyle;
import com.scriptographer.ai.Color;
import com.scriptographer.ai.CompoundPath;
import com.scriptographer.ai.Curve;
import com.scriptographer.ai.Dictionary;
import com.scriptographer.ai.Document;
import com.scriptographer.ai.DocumentList;
import com.scriptographer.ai.DocumentView;
import com.scriptographer.ai.FileFormat;
import com.scriptographer.ai.FillStyle;
import com.scriptographer.ai.FontFamily;
import com.scriptographer.ai.FontList;
import com.scriptographer.ai.FontWeight;
import com.scriptographer.ai.Gradient;
import com.scriptographer.ai.GradientColor;
import com.scriptographer.ai.GradientStop;
import com.scriptographer.ai.GrayColor;
import com.scriptographer.ai.Group;
import com.scriptographer.ai.HitResult;
import com.scriptographer.ai.Layer;
import com.scriptographer.ai.LiveEffect;
import com.scriptographer.ai.LiveEffectParameters;
import com.scriptographer.ai.Matrix;
import com.scriptographer.ai.ParagraphStyle;
import com.scriptographer.ai.Path;
import com.scriptographer.ai.PathStyle;
import com.scriptographer.ai.PathText;
import com.scriptographer.ai.Pathfinder;
import com.scriptographer.ai.Pattern;
import com.scriptographer.ai.PatternColor;
import com.scriptographer.ai.PlacedFile;
import com.scriptographer.ai.PlacedSymbol;
import com.scriptographer.ai.PointText;
import com.scriptographer.ai.RGBColor;
import com.scriptographer.ai.Raster;
import com.scriptographer.ai.Segment;
import com.scriptographer.ai.StrokeStyle;
import com.scriptographer.ai.Swatch;
import com.scriptographer.ai.Symbol;
import com.scriptographer.ai.TextRange;
import com.scriptographer.ai.TextStory;
import com.scriptographer.ai.Tool;
import com.scriptographer.ai.ToolEventHandler;
import com.scriptographer.ai.Tracing;
import com.scriptographer.sg.Application;
import com.scriptographer.sg.Scriptographer;
import com.scriptographer.ui.Border;
import com.scriptographer.ui.Button;
import com.scriptographer.ui.ChasingArrows;
import com.scriptographer.ui.CheckBox;
import com.scriptographer.ui.Dial;
import com.scriptographer.ui.Dialog;
import com.scriptographer.ui.DialogColor;
import com.scriptographer.ui.DialogGroupInfo;
import com.scriptographer.ui.Drawer;
import com.scriptographer.ui.FloatingDialog;
import com.scriptographer.ui.FontInfo;
import com.scriptographer.ui.Frame;
import com.scriptographer.ui.HierarchyListBox;
import com.scriptographer.ui.HierarchyListEntry;
import com.scriptographer.ui.Image;
import com.scriptographer.ui.ImageButton;
import com.scriptographer.ui.ImageCheckBox;
import com.scriptographer.ui.ImagePane;
import com.scriptographer.ui.ImageRadioButton;
import com.scriptographer.ui.ItemGroup;
import com.scriptographer.ui.Key;
import com.scriptographer.ui.ListBox;
import com.scriptographer.ui.ListEntry;
import com.scriptographer.ui.ListItem;
import com.scriptographer.ui.MenuGroup;
import com.scriptographer.ui.MenuItem;
import com.scriptographer.ui.ModalDialog;
import com.scriptographer.ui.Palette;
import com.scriptographer.ui.PopupDialog;
import com.scriptographer.ui.PopupList;
import com.scriptographer.ui.PopupMenu;
import com.scriptographer.ui.ProgressBar;
import com.scriptographer.ui.RadioButton;
import com.scriptographer.ui.ScrollBar;
import com.scriptographer.ui.Slider;
import com.scriptographer.ui.Spacer;
import com.scriptographer.ui.SpinEdit;
import com.scriptographer.ui.TextEdit;
import com.scriptographer.ui.TextPane;
import com.scriptographer.ui.TextValueItem;
import com.scriptographer.ui.ToggleItem;
import com.scriptographer.ui.Tracker;
/**
 * @author lehni
 */
public class TopLevel extends com.scratchdisk.script.rhino.TopLevel {

	final static Class classes[] = {
		// AI, alphabetically
		Annotator.class,
		AreaText.class,
		Artboard.class,
		CharacterStyle.class,
		Color.class,
		CMYKColor.class,
		DialogColor.class,
		CompoundPath.class,
		Curve.class,
		Dictionary.class,
		Document.class,
		DocumentView.class,
		FileFormat.class,
		FillStyle.class,
		FontFamily.class,
		FontWeight.class,
		Gradient.class,
		GradientColor.class,
		GradientStop.class,
		GrayColor.class,
		Group.class,
		HitResult.class,
		com.scriptographer.ai.Item.class,
		Layer.class,
		LiveEffect.class,
		LiveEffectParameters.class,
		Matrix.class,
		ParagraphStyle.class,
		Path.class,
		Pathfinder.class,
		PathStyle.class,
		PathText.class,
		Pattern.class,
		PatternColor.class,
		PlacedFile.class,
		com.scriptographer.ai.Point.class,
		PointText.class,
		Raster.class,
		com.scriptographer.ai.Rectangle.class,
		RGBColor.class,
		Segment.class,
		com.scriptographer.ai.Size.class,
		StrokeStyle.class,
		Swatch.class,
		Symbol.class,
		PlacedSymbol.class,
		com.scriptographer.ai.TextItem.class,
		TextRange.class,
		TextStory.class,
		Tool.class,
		ToolEventHandler.class,
		Tracing.class,

		// UI, alphabetically
		Border.class,
		Button.class,
		ChasingArrows.class,
		Dial.class,
		CheckBox.class,
		Dialog.class,
		DialogGroupInfo.class,
		Drawer.class,
		FloatingDialog.class,
		FontInfo.class,
		Frame.class,
		HierarchyListBox.class,
		HierarchyListEntry.class,
		Image.class,
		ImageButton.class,
		ImageCheckBox.class,
		ImageRadioButton.class,
		ImagePane.class,
		ItemGroup.class,
		Key.class,
		ListBox.class,
		ListEntry.class,
		ListItem.class,
		MenuGroup.class,
		MenuItem.class,
		ModalDialog.class,
		Palette.class,
		PopupDialog.class,
		PopupList.class,
		PopupMenu.class,
		ProgressBar.class,
		RadioButton.class,
		ScrollBar.class,
		Slider.class,
		Spacer.class,
		SpinEdit.class,
		TextPane.class,
		TextEdit.class,
		TextValueItem.class,
		ToggleItem.class,
		Tracker.class
	};

	public TopLevel(Context context) {
		super(context);

		// define classes. the createPrototypes flag is set so
		// the classes' constructors can now whether an object
		// is created as prototype or as real object through
		// isCreatingPrototypes()

		for (int i = 0; i < classes.length; i++) {
			ExtendedJavaClass cls = new ExtendedJavaClass(this, classes[i], true);
			// Put it in the global scope:
			ScriptableObject.defineProperty(this, cls.getClassName(), cls,
				ScriptableObject.PERMANENT | ScriptableObject.READONLY
					| ScriptableObject.DONTENUM);
		}

		// Define some global functions and objects:
		String[] names = { "include", "execute", "mapJavaClass" };
		defineFunctionProperties(names, TopLevel.class,
			ScriptableObject.READONLY | ScriptableObject.DONTENUM);

		// Properties:

		// Define the global reference here, for scripts that get executed
		// directly in the TopLevel scope (libraries)
		// This is overridden by RhinoEngine#createScope for all other scopes.
		defineProperty("global", this,
				ScriptableObject.READONLY | ScriptableObject.DONTENUM);

		defineProperty("documents", DocumentList.getInstance(),
			ScriptableObject.READONLY | ScriptableObject.DONTENUM);

		defineProperty("fonts", FontList.getInstance(),
			ScriptableObject.READONLY | ScriptableObject.DONTENUM);

		try {
			defineProperty(this, "document", "getActiveDocument", null);
			defineProperty(this, "activeDocument", "getActiveDocument", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		defineProperty("app", Application.getInstance(),
				ScriptableObject.READONLY | ScriptableObject.DONTENUM);

		defineProperty("scriptographer", Scriptographer.getInstance(),
				ScriptableObject.READONLY | ScriptableObject.DONTENUM);
	}

	/**
	 * Determines the directory of a script by reading it's scriptFile property
	 * in the main scope.
	 * 
	 * @param scope
	 */
	protected static File getDirectory(Scriptable scope) {
		Object obj = scope.get("script", scope);
		if (obj instanceof Wrapper)
			obj = ((Wrapper) obj).unwrap();
		if (obj instanceof com.scriptographer.sg.Script)
			return ((com.scriptographer.sg.Script) obj).getFile().getParentFile();
		return null;
	}

	/**
	 * @param script
	 * @param scope
	 * @throws ScriptException 
	 */
	private static void executeScript(Script script, Scope scope)
			throws ScriptException {
		if (script != null) {
			// Temporarily override script with the new one, so includes in
			// other directories work
			com.scriptographer.sg.Script prevScript =
					(com.scriptographer.sg.Script) scope.get("script");
			try {
				scope.put("script", new com.scriptographer.sg.Script(
						script.getFile(), prevScript), true);
				script.execute(scope);
			} finally {
				scope.put("script", prevScript, true);
			}
		}
	}

	/*
	 * JavaScript functions
	 */
	protected static Object getActiveDocument(Scriptable obj) {
		return Context.javaToJS(Document.getActiveDocument(), obj);
	}

	/**
	 * Loads and executes a set of JavaScript source files in the current scope.
	 */
	public static void include(Context cx, Scriptable thisObj, Object[] args,
			Function funObj) throws Exception {
		File baseDir = getDirectory(thisObj);
		ScriptEngine engine = ScriptEngine.getEngineByName("JavaScript");
		for (int i = 0; i < args.length; i++) {
			File file = new File(baseDir, Context.toString(args[i]));
			executeScript(engine.compile(file), engine.getScope(thisObj));
		}
	}

	/**
	 * Loads and executes a set of JavaScript source files in a newly created
	 * scope.
	 */
	public static void execute(Context cx, Scriptable thisObj, Object[] args,
			Function funObj) throws Exception {
		File baseDir = getDirectory(thisObj);
		ScriptEngine engine = ScriptEngine.getEngineByName("JavaScript");
		for (int i = 0; i < args.length; i++) {
			File file = new File(baseDir, Context.toString(args[i]));
			executeScript(engine.compile(file), engine.createScope());
		}
	}

	/**
	 * Maps a Java class to a JavaScript prototype, so this can be used
	 * instead for wrapping of returned java types. So far this is only
	 * used for java.io.File in Scriptographer.
	 */
	public static void mapJavaClass(Context cx, Scriptable thisObj,
			Object[] args, Function funObj) {
		if (args.length == 2) {
			for (int i = 0; i < args.length; i++)
				args[i] = Context.jsToJava(args[i], Object.class);
			if (args[0] instanceof Class && args[1] instanceof Function) {
				Class cls = (Class) args[0];
				Function proto = (Function) args[1];
				RhinoWrapFactory factory =
						(RhinoWrapFactory) cx.getWrapFactory();
				factory.mapJavaClass(cls, proto);
			}
		}
	}
}
