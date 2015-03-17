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
import org.cytoscape.model.CyNode;

/**
 * <code> NodeGraphicsAttribute </code> is used to store node graphics attributes
 * to be added later to the visualization.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public final class NodeGraphicsAttribute<T>
{
	private final DynNetwork<T> currentNetwork;
	private final CyNode currentNode;
	
	private final String type;
	private final String height;
	private final String width;
	private final String x;
	private final String y;
	private final String fill;
	private final String linew;
	private final String outline;
	
	/**
	 * <code> NodeGraphicsAttribute </code> constructor.
	 * @param currentNetwork
	 * @param currentNode
	 * @param type
	 * @param height
	 * @param width
	 * @param x
	 * @param y
	 * @param fill
	 * @param linew
	 * @param outline
	 */
	public NodeGraphicsAttribute(
			DynNetwork<T> currentNetwork,
			CyNode currentNode,
			final String type, 
			final String height, 
			final String width, 
			final String x, 
			final String y, 
			final String fill,  
			final String linew,
			final String outline)
	{
		this.currentNetwork = currentNetwork;
		this.currentNode = currentNode;
		this.type = type;
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
		this.fill = fill;
		this.linew = linew;
		this.outline = outline;
	}

	/**
	 * Add node graphics attribute.
	 * @param viewFactory
	 */
	public void add(DynNetworkViewFactory<T> viewFactory)
	{
		viewFactory.setNodeGraphics(currentNetwork, currentNode, type, height, width, x, y, fill, linew, outline);
	}
	
}
