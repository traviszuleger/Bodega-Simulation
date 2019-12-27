import java.util.concurrent.TimeUnit;
import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

/**
 * CustomerGenerator extends SimProcess
 * 
 * @description Fake entity in the model that generates customer at given interarrival times.
 * Also keeps track of what foods should be sold during which day by passing those distributions to
 * the customers that are generated.
 *
 * @author Travis Zuleger
 * @date Nov 3, 2019
 */
public class CustomerGenerator extends SimProcess
{
	
	private final int MONDAY = 0, TUESDAY = 1, WEDNESDAY = 2, THURSDAY = 3, FRIDAY = 4, SATURDAY = 5, SUNDAY = 6;


	/**
	 * CustomerGenerator(Model, String)
	 *
	 * @description Creates an instance of a CustomerGenerator entity.
	 *
	 * @param owner -> Model this entity belongs
	 * @param name -> Name of this entity
	 */
	public CustomerGenerator( Model owner, String name )
	{
		super( owner, name, true );
	}

	/**
	 * lifeCycle()
	 * 
	 * @description An abstract layout of what is expected from an entity once they
	 *              are activated within the simulation. 1.) Check what day the model
	 *              is in, and adjust probabilities accordingly. 2.) Sample a new interarrival
	 *              time and hold for that amount of time. 3.) Activate the customer,
	 */
	@ Override
	public void lifeCycle() throws SuspendExecution
	{
		BodegaModel model = ( BodegaModel ) this.getModel();

		int day = MONDAY;

		int currentDay = changeDay( day );

		while( true )
		{
			double time = model.presentTime().getTimeAsDouble();

			double interarrival = getInterarrival( model, currentDay );

			this.hold( new TimeSpan( interarrival, TimeUnit.MINUTES ) );

			new Customer( model, "Customer ", model.foodSelectionDistList.get( currentDay % 7 ) ).activate();
			if( time > ( 60 * BodegaModel.BUSINESS_HOURS ) * day )
			{
				day++;
				currentDay = changeDay( currentDay );
			}
		}
	}

	/**
	 * changeDay(int)
	 * 
	 * @description Returns a new integer (corresponding to a day of the week) 
	 * where the integer <day> is what determines the returning integer.
	 *
	 * @param day -> Integer for the current day of the week.
	 * @return Integer for a new day that corresponds to the next day of the week.
	 */
	private int changeDay( int day )
	{
		switch( day )
		{
		case MONDAY:
			this.sendTraceNote(
					"New day: Tuesday. Special: All items - Buy one get one half off til 7 PM, after 7PM: buy one get one free."
			);
			return TUESDAY;
		case TUESDAY:
			this.sendTraceNote( "New day: Wednesday. Special: Reuben/Ozzie/Pusan for $2 off" );
			return WEDNESDAY;
		case WEDNESDAY:
			this.sendTraceNote( "New day: Thursday. Special: Carnitas Tacos for $4 per taco." );
			return THURSDAY;
		case THURSDAY:
			this.sendTraceNote( "New day: Friday. Special: Fish tacos for $4 per taco." );
			return FRIDAY;
		case FRIDAY:
			this.sendTraceNote( "New day: Saturday. Special: Chili Dog for $4 per chili dog." );
			return SATURDAY;
		case SATURDAY:
			this.sendTraceNote( "New day: Sunday. No Special." );
			return SUNDAY;
		case SUNDAY:
			this.sendTraceNote( "New day: Monday. Special: BBQ Pork Sandwiches for $7/$4" );
			return MONDAY;
		default:
			this.sendTraceNote( "Error: day in changeDay method is less than 1 or greater than 7." );
			return -1;
		}
	}

	/**
	 * getInterarrival(BodegaModel, int)
	 * 
	 * @description Returns a sample of the correct day's distribution.
	 *
	 * @param model -> Model where the distributions are held.
	 * @param day -> Day that determines which distribution should be sampled.
	 * @return Double that is generated from the proper distribution's sampling.
	 */
	private double getInterarrival( BodegaModel model, int day )
	{
		switch( day )
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

}
