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

package org.cytoscape.dyn.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cytoscape.dyn.internal.io.read.util.AttributeTypeMap;
import org.cytoscape.dyn.internal.model.attribute.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.group.CyGroup;
import org.cytoscape.group.CyGroupFactory;
import org.cytoscape.group.CyGroupManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CyRootNetworkManager;
import org.cytoscape.session.CyNetworkNaming;

/**
 * <code> DynNetworkFactoryImpl </code> implements the interface
 * {@link DynNetworkFactory} for creating {@link DynNetwork}s.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public final class DynNetworkFactoryImpl<T> implements DynNetworkFactory<T>
{
	private final AttributeTypeMap typeMap;
	
	private final CyGroupManager groupManager;
	private final CyGroupFactory groupFactory;
	private final CyNetworkFactory networkFactory;
	private final CyRootNetworkManager rootNetworkManager;
	private final DynNetworkManager<T> manager;
	private final CyNetworkNaming nameUtil;
	
	private final List<CyNode> metaNodes; 
	
	/**
	 * <code> DynNetworkFactoryImpl </code> constructor.
	 * @param networkFactory
	 * @param rootNetworkManager
	 * @param groupManager
	 * @param groupFactory
	 * @param manager
	 * @param nameUtil
	 */
	public DynNetworkFactoryImpl(
			final CyNetworkFactory networkFactory,
			final CyRootNetworkManager rootNetworkManager,
			final CyGroupManager groupManager,
			final CyGroupFactory groupFactory,
			final DynNetworkManager<T> manager,
			final CyNetworkNaming nameUtil)
	{
		this.networkFactory = networkFactory;
		this.rootNetworkManager = rootNetworkManager;
		this.groupManager = groupManager;
		this.groupFactory = groupFactory;
		this.manager = manager;
		this.nameUtil = nameUtil;
		
		this.typeMap = new AttributeTypeMap();
		this.metaNodes = new ArrayList<CyNode>();
	}

	@Override
	public DynNetwork<T> addedGraph(String id, String label, String start, String end, String directed)
	{
		DynNetwork<T> dynNetwork = createGraph(directed, id, label, start, end);
		setElement(dynNetwork, id, label, null, start, end);
		manager.addDynNetwork(dynNetwork);
		return dynNetwork;
	}

	@Override
	public CyNode addedNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode currentNode = createNode(dynNetwork, group, id, label, start, end);
		setElement(dynNetwork, currentNode, group, id, label, null, start, end);
		return currentNode;
	}
	
	@Override
	public CyGroup addedGroup(DynNetwork<T> dynNetwork, CyNode currentNode)
	{
		CyGroup currentGroup = groupFactory.createGroup(dynNetwork.getNetwork(), currentNode, true);
		groupManager.addGroup(currentGroup);
		metaNodes.add(currentNode);
		return currentGroup;
	}
	
	@Override
	public CyEdge addedEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end)
	{
		if(dynNetwork.containsCyNode(source) && dynNetwork.containsCyNode(target))
		{
			CyEdge currentEdge = createEdge(dynNetwork, id, label, source, target, start, end);
			setElement(dynNetwork, currentEdge, id, label, null, start, end);
			return currentEdge;
		}
		else
			return null;
	}
	
	@Override
	public void addedGraphAttribute(DynNetwork<T> dynNetwork, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addedNodeAttribute(DynNetwork<T> dynNetwork, CyNode currentNode, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, currentNode, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addedEdgeAttribute(DynNetwork<T> dynNetwork, CyEdge currentEdge, String attName, String attValue, String attType, String start, String end)
	{
		setAttributes(dynNetwork, currentEdge, attName, attValue, attType, start, end);
	}
	
	@Override
	public void addedGraphGraphics(DynNetwork<T> dynNetwork, String fill) 
	{
		
	}
	
	@Override
	public void addedNodeGraphics(DynNetwork<T> dynNetwork, CyNode currentNode, String type, String height, String width, String x, String y, String fill, String linew, String outline) 
	{

	}
	
	@Override
	public void addedEdgeGraphics(DynNetwork<T> dynNetwork, CyEdge currentEdge, String width, String fill) 
	{

	}

	@Override
	public void deletedGraph(DynNetwork<T> dynNetwork)
	{
		dynNetwork.removeAllIntervals();
		CyRootNetwork rootNetwork = this.rootNetworkManager.getRootNetwork(dynNetwork.getNetwork());
		rootNetwork.removeSubNetwork(rootNetwork.getBaseNetwork());
	}

	@Override
	public void deletedNode(DynNetwork<T> dynNetwork, CyNode node) 
	{
		dynNetwork.removeNode(node);
		dynNetwork.getNetwork().removeNodes(getNodeRemoveList(node));
	}
	
	@Override
	public void deletedEdge(DynNetwork<T> dynNetwork, CyEdge edge)
	{
		dynNetwork.removeEdge(edge);
		dynNetwork.getNetwork().removeEdges(getEdgeRemoveList(edge));
	}

	@SuppressWarnings("unchecked")
	private void setElement(DynNetwork<T> dynNetwork, String id, String label, String value, String start, String end)
	{
		dynNetwork.getNetwork().getRow(dynNetwork.getNetwork()).set(CyNetwork.NAME, nameUtil.getSuggestedNetworkTitle(label));
		DynInterval<T> interval = getInterval((Class<T>) String.class,(T)label,start,end);
		if (interval.getStart()<=interval.getEnd())
			dynNetwork.insertGraph(CyNetwork.NAME, interval);
		else
		{
			System.out.println("\nXGMML Parser Error: invalid interval for graph label=" + label + " start=" + start + " end=" + end + "\n");
			throw new IndexOutOfBoundsException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setElement(DynNetwork<T> dynNetwork, CyNode node, CyGroup group, String id, String label, String value, String start, String end)
	{
		dynNetwork.getNetwork().getRow(node).set(CyNetwork.NAME, label);
		DynInterval<T> interval;
		if (group==null)
			interval = getInterval((Class<T>) String.class,dynNetwork,(T)label,start,end);
		else
			interval = getInterval((Class<T>) String.class,dynNetwork,group.getGroupNode(),(T)label,start,end);
		if (interval.getStart()<=interval.getEnd())
			dynNetwork.insertNode(node, CyNetwork.NAME, interval);
		else
		{
			System.out.println("\nXGMML Parser Error: invalid interval for node label=" + label + " start=" + start + " end=" + end + "\n");
			throw new IndexOutOfBoundsException();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setElement(DynNetwork<T> dynNetwork, CyEdge edge, String id, String label, String value, String start, String end)
	{
		dynNetwork.getNetwork().getRow(edge).set(CyNetwork.NAME, label);
		DynInterval<T> interval = getInterval((Class<T>) String.class,dynNetwork,edge.getSource(),edge.getTarget(),(T)label,start,end);
		if (interval.getStart()<=interval.getEnd())
			dynNetwork.insertEdge(edge, CyNetwork.NAME, interval);
		else
		{
			System.out.println("\nXGMML Parser Error: invalid interval for edge label=" + label + " start=" + start + " end=" + end + "\n");
			throw new IndexOutOfBoundsException();
		}
	}

	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		DynInterval<T> interval = getInterval((Class<T>) attr.getClass(), dynNetwork, (T)attr ,start, end);
		if (interval.getStart()<=interval.getEnd())
		{
			addRow(dynNetwork.getNetwork(), dynNetwork.getNetwork().getDefaultNetworkTable(), dynNetwork.getNetwork(), attName, attr);
			dynNetwork.insertGraphAttr(attName, interval);
		}
		else
		{
			String label = dynNetwork.getNetworkLabel();
			System.out.println("\nXGMML Parser Warning: skipped invalid interval for graph label=" + label + " attr=" + attName +" start=" + start + " end=" + end);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyNode node, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		DynInterval<T> interval = getInterval((Class<T>) attr.getClass(),dynNetwork, (T)attr ,start, end);
		if (interval.getStart()<=interval.getEnd())
		{
			addRow(dynNetwork.getNetwork(), dynNetwork.getNetwork().getDefaultNodeTable(), node, attName, attr);
			dynNetwork.insertNodeAttr(node, attName, interval);
		}
		else
		{
			String label = dynNetwork.getNodeLabel(node);
			System.out.println("\nXGMML Parser Warning: skipped invalid interval for node label=" + label + " attr=" + attName +" start=" + start + " end=" + end);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setAttributes(DynNetwork<T> dynNetwork, CyEdge edge, String attName, String attValue, String attType, String start, String end)
	{
		Object attr = typeMap.getTypedValue(typeMap.getType(attType), attValue);
		DynInterval<T> interval = getInterval((Class<T>) attr.getClass(),dynNetwork,edge, (T)attr, start, end);
		if (interval.getStart()<=interval.getEnd())
		{
			addRow(dynNetwork.getNetwork(), dynNetwork.getNetwork().getDefaultEdgeTable(), edge, attName, attr);
			dynNetwork.insertEdgeAttr(edge, attName, interval);
		}
		else
		{
			String label = dynNetwork.getEdgeLabel(edge);
			System.out.println("\nXGMML Parser Warning: skipped invalid interval for edge label=" + label + " attr=" + attName +" start=" + start + " end=" + end);
		}
	}
	
	@Override
	public void finalizeNetwork(DynNetwork<T> dynNetwork) 
	{
		dynNetwork.finalizeNetwork();
		for (CyNode node : metaNodes)
			dynNetwork.getNetwork().removeEdges(dynNetwork.getNetwork().getAdjacentEdgeList(node, CyEdge.Type.ANY));
		dynNetwork.getNetwork().removeNodes(metaNodes);
//		dynNetwork.print();
	}
	
	@Override
	public DynNetworkView<T> createView(DynNetwork<T> dynNetwork) 
	{
		// do nothing
		return null;
	}

	private void addRow(CyNetwork currentNetwork, CyTable table, CyIdentifiable ci, String attName, Object attr)
	{
		if (table.getColumn(attName)==null)
			table.createColumn(attName, attr.getClass(), false);
		currentNetwork.getRow(ci).set(attName, attr);
	}

	private DynNetwork<T> createGraph(String directed, String id, String label, String start, String end)
	{
		CyRootNetwork rootNetwork = this.rootNetworkManager.getRootNetwork(networkFactory.createNetwork());
		DynNetworkImpl<T> dynNetwork = new DynNetworkImpl<T>(rootNetwork.getBaseNetwork(), groupManager, directed.equals("1")?true:false);
		return dynNetwork;
	}

	private CyNode createNode(DynNetwork<T> dynNetwork, CyGroup group, String id, String label, String start, String end)
	{
		CyNode node;
		if (!dynNetwork.containsCyNode(id))
		{
			node = dynNetwork.getNetwork().addNode();
			dynNetwork.setCyNode(id, node.getSUID());
		}
		else
		{
			System.out.println("\nXGMML Parser Warning: updated node label=" + label + " (duplicate)");
			node = dynNetwork.getNetwork().getNode(dynNetwork.getNode(id));
		}
		
		if (group!=null)
		{
			ArrayList<CyNode> groupNodes = new ArrayList<CyNode>();
			groupNodes.add(node);
			group.addNodes(groupNodes);
		}
		
		return node;
	}
	
	private CyEdge createEdge(DynNetwork<T> dynNetwork, String id, String label, String source, String target, String start, String end)
	{
			CyNode nodeSource = dynNetwork.getNetwork().getNode(dynNetwork.getNode(source));
			CyNode nodeTarget = dynNetwork.getNetwork().getNode(dynNetwork.getNode(target));

			CyEdge edge;
			if (!dynNetwork.containsCyEdge(id))
			{
				edge = dynNetwork.getNetwork().addEdge(nodeSource, nodeTarget, dynNetwork.isDirected());
				dynNetwork.setCyEdge(id, edge.getSUID());
			}
			else
			{
				System.out.println("\nXGMML Parser Warning: updated edge label=" + label 
						+ " source=" + source + " target=" + target + " (duplicate)");
				edge = dynNetwork.getNetwork().getEdge(dynNetwork.getEdge(id));
			}

			for (CyGroup group : groupManager.getGroupsForNode(nodeSource))
			{
				ArrayList<CyEdge> groupEdges = new ArrayList<CyEdge>();
				groupEdges.add(edge);
				group.addEdges(groupEdges);
			}
			
			for (CyGroup group : groupManager.getGroupsForNode(nodeTarget))
			{
				ArrayList<CyEdge> groupEdges = new ArrayList<CyEdge>();
				groupEdges.add(edge);
				group.addEdges(groupEdges);
			}

			return edge;
	}
	
	private Collection<CyNode> getNodeRemoveList(CyNode node)
	{
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		list.add(node);
		return list;
	}
	
	private Collection<CyEdge> getEdgeRemoveList(CyEdge edge)
	{
		ArrayList<CyEdge> list = new ArrayList<CyEdge>();
		list.add(edge);
		return list;
	}

	private DynInterval<T> getInterval(Class<T> type, T value, String start, String end)
	{
		return new DynInterval<T>(type, value, parseStart(start), parseEnd(end));
	}

	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(dynNetwork.getNetwork(), CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)),
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyNode node, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(node, CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)),
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyNode souce, CyNode target, T value, String start, String end)
	{
		DynAttribute<T> parentAttrSoruce = dynNetwork.getDynAttribute(souce, CyNetwork.NAME);
		DynAttribute<T> parentAttrTarget = dynNetwork.getDynAttribute(target, CyNetwork.NAME);
			return new DynInterval<T>(type, value, 
				max(max(parentAttrSoruce.getMinTime(),parentAttrTarget.getMinTime()), parseStart(start)) ,
				min(min(parentAttrSoruce.getMaxTime(),parentAttrTarget.getMaxTime()), parseEnd(end)) );
	}
	
	private DynInterval<T> getInterval(Class<T> type, DynNetwork<T> dynNetwork, CyEdge edge, T value, String start, String end)
	{
		DynAttribute<T> parentAttr = dynNetwork.getDynAttribute(edge, CyNetwork.NAME);
		return new DynInterval<T>(type, value, 
				max(parentAttr.getMinTime(), parseStart(start)),
				min(parentAttr.getMaxTime(), parseEnd(end)) );
	}

	private double min(double a, double b)
	{
		if (a>b)
			return b;
		else
			return a;
	}
	
	private double max(double a, double b)
	{
		if (a<b)
			return b;
		else
			return a;
	}
	
	private double parseStart(String start)
	{
		if (start!=null)
			return Double.parseDouble(start);
		else
			return Double.NEGATIVE_INFINITY;
	}
	
	private double parseEnd(String end)
	{
		if (end!=null)
			return Double.parseDouble(end);
		else
			return Double.POSITIVE_INFINITY;
	}

}
