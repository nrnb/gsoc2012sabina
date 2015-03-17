package org.cytoscape.dyn.internal.view.model;

import org.cytoscape.dyn.internal.model.DynNetwork;

/**
 * <code> GraphGraphicsAttribute </code> is used to store graph graphics attributes
 * to be added later to the visualization.
 * 
 * @author Sabina Sara Pfister
 * 
 */
public class GraphGraphicsAttribute<T>
{
	private final DynNetwork<T> currentNetwork;

	private final String fill;
	
	/**
	 * <code> GraphGraphicsAttribute </code> constructor.
	 * @param currentNetwork
	 * @param currentEdge
	 * @param width
	 * @param fill
	 */
	public GraphGraphicsAttribute(
			DynNetwork<T> currentNetwork,
			String fill)
	{
		this.currentNetwork = currentNetwork;
		this.fill = fill;
	}
	
	/**
	 * Add edge graphics attribute.
	 * @param viewFactory
	 */
	public void add(DynNetworkViewFactory<T> viewFactory)
	{
		viewFactory.setGraphGraphics(currentNetwork, fill);
	}
	
}
