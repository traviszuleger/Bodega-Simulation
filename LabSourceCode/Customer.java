import java.util.concurrent.TimeUnit;

import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.dist.BoolDistBernoulli;
import desmoj.core.dist.DiscreteDistEmpirical;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

/**
 * Customer extends SimProcess
 * 
 * @description Entity that is provided service in the Bodega Brew Pub model.
 *
 * @author Travis Zuleger
 * @date Nov 3, 2019
 */
public class Customer extends SimProcess
{

	// Distribution that covers all of the foods that a customer can order.
	private DiscreteDistEmpirical<Integer> foodSelectionDist;


	/**
	 * Customer (Model, String, DiscreteDistEmpirical<Integer>)
	 *
	 * @description Creates an instance of a Customer entity.
	 *
	 * @param owner             -> Model this entity belongs to.
	 * @param name              -> Name of this customer.
	 * @param foodSelectionDist -> Items the customer can order on the day they were
	 *                          generated.
	 */
	public Customer( Model owner, String name, DiscreteDistEmpirical<Integer> foodSelectionDist )
	{
		super( owner, name, true );
		this.foodSelectionDist = foodSelectionDist;
	}


	/**
	 * lifeCycle ()
	 * 
	 * @description An abstract layout of what is expected from an entity once they
	 *              are activated within the simulation. 1.) Check the line and
	 *              increase probability of balking based on the length of the line.
	 *              2.) Insert into the line, if not balked, or order from the Cook,
	 *              if available. 3.) Order food and update variables based on what
	 *              the Customer's food selection distribution is. 4.) Wait until
	 *              their food is received, once received, leave and activate the
	 *              next customer.
	 */
	@ Override
	public void lifeCycle() throws SuspendExecution
	{
		BodegaModel model = ( BodegaModel ) getModel();
		boolean balked = model.balkChanceDist.sample().intValue() <= model.kitchenOrderQueue.size();
		if( balked )
		{
			model.sendTraceNote( this + " has balked." );
			return;
		}

		if( model.availableCooks.isEmpty() )
		{
			model.kitchenOrderQueue.insert( this );
			this.passivate();
		}
		Cook cook = model.availableCooks.removeFirst();

		double cookTime = 0;

		boolean isTuesday = foodSelectionDist == model.foodSelectionDistList.get( 1 );
		BoolDistBernoulli amountOfFoodDist = isTuesday ? model.foodAmountDistTu : model.foodAmountDist;
		int itemsInOrder = 0;
		do
		{
			itemsInOrder++;
			Food food = Food.generateOrder( foodSelectionDist.sample() );
			model.sendTraceNote( this + " ordered " + food + " (Cook Time: " + food.getTimeToCook() + " minutes)" );
			cookTime = food.getTimeToCook() > cookTime ? food.getTimeToCook() : cookTime;
			if( ( itemsInOrder % 2 ) == 0 && isTuesday )
			{
				model.sendTraceNote( this + " received " + food + " for free. (Tuesday special)" );
				model.itemsSold.update( +1 );
			} else
			{
				model.itemsSold.update( +1 );
				food.sold();
			}
		} while( amountOfFoodDist.sample() );
		model.sendTraceNote( "Time to cook: " + cookTime );
		this.hold( new TimeSpan( cookTime, TimeUnit.MINUTES ) );

		model.availableCooks.insert( cook );
		if( !model.kitchenOrderQueue.isEmpty() )
		{
			model.kitchenOrderQueue.removeFirst().activate();
		}
		model.sendTraceNote( this + " has left." );
	}

}
