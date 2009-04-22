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
 * $Id$
 */

var consoleDialog = new FloatingDialog('tabbed show-cycle resizing remember-placing', function() {
	var engine = ScriptEngine.getEngineByName('JavaScript');
	var consoleScope = engine != null ? engine.createScope() : null;

	var textIn = new TextEdit(this, 'multiline') {
		size: [300, 100],
		minimumSize: [200, 18],
		maxLength: 32767,
		font: 'palette',
		onTrack: function(tracker) {
			if (tracker.action == Tracker.ACTION_KEY_STROKE
				&& tracker.virtualKey == Tracker.KEY_RETURN) {
				// enter was pressed in the input field. determine the
				// current line:
				var text = this.text;
				var end = this.selection[1] - 1;
				if (/[\n\r]/.test(text.charAt(end))) { // empty line?
					text = '';
				} else {
					while (end >= 0 && /[\n\r]/.test(text[end]))
						end--;
					var start = end;
					end++;
					while (start >= 0 && !/[\n\r]/.test(text[start]))
						start--;
					start++;
					text = text.substring(start, end + 1);
				}
				try {
					engine.evaluate(text, consoleScope);
				} catch (e) {
					if (e.javaException) {
						print(e.javaException.message);
					} else {
						print(e);
					}
				}
			}
			return true;
		}
	};

	var textOut = new TextEdit(this, 'readonly multiline') {
		size: [300, 100],
		minimumSize: [200, 18],
		maxLength: 32767,
		font: 'palette',
		backgroundColor: 'inactive-tab',
		// the onDraw workaround for display problems is only needed on mac
		onDraw: app.macintosh && function(drawer) {
			// Workaround for mac, where TextEdit fields with a background
			// color
			// do not get completely filled
			// Fill in the missing parts.
			drawer.color = 'inactive-tab';
			var rect = drawer.boundsRect;
			// A tet line with the small font is 11 pixels heigh. there
			// seems to be a shift, which was detected by trial and error.
			// This might change in future versions!
			var height = rect.height - (rect.height - 6) % 11 - 3;
			// 18 is the width of the scrollbar. This might change in future
			// versions!
			drawer.fillRect(rect.width - 18, 0, 1, height);
			drawer.fillRect(0, height, rect.width - 1, rect.height - height - 2);
		}
	};

	var that = this;
	var consoleText = new java.lang.StringBuffer();

	function showText() {
		if (textOut != null) {
			textOut.text = consoleText;
			that.visible = true;
			textOut.selection = consoleText.length() - 1;
		}
	}

	// Buttons:
	var clearButton = new ImageButton(this) {
		onClick: function() {
			textOut.text = '';
			consoleText.setLength(0);
		},
		image: getImage('refresh.png'),
		size: buttonSize
	};

	// Layout:
	return {
		title: 'Scriptographer Console',
		size: [400, 300],
		margin: -1,
		layout: [
			'preferred fill',
			'0.2 fill 15',
			-1, -1
		],
		content: {
			'0, 0, 1, 0': textIn,
			'0, 1, 1, 1': textOut,
			'0, 2': clearButton
		},

		println: function(str) {
			if (textOut) {
				// If the text does not grow too long, remove old lines again:
				consoleText.append(str);
				consoleText.append(lineBreak);
				while (consoleText.length() >= 8192) {
					var pos = consoleText.indexOf(lineBreak);
					if (pos == -1)
						pos = consoleText.length() - 1;
					consoleText['delete'](0, pos + 1);
				}
				if (that.isInitialized())
					showText();
			}
		},

		onInitialize: function() {
			showText();
		},

		onDestroy: function() {
			textOut = null;
		}
	}
});

// Interface with ScriptographerEngine
ScriptographerEngine.setCallback(new ScriptographerCallback() {
	println: function(str) {
		consoleDialog.println(str);
	}
});

function onAbout() {
	aboutDialog.doModal();
}
