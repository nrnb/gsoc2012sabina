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

import java.util.List;

import org.cytoscape.dyn.internal.layout.DynLayout;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

/**
 * <code> Transformator </code> is used to change visual properties by interpolating
 * the values to change.
 * 
 * @author Sabina Sara Pfister
 *
 */
public class Transformator
{
	private double alpha;
    private int iterations;
    private int delay;
	
	private double timeStart;
	private double timeEnd;
	
	private int onCounter;
	private int offCounter;

	/**
	 * <code> Transformator </code> constructor.
	 */
	public Transformator() 
	{

	}
	
	/**
	 * Run transformation on given interval lists.
	 * @param <T>
	 * @param interval list changedNodes
	 * @param interval list changedEdges
	 * @param interval list changedNodePositions
	 */
	public <T> void run(
			final DynNetwork<T> dynNetwork,
			final DynNetworkView<T> view,
			final DynInterval<T> timeInterval,
			final int visibility,
			final int smoothness)
	{
		setSmoothness(smoothness);
		
		this.onCounter = 255;
		this.offCounter = visibility;
		
		List<DynInterval<T>> nodes = view.searchChangedNodes(timeInterval);
		List<DynInterval<T>> edges = view.searchChangedEdges(timeInterval);
		
		if (nodes.isEmpty() && edges.isEmpty())
			return;
		
		for (int i=0;i<iterations;i++)
		{
			timeStart = System.currentTimeMillis();
			
			if (i<iterations-1)
			{
				onCounter = (int) ((1-alpha)*onCounter+alpha*visibility);
				offCounter = (int) ((1-alpha)*offCounter+alpha*255);
			}
			else
			{
				onCounter = visibility;
				offCounter = 255;
			}

			for (DynInterval<T> interval : nodes)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getNode(interval),offCounter);
				else
					updateTransparency(view, dynNetwork.getNode(interval),onCounter);

			for (DynInterval<T> interval : edges)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getEdge(interval),offCounter);
				else
					updateTransparency(view, dynNetwork.getEdge(interval),onCounter);

			timeEnd = System.currentTimeMillis();
			if (round(timeEnd-timeStart)<delay)
				try {
					Thread.sleep(delay-round(timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				view.updateView();
		}
	}

	/**
	 * Run transformation on given interval lists.
	 * @param <T>
	 * @param interval list changedNodes
	 * @param interval list changedEdges
	 * @param interval list changedNodePositions
	 */
	@SuppressWarnings("unchecked")
	public <T> void run(
			final DynNetwork<T> dynNetwork,
			final DynNetworkView<T> view,
			final DynInterval<T> timeInterval,
			final DynLayout layout,
			final int visibility,
			final int smoothness)
	{	
		setSmoothness(smoothness);
		
		this.onCounter = 255;
		this.offCounter = visibility;
		
		List<DynInterval<T>> nodes = view.searchChangedNodes(timeInterval);
		List<DynInterval<T>> edges = view.searchChangedEdges(timeInterval);
		List<DynInterval<T>> nodesPosX = layout.searchChangedNodePositionsX(timeInterval);
		List<DynInterval<T>> nodesPosY = layout.searchChangedNodePositionsY(timeInterval);

		if (nodes.isEmpty() && edges.isEmpty() && nodesPosX.isEmpty() && nodesPosY.isEmpty())
			return;
		
		for (int i=0;i<iterations;i++)
		{
			timeStart = System.currentTimeMillis();
			
			if (i<iterations-1)
			{
				onCounter = (int) ((1-alpha)*onCounter+alpha*visibility);
				offCounter = (int) ((1-alpha)*offCounter+alpha*255);
			}
			else
			{
				onCounter = visibility;
				offCounter = 255;
			}

			for (DynInterval<T> interval : nodes)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getNode(interval),offCounter);
				else
					updateTransparency(view, dynNetwork.getNode(interval),onCounter);

			for (DynInterval<T> interval : edges)
				if (interval.isOn())
					updateTransparency(view, dynNetwork.getEdge(interval),offCounter);
				else
					updateTransparency(view, dynNetwork.getEdge(interval),onCounter);
			
			for (DynInterval<T> interval : nodesPosX)
				if (!interval.isOn() && interval.getOffValue()!=null)
					updatePositionX(view,dynNetwork.getNode(interval),interval.getAttribute().getColumn(),(Double)interval.getOffValue());
				else if (interval.isOn())
					updatePositionX(view,dynNetwork.getNode(interval),interval.getAttribute().getColumn(),(Double)interval.getOnValue());

			for (DynInterval<T> interval : nodesPosY)
				if (!interval.isOn() && interval.getOffValue()!=null)
					updatePositionY(view,dynNetwork.getNode(interval),interval.getAttribute().getColumn(),(Double)interval.getOffValue());
				else if (interval.isOn())
					updatePositionY(view,dynNetwork.getNode(interval),interval.getAttribute().getColumn(),(Double)interval.getOnValue());
				
			timeEnd = System.currentTimeMillis();
			if (round(timeEnd-timeStart)<delay)
				try {
					Thread.sleep(delay-round(timeEnd-timeStart));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				view.updateView();
		}
	}

	private final <T> void updateTransparency(final DynNetworkView<T> view, final CyNode node, final int value)
	{
		if (node!=null)
		{
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_TRANSPARENCY,value);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_LABEL_TRANSPARENCY,value);
			view.writeLockedVisualProperty(node, BasicVisualLexicon.NODE_BORDER_TRANSPARENCY,value);
		}
	}
	
	private final <T> void updateTransparency(final DynNetworkView<T> view, final CyEdge edge, final int value)
	{
		if (edge!=null)
		{
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_TRANSPARENCY,value);
			view.writeLockedVisualProperty(edge, BasicVisualLexicon.EDGE_LABEL_TRANSPARENCY,value);
		}
	}

	private final <T> void updatePositionX(final DynNetworkView<T> view, final CyNode node, final String attr, final double value)
	{
		if (node!=null)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION, 
					(1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_X_LOCATION)+alpha*value);
	}
	
	private final <T> void updatePositionY(final DynNetworkView<T> view, final CyNode node, final String attr, final double value)
	{
		if (node!=null)
			view.writeVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION, 
					(1-alpha)*view.readVisualProperty(node, BasicVisualLexicon.NODE_Y_LOCATION)+alpha*value);
	}
	
	private void setSmoothness(int smoothness)
	{
		if (smoothness==0)
		{
			this.iterations = 1;
			this.delay = 0;
			this.alpha = 1;
		}
		else
		{
			this.iterations = (int) (smoothness*25/1000);
			this.delay = (int) (smoothness/iterations);
			
			switch(smoothness)
			{
			case 250:
				this.alpha = 0.35;
				break;
			case 500:
				this.alpha = 0.2;
				break;
			case 750:
				this.alpha = 0.15;
				break;
			case 1000:
				this.alpha = 0.10;
				break;
			case 2000:
				this.alpha = 0.05;
				break;
			case 3000:
				this.alpha = 0.03;
				break;
			case 4000:
				this.alpha = 0.025;
				break;
			}

		}
	}
	
	private int round(double value)
	{
		return (int) value;
	}

}
