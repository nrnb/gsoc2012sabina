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

package org.cytoscape.dyn.internal.model.snapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNode;

/**
 * <code> DynNetworkSnapshotImpl </code> implements all methods to investigate a network in
 * time snapshots within the given time interval. It's computationally intensive, and should 
 * be used only when it is not sufficient to search for time intervals only.
 * 
 * @author Sabina Sara Pfister
 *
 * @param <T>
 */
public class DynNetworkSnapshotImpl<T> implements DynNetworkSnapshot<T>
{
	protected final DynNetwork<T> network;
	protected final DynNetworkView<T> view;
	
	protected final List<CyNode> nodeList;
	protected final List<CyEdge> edgeList;
	
	protected final Map<CyNode,List<CyEdge>> inEdges;
	protected final Map<CyNode,List<CyEdge>> outEdges;
	
	private String attName;
	
	private DynInterval<T> timeInterval;
	
	private List<DynInterval<T>> currentNodes;
	private List<DynInterval<T>> currentEdges;
	private List<DynInterval<T>> currentEdgesAttr;
	
	private final Map<CyNode,DynInterval<T>> nodeIntervals;
	private final Map<CyEdge,DynInterval<T>> edgeIntervals;
	private final Map<CyEdge,DynInterval<T>> edgeAttrIntervals;
	private final Map<CyEdge,List<Double>> edgeAttrValues;
	
	private final Map<CyEdge,Double> weightMap;

	/**
	 * <code> DynNetworkSnapshotImpl </code> constructor.
	 * @param view
	 */
	public DynNetworkSnapshotImpl(final DynNetworkView<T> view) 
	{
		this.view = view;
		this.network = view.getNetwork();
		
		this.nodeList = new ArrayList<CyNode>();
		this.edgeList = new ArrayList<CyEdge>();
		
		this.inEdges = new HashMap<CyNode,List<CyEdge>>();
		this.outEdges = new HashMap<CyNode,List<CyEdge>>();
		
		this.currentNodes = new ArrayList<DynInterval<T>>();
		this.currentEdges = new ArrayList<DynInterval<T>>();
		this.currentEdgesAttr = new ArrayList<DynInterval<T>>();
		
		this.nodeIntervals = new HashMap<CyNode,DynInterval<T>>();
		this.edgeIntervals = new HashMap<CyEdge,DynInterval<T>>();
		this.edgeAttrIntervals = new HashMap<CyEdge,DynInterval<T>>();
		this.edgeAttrValues = new HashMap<CyEdge,List<Double>>();
		
		this.weightMap = new HashMap<CyEdge,Double>();
	}
	
	/**
	 * <code> DynNetworkSnapshotImpl </code> constructor.
	 * @param view
	 * @param attName
	 */
	public DynNetworkSnapshotImpl(final DynNetworkView<T> view, String attName) 
	{
		this(view);
		this.attName = attName;
	}
	
	@Override
	public void setInterval(DynInterval<T> timeInterval) 
	{
		this.timeInterval = timeInterval; 
		
		for (DynInterval<T> i : getChangedNodeIntervals(timeInterval))
			if (i.isOn())
			{
				CyNode node = network.getNode(i);
				addNode(node);
				nodeIntervals.put(node, i);
			}
			else
			{
				CyNode node = network.getNode(i);
				removeNode(node);
				nodeIntervals.remove(node);
			}
		
		for (DynInterval<T> i : getChangedEdgeIntervals(timeInterval))
			if (i.isOn())
			{
				CyEdge edge = network.getEdge(i);
				addEdge(edge);
				edgeIntervals.put(edge, i);
			}
			else
			{
				CyEdge edge = network.getEdge(i);
				removeEdge(network.getEdge(i));
				edgeIntervals.remove(edge);
			}
		
		if (attName!=null && !attName.equals("none"))
		{
			for (DynInterval<T> i : getChangedEdgeAttrIntervals(timeInterval))
				if (i.isOn())
				{
					CyEdge edge = network.getEdge(i);
					addEdgeAttr(edge, i);
					edgeAttrIntervals.put(edge, i);
				}
				else
				{
					CyEdge edge = network.getEdge(i);
					removeEdgeAttr(edge);
					edgeAttrIntervals.remove(edge);
				}
//			updateEdgeAttr();
		}
	}
	
	@Override
	public List<CyNode> getNeighbors(CyNode node)
	{
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		if (this.inEdges.containsKey(node))
			for (CyEdge edge : this.inEdges.get(node))
				list.add(edge.getTarget());
		if (this.outEdges.containsKey(node))
			for (CyEdge edge : this.outEdges.get(node))
				list.add(edge.getSource());
		return list;
	}
	
	@Override
	public List<CyEdge> getEdges(CyNode node)
	{
		ArrayList<CyEdge> list = new ArrayList<CyEdge>();
		if (this.inEdges.containsKey(node))
			list.addAll(this.inEdges.get(node));
		if (this.outEdges.containsKey(node))
			list.addAll(this.outEdges.get(node));
		return list;
	}
	
	@Override
	public List<CyEdge> getInEdges(CyNode node)
	{
		if (this.inEdges.containsKey(node))
			return this.inEdges.get(node);
		else
			return new ArrayList<CyEdge>();
	}
	
	@Override
	public List<CyEdge> getOutEdges(CyNode node)
	{
		if (this.outEdges.containsKey(node))
			return this.outEdges.get(node);
		else
			return new ArrayList<CyEdge>();
	}
	
	@Override
	public List<CyNode> getNodes(CyEdge edge)
	{
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		list.add(edge.getSource());
		list.add(edge.getTarget());
		return list;
	}
	
	@Override
	public CyNode getOpposite(CyNode node, CyEdge edge)
	{
		if (edge.getSource()==node)
			return edge.getTarget();
		else
			return edge.getSource();
	}
	
	@Override
	public CyEdge findEdge(CyNode node1, CyNode node2)
	{
		for (CyEdge edge : this.edgeList)
			if (edge.getSource()==node1 && edge.getTarget()==node2)
				return edge;
			else if (edge.getSource()==node2 && edge.getTarget()==node1)
				return edge;
		return null;
	}
	
	@Override
	public List<CyEdge> findEdgeSet(CyNode node1, CyNode node2)
	{
		ArrayList<CyEdge> list = new ArrayList<CyEdge>();
		for (CyEdge edge : this.edgeList)
			if (edge.getSource()==node1 && edge.getTarget()==node2)
				list.add(edge);
			else if (edge.getSource()==node2 && edge.getTarget()==node1)
				list.add(edge);
		return list;
	}
	
	@Override
	public boolean isNeighbor(CyNode node1, CyNode node2)
	{
		for (CyEdge edge : this.inEdges.get(node1))
			if (edge.getSource()==node2 || edge.getTarget()==node2)
				return true;
		for (CyEdge edge : this.outEdges.get(node1))
				if (edge.getSource()==node2 || edge.getTarget()==node2)
					return true;
		return false;
	}
	
	@Override
	public boolean isIncident(CyNode node, CyEdge edge)
	{
		if (edge.getSource()==node || edge.getTarget()==node)
			return true;
		else
			return false;
	}
	
	@Override
	public List<CyEdge> getEdges(boolean isDirected)
	{
		ArrayList<CyEdge> list = new ArrayList<CyEdge>();
		for (CyEdge edge : this.edgeList)
			if (edge.isDirected()==isDirected)
				list.add(edge);
		return list;
	}
	
	@Override
	public  int getEdgeCount(boolean isDirected)
	{
		return getEdges(isDirected).size();
	}
	
	@Override
	public List<CyNode> getNodes() 
	{
		return nodeList;
	}

	@Override
	public List<CyEdge> getEdges() 
	{
		return edgeList;
	}
	
	@Override
	public Map<CyEdge,? extends Number> getWeightMap()
	{
		return weightMap;
	}
	
	@Override
	public int getDegree(CyNode node)
	{
		return this.inDegree(node)+this.outDegree(node);
	}
	
	@Override
	public int inDegree(CyNode node)
	{
		if (this.inEdges.containsKey(node))
			return this.inEdges.get(node).size();
		else
			return 0;
	}
	
	@Override
	public int outDegree(CyNode node)
	{
		if (this.outEdges.containsKey(node))
			return this.outEdges.get(node).size();
		else
			return 0;
	}
	
	@Override
	public List<CyNode> getPredecessors(CyNode node)
	{
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		if (this.inEdges.containsKey(node))
			for (CyEdge edge : this.inEdges.get(node))
				list.add(edge.getSource());
		return list;
	}
	
	@Override
	public List<CyNode> getSuccessors(CyNode node)
	{
		ArrayList<CyNode> list = new ArrayList<CyNode>();
		if (this.outEdges.containsKey(node))
			for (CyEdge edge : this.outEdges.get(node))
				list.add(edge.getTarget());
		return list;
	}
	
	@Override
	public boolean isPredecessor(CyNode node1, CyNode node2)
	{
		if (this.inEdges.containsKey(node2))
			if (this.inEdges.get(node2).contains(node1))
				return true; 
		return false;
	}
	
	@Override
	public boolean isSuccessor(CyNode node1, CyNode node2)
	{
		if (this.outEdges.containsKey(node1))
			if (this.outEdges.get(node1).contains(node2))
				return true; 
		return false;
	}
	
	@Override
	public int getPredecessorCount(CyNode node)
	{
		return getPredecessors(node).size();
	}
	
	@Override
	public int getSuccessorCount(CyNode node)
	{
		return getSuccessors(node).size();
	}
	
	@Override
	public boolean conatinsNode(CyNode node) 
	{
		return nodeList.contains(node);
	}
	
	@Override
	public boolean conatinsEdge(CyEdge edge) 
	{
		return edgeList.contains(edge);
	}
	
	@Override
	public int getNodeCount() 
	{
		return nodeList.size();
	}
	
	@Override
	public int getEdgeCount() 
	{
		return edgeList.size();
	}

	@Override
	public DynNetworkView<T> getNetworkView() 
	{
		return view;
	}
	
	@Override
	public DynInterval<T> getInterval()
	{
		return this.timeInterval;
	}
	
	@Override
	public DynInterval<T> getInterval(CyNode node)
	{
		return this.nodeIntervals.get(node);
	}
	
	@Override
	public DynInterval<T> getInterval(CyEdge edge)
	{
		return this.edgeIntervals.get(edge);
	}

	@Override
	public void print() 
	{
		System.out.println("\n********************");
		for (CyNode node : this.nodeList)
		{
			System.out.println("\nLABEL:" + network.getNodeLabel(node) +
					" all-edges:" + this.edgeList.size() +
					" in-edges:" + this.inDegree(node) +
					" out-edges:" + this.outDegree(node));
			for (CyEdge edge: this.getInEdges(node))
				System.out.println("    in-edge label:" + network.getEdgeLabel(edge));
			for (CyEdge edge: this.getOutEdges(node))
				System.out.println("    out-edge label:" + network.getEdgeLabel(edge));
			
			System.out.println(" predecessors:");
			for (CyNode n: this.getPredecessors(node))
				System.out.println("    p-node:" + network.getNodeLabel(n));
			System.out.println(" successors:");
			for (CyNode n: this.getSuccessors(node))
				System.out.println("    s-node:" + network.getNodeLabel(n));
		}
	}

	protected void addNode(CyNode node)
	{
		if (node!=null)
		{
			this.nodeList.add(node);
			for (CyEdge edge : this.edgeList)
				if (edge.getSource()==node)
					addOutEdge(node, edge);
				else if (edge.getTarget()==node)
					addInEdge(node, edge);
		}
	}

	protected void removeNode(CyNode node)
	{
		if (node!=null)
		{
			this.inEdges.remove(node);
			this.outEdges.remove(node);
			this.nodeList.remove(node);
		}
	}
	
	protected void addEdge(CyEdge edge)
	{
		if (edge!=null)
		{
			this.edgeList.add(edge);
			for (CyNode node : this.nodeList)
			{
				if (edge.getSource()==node)
					addOutEdge(node, edge);
				if (edge.getTarget()==node)
					addInEdge(node, edge);
			}
			
			edgeAttrValues.put(edge, new ArrayList<Double>());
			weightMap.put(edge, new Double(1));
		}
	}

	protected void removeEdge(CyEdge edge)
	{
		if (edge!=null)
		{
			for (CyNode node : this.inEdges.keySet())
				this.inEdges.get(node).remove(edge);
			for (CyNode node : this.outEdges.keySet())
				this.outEdges.get(node).remove(edge);
			this.edgeList.remove(edge);
			weightMap.remove(edge);
			edgeAttrValues.remove(edge);
		}
	}

	protected void addEdgeAttr(CyEdge edge, DynInterval<T> i)
	{
		if (edge!=null)
		{
			if (i.getOnValue() instanceof Integer)
				weightMap.put(edge, new Double((Integer)i.getOnValue()));
//				edgeAttrValues.get(edge).add(new Double((Integer)i.getOnValue()));
			else if (i.getOnValue() instanceof Double)
				weightMap.put(edge, new Double((Double)i.getOnValue()));
//				edgeAttrValues.get(edge).add((Double)i.getOnValue());
		}
	}
	
	protected void updateEdgeAttr()
	{
		for (CyEdge edge : edgeAttrValues.keySet())
			if (edge!=null)
				weightMap.put(edge, getAverage(edgeAttrValues.get(edge)));
	}
	
	protected void removeEdgeAttr(CyEdge edge)
	{
		if (edge!=null)
		{
			edgeAttrIntervals.remove(edge);
			edgeAttrValues.put(edge, new ArrayList<Double>());
		}
	}

	protected void addInEdge(CyNode node, CyEdge edge)
	{
		if (this.inEdges.containsKey(node))
		{
			if(!this.inEdges.get(node).contains(edge))
				this.inEdges.get(node).add(edge);
		}
		else
		{
			ArrayList<CyEdge> list = new ArrayList<CyEdge>();
			list.add(edge);
			this.inEdges.put(node, list);
		}
	}

	protected void addOutEdge(CyNode node, CyEdge edge)
	{
		if (this.outEdges.containsKey(node))
		{
			if(!this.outEdges.get(node).contains(edge))
				this.outEdges.get(node).add(edge);
		}
		else
		{
			ArrayList<CyEdge> list = new ArrayList<CyEdge>();
			list.add(edge);
			this.outEdges.put(node, list);
		}
	}
	
	private List<DynInterval<T>> getChangedNodeIntervals(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = network.searchNodes(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentNodes, tempList);
		currentNodes = tempList;
		return changedList;
	}
	
	private List<DynInterval<T>> getChangedEdgeIntervals(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = network.searchEdges(interval);
		List<DynInterval<T>> changedList = nonOverlap(currentEdges, tempList);
		currentEdges = tempList;
		return changedList;
	}
	
	private List<DynInterval<T>> getChangedEdgeAttrIntervals(DynInterval<T> interval)
	{
		List<DynInterval<T>> tempList = network.searchEdgesAttr(interval,attName);
		List<DynInterval<T>> changedList = nonOverlap(currentEdgesAttr, tempList);
		currentEdgesAttr = tempList;
		return changedList;
	}

	private List<DynInterval<T>> nonOverlap(List<DynInterval<T>> list1, List<DynInterval<T>> list2) 
	{
		List<DynInterval<T>> diff = new ArrayList<DynInterval<T>>();
		for (DynInterval<T> i : list1)
			if (!list2.contains(i))
			{
				diff.add(i);
				i.setOn(false);
			}
		for (DynInterval<T> i : list2)
			if (!list1.contains(i))
			{
				diff.add(i);
				i.setOn(true);
			}
		return diff;
	}
	
	private Double getAverage(Collection<Double> collection)
	{
		if (collection.size()>0)
		{
			double sum = 0;
			for (Double item : collection)
				sum =+ item;
			return sum/collection.size();
		}
		else
			return new Double(1);
	}

}
