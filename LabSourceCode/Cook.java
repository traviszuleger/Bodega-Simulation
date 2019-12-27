import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

/**
 * Cook extends SimProcess
 * 
 * @description Entity that provides service in the Bodega Brew Pub model.
 *
 * @author Travis Zuleger
 * @date Nov 3, 2019
 */
public class Cook extends SimProcess
{
	/**
	 * Cook (Model, String)
	 *
	 * @description Creates an entity of a Cook in the model.
	 *
	 * @param owner -> Model this entity belongs to.
	 * @param name  -> Name of this entity.
	 */
	public Cook( Model owner, String name )
	{
		super( owner, name, true );
	}


	/**
	 * lifeCycle ()
	 * 
	 * @description An abstract layout of what is expected from an entity once they
	 *              are activated within the simulation. 1.) Update the cost of the
	 *              kitchen by their hourly wage, every hour. (-$11)
	 */
	@ Override
	public void lifeCycle() throws SuspendExecution
	{
		BodegaModel model = ( BodegaModel ) this.getModel();
		while( true )
		{
			if( model.presentTime().getTimeAsDouble() % 60 == 0 )
				model.grossProfit.update( -11 );
		}
	}

}
