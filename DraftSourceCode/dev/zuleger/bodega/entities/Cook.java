package dev.zuleger.bodega.entities;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import dev.zuleger.bodega.model.BodegaModel;

public class Cook extends Employee
{

	public Cook(Model owner, String name) 
	{
		super(owner, name);
	}

	@Override
	public void lifeCycle() throws SuspendExecution 
	{
		BodegaModel model = (BodegaModel) this.getModel();
		while(true)
		{
			if(model.presentTime().getTimeAsDouble() % 60 == 0)
				model.grossProfit.update(-11);
		}
	}

}
