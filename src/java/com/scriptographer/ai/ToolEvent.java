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
 * File created on 21.12.2004.
 */

package com.scriptographer.ai;

import com.scriptographer.script.EnumUtils;
import com.scriptographer.sg.Event;

/**
 * The ToolEvent object is received by the {@link Tool}'s mouse event handlers
 * {@link Tool#getOnMouseDown()}, {@link Tool#getOnMouseDrag()},
 * {@link Tool#getOnMouseMove()} and {@link Tool#getOnMouseUp()}. The ToolEvent
 * object is the only parameter passed to these functions and contains
 * information about the mouse event.
 * 
 * Sample code:
 * <code>
 * function onMouseUp(event) {
 * 	// the position of the mouse when it is released
 * 	print(event.point);
 * }
 * </code>
 * 
 * @author lehni
 */
public class ToolEvent extends Event {
	private ToolHandler tool;
	private ToolEventType type;
	private Point point = null;
	private Point lastPoint = null;
	private Point downPoint = null;
	private Point middlePoint = null;
	private Point delta = null;
	private double pressure = -1;
	private Item item = null;

	protected ToolEvent(ToolHandler tool, ToolEventType type, int modifiers) {
		super(modifiers);
		this.tool = tool;
		this.type = type;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer(32);
		buf.append("{ type: ").append(EnumUtils.getScriptName(type)); 
		buf.append(", point: ").append(getPoint());
		buf.append(", pressure: ").append(getPressure());
		buf.append(", count: ").append(getCount());
		buf.append(", modifiers: ").append(getModifiers());
		buf.append(" }");
		return buf.toString();
	}

	/**
	 * Convenience method to allow local overrides of point values.
	 * See application below.
	 */
	private Point choosePoint(Point point, Point toolPoint) {
		if (point != null)
			return point;
		if (toolPoint != null)
			return new Point(toolPoint);
		return null;
	}

	/**
	 * The position of the mouse in document coordinates when the event was
	 * fired.
	 * 
	 * Sample code:
	 * <code>
	 * function onMouseDrag(event) {
	 * 	// the position of the mouse when it is dragged
	 * 	print(event.point);
	 * }
	 * 
	 * function onMouseUp(event) {
	 * 	// the position of the mouse when it is released
	 * 	print(event.point);
	 * }
	 * </code>
	 */
	public Point getPoint() {
		return choosePoint(point, tool.point);
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	/**
	 * The position of the mouse in document coordinates when the previous
	 * event was fired.
	 */
	public Point getLastPoint() {
		return choosePoint(lastPoint, tool.lastPoint);
	}

	public void setLastPoint(Point lastPoint) {
		this.lastPoint = lastPoint;
	}

	/**
	 * The position of the mouse in document coordinates when the mouse button
	 * was last clicked.
	 */
	public Point getDownPoint() {
		return choosePoint(downPoint, tool.downPoint);
	}

	public void setDownPoint(Point downPoint) {
		this.downPoint = downPoint;
	}

	/**
	 * The point in the middle between {@link #getLastPoint()} and
	 * {@link #getPoint()}. This is a useful position to use when creating
	 * artwork based on the moving direction of the mouse, as returned by
	 * {@link #getDelta()}.
	 */
	public Point getMiddlePoint() {
		// For explanations, see getDelta()
		if (middlePoint == null && tool.lastPoint != null) {
			// (point + lastPoint) / 2
			return tool.point.add(tool.lastPoint).divide(2);
		}
		return middlePoint;
	}

	public void setMiddlePoint(Point middlePoint) {
		this.middlePoint = middlePoint;
	}

	/**
	 * The difference between the current position and the last position of the
	 * mouse when the event was fired. In case of the mouse-up event, the
	 * difference to the mouse-down position is returned.
	 */
	public Point getDelta() {
		// Do not put the calculated delta into delta, since this only reserved
		// for overriding event.delta.
		// Instead, keep calculating the delta each time, so the result can be
		// directly modified by the script without changing the internal values.
		// We could cache this and use clone, but this is almost as fast...
		if (delta == null && tool.lastPoint != null) {
			return tool.point.subtract(tool.lastPoint);
		}
		return delta;
	}

	public void setDelta(Point delta) {
		this.delta = delta;
	}

	/**
	 * The amount of force being applied by a pressure-sensitive input device,
	 * such as a graphic tablet.
	 * 
	 * @return the pressure as a value between 0 and 1
	 */
	public double getPressure() {
		return pressure != -1 ? pressure : tool.pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	/**
	 * The number of times the mouse event was fired.
	 * 
	 * Sample code:
	 * <code>
	 * function onMouseDrag(event) {
	 * 	// the amount of times the onMouseDrag event was fired
	 * 	// since the last onMouseDown event
	 * 	print(event.count);
	 * }
	 * 
	 * function onMouseUp(event) {
	 * 	// the amount of times the onMouseUp event was fired
	 * 	// since the tool was activated 
	 * 	print(event.point);
	 * }
	 * </code>
	 */
	public int getCount() {
		switch (type) {
		case MOUSEDOWN:
		case MOUSEUP:
			// Return downCount for both mouse down and up, since
			// the count is the same.
			return tool.downCount;
		default:
			return tool.count;
		}
	}

	public void setCount(int count) {
		switch (type) {
		case MOUSEDOWN:
		case MOUSEUP:
			tool.downCount = count;
			break;
		default:
			tool.count = count;
			break;
		}
	}

	public ToolEventType getType() {
		return type;
	}
	
	public void setType(ToolEventType type) {
		this.type = type;
	}

	public Item getItem() {
		if (item == null) {
			HitResult result = Document.getActiveDocument().hitTest(getPoint());
			if (result != null) {
				item = result.getItem();
				// Find group parent
				Item parent = item.getParent();
				while (parent instanceof Group || parent instanceof CompoundPath) {
					item = parent;
					parent = parent.getParent();
				}
			}
		}
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	// TODO: Consider adding these, present since CS2
	/*
	 * For graphic tablets, tangential pressure on the finger wheel of the
	 * airbrush tool.
	 */
	// AIToolPressure stylusWheel;
	/*
	 * How the tool is angled, also called altitude or elevation.
	 */
	// AIToolAngle tilt;
	/*
	 * The direction of tilt, measured clockwise in degrees around the Z axis,
	 * also called azimuth,
	 */
	// AIToolAngle bearing;
	/*
	 * Rotation of the tool, measured clockwise in degrees around the tool's
	 * barrel.
	 */
	// AIToolAngle rotation;
}
