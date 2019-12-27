package dev.zuleger.bodega.constants;

public class Constants 
{
	
	public final static Day[] days = { Day.SUNDAY, Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY };
	
	public enum Day
	{
		MONDAY, 
		TUESDAY, 
		WEDNESDAY, 
		THURSDAY, 
		FRIDAY, 
		SATURDAY, 
		SUNDAY;
	}
	
	public enum Interarrival
	{
		SUN_OVERALL(23.73), MON_OVERALL(26.84), TUE_OVERALL(16.83), WED_OVERALL(24.88), THU_OVERALL(24.77), FRI_OVERALL(19.8), SAT_OVERALL(19.99);
		
		private double sample;
		
		Interarrival(double sample)
		{
			this.sample = sample;
		}
		
		public double getSample() { return sample; }
	}
	
	public static final int BUSINESS_HOURS = 14;
	
	public static final int SKIP_LOWER = 1, SKIP_UPPER = 5;
	
	public static final int BALK_LOWER = 1, BALK_UPPER = 6;
	
}
