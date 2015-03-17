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

import java.util.ArrayList;
import java.util.List;

import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.gui.DynCytoPanelImpl;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> DynNetworkViewTaskGroup </code> is the task that is responsible for updating
 * the visualization after metanodes are expanded or collapsed. this is necessary since the
 * visualization computes only changed elements from the last visualizations, and thus we need to
 * manually initialize new nodes or edges.
 * 
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 * @param <C>
 */
public final class DynNetworkViewTaskGroup<T,C> extends AbstractDynNetworkViewTask<T,C>  
{
	private final CyGroup group;
	
//	private double time;
	private int visibility;

	/**
	 * <code> DynNetworkViewTaskGroup </code> constructor.
	 * @param panel
	 * @param view
	 * @param queue
	 * @param group
	 */
	public DynNetworkViewTaskGroup(
			final DynCytoPanelImpl<T,C> panel,
			final DynNetworkView<T> view,
			final BlockingQueue queue,
			final CyGroup group) 
	{
		super(panel, view, null, null, queue);
		this.group = group;
	}

	@Override
	public void run()
	{
		queue.lock();
		
		view.updateView();
		
		setParameters();

		List<CyNode> nodeList = new ArrayList<CyNode>();
		List<CyEdge> edgeList = new ArrayList<CyEdge>();
		if (group.isCollapsed(dynNetwork.getNetwork()))
		{
			nodeList.add(group.getGroupNode());
			edgeList = dynNetwork.getNetwork().getAdjacentEdgeList(group.getGroupNode(), CyEdge.Type.ANY);
		}
		else
		{
			nodeList = group.getNodeList();
			edgeList = group.getInternalEdgeList();
			edgeList.addAll(group.getExternalEdgeList());
		}

		for (CyNode node : nodeList)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY, visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY, visibility);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY, visibility);
		}
		for (CyEdge edge : edgeList)
			view.writeVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY, visibility);
		
		// update nodes
		List<DynInterval<T>> intervalList = dynNetwork.searchNodes(timeInterval);
		for (DynInterval<T> interval : intervalList)
		{
			CyNode node = dynNetwork.getNode(interval);
			if (node!=null && nodeList.contains(node))
			{
				view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY, 255);
				view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY, 255);
				view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY, 255);
			}
		}
		
		// update edges
		intervalList = dynNetwork.searchEdges(timeInterval);
		for (DynInterval<T> interval : intervalList)
		{
			CyEdge edge = dynNetwork.getEdge(interval);
			if (edge!=null && edgeList.contains(edge))
				view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY, 255);
		}

		view.updateView();
		
		queue.unlock(); 

	}
	
	private void setParameters()
	{
//		this.time = this.panel.getTime();
//		if (time>=panel.getMaxTime())
//			timeInterval = new DynInterval<T>(time-0.0000001, time+0.0000001);
//		else
//			timeInterval = new DynInterval<T>(time, time);
		
		timeInterval = new DynInterval<T>(panel.getTime(), panel.getTime()>=panel.getMaxTime()?panel.getTime()+0.0000001:panel.getTime());
		this.visibility = this.panel.getVisibility();
	}

}

