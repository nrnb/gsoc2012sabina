/*
 * DynNetwork plugin for Cytoscape 3.0 (http://www.cytoscape.org/).
 * Copyright (C) 2012 Sabina Sara Pfister
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.cytoscape.dyn.internal.model.tree;

import org.cytoscape.dyn.internal.model.attribute.DynAttribute;


/**
 * <code> DynInterval </code> represent a value list and its right half-open interval. 
 * Intervals are convenient for representing events that each occupy a continuous 
 * period of time. A half-open interval is an ordered pair of real numbers [startTime, 
 * endTime[ with startTime =< endTime, where startTime is included and endTime is excluded.
 * <P>
 * 
 * <code> DynInterval </code> offers the possibility to store two different values for on 
 * and off events.
 *  
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class DynInterval<T> implements Comparable<DynInterval<T>>
{
	private Class<T> type;
	
	private T onValue;
	private T offValue;
	private double start;
	private double end;
	private boolean isOn;
	
	private DynAttribute<T> attribute;
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param type of generic
	 * @param generic value
	 * @param start
	 * @param end
	 */
	public DynInterval(Class<T> type, T onValue, double start, double end)
	{
		this.onValue = onValue;
		this.type = type;
		this.start = start;
		this.end = end;
		this.isOn = false;
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param int value
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(int onValue, double start, double end)
	{
		this((Class<T>) Integer.class, (T) new Integer(onValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param double value
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(double onValue, double start, double end)
	{
		this((Class<T>) Double.class, (T) new Double(onValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param boolean value
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(boolean onValue, double start, double end)
	{
		this((Class<T>) Boolean.class, (T) new Boolean(onValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param string value
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(String onValue, double start, double end)
	{
		this((Class<T>) String.class, (T) new String(onValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param type
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	public DynInterval(Class<T> type, T onValue,  T offValue, double start, double end)
	{
		this.onValue = onValue;
		this.offValue = offValue;
		this.type = type;
		this.start = start;
		this.end = end;
		this.isOn = false;
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(int onValue, int offValue, double start, double end)
	{
		this((Class<T>) Integer.class, (T) new Integer(onValue), (T) new Integer(offValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(double onValue, double offValue, double start, double end)
	{
		this((Class<T>) Double.class, (T) new Double(onValue), (T) new Double(offValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(boolean onValue, boolean offValue, double start, double end)
	{
		this((Class<T>) Boolean.class, (T) new Boolean(onValue), (T) new Boolean(offValue), start, end);
	}
	
	/**
	 * <code> DynInterval </code> constructor.
	 * @param onValue
	 * @param offValue
	 * @param start
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public DynInterval(String onValue, String offValue, double start, double end)
	{
		this((Class<T>) String.class, (T) new String(onValue), (T) new String(offValue), start, end);
	}

	/**
	 * <code> DynInterval </code> constructor.
	 * @param start
	 * @param end
	 */
	public DynInterval(double start, double end)
	{
		this.start = start;
		this.end = end;
	}

	@Override
	public int compareTo(DynInterval<T> interval)
	{
		if ((start <= interval.end && interval.start <= end) &&	
				((start < interval.end && interval.start < end) ||
				 (interval.start == interval.end && (start <= interval.end && interval.start < end)) ||
				 (start == end && (start < interval.end && interval.start <= end)) ||
				 (start == end && interval.start == interval.end && start == interval.end)))
			return 1;
		else
			return -1;
	}

	/**
	 * Get interval value when this interval is on. It is used to deal with on events.
	 * @return value
	 */
	public T getOnValue()
	{
		return onValue;
	}
	
	/**
	 * Get interval value when this interval is off. It is used to deal with off events.
	 * @return value
	 */
	public T getOffValue()
	{
		return offValue;
	}
	
	/**
	 * Get interval value if the time interval overlaps with the given time interval..
	 * @param interval
	 * @return value
	 */
	public T getOverlappingValue(DynInterval<T> interval)
	{
		if (this.compareTo(interval)>0)
			return onValue;
		else
			return null;
	}

	/**
	 * Set time interval start.
	 * @param start
	 */
	public void setStart(double start)
	{
		this.start = start;
	}

	/**
	 * Set time interval end.
	 * @param end
	 */
	public void setEnd(double end)
	{
		this.end = end;
	}

	/**
	 * Get time interval start.
	 * @return start
	 */
	public double getStart()
	{
		return start;
	}

	/**
	 * Get time interval end.
	 * @return end
	 */
	public double getEnd()
	{
		return end;
	}
	
	/**
	 * Get time of the closest next interval. Return Double.POSITIVE_INFINITY
	 * if none is found.
	 * @return end
	 */
	public double getNext()
	{
		double next = Double.POSITIVE_INFINITY;
		for (DynInterval<T> interval : this.getAttribute().getIntervalList())
			next = Math.min(next,interval.getStart());
		return next;
	}
	
	/**
	 * Get time of the closest previous interval. Return Double.NEGATIVE_INFINITY
	 * if none is found.
	 * @return end
	 */
	public double getPrevious()
	{
		double previous = Double.NEGATIVE_INFINITY;
		for (DynInterval<T> interval : this.getAttribute().getIntervalList())
			previous = Math.max(previous,interval.getStart());
		return previous;
	}
	
	/**
	 * Get the attribute corresponding to this time interval.
	 * @return
	 */
	public DynAttribute<T> getAttribute()
	{
		return attribute;
	}

	/**
	 * Set the attribute corresponding to this time interval.
	 * @param attribute
	 */
	public void setAttribute(DynAttribute<T> attribute) 
	{
		this.attribute = attribute;
	}

	/**
	 * Get is on. Is used to know if this interval was turned on or off.
	 * @return
	 */
	public boolean isOn() 
	{
		return isOn;
	}

	/**
	 * Set is on. Is used to know if this interval was turned on or off..
	 * @param isOn
	 */
	public void setOn(boolean isOn) 
	{
		this.isOn = isOn;
	}

	/**
	 * Return class type
	 * @return class
	 */
	public Class<T> getType()
	{
		return type;
	}
	
}
