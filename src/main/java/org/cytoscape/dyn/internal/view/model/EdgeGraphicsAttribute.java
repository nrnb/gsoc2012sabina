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

package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.model.CyEdge;

/**
 * <code> EdgeGraphicsAttribute </code> is used to store edge graphics attributes
 * to be added later to the visualization.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public final class EdgeGraphicsAttribute<T>
{
	private final DynNetwork<T> currentNetwork;
	private final CyEdge currentEdge;
	
	private final String width;
	private final String fill;
	
	/**
	 * <code> EdgeGraphicsAttribute </code> constructor.
	 * @param currentNetwork
	 * @param currentEdge
	 * @param width
	 * @param fill
	 */
	public EdgeGraphicsAttribute(
			DynNetwork<T> currentNetwork,
			CyEdge currentEdge,
			String width, 
			String fill)
	{
		this.currentNetwork = currentNetwork;
		this.currentEdge = currentEdge;
		this.width = width;
		this.fill = fill;
	}
	
	/**
	 * Add edge graphics attribute.
	 * @param viewFactory
	 */
	public void add(DynNetworkViewFactory<T> viewFactory)
	{
		viewFactory.setEdgeGraphics(currentNetwork, currentEdge, width, fill);
	}
	
}
