package dev.zuleger.bodega.constants;

import dev.zuleger.bodega.constants.Constants.Day;

public enum Food 
{
	/**
	 * List of Foods sold at Bodega Brew Pub.
	 */
	
	BAKED_ARTICHOKE_DIP_F	(12.00, 5.00, 8.5, 0.037846803, 3),
	BAKED_ARTICHOKE_DIP_H	(6.50, 2.50, 8, 0.043576598, 4), 
	BACON_BLUE_BRUSCHETTA	(8.00, 3.50, 10, 0.016737033, 2), 
	WALNUT_PESTO_BRUSCHETTA	(7.50, 3.00, 7.5, 0.01221351, 46),
	CAPRESE_BRUSCHETTA		(8.00, 3.25, 9, 0.023371532, 14), 
	CHIPS_MANGO_SALSA		(5.50, 1.50, 3.5, 0.009348613, 18), 
	CHIPS_SALSA_GUAC		(7.50, 1.65, 4, 0.014022919, 19), 
	BBQ_PORK_FB				(9.00, 3.50, 15, 0.015983112, 6), 
	REUBEN_FB				(10.50, 4.25, 12, 0.010253317, 32), 
	CHICKEN_BACON_RANCH		(10.00, 3.25, 12, 0.034529554, 16),
	BUFFALO_CHICKEN			(9.50, 2.00, 12, 0.013268999, 10), 
	VEGETARIAN				(9.00, 3.25, 12, 0.01628468, 45), 
	SPICY_SAUSAGE_BNPEPPER	(10.00, 4.50, 12, 0.021109771, 38), 
	MARGARITA				(8.50, 2.75, 12, 0.025482509, 27), 
	TOMATO_BASIL_CUP		(3.00, 0.90, 2, 0.024125452, 42), 
	TOMATO_BASIL_BOWL		(5.00, 1.80, 2, 0.011158022, 41),
	SOUP_OF_DAY_CUP			(3.00, 1.50, 2, 0.010705669, 36), 
	SOUP_OF_DAY_BOWL		(5.00, 3.00, 2, 0.011007238, 35), 
	BUILD_OWN_HALF			(5.50, 2.00, 10, 0.013872135, 12), // ADJUSTED DUE TO MEAT ADD-ON
	BUILD_OWN_FULL			(10.00, 4.00, 10, 0.00814234, 11), // ADJUSTED DUE TO MEAT ADD-ON
	BLT						(6.00, 2.00, 6.5, 0.042671894, 8),
	BRAUN					(7.75, 1.50, 5, 0.007539204, 9), 
	CUBANO					(10.50, 4.00, 7, 0.041465621, 21), 
	CUBANO_HALF				(6.25, 2.00, 7.5, 0.016737033, 22), 
	SICILIAN				(8.00, 3.50, 8.5, 0.020054282, 33), 
	SICILIAN_HALF			(5.00, 1.75, 9, 0.008745476, 34), 
	BBQ_PORK				(8.75, 3.50, 5.5, 0.031664656, 5), 
	BBQ_PORK_HALF			(5.00, 1.75, 6, 0.010856454, 7), 
	AVOCUBANO				(9.50, 3.00, 7.5, 0.02699035, 0), 
	AVOCUBANO_HALF			(5.00, 1.50, 8, 0.01221351, 1),
	HEN_HOG					(8.00, 2.90, 9.5, 0.014626055, 25), 
	HEN_HOG_HALF			(5.00, 1.45, 10, 0.005729795, 26), 
	SOUTH_BY_SOUTHWEST		(8.00, 3.60, 7.5, 0.035434258, 37), 
	SXSW_HALF				(5.00, 1.80, 8, 0.00814234, 40), 
	TURKEY_AVOCADO			(8.00, 4.00, 7.5, 0.022466828, 43), 
	TURKEY_AVOCADO_HALF		(5.00, 2.00, 8, 0.009499397, 44), 
	GRILLED_CHEESE			(5.00, 1.70, 7, 0.034529554, 23),
	SWEET_TANG_GC			(8.00, 1.75, 7, 0.018395657, 39), 
	HAM_CHEESE				(6.50, 2.20, 5, 0.023069964, 24), 
	REUBEN					(10.50, 4.25, 8, 0.032267793, 31),
	PUSAN					(11.25, 4.45, 8, 0.013721351, 29), 
	OZZIE					(10.50, 4.15, 8, 0.011911942, 28), 
	RACHEL					(10.50, 3.75, 8, 0.001055489, 30), 
	CAPRESE					(7.00, 1.50, 8, 0.023069964, 13), // ADJUSTED DUE TO VEGETARIAN OPTION
	FISH_TACO				(4.00, 1.50, 12, 0.067702051 * 5, 47), 
	CHILI_DOG				(4.00, 1.00, 4, 0.028799759 * 3, 17), 
	CARNITAS				(4.00, 2.00, 12, 0.020229469 * 10, 15), 
	CREME_BRULEE			(5.00, 3, 2, 0.004981884, 20);
	
	
	public final static int AMT_FOOD_ITEMS = 48;
	public static Food[] foods = 
		{ 
				Food.AVOCUBANO, Food.AVOCUBANO_HALF, Food.BACON_BLUE_BRUSCHETTA, //ID: 0, 1, 2
				Food.BAKED_ARTICHOKE_DIP_F, Food.BAKED_ARTICHOKE_DIP_H, Food.BBQ_PORK, //ID: 3, 4, 5
				Food.BBQ_PORK_FB, Food.BBQ_PORK_HALF, Food.BLT, Food.BRAUN,  //ID: 6, 7, 8, 9
				Food.BUFFALO_CHICKEN, Food.BUILD_OWN_FULL, Food.BUILD_OWN_HALF, //ID: 10, 11, 12
				Food.CAPRESE, Food.CAPRESE_BRUSCHETTA, Food.CARNITAS, //ID: 13, 14, 15
				Food.CHICKEN_BACON_RANCH, Food.CHILI_DOG, Food.CHIPS_MANGO_SALSA, //ID: 16, 17, 18
				Food.CHIPS_SALSA_GUAC, Food.CREME_BRULEE, Food.CUBANO, //ID: 19, 20, 21
				Food.CUBANO_HALF, Food.GRILLED_CHEESE, Food.HAM_CHEESE, //ID: 22, 23, 24
				Food.HEN_HOG, Food.HEN_HOG_HALF, Food.MARGARITA, //ID: 25, 26, 27
				Food.OZZIE, Food.PUSAN, Food.RACHEL, Food.REUBEN, //ID: 28, 29, 30, 31
				Food.REUBEN_FB, Food.SICILIAN, Food.SICILIAN_HALF, //ID; 32, 33, 34
				Food.SOUP_OF_DAY_BOWL, Food.SOUP_OF_DAY_CUP, Food.SOUTH_BY_SOUTHWEST, //ID: 35, 36, 37
				Food.SPICY_SAUSAGE_BNPEPPER, Food.SWEET_TANG_GC, Food.SXSW_HALF, //ID: 38, 39, 40
				Food.TOMATO_BASIL_BOWL, Food.TOMATO_BASIL_CUP, Food.TURKEY_AVOCADO,  //ID: 41, 42, 43
				Food.TURKEY_AVOCADO_HALF, Food.VEGETARIAN, Food.WALNUT_PESTO_BRUSCHETTA, //ID: 44, 45, 46
				Food.FISH_TACO //ID 47
		};
	
	private final double cookTime;
	private final double cost;
	private final double basePrice, baseProbability;
	private double price, probability;
	private int totalSold;
	private int id;
	
	Food(double price, double cost, double cookTime, double probability, int id)
	{
		this.basePrice = price;
		this.price = price;
		this.cost = cost;
		this.cookTime = cookTime;
		this.baseProbability = probability;
		this.probability = probability;
		this.id = id;
		this.totalSold = 0;
	}
	
	public double getPrice() { return price; }
	public double getBasePrice() { return basePrice; }
	public double getCost() { return cost; }
	public double getCookTime() { return cookTime; }
	public double getProbability() { return probability; }
	public double getBaseProbability() { return baseProbability; }
	public double getProfitFromOneSale() { return price - cost; }
	public double getProfitFromAllSales() { return (price - cost) * totalSold; }
	public int getAmountSold() { return totalSold; }
	public int getId() { return id; }
	public boolean isAvailable(Day day) { return ((this != CARNITAS) || (day == Day.THURSDAY)) && 
			((this != FISH_TACO) || (day == Day.FRIDAY)) && 
			((this != CHILI_DOG) || (day == Day.SATURDAY)); }
	public void sold() { totalSold++; }
	public void setPrice(double price) { this.price = price; }
	public void setProbability(double probability) { this.probability = probability; }
	
	public static Food generateOrder(double sample) 
	{ 
		Food food = Food.foods[(int) sample];
		return food; 
	} // NEEDS A BETTER ALGORITHM, CURRENTLY ONLY RANDOMLY CHOOSES
	
	public static void updateProbOf(Food food, double price, double probability)
	{
		food.setPrice(price);
		food.setProbability(probability);
	}
	
	public static void defaultProbOf(Food food)
	{
		food.setPrice(food.getBasePrice());
		food.setProbability(food.getBaseProbability());
	}
	
}
