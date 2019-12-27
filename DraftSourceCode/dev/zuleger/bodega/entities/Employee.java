package dev.zuleger.bodega.entities;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import dev.zuleger.bodega.constants.Show;

/**
 * Abstract class that represents a general employee at Bodega Brew Pub at any given time.
 * Attributes include pay and skill, 
 * raises would be distributed based off of data collection,
 * skill would logarithmically increase with time.
 * @author Travis Roy Zuleger
 * Date Last Modified: December 22, 2018
 *
 */
public abstract class Employee extends SimProcess 
{
	
	public Employee(Model owner, String name) 
	{
		super(owner, name, Show.ENTITIES_IN_TRACE);
	}

	@Override
	public abstract void lifeCycle() throws SuspendExecution;

}
