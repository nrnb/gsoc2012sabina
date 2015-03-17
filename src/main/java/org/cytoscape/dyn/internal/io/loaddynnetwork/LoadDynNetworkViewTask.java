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

package org.cytoscape.dyn.internal.io.loaddynnetwork;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.dyn.internal.io.event.Sink;
import org.cytoscape.dyn.internal.io.event.Source;
import org.cytoscape.dyn.internal.model.DynNetwork;
import org.cytoscape.dyn.internal.model.DynNetworkManager;
import org.cytoscape.dyn.internal.view.model.DynNetworkView;
import org.cytoscape.dyn.internal.view.model.DynNetworkViewFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskMonitor;

/**
 * <code> LoadNetworkViewTask </code> implements {@link Task} and is responsible
 * for creating the network view {@link DynNetworkView} from a network {@link DynNetwork}.
 * 
 * @author Sabina Sara Pfister
 *
 */
public final class LoadDynNetworkViewTask<T> extends AbstractTask implements Source<T> 
{
	private final CyApplicationManager appManager;
	private final DynNetworkManager<T> dynNetworkManager;
	
	private Sink<T> sink;
	
	/**
	 * <code> LoadDynNetworkViewTask </code> constructor.
	 * @param appManager
	 * @param dynNetworkManager
	 * @param dynNetworkViewFactory
	 */
	public LoadDynNetworkViewTask(
			final CyApplicationManager appManager,
			final DynNetworkManager<T> dynNetworkManager,
			final DynNetworkViewFactory<T> dynNetworkViewFactory)
	{
		this.appManager = appManager;
		this.dynNetworkManager = dynNetworkManager;
		this.addSink(dynNetworkViewFactory);
	}
	
	/**
	 * Run.
	 */
	public void run(TaskMonitor tm) throws Exception
	{
		tm.setProgress(0.0);
		DynNetwork<T> dynNetwork = dynNetworkManager.getDynNetwork(appManager.getCurrentNetwork());
		sink.createView(dynNetwork);
		sink.finalizeNetwork(dynNetwork);
		tm.setProgress(1.0);
	}

	@Override
	public void addSink(Sink<T> sink) 
	{
		this.sink = sink;
	}
	
	@Override
	public void removeSink(Sink<T> sink) 
	{
		if (this.sink == sink)
			this.sink = null;
	}
	
}
