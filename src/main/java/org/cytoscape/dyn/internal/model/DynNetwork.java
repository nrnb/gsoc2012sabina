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

import java.util.List;

import org.cytoscape.dyn.internal.model.attribute.DynAttribute;
import org.cytoscape.dyn.internal.model.tree.DynInterval;
import org.cytoscape.dyn.internal.model.tree.DynIntervalTree;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

/**
 * <code> DynNetwork </code> is an the interface to the object that represents a dynamic network
 * composed of {@link CyNode}s, connecting {@link CyEdge}s, attributes, and
 * the respective time intervals {@link DynInterval}s. It provides the link 
 * to the current static {@link CyNewtork}. In addition it maintains the
 * information about dynamic attributes {@link DynAttribute} in form of a
 * {@link DynIntervalTree}. 
 *
 * @author Sabina Sara Pfister
 *
 */
public interface DynNetwork<T>
{
	/**
	 * Insert graph.
	 * @param column
	 * @param interval
	 */
	public void insertGraph(String column, DynInterval<T> interval);
	
	/**
	 * Insert node.
	 * @param node
	 * @param column
	 * @param interval
	 */
	public void insertNode(CyNode node, String column, DynInterval<T> interval);
	
	/**
	 * Insert edge.
	 * @param ede
	 * @param column
	 * @param interval
	 */
	public void insertEdge(CyEdge ede, String column, DynInterval<T> interval);
	
	/**
	 * Insert graph attribute.
	 * @param column
	 * @param interval
	 */
	public void insertGraphAttr(String column, DynInterval<T> interval);
	
	/**
	 * Insert node attribute.
	 * @param node
	 * @param column
	 * @param interval
	 */
	public void insertNodeAttr(CyNode node, String column, DynInterval<T> interval);
	
	/**
	 * Insert edge attribute.
	 * @param ede
	 * @param column
	 * @param interval
	 */
	public void insertEdgeAttr(CyEdge ede, String column, DynInterval<T> interval);
	
	/**
	 * Remove graph.
	 */
	public void removeAllIntervals();
	
	/**
	 * Remove node.
	 * @param node
	 */
	public void removeNode(CyNode node);
	
	/**
	 * Remove edge.
	 * @param edge
	 */
	public void removeEdge(CyEdge edge);
	
	/**
	 * Remove graph attribute.
	 */
	public void removeGraphAttr();
	
	/**
	 * Remove node attribute.
	 * @param node
	 */
	public void removeNodeAttr(CyNode node);
	
	/**
	 * Remove edge attribute.
	 * @param edge
	 */
	public void removeEdgeAttr(CyEdge edge);
	
	/**
	 * Get all graph intervals (without attribute intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getGraphIntervals();
	
	/**
	 * Get all node intervals (without attribute intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getNodesIntervals();
	
	/**
	 * Get all edge intervals (without attribute intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getEdgesIntervals();
	
	/**
	 * Get all graph attributes intervals (without graph intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getGraphAttrIntervals();
	
	/**
	 * Get all node attributes intervals (without node intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getNodesAttrIntervals();
	
	/**
	 * Get all edge attributes intervals (without edge intervals).
	 * @return interval list
	 */
	public List<DynInterval<T>> getEdgesAttrIntervals();
	
	/**
	 * Get all intervals belonging to the network (with attribute intervals). 
	 * If the network is not contained in this DynNetwork, a empty list id returned. 
	 * @param net
	 * @return interval list
	 */
	public List<DynInterval<T>> getIntervals(CyNetwork net);

	/**
	 * Get all intervals belonging to this node (with attribute intervals). 
	 * If the node is not contained in this DynNetwork, a empty list id returned.
	 * @param node
	 * @return interval list
	 */
	public List<DynInterval<T>> getIntervals(CyNode node);

	/**
	 * Get all intervals belonging to this edge (with attribute intervals). 
	 * If the edge is not contained in this DynNetwork, a empty list id returned.
	 * @param edge
	 * @return interval list
	 */
	public List<DynInterval<T>> getIntervals(CyEdge edge);
	
	/**
	 * Search overlapping intervals for nodes given an interval.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchNodes(DynInterval<T> interval);
	
	/**
	 * Search nodes given an interval.
	 * @param interval
	 * @return list of nodes
	 */
	public List<CyNode> getVisibleNodeList(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edges given an interval.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchEdges(DynInterval<T> interval);
	
	/**
	 * Search edges given an interval.
	 * @param interval
	 * @return list of edges
	 */
	public List<CyEdge> getVisibleEdgeList(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for graph attributes given an interval.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchGraphsAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for graph attributes given an interval
	 * filtered by the given attribute name
	 * @param interval
	 * @param attName
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchGraphsAttr(DynInterval<T> interval, String attName);
	
	/**
	 * Search overlapping intervals for node attributes given an interval.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for node attributes given an interval
	 * filtered by the given attribute name
	 * @param interval
	 * @param attName
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchNodesAttr(DynInterval<T> interval, String attName);
	
	/**
	 * Search overlapping intervals for edge attributes given an interval.
	 * @param interval
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval);
	
	/**
	 * Search overlapping intervals for edge attributes given an interval.
	 * filtered by the given attribute name.
	 * @param interval
	 * @param attName
	 * @return list of overlapping intervals
	 */
	public List<DynInterval<T>> searchEdgesAttr(DynInterval<T> interval, String attName);
	
	/**
	 * Search not overlapping intervals for nodes given an interval.
	 * @param interval
	 * @return list of not overlapping intervals
	 */
	public List<DynInterval<T>> searchNodesNot(DynInterval<T> interval);
	
	/**
	 * Search nodes outside the interval.
	 * @param interval
	 * @return list of invisible nodes
	 */
	public List<CyNode> getVisibleNodeNotList(DynInterval<T> interval);
	
	/**
	 * Search not overlapping intervals for edges given an interval.
	 * @param interval
	 * @return list of not overlapping intervals
	 */
	public List<DynInterval<T>> searchEdgesNot(DynInterval<T> interval);
	
	/**
	 * Search edges outside the interval.
	 * @param interval
	 * @return list of invisible edges
	 */
	public List<CyEdge> getVisibleEdgeNotList(DynInterval<T> interval);

	/**
	 * Get a list of times at which events occur.
	 * @return time list
	 */
	public List<Double> getEventTimeList();
	
	/**
	 * Get a list of times at which events occur filtered by attribute name.
	 * @param attName
	 * @return
	 */
	public List<Double> getEventTimeList(String attName);
	
	/**
	 * Get dynamic attribute for given network and name.
	 * @param network
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyNetwork network, String column);
	
	/**
	 *  Get dynamic attribute for given node and name.
	 * @param node
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyNode node, String column);
	
	/**
	 *  Get dynamic attribute for given edge and name.
	 * @param edge
	 * @param column
	 * @return dynamic attribute
	 */
	public DynAttribute<T> getDynAttribute(CyEdge edge, String column);
	
    /**
     * Get network.
     * @return CyNetwork
     */
	public CyNetwork getNetwork();
	
	/**
	 * Get network label.
	 * @return network label
	 */
	public String getNetworkLabel();
	
	/**
	 * Get node.
	 * @param id
	 * @return node
	 */
	public long getNode(String id);
	
	/**
	 * Get node label.
	 * @param node
	 * @return node label
	 */
	public String getNodeLabel(CyNode node);
	
	/**
	 * Get node.
	 * @param interval
	 * @return node
	 */
	public CyNode getNode(DynInterval<T> interval);
	
	/**
	 * Get edge.
	 * @param id
	 * @return edge
	 */
	public long getEdge(String id);
	
	/**
	 * Get edge label.
	 * @param edge
	 * @return edge label
	 */
	public String getEdgeLabel(CyEdge edge);
	
	/**
	 * Get edge.
	 * @param interval
	 * @return edge
	 */
	public CyEdge getEdge(DynInterval<T> interval);
	
	/**
	 * Contains node.
	 * @param id
	 * @return boolean
	 */
	public boolean containsCyNode(String id);
	
	/**
	 * Contains edge.
	 * @param id
	 * @return boolean
	 */
	public boolean containsCyEdge(String id);
	
	/**
	 * Set node.
	 * @param id
	 * @param value
	 */
	public void setCyNode(String id, long value);
	
	/**
	 * Set Edge.
	 * @param id
	 * @param value
	 */
	public void setCyEdge(String id, long value);
	
	/**
	 * Write to graph table.
	 * @param name
	 * @param value
	 */
	public void writeGraphTable(String name, T value);
	
	/**
	 * Read graph table.
	 * @param name
	 * @param value
	 * @return value
	 */
	public T readGraphTable(String name, T value);
	
	/**
	 * Write to node table.
	 * @param node
	 * @param name
	 * @param value
	 */
	public void writeNodeTable(CyNode node, String name, T value);
	
	/**
	 * Read node table.
	 * @param node
	 * @param name
	 * @param value
	 * @return value
	 */
	public T readNodeTable(CyNode node, String name, T value);
	
	/**
	 * Write to edge table.
	 * @param edge
	 * @param name
	 * @param value
	 */
	public void writeEdgeTable(CyEdge edge, String name, T value);
	
	/**
	 * Read edge table.
	 * @param edge
	 * @param name
	 * @param value
	 * @return value
	 */
	public T readEdgeTable(CyEdge edge, String name, T value);
	
	/**
	 * Get a list of double attributes for this network.
	 * @return double attribute map
	 */
	public List<String>  getGraphAttributes() ;
	
	/**
	 * Get a list of double attributes for nodes.
	 * @return double attribute map
	 */
	public List<String>  getNodeAttributes();
	
	/**
	 * Get a list of double attributes for edges.
	 * @return double attribute list
	 */
	public List<String>  getEdgeAttributes();
	
	/**
	 * Finalize network. We perform here all operations that require the network construction
	 * to be finished.
	 */
	public void finalizeNetwork();
	
	/**
	 * Get minimum time.
	 * @return minimum time
	 */
	public double getMinTime();
	
	/**
	 * Get maximum time.
	 * @return maximum time
	 */
	public double getMaxTime();
	
	/**
	 * Get minimum value over the entire dynamic network for this attribute.
	 * @param AttName
	 * @param type
	 * @return
	 */
	public T getMinValue(String AttName, Class<? extends CyIdentifiable> type);
	
	/**
	 * Get maximum value over the entire dynamic network for this attribute.
	 * @param AttName
	 * @param type
	 * @return
	 */
	public T getMaxValue(String AttName, Class<? extends CyIdentifiable> type);
	
	/**
	 * Get if the network is directed
	 * @return boolean
	 */
	public boolean isDirected();
	
	/**
	 * Print out network structure.
	 * @return
	 */
	public void print();

}
