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

package org.cytoscape.dyn.internal.view.task;

import org.cytoscape.dyn.internal.layout.DynLayout;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanel;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanelImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

/**
 * <code> AbstractDynNetworkViewTask </code> is the abstract calls all visual task
 * have to extend. It provides the functionality to communicate with {@link DynCytoPanelImpl},
 * and update the visualization.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public abstract class AbstractDynNetworkViewTask<T,C>  implements Runnable
{
	protected final DynCytoPanel<T,C> panel;
	protected final DynNetworkView<T> view;
	protected final DynNetwork<T> dynNetwork;
	protected final DynLayout<T> layout;
	protected final Transformator transformator;
	protected final BlockingQueue queue;
	
	protected double timeStart;
	protected double timeEnd;
	
	protected DynInterval<T> timeInterval;
	
	protected boolean cancelled = false;
	
	/**
	 * <code> AbstractDynNetworkViewTask </code> constructor.
	 * @param panel
	 * @param view
	 * @param layout
	 * @param queue
	 * @param low
	 * @param high
	 */
	protected AbstractDynNetworkViewTask(
			final DynCytoPanel<T, C> panel,
			final DynNetworkView<T> view,
			final DynLayout<T> layout,
			final Transformator transformator,
			final BlockingQueue queue) 
	{
		this.panel = panel;
		this.view = view;
		this.dynNetwork = view.getNetwork();
		this.layout = layout;
		this.transformator = transformator;
		this.queue = queue;
	}

	/**
	 * Cancel task.
	 */
	public void cancel() 
	{
		this.cancelled = true;
	}

	@Override
	public void run()
	{
		
	}
	
	protected void updateAttr(DynInterval<T> interval)
	{
		dynNetwork.writeGraphTable(interval.getAttribute().getColumn(), interval.getOverlappingValue(timeInterval));
	}
	
	protected void updateAttr(CyNode node, DynInterval<T> interval)
	{
		if (node!=null)
			dynNetwork.writeNodeTable(node, interval.getAttribute().getColumn(), interval.getOverlappingValue(timeInterval));
	}
	
	protected void updateAttr(CyEdge edge, DynInterval<T> interval)
	{
		if (edge!=null)
			dynNetwork.writeEdgeTable(edge, interval.getAttribute().getColumn(), interval.getOverlappingValue(timeInterval));
	}
	
}
