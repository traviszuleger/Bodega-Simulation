package dev.zuleger.bodega.entities;

import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;
import dev.zuleger.bodega.constants.Constants.Day;
import dev.zuleger.bodega.constants.Constants;
import dev.zuleger.bodega.constants.Show;
import dev.zuleger.bodega.model.BodegaModel;

public class CustomerGenerator extends SimProcess
{
	
	public CustomerGenerator(Model owner, String name) 
	{
		super(owner, name, Show.GENERATORS_IN_REPORT, Show.GENERATORS_IN_TRACE);
	}

	@Override
	public void lifeCycle() throws SuspendExecution 
	{
		BodegaModel model = (BodegaModel) this.getModel();
		Day currentDay = changeDay(model, Day.SUNDAY);
		
		
		int dayAsInteger = 1;
		
		while(true) 
		{
			double time = model.presentTime().getTimeAsDouble();
			
			double interarrival = getInterarrival(model, currentDay);
			
			this.hold(new TimeSpan(interarrival, TimeUnit.MINUTES));

			new Customer(model, "Customer ", model.foodSelectionDistList.get(dayAsInteger % 7)).activate();
			if(time > (60 * Constants.BUSINESS_HOURS) * dayAsInteger)
			{
				dayAsInteger++;
				currentDay = changeDay(model, currentDay);
			}
		}
	}
	
	private double getInterarrival(BodegaModel model, Day day)
	{
		switch(day)
		{
			case SUNDAY:
				return model.sundayDist.sample();
			case MONDAY:
				return model.mondayDist.sample();
			case TUESDAY:
				return model.tuesdayDist.sample();
			case WEDNESDAY:
				return model.wednesdayDist.sample();
			case THURSDAY:
				return model.thursdayDist.sample();
			case FRIDAY:
				return model.fridayDist.sample();
			case SATURDAY:
				return model.saturdayDist.sample();
			default:
				return 1.99;
		}
	}
	
	private Day changeDay(BodegaModel model, Day day)
	{
		switch(day)
		{
		case MONDAY:
			this.sendTraceNote("New day: Tuesday. Special: All items - Buy one get one half off til 7 PM, after 7PM: buy one get one free.");
			return Day.TUESDAY;
		case TUESDAY:
			this.sendTraceNote("New day: Wednesday. Special: Reuben/Ozzie/Pusan for $2 off");
			return Day.WEDNESDAY;
		case WEDNESDAY:
			this.sendTraceNote("New day: Thursday. Special: Carnitas Tacos for $4 per taco.");
			return Day.THURSDAY;
		case THURSDAY:
			this.sendTraceNote("New day: Friday. Special: Fish tacos for $4 per taco.");
			return Day.FRIDAY;
		case FRIDAY:
			this.sendTraceNote("New day: Saturday. Special: Chili Dog for $4 per chili dog.");
			return Day.SATURDAY;
		case SATURDAY:
			this.sendTraceNote("New day: Sunday. Special: Ham and Cheese with a Bloody Mary or just discounted for $5");
			return Day.SUNDAY;
		case SUNDAY:
			this.sendTraceNote("New day: Monday. Special: BBQ Pork Sandwiches for $7/$4");
			return Day.MONDAY;
		default:
			this.sendTraceNote("Error");
			return null;
		}
	}

	
}
