package dev.zuleger.bodega.entities;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;

public class Bartender extends Employee 
{

	public Bartender(Model owner, String name) 
	{
		super(owner, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lifeCycle() throws SuspendExecution 
	{
		
	}

}
