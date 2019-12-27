package dev.zuleger.bodega.entities;

import java.util.concurrent.TimeUnit;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.dist.DiscreteDistEmpirical;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;
import dev.zuleger.bodega.constants.Food;
import dev.zuleger.bodega.constants.Show;
import dev.zuleger.bodega.model.BodegaModel;

public class Customer extends SimProcess
{
	
	private boolean orderingFromKitchen;
	private DiscreteDistEmpirical<Integer> foodSelectionDist;
	
	public Customer(Model owner, String name, DiscreteDistEmpirical<Integer> foodSelectionDist) 
	{
		super(owner, name, Show.ENTITIES_IN_TRACE);
		this.foodSelectionDist = foodSelectionDist;
	}

	@Override
	public void lifeCycle() throws SuspendExecution 
	{
		BodegaModel model = (BodegaModel) this.getModel();

		orderingFromKitchen = true;
		boolean balked;
		int balkSample = model.balkChanceDist.sample().intValue();
		if(orderingFromKitchen)
			balked = balkSample <= model.kitchenOrderQueue.size();
		else
			balked = balkSample <= model.barOrderQueue.size();
		
		if(!balked)
		{
			Cook cook = null;
			Bartender bartender = null;
			
			// BEGIN SERVICE
			if(orderingFromKitchen)
			{
				if(model.availableCooks.isEmpty())
				{
					model.kitchenOrderQueue.insert(this);
					this.passivate();
				}
				cook = model.availableCooks.removeFirst();
			}
			else
			{
				if(model.availableBartenders.isEmpty())
				{
					model.barOrderQueue.insert(this);
					this.passivate();
				}
				bartender = model.availableBartenders.removeFirst();
			}
			
			// CUSTOMER ORDERS FOOD HERE
			double cookTime = 0;
			do
			{
				Food food = Food.generateOrder(foodSelectionDist.sample());
				model.sendTraceNote("Customer ordered " + food + " (Cook Time: " + food.getCookTime() + " minutes)");
				cookTime = food.getCookTime() > cookTime ? food.getCookTime() : cookTime;
				model.grossProfit.update(+(long)food.getProfitFromOneSale());
				model.itemsSold.update(+1);
				food.sold();
			}
			while(model.foodAmountDist.sample());
//			for(int i = 0; i < (int) Math.ceil(model.foodAmountDist.sample()); ++i)
//			{
//				Food food = Food.generateOrder(foodSelectionDist.sample());
//				model.sendTraceNote("Customer ordered " + food + " (Cook Time: " + food.getCookTime() + " minutes)");
//				cookTime = food.getCookTime() > cookTime ? food.getCookTime() : cookTime;
//				model.grossProfit.update(+(long)food.getProfitFromOneSale());
//				model.itemsSold.update(+1);
//				food.sold();
//			}
			model.sendTraceNote("Cook time: " + cookTime);
			this.hold(new TimeSpan(cookTime, TimeUnit.MINUTES));
			
			 
			// END SERVICE
			if(orderingFromKitchen)
			{
				model.availableCooks.insert(cook);
				if(!model.kitchenOrderQueue.isEmpty())
				{
					model.kitchenOrderQueue.removeFirst().activate();
	 			}
			}
			else
			{
				model.availableBartenders.insert(bartender);
				if(!model.barOrderQueue.isEmpty())
				{
					model.barOrderQueue.removeFirst().activate();
				}
			}
		}
		else
		{
			model.sendTraceNote(this + " has balked");
		}
		model.sendTraceNote(this + " has left");
		//System.exit(1);
	}
}
