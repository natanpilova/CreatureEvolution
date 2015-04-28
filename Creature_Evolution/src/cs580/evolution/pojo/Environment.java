/**
 * Class to represent environmental characteristics
 */
package cs580.evolution.pojo;

/**
 * @author Natalia Anpilova, Ankith Raj
 *
 */
public class Environment {
	/**
	 * constants limiting min and max values for the variables
	 */
	public static final int MIN_LIGHT_LEVEL = 0;				//total darkness - always night - 0 hours of sunlight
	public static final int MAX_LIGHT_LEVEL = 24;				//always daylight - 24 hours sunlight
	
	public static final int MIN_TEMPERATURE_LEVEL = -25;		//cannot be less than -25
	public static final int MAX_TEMPERATURE_LEVEL = 55;			//cannot exceed 55C
	
	public static final int MIN_RAIN_LEVEL = 50;				//In millimeters. Desert.
	public static final int MAX_RAIN_LEVEL = 5000;				//Tropical Rainforest
	
	public static final int MIN_POLLUTION_LEVEL = 0;			//no pollution
	public static final int MAX_POLLUTION_LEVEL = 100;			//100% polluted non-survivable environment
	public static final int POLLUTION_MUTATION_THRESHOLD = 40; 	//if 40% polluted then possibly higher mutation probability
	
	public static final int MIN_FOOD_LEVEL = 0;					//no food available
	public static final int MAX_FOOD_LEVEL = 100;				//Food abundantly available (100%)
	
	public static final int MIN_PREDATOR_LEVEL = 0;				//no Predators
	public static final int MAX_PREDATOR_LEVEL = 100;			//Very hostile environment for ants
	
	
	/**
	 * variables - environmental characteristics
	 */
	private int lightLevel;				//how long is the day
	private int temperature;			//average daily temperature in C
	private int rainLevel;				//rain level: 50mm means pretty dry climate, 5000mm means heavy rains
	private int pollutionLevel;			//pollution (CO, radioactivity, waste, etc.) level: 0% means no pollution, 50% is pretty bad pollution, 100% is non-survivable
	private int foodLevel;				//total amount of food available with the percentage
	private int predatorLevel;			//the level of predators existing. 0-100%
	/**
	 * no-argument constructor
	 */
	public Environment() {
		this.lightLevel = 12;
		this.temperature = 30;
		this.rainLevel = 50;
		this.pollutionLevel = MIN_POLLUTION_LEVEL;
		this.foodLevel = 50;
		this.predatorLevel = 50;
	}
	
	/**
	 * constructor
	 * @param lightLevel
	 * @param temperature
	 * @param rainLevel
	 * @param pollutionLevel
	 * @param foodLevel
	 */
	public Environment(int lightLevel, int temperature, int rainLevel,
			int pollutionLevel, int foodLevel, int predatorLevel) {
		this.lightLevel = lightLevel;
		this.temperature = temperature;
		this.rainLevel = rainLevel;
		this.pollutionLevel = pollutionLevel;
		this.foodLevel = foodLevel;
		this.predatorLevel = predatorLevel;
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
	 * @return the rainLevel
	 */
	public int getrainLevel() {
		return rainLevel;
	}

	/**
	 * @param rainLevel the rainLevel to set
	 */
	public void setrainLevel(int rainLevel) {
		this.rainLevel = rainLevel;
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
	 * @return the foodLevel
	 */
	public int getfoodLevel() {
		return foodLevel;
	}

	/**
	 * @param foodLevel the foodLevel to set
	 */
	public void setfoodLevel(int foodLevel) {
		this.foodLevel = foodLevel;
	}
	
	/**
	 * @return the predatorLevel
	 */
	public int getpredatorLevel() {
		return predatorLevel;
	}

	/**
	 * @param predatorLevel the predatorLevel to set
	 */
	public void setpredatorLevel(int predatorLevel) {
		this.predatorLevel = predatorLevel;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\n light level = " + lightLevel + " hrs per day " +
				"\n avg daily temperature = " + temperature + "C " +
				"\n rain level = " + rainLevel + " mm per year " +
				"\n pollution level = " + pollutionLevel + "% " +
				"\n total food level = " + foodLevel + "% " +
				"\n predator level = " + predatorLevel + "%";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + foodLevel;
		result = prime * result + rainLevel;
		result = prime * result + lightLevel;
		result = prime * result + pollutionLevel;
		result = prime * result + temperature;
		result = prime * result + predatorLevel;
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
		if (foodLevel != other.foodLevel)
			return false;
		if (rainLevel != other.rainLevel)
			return false;
		if (lightLevel != other.lightLevel)
			return false;
		if (pollutionLevel != other.pollutionLevel)
			return false;
		if (temperature != other.temperature)
			return false;
		if (predatorLevel != other.predatorLevel)
			return false;
		return true;
	}
	
}
