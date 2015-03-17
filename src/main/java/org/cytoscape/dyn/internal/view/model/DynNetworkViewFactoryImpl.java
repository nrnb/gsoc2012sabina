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

import java.awt.Color;
import java.awt.Paint;
import java.util.Stack;

import org.cytoscape.dyn.internal.io.read.util.GraphicsTypeMap;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.values.NodeShape;
import org.cytoscape.view.vizmap.VisualMappingManager;

/**
 * <code> DynNetworkViewFactoryImpl </code> implements the interface
 * {@link DynNetworkViewFactory} for creating {@link DynNetworkView}s.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class DynNetworkViewFactoryImpl<T> implements DynNetworkViewFactory<T>
{
	private final GraphicsTypeMap graphicsTypeMap;
	
	private final DynNetworkViewManager<T> viewManager;
	private final CyNetworkViewFactory cyNetworkViewFactory;
	private final CyNetworkViewManager networkViewManager;
	private final VisualMappingManager vmm;
	
	private final Stack<GraphGraphicsAttribute<T>> graphGraphicsList;
	private final Stack<NodeGraphicsAttribute<T>> nodeGraphicsList;
	private final Stack<EdgeGraphicsAttribute<T>> edgeGraphicsList;
	
	/**
	 * <code> DynNetworkViewFactoryImpl </code> constructor.
	 * @param viewManager
	 * @param cyNetworkViewFactory
	 * @param networkViewManager
	 * @param vmm
	 */
	public DynNetworkViewFactoryImpl(
			DynNetworkViewManager<T> viewManager,
			CyNetworkViewFactory cyNetworkViewFactory,
			final CyNetworkViewManager networkViewManager,
			final VisualMappingManager vmm)
	{
		
		this.viewManager = viewManager;
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		this.networkViewManager = networkViewManager;
		this.vmm = vmm;
		
		this.graphicsTypeMap = new GraphicsTypeMap();
		this.graphGraphicsList = new Stack<GraphGraphicsAttribute<T>>();
		this.nodeGraphicsList = new Stack<NodeGraphicsAttribute<T>>();
		this.edgeGraphicsList = new Stack<EdgeGraphicsAttribute<T>>();
	}
	
	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork)
	{
		DynNetworkViewImpl<T> dynNetworkView = new DynNetworkViewImpl<T>(dynNetwork, networkViewManager,cyNetworkViewFactory,vmm);
		viewManager.addDynNetworkView(dynNetworkView);
		return dynNetworkView;
	}

	@Override
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork, CyEdge currentEdge, String name, String value, String Type, String start, String end) 
	{
		
	}
	
	@Override
	public void addedGraphGraphics(DynNetwork<T> dynNetwork, String fill) 
	{
		this.graphGraphicsList.push(new GraphGraphicsAttribute<T>(dynNetwork,fill));
	}
	
	@Override
	public void addedNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode, String type, String height, String width, String x, String y, String fill, String linew, String outline) 
	{
		this.nodeGraphicsList.push(new NodeGraphicsAttribute<T>(dynNetwork,currentNode,type,height,width,x,y,fill,linew,outline));
	}
	
	@Override
	public void addedEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge, String width, String fill) 
	{
		this.edgeGraphicsList.push(new EdgeGraphicsAttribute<T>(dynNetwork,currentEdge,width,fill));
	}
	
	@Override
	public void setGraphGraphics(DynNetwork<T> dynNetwork, String fill)
	{
		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
		
		if (fill!=null)
			view.setVisualProperty(BasicVisualLexicon.NETWORK_BACKGROUND_PAINT, decodeHEXColor(fill));
	}
	
	@Override
	public void setNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode, String type, String h, String w, String x, String y, String fill, String linew, String outline) 
	{
		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
		
		if (type!=null && graphicsTypeMap.getTypedValue(graphicsTypeMap.getType(type))!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_SHAPE, (NodeShape) graphicsTypeMap.getTypedValue(graphicsTypeMap.getType(type)));
		
		if (h!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_HEIGHT, new Double(h));
		
		if (w!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_WIDTH, new Double(w));
		
		if (x!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, new Double(x));
		
		if (y!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, new Double(y));
		
		if (linew!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_WIDTH, new Double(linew));
		
		if (fill!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, decodeHEXColor(fill));
		
		if (outline!=null)
			view.getNodeView(currentNode).setVisualProperty(BasicVisualLexicon.NODE_BORDER_PAINT, decodeHEXColor(outline));
		
	}
	
	@Override
	public void setEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge, String width, String fill) 
	{
		CyNetworkView view = networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next();
		
		if (width!=null)
			view.getEdgeView(currentEdge).setVisualProperty(BasicVisualLexicon.EDGE_WIDTH, new Double(width));
		
		if (fill!=null)
			view.getEdgeView(currentEdge).setVisualProperty(BasicVisualLexicon.EDGE_UNSELECTED_PAINT, decodeHEXColor(fill));
	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed) 
	{
		return null;
	}

	@Override
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String name, String value, String Type, String start, String end) 
	{

	}

	@Override
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode) 
	{
		return null;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end) 
	{
		return null;
	}

	@Override
	public void addedNodeAttribute(DynNetwork<T> dynNetwork, CyNode currentNode, String name, String value, String Type, String start, String end) 
	{

	}

	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge) 
	{

	}

	@Override
	public void deletedGraph(DynNetwork<T> dynNetwork) 
	{

	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{

	}
	
	@Override
	public void finalizeNetwork(DynNetwork<T> dynNetwork) 
	{	
		while (!graphGraphicsList.isEmpty())
			graphGraphicsList.pop().add(this);
		
		while (!nodeGraphicsList.isEmpty())
			nodeGraphicsList.pop().add(this);
		
		while (!edgeGraphicsList.isEmpty())
			edgeGraphicsList.pop().add(this);
		
		networkViewManager.getNetworkViews(dynNetwork.getNetwork()).iterator().next().updateView();
	}
	
    private static Paint decodeHEXColor(String nm) throws NumberFormatException 
    {
    	Integer intval = Integer.decode(nm);
    	int i = intval.intValue();
    	return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

}
