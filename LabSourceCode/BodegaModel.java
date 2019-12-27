import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import desmoj.core.dist.BoolDistBernoulli;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.DiscreteDistEmpirical;
import desmoj.core.dist.DiscreteDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.Queue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.statistic.Count;

/**
 * BodegaModel extends Model
 * 
 * @description Process-Oriented Model of the real-life business, Bodega Brew
 *              Pub, specifically the kitchen.
 *
 * @author Travis Zuleger
 * @date Nov 3, 2019
 */
public class BodegaModel extends Model
{

	// counts to keep track of profit, items sold, and the amount of time cooks are
	// being utilized.
	protected Count grossProfit, itemsSold, cookUtilization;

	// queue to track which customer is in line to order food
	protected Queue<Customer> kitchenOrderQueue;

	// queues to track how often a cook is available to cook/take an order.
	protected Queue<Cook> availableCooks;

	// list of Empirical Distributions meant for the customer's sampling of what
	// food they order.
	protected ArrayList<DiscreteDistEmpirical<Integer>> foodSelectionDistList = new ArrayList<DiscreteDistEmpirical<Integer>>();

	// sampling for chance of customer leaving the restaurant (due to length of
	// line).
	protected DiscreteDistUniform balkChanceDist;

	// sampling for how much food a customer may order.
	protected BoolDistBernoulli foodAmountDist, foodAmountDistTu;

	// sampling for interarrival times for every day.
	protected ContDistExponential sundayDist, mondayDist, tuesdayDist, wednesdayDist, thursdayDist, fridayDist,
			saturdayDist;

	protected static final int BUSINESS_HOURS = 14;

	protected static final double SUNDAY_INTERARRIVAL = 24, MONDAY_INTERARRIVAL = 27, TUESDAY_INTERARRIVAL = 17,
			WEDNESDAY_INTERARRIVAL = 25, THURSDAY_INTERARRIVAL = 25, FRIDAY_INTERARRIVAL = 20,
			SATURDAY_INTERARRIVAL = 20;


	/**
	 * BodegaModel (Model, String, boolean, boolean)
	 *
	 * @description Creates an instance of a the BodegaModel
	 *
	 * @param owner        -> Model this belongs to. (should always be null unless a
	 *                     Repition model is created)
	 * @param name         -> Name of the model.
	 * @param showInReport -> True if the model should be shown in reports.
	 * @param showInTrace  -> True if the model should be shown in traces.
	 */
	public BodegaModel( Model owner, String name, boolean showInReport, boolean showInTrace )
	{
		super( owner, name, showInReport, showInTrace );
	}


	/**
	 * description()
	 * 
	 * @description Returns a string of the model's description.
	 * 
	 * @return String of description of this model.
	 */
	@ Override
	public String description()
	{
		return "Small business model simulated in a process-oriented environment.";
	}


	/**
	 * doInitialSchedules()
	 * 
	 * @description Initially sets the queues and entities so the simulation can
	 *              begin.
	 */
	@ Override
	public void doInitialSchedules()
	{
		availableCooks.insert( new Cook( this, "Cook " ) );
		availableCooks.insert( new Cook( this, "Cook " ) );
		new CustomerGenerator( this, "Customer Generator " ).activate();
	}


	/**
	 * init()
	 * 
	 * @description Initiates all of the queues, counts, and distributions that are
	 *              necessary for the model
	 */
	@ Override
	public void init()
	{
		grossProfit = new Count( this, "Gross profit", true, true );
		itemsSold = new Count( this, "Items sold", true, true );
		cookUtilization = new Count( this, "Cook time being used.", true, true );

		kitchenOrderQueue = new Queue<Customer>( this, "Customers waiting to order food at the kitchen.", true, true );

		availableCooks = new Queue<Cook>( this, "Cooks available to serve customers for food.", true, true );

		balkChanceDist = new DiscreteDistUniform(
				this, "Discrete Uniform Distribution for a Customer to leave.", 1, 6, true, true
		);

		foodAmountDist = new BoolDistBernoulli(
				this, "Bernoulli Distribution for how much food a customer may want in their order.", 0.15, true, true
		);

		foodAmountDistTu = new BoolDistBernoulli(
				this, "Bernoulli Distribution for how much food a customer may want in their order.", 0.55, true, true
		);

		sundayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for SUNDAY Interarrivals.", SUNDAY_INTERARRIVAL, true, true
		);
		mondayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for MONDAY Interarrivals.", MONDAY_INTERARRIVAL, true, true
		);
		tuesdayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for TUESDAY Interarrivals.", TUESDAY_INTERARRIVAL, true, true
		);
		wednesdayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for WEDNESDAY Interarrivals.", WEDNESDAY_INTERARRIVAL, true,
				true
		);
		thursdayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for THURSDAY Interarrivals.", THURSDAY_INTERARRIVAL, true,
				true
		);
		fridayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for FRIDAY Interarrivals.", FRIDAY_INTERARRIVAL, true, true
		);
		saturdayDist = new ContDistExponential(
				this, "Continuous Exponential Distribution for SATURDAY Interarrivals.", SATURDAY_INTERARRIVAL, true,
				true
		);

		String[] days =
		{ "MO", "TU", "WE", "TH", "FR", "SA", "SU" };
		int x = 0;
		for( String day : days )
		{
			Food.MENU[Food.CARNITAS.getId()].setProb( 0 );
			Food.MENU[Food.FISH_TACO.getId()].setProb( 0 );
			switch( day )
			{
			case "MO":
				Food.MENU[Food.BBQ_SAND.getId()].setPrice( 7.00 );
				Food.MENU[Food.BBQ_SAND.getId()].setProb( 0.16 );
				break;
			case "TU":
				Food.MENU[Food.BBQ_SAND.getId()].defaultBases();
				break;
			case "WE":
				Food.MENU[Food.REUBEN.getId()].setPrice( Food.REUBEN.getPrice() - 2 );
				Food.MENU[Food.OZZIE.getId()].setPrice( Food.OZZIE.getPrice() - 2 );
				Food.MENU[Food.PUSAN.getId()].setPrice( Food.PUSAN.getPrice() - 2 );
				Food.MENU[Food.REUBEN.getId()].setProb( Food.REUBEN.getProb() + 0.04 );
				Food.MENU[Food.OZZIE.getId()].setProb( Food.OZZIE.getProb() + 0.02 );
				Food.MENU[Food.PUSAN.getId()].setProb( Food.PUSAN.getProb() + 0.02 );
				break;
			case "TH":
				Food.MENU[Food.REUBEN.getId()].defaultBases();
				Food.MENU[Food.OZZIE.getId()].defaultBases();
				Food.MENU[Food.PUSAN.getId()].defaultBases();
				Food.MENU[Food.CARNITAS.getId()].defaultBases();
				break;
			case "FR":
				Food.MENU[Food.FISH_TACO.getId()].defaultBases();
				break;
			case "SA":
				Food.MENU[Food.CHILI_DOG.getId()].setProb( Food.CHILI_DOG.getProb() + 0.04 );
				Food.MENU[Food.CHILI_DOG.getId()].setPrice( Food.CHILI_DOG.getPrice() - 1 );
				break;
			case "SU":
				Food.MENU[Food.CHILI_DOG.getId()].defaultBases();
				break;
			default:
				break;
			}
			DiscreteDistEmpirical<Integer> foodSelectionDist = new DiscreteDistEmpirical<Integer>(
					this, "Discrete Empirical Distribution for the selection of food the customer wants on " + day,
					true, true
			);
			for( int i = 0; i < Food.MENU.length; ++i )
			{
				foodSelectionDist.addEntry( Food.MENU[i].getId(), Food.MENU[i].getProb() );
			}
			foodSelectionDistList.add( x, foodSelectionDist );
			++x;
		}
	}


	public static void main( String[] args )
	{
		Experiment.setReferenceUnit( TimeUnit.MINUTES );
		// Creates our BodegaModel model.
		BodegaModel model = new BodegaModel( null, "Bodega Brew Pub Model", true, true );
		// Creates an experiment to attach to the model.
		Experiment exp = new Experiment( "Bodega Brew Pub Model" );
		// Setting a seed is good for debugging. 89 is just an arbitrarily chosen
		// number.
		exp.setSeedGenerator( 89 );
		// Connect model to experiment
		model.connectToExperiment( exp );
		exp.setShowProgressBar( false );
		// Reference variables for readability
		int minutesInBusinessDay = BUSINESS_HOURS * 60;
		int daysToSimulate = 7;
		// Stop the simulation after so much time (98 hours for one week)
		exp.stop( new TimeInstant( ( minutesInBusinessDay * daysToSimulate ), TimeUnit.MINUTES ) );
		exp.tracePeriod(
				new TimeInstant( 0, TimeUnit.MINUTES ), new TimeInstant( minutesInBusinessDay * daysToSimulate, TimeUnit.MINUTES )
		);
		exp.debugPeriod(
				new TimeInstant( 0, TimeUnit.MINUTES ), new TimeInstant( minutesInBusinessDay * daysToSimulate, TimeUnit.MINUTES )
		);
		// Begin simulation
		exp.start();
		exp.report();
		exp.finish();
		// OPTIONAL: Print all of the units that were sold.
		DecimalFormat df = new DecimalFormat( "#0.00" );
		double totalProfit = 0;
		int totalSold = 0;
		for( int i = 0; i < Food.MENU.length; ++i )
		{
			System.out.println(
					Food.MENU[i] + " - Amount sold: " + Food.MENU[i].getSales() + ", Total profit: $"
							+ df.format( Food.MENU[i].totalProfitFrom() )
			);

			totalProfit += Food.MENU[i].totalProfitFrom();
			totalSold += Food.MENU[i].getSales();
		}
		System.out.println( "Total Units Sold: " + totalSold + ", Total Profit: $" + df.format( totalProfit ) );
	}

}
