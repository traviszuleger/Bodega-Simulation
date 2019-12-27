package dev.zuleger.bodega.model;

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
import dev.zuleger.bodega.constants.Constants;
import dev.zuleger.bodega.constants.Constants.Day;
import dev.zuleger.bodega.constants.Food;
import dev.zuleger.bodega.constants.Show;
import dev.zuleger.bodega.entities.Bartender;
import dev.zuleger.bodega.entities.Cook;
import dev.zuleger.bodega.entities.Customer;
import dev.zuleger.bodega.entities.CustomerGenerator;

public class BodegaModel extends Model {
	
	public Count grossProfit, itemsSold, cookUtilization;

	public Queue<Customer> barOrderQueue, kitchenOrderQueue;
	public Queue<Bartender> availableBartenders, totalBartenders;
	public Queue<Cook> availableCooks, totalCooks;

	public ArrayList<DiscreteDistEmpirical<Integer>> foodSelectionDistList = new ArrayList<DiscreteDistEmpirical<Integer>>();
	
	public ContDistExponential takeOrderDist_kitchen;
	
	public DiscreteDistUniform balkChanceDist;

	public BoolDistBernoulli foodAmountDist;
	public ContDistExponential sundayDist, mondayDist, tuesdayDist, wednesdayDist, thursdayDist, fridayDist, saturdayDist;

	protected static enum Interarrival
	{
		
	}
	
	public BodegaModel(Model owner, String name, boolean showInReport, boolean showInTrace) {
		super(owner, name, showInReport, showInTrace);
	}

	@Override
	public String description() {
		String description = "Model that performs a Simulation on the real-life business, "
				+ "Bodega Brew Pub, owned and operated by Jeffrey Hotson and "
				+ "managed by John Lochner and Jarrid Larson. This simulation builds off of "
				+ "random numbers and variates generated through real life numbers found through "
				+ "data collected by the real-life system. "
				+ "The coding and data collection is done by Travis Zuleger, a student "
				+ "at the University of Wisconsin-La Crosse. This project is also supervised under "
				+ "Assistant Professor, Jason Sauppe, also of the University of Wisconsin-La Crosse. "
				+ "The goal of this model is to create a greater understanding of the Bodega Brew Pub "
				+ "to maintain and improve the well-being of the business.";
		return description;
	}

	@Override
	public void doInitialSchedules() {
		availableBartenders.insert(new Bartender(this, "Bartender "));
		availableCooks.insert(new Cook(this, "Cook "));
		availableCooks.insert(new Cook(this, "Cook "));
		new CustomerGenerator(this, "Customer Generator ").activate();
	}

	@Override
	public void init() {
		grossProfit = new Count(this, "Gross profit", Show.FINAL_VARIABLES_IN_REPORT, Show.FINAL_VARIABLES_IN_TRACE);
		itemsSold = new Count(this, "Items sold", Show.FINAL_VARIABLES_IN_REPORT, Show.FINAL_VARIABLES_IN_TRACE);
		cookUtilization = new Count(this, "Cook time being used.", Show.FINAL_VARIABLES_IN_REPORT, Show.FINAL_VARIABLES_IN_TRACE);

		// INITIALIZE QUEUES.
		initQueues();

		// INITIALIZE PROCESS DISTRIBUTIONS.
		initSimulativeDists();

		// INITIALIZE INTERARRIVAL DISTRIBUTIONS
		initInterarrivalDists();
	}

	private void initQueues() {
		barOrderQueue = new Queue<Customer>(this, "Customers waiting to order food at the bar.",
				Show.ENTITIES_IN_REPORT, Show.ENTITIES_IN_TRACE);

		kitchenOrderQueue = new Queue<Customer>(this, "Customers waiting to order food at the kitchen.",
				Show.ENTITIES_IN_REPORT, Show.ENTITIES_IN_TRACE);

		totalCooks = new Queue<Cook>(this, "Cooks off-schedule.", Show.ENTITIES_IN_REPORT, Show.ENTITIES_IN_TRACE);
		totalBartenders = new Queue<Bartender>(this, "Bartenders off-schedule.", Show.ENTITIES_IN_REPORT, Show.ENTITIES_IN_TRACE);
		
		availableCooks = new Queue<Cook>(this, "Cooks available to serve customers for food.", Show.ENTITIES_IN_REPORT,
				Show.ENTITIES_IN_TRACE);

		availableBartenders = new Queue<Bartender>(this, "Bartenders available to take Customer's order.",
				Show.ENTITIES_IN_REPORT, Show.ENTITIES_IN_TRACE);
	}

	private void initSimulativeDists() {
		initFoodSelectionDists();

		foodAmountDist = new BoolDistBernoulli(this,
				"Bernoulli Distribution for how much food a customer may want in their order.", 0.15,
				Show.DISTRIBUTIONS_IN_REPORT, Show.DISTRIBUTIONS_IN_TRACE);

		balkChanceDist = new DiscreteDistUniform(this, "Discrete Uniform Distribution for a Customer to leave.",
				Constants.BALK_LOWER, Constants.BALK_UPPER, Show.DISTRIBUTIONS_IN_REPORT, Show.DISTRIBUTIONS_IN_TRACE);

	}

	private void initFoodSelectionDists() {
		for (Day day : Constants.days) {
			switch (day) {
			case TUESDAY:
				Food.defaultProbOf(Food.BBQ_PORK);
				Food.defaultProbOf(Food.BBQ_PORK_HALF);
				
				for (int i = 0; i < Food.foods.length; ++i)
					Food.updateProbOf(Food.foods[i], Food.foods[i].getBasePrice() / 2, Food.foods[i].getBaseProbability() * 2);
			case WEDNESDAY:
				for (int i = 0; i < Food.foods.length; ++i)
					Food.defaultProbOf(Food.foods[i]);
				
				Food.updateProbOf(Food.OZZIE, Food.OZZIE.getBasePrice() - 2, Food.OZZIE.getBaseProbability() * 1.5);
				Food.updateProbOf(Food.PUSAN, Food.PUSAN.getBasePrice() - 2, Food.PUSAN.getBaseProbability() * 1.5);
				Food.updateProbOf(Food.REUBEN, Food.REUBEN.getBasePrice() - 2, Food.REUBEN.getBaseProbability() * 2.5);
			case THURSDAY:
				Food.defaultProbOf(Food.OZZIE);
				Food.defaultProbOf(Food.PUSAN);
				Food.defaultProbOf(Food.REUBEN);
			case FRIDAY:
				Food.defaultProbOf(Food.FISH_TACO);
			case SATURDAY:
				
			case SUNDAY:
				Food.updateProbOf(Food.HAM_CHEESE, 5.00, Food.HAM_CHEESE.getBaseProbability() * 4);	
			case MONDAY:
				Food.defaultProbOf(Food.HAM_CHEESE);
				
				Food.updateProbOf(Food.BBQ_PORK, 7.00, Food.BBQ_PORK.getBaseProbability() * 3);
				Food.updateProbOf(Food.BBQ_PORK_HALF, 4.00, Food.BBQ_PORK_HALF.getBaseProbability() * 2);
			}

			DiscreteDistEmpirical<Integer> foodSelectionDist = new DiscreteDistEmpirical<Integer>(this,
					"Discrete Empirical Distribution for the selection of food the customer wants on " + day,
					Show.DISTRIBUTIONS_IN_REPORT, Show.DISTRIBUTIONS_IN_TRACE);

			for (int i = 0; i < Food.foods.length; ++i)
			{
				if(Food.foods[i].isAvailable(day))
					foodSelectionDist.addEntry(i, Food.foods[i].getProbability());
			}
			foodSelectionDistList.add(foodSelectionDist);
		}
	}

	private void initInterarrivalDists() {
		sundayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Sunday Interarrivals.",
				Constants.Interarrival.SUN_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		mondayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Monday Interarrivals.",
				Constants.Interarrival.MON_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		tuesdayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Tuesday Interarrivals.",
				Constants.Interarrival.TUE_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		wednesdayDist = new ContDistExponential(this,
				"Continuous Exponential Distribution for Wednesday Interarrivals.",
				Constants.Interarrival.WED_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		thursdayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Thursday Interarrivals.",
				Constants.Interarrival.THU_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		fridayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Friday Interarrivals.",
				Constants.Interarrival.FRI_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
		saturdayDist = new ContDistExponential(this, "Continuous Exponential Distribution for Saturday Interarrivals.",
				Constants.Interarrival.SAT_OVERALL.getSample(), Show.DISTRIBUTIONS_IN_REPORT,
				Show.DISTRIBUTIONS_IN_TRACE);
	}

	public static void main(String[] args) {
		Experiment.setReferenceUnit(TimeUnit.MINUTES);

		BodegaModel model = new BodegaModel(null, "Bodega Brew Pub Model", true, true);
		Experiment exp = new Experiment("Bodega Brew Pub Model");

		exp.setSeedGenerator(89);

		model.connectToExperiment(exp);
		exp.setShowProgressBar(false);

		int minutesInBusinessDay = Constants.BUSINESS_HOURS * 60;
		int daysToSimulate = 7;

		exp.stop(new TimeInstant((minutesInBusinessDay * daysToSimulate), TimeUnit.MINUTES));
		exp.tracePeriod(new TimeInstant(0, TimeUnit.MINUTES),
				new TimeInstant(minutesInBusinessDay * daysToSimulate, TimeUnit.MINUTES));
		exp.debugPeriod(new TimeInstant(0, TimeUnit.MINUTES),
				new TimeInstant(minutesInBusinessDay * daysToSimulate, TimeUnit.MINUTES));

		exp.start();
		exp.report();
		exp.finish();

		DecimalFormat df = new DecimalFormat("#0.00");
		
		double totalProfit = 0;
		int totalSold = 0;
		for (int i = 0; i < Food.foods.length; ++i) {
			System.out.println(Food.foods[i] + " - Amount sold: " + Food.foods[i].getAmountSold() + ", Total profit: $"
					+ df.format(Food.foods[i].getProfitFromAllSales()));
			
			totalProfit += Food.foods[i].getProfitFromAllSales();
			totalSold += Food.foods[i].getAmountSold();
		}
		System.out.println("Total Units Sold: " + totalSold + ", Total Profit: $" + df.format(totalProfit));
	}
}
