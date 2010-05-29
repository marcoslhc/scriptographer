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
 * File created on Feb 9, 2010.
 */

package com.scriptographer.sg;

import com.scriptographer.ui.KeyModifiers;

/**
 * @author lehni
 * 
 * @jshide
 */
public abstract class Event {
	private int modifiers;

	public Event(int modifiers) {
		this.modifiers = modifiers;
	}

	/**
	 * Returns an object representing the state of various modifiers keys. These
	 * properties are supported:
	 * {@code shift, control, option, meta, capsLock}.
	 * 
	 * Sample code:
	 * <code>
	 * function onMouseDown(event) {
	 *     print(event.modifiers.shift);
	 * }
	 * </code>
	 * 
	 * @return
	 */
	public KeyModifiers getModifiers() {
		return new KeyModifiers(modifiers);
	}
}
