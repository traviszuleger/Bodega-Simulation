/**
 * <ClassName> extends (if applicable) <ParentClassName> implements (if applicable) <ImplementationClassName>
 * 
 * @description Enumeration that helps track the prices and probabilities of each food offered in the simulation. 
 * 
 * @author Travis Zuleger
 * @date Nov 3, 2019
 */
public enum Food
{
	//INITIAL FOODS
	ART_DIP(9.00, 3.50, 0.10, 5), // + 0.02
	BBQ_FLAT(9.00, 3.50, 0.04, 11), 
	BBQ_SAND(8.75, 3.50, 0.04, 4),
	BLT(6.00, 2.00, 0.06, 6),
	SALAD(9, 4, 0.03, 6.5), // + 0.01
	CHX_BAC_FLAT(10.00, 3.25, 0.10, 11), // + 0.02
	CHILI_DOG(4.00, 1.00, 0.03, 6.5), // + 0.01
	CHIPS(5.50, 2.00, 0.03, 3),
	CREME_BRU(5.00, 3.00, 0.01, 3),
	CUBANO(10.50, 4.00, 0.09, 5), // + 0.01
	GRILL_CHZ(5.00, 2.00, 0.06, 6),
	OZZIE(10.50, 4.25, 0.05, 6),
	PUSAN(11.25, 4.50, 0.06, 6),
	REUBEN(10.50, 4.25, 0.09, 6), // + 0.01
	SOUTH_SW(8.00, 3.50, 0.08, 5.5),
	TOMATO_SOUP(4.00, 1.50, 0.05, 1),
	VEG_FLAT(9.00, 3.50, 0.06, 11.5),
	WAL_PESTO(7.50, 3.00, 0.06, 5),
	
	CARNITAS(4.00, 2.00, 0.08, 8.5),
	FISH_TACO(4.00, 1.50, 0.08, 8.5);
	
	// STATICS
	public static final Food[] MENU = 
		{ 
			ART_DIP, 
			BBQ_FLAT, 
			BBQ_SAND, 
			BLT, 
			SALAD, 
			CHX_BAC_FLAT, 
			CHILI_DOG,
			CHIPS,
			CREME_BRU,
			CUBANO,
			GRILL_CHZ,
			OZZIE,
			PUSAN,
			REUBEN,
			SOUTH_SW,
			TOMATO_SOUP,
			VEG_FLAT,
			WAL_PESTO,
			CARNITAS,
			FISH_TACO
		};	
	private static int id = 0;
	
	private final double basePrice, cost, baseProb, timeToCook;
	private double price, prob;
	private int sales;
	private int FOOD_ID;
	
	// CONSTRUCTOR
	Food(double price, double cost, double prob, double timeToCook)
	{
		this.basePrice = price;
		this.cost = cost;
		this.baseProb = prob;
		this.timeToCook = timeToCook;
		this.price = price;
		this.prob = prob;
		setId();
	}
	
	// INITIALIZER
	private void setId()
	{
		this.FOOD_ID = Food.id++;
	}
	
	public static Food generateOrder(double sample)
	{
		return Food.MENU[(int) sample];
	}
	
	// ACCESSOR
	public int getId()
	{
		return FOOD_ID;
	}	
	public double getCost()
	{
		return cost;
	}
	public double getTimeToCook()
	{
		return timeToCook;
	}
	public double getPrice()
	{
		return price;
	}
	public double getProb()
	{
		return prob;
	}
	
	// UPDATE
	public void defaultBases()
	{
		price = basePrice;
		prob = baseProb;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}	
	public void setProb(double prob)
	{
		this.prob = prob;
	}	
	public void sold()
	{
		sales++;
	}
	
	// MORE ACCESSOR
	public int getSales()
	{
		return sales;
	}
	public double totalProfitFrom()
	{
		return (getPrice() - getCost()) * getSales();
	}
}