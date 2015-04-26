/**
 * Class to represent environmental characteristics
 */
package cs580.evolution.pojo;

/**
 * @author Natalia Anpilova (will add all the team here)
 *
 */
public class Environment {
	/**
	 * constants limiting min and max values for the variables
	 */
	public static final int MIN_LIGHT_LEVEL = 0;				//total darkness - always night
	public static final int MAX_LIGHT_LEVEL = 100;				//always daylight
	
	public static final int MIN_TEMPERATURE_LEVEL = -200;		//cannot be absolute zero - we are not in space
	public static final int MAX_TEMPERATURE_LEVEL = 500;		//cannot exceed 500C
	
	public static final int MIN_HUMIDITY_LEVEL = 0;				//extra dry air
	public static final int MAX_HUMIDITY_LEVEL = 100;			//water environment: ocean
	
	public static final int MIN_POLLUTION_LEVEL = 0;			//no pollution
	public static final int MAX_POLLUTION_LEVEL = 100;			//100% polluted non-survivable environment
	public static final int POLLUTION_MUTATION_THRESHOLD = 50;	//if 50% polluted then possibly higher mutation probability
	
	public static final int MIN_FOOD_AMOUNT = 0;				//no food available
	public static final int MAX_FOOD_AMOUNT = 1000000000;		//1 billion kCal
	
	/**
	 * variables - environmental characteristics
	 */
	private int lightLevel;				//how long is the day: 50% means half-day night, half-day daylight; 80% means 80% of daylight time, 20% of night time
	private int temperature;			//average daily temperature in C
	private int humidityLevel;			//humidity level: 30% means pretty dry air, 80% means very humid air, 100% means water environment
	private int pollutionLevel;			//pollution (CO, radioactivity, waste, etc.) level: 0% means no pollution, 50% is pretty bad pollution, 100% is non-survivable
	private int foodTotalAmount;		//total amount of food available (for all population) in kCal. Assumption is that water and oxygen are always available in any amount, or these creatures don't need them

	/**
	 * no-argument constructor
	 */
	public Environment() {
		this.lightLevel = 60;
		this.temperature = 20;
		this.humidityLevel = 50;
		this.pollutionLevel = MIN_POLLUTION_LEVEL;
		this.foodTotalAmount = MAX_FOOD_AMOUNT;
	}
	
	/**
	 * constructor
	 * @param lightLevel
	 * @param temperature
	 * @param humidityLevel
	 * @param pollutionLevel
	 * @param foodTotalAmount
	 */
	public Environment(int lightLevel, int temperature, int humidityLevel,
			int pollutionLevel, int foodTotalAmount) {
		this.lightLevel = lightLevel;
		this.temperature = temperature;
		this.humidityLevel = humidityLevel;
		this.pollutionLevel = pollutionLevel;
		this.foodTotalAmount = foodTotalAmount;
	}

	/**
	 * @return the lightLevel
	 */
	public int getLightLevel() {
		return lightLevel;
	}

	/**
	 * @param lightLevel the lightLevel to set
	 */
	public void setLightLevel(int lightLevel) {
		this.lightLevel = lightLevel;
	}

	/**
	 * @return the temperature
	 */
	public int getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature the temperature to set
	 */
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	/**
	 * @return the humidityLevel
	 */
	public int getHumidityLevel() {
		return humidityLevel;
	}

	/**
	 * @param humidityLevel the humidityLevel to set
	 */
	public void setHumidityLevel(int humidityLevel) {
		this.humidityLevel = humidityLevel;
	}

	/**
	 * @return the pollutionLevel
	 */
	public int getPollutionLevel() {
		return pollutionLevel;
	}

	/**
	 * @param pollutionLevel the pollutionLevel to set
	 */
	public void setPollutionLevel(int pollutionLevel) {
		this.pollutionLevel = pollutionLevel;
	}

	/**
	 * @return the foodTotalAmount
	 */
	public int getFoodTotalAmount() {
		return foodTotalAmount;
	}

	/**
	 * @param foodTotalAmount the foodTotalAmount to set
	 */
	public void setFoodTotalAmount(int foodTotalAmount) {
		this.foodTotalAmount = foodTotalAmount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\n light level = " + String.format("%,d", lightLevel) + "% " +
				"\n temperature = " + String.format("%,d", temperature) + "C " +
				"\n humidity level = " + String.format("%,d", humidityLevel) + "% " +
				"\n pollution level = " + String.format("%,d", pollutionLevel) + "% " +
				"\n total food amount = " + String.format("%,d", foodTotalAmount) + " kCal";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + foodTotalAmount;
		result = prime * result + humidityLevel;
		result = prime * result + lightLevel;
		result = prime * result + pollutionLevel;
		result = prime * result + temperature;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Environment other = (Environment) obj;
		if (foodTotalAmount != other.foodTotalAmount)
			return false;
		if (humidityLevel != other.humidityLevel)
			return false;
		if (lightLevel != other.lightLevel)
			return false;
		if (pollutionLevel != other.pollutionLevel)
			return false;
		if (temperature != other.temperature)
			return false;
		return true;
	}
	
}
