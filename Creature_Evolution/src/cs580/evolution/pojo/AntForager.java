/**
 * Class to represent black ant forager features
 */
package cs580.evolution.pojo;

import java.util.Random;

import cs580.evolution.function.Reproduce;

/**
 * @author Natalia Anpilova
 *
 */
@SuppressWarnings("rawtypes")
public class AntForager implements Comparable {
	/**
	 * constants limiting min and max values for the variables
	 */
	public static final double MAX_SIZE = 25;		//25 mm
	public static final double MIN_SIZE = 2;		//2 mm

	public static final double MAX_WEIGHT = 5;		//5 mg
	public static final double MIN_WEIGHT = 1;		//1 mg

	public static final double MAX_JAW_SIZE = 13;	//13 mm
	public static final double MIN_JAW_SIZE = 0.2;	//0.2 mm

	public static final double MAX_FEROMONE_INTENSITY = 100;	//mark 100% of the trail
	public static final double MIN_FEROMONE_INTENSITY = 0;		//no feromone marks at all
	
	public static final int MAX_SENSORS = 9000;
	public static final int MIN_SENSORS = 6000;

	public static final int MAX_OFFSPRING_COUNT = 100;
	public static final int MIN_OFFSPRING_COUNT = 0;
	
	/**
	 * variables - features, offspring count, and utility/fitness
	 */
	private double size;				//gene #1: size in millimeters (mm)
	private double weight;				//gene #2: weight in milligrams (mg)
	private double jawSize;				//gene #3: jaws size in mm
	private double feromoneIntensity;	//gene #4: intensity with which feromones are used to mark a trail
	private int antennaeSensors;		//gene #5: antennae sensory cells count
	
	private int offspringCount;			//number of offspring produced
	private double foodSurplus;			//food found - food consumed in grams (g): used as utility/fitness level

	/**
	 * no-argument constructor
	 */
	public AntForager() {
		this.size = MIN_SIZE;
		this.weight = MIN_WEIGHT;
		this.jawSize = MIN_JAW_SIZE;
		this.feromoneIntensity = MIN_FEROMONE_INTENSITY;
		this.antennaeSensors = MIN_SENSORS;
		this.offspringCount = 0;	//freshly created ant does not have offspring yet
		this.foodSurplus = 0;		//utility is calculated outside this class since it also depends on environment
	}	
	
	/**
	 * constructor
	 * @param size
	 * @param weight
	 * @param jawSize
	 * @param feromoneIntensity
	 * @param antennaeSensors
	 * @param offspringCount
	 * @param foodSurplus
	 */
	public AntForager(double size, double weight, double jawSize, double feromoneIntensity, int antennaeSensors) {
		this.size = size;
		this.weight = weight;
		this.jawSize = jawSize;
		this.feromoneIntensity = feromoneIntensity;
		this.antennaeSensors = antennaeSensors;
		this.offspringCount = 0;	//freshly created ant does not have offspring yet
		this.foodSurplus = 0;		//utility is calculated outside this class since it also depends on environment
	}

	/**
	 * Generates a random ant feature set
	 * @return ant feature set with random features
	 */
	public static AntForager generateRandomAnt() {
		AntForager randomAnt = new AntForager();
		Random rand = new Random(); 
		
		do {
			double randomDouble = MAX_SIZE*rand.nextDouble();
			if (randomDouble < MIN_SIZE)
				randomDouble = MIN_SIZE;
			else if (randomDouble > MAX_SIZE)
				randomDouble = MAX_SIZE;
			randomAnt.setSize(randomDouble);
			
			randomDouble = MAX_WEIGHT*rand.nextDouble();
			if (randomDouble < MIN_WEIGHT)
				randomDouble = MIN_WEIGHT;
			else if (randomDouble > MAX_WEIGHT)
				randomDouble = MAX_WEIGHT;
			randomAnt.setWeight(randomDouble);
			
			randomDouble = MAX_JAW_SIZE*rand.nextDouble();
			if (randomDouble < MIN_JAW_SIZE)
				randomDouble = MIN_JAW_SIZE;
			else if (randomDouble > MAX_JAW_SIZE)
				randomDouble = MAX_JAW_SIZE;
			randomAnt.setJawSize(randomDouble);
			
			randomDouble = MAX_FEROMONE_INTENSITY*rand.nextDouble();
			if (randomDouble < MIN_FEROMONE_INTENSITY)
				randomDouble = MIN_FEROMONE_INTENSITY;
			else if (randomDouble > MAX_FEROMONE_INTENSITY)
				randomDouble = MAX_FEROMONE_INTENSITY;
			randomAnt.setFeromoneIntensity(randomDouble);
			
			int randomInt = rand.nextInt(MAX_SENSORS+1);
			if (randomInt < MIN_SENSORS)
				randomInt = MIN_SENSORS;
			randomAnt.setAntennaeSensors(randomInt);
			
		} while (!Reproduce.AntForagerCheck(randomAnt)); //check for realistic features
		
		return randomAnt;
	}
	
	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/**
	 * @return the jawSize
	 */
	public double getJawSize() {
		return jawSize;
	}

	/**
	 * @param jawSize the jawSize to set
	 */
	public void setJawSize(double jawSize) {
		this.jawSize = jawSize;
	}

	/**
	 * @return the feromoneIntensity
	 */
	public double getFeromoneIntensity() {
		return feromoneIntensity;
	}

	/**
	 * @param feromoneIntensity the feromoneIntensity to set
	 */
	public void setFeromoneIntensity(double feromoneIntensity) {
		this.feromoneIntensity = feromoneIntensity;
	}

	/**
	 * @return the antennaeSensors
	 */
	public int getAntennaeSensors() {
		return antennaeSensors;
	}

	/**
	 * @param antennaeSensors the antennaeSensors to set
	 */
	public void setAntennaeSensors(int antennaeSensors) {
		this.antennaeSensors = antennaeSensors;
	}

	/**
	 * @return the offspringCount
	 */
	public int getOffspringCount() {
		return offspringCount;
	}

	/**
	 * @param offspringCount the offspringCount to set
	 */
	public void setOffspringCount(int offspringCount) {
		this.offspringCount = offspringCount;
	}
	
	/**
	 * @param add offspringProduced to the offspringCount
	 */
	public void addOffspring(int offspringProduced) {
		this.offspringCount += offspringProduced;
	}
	
	/**
	 * @return the foodSurplus
	 */
	public double getFoodSurplus() {
		return foodSurplus;
	}

	/**
	 * @param foodSurplus the foodSurplus to set
	 */
	public void setFoodSurplus(double foodSurplus) {
		this.foodSurplus = foodSurplus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\n size = " + Math.round(size*100)/100.00 + " mm " +
				"\n weight = " + Math.round(weight*100)/100.00 + " mg " +
				"\n jaw size = " + Math.round(jawSize*100)/100.00 + " mm " +
				"\n feromone marks intensity = " + Math.round(feromoneIntensity) + "% of trail " +
				"\n antennae sensory cells = " + String.format("%,d", antennaeSensors) + " " +
				"\n offspring count = " + String.format("%,d", offspringCount) + " " +
				"\n Food surplus = " + Math.round(foodSurplus*10000)/10000.00 + " g";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + antennaeSensors;
		long temp;
		temp = Double.doubleToLongBits(feromoneIntensity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(foodSurplus);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(jawSize);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + offspringCount;
		temp = Double.doubleToLongBits(size);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		AntForager other = (AntForager) obj;
		if (antennaeSensors != other.antennaeSensors)
			return false;
		if (Double.doubleToLongBits(feromoneIntensity) != Double
				.doubleToLongBits(other.feromoneIntensity))
			return false;
		if (Double.doubleToLongBits(foodSurplus) != Double
				.doubleToLongBits(other.foodSurplus))
			return false;
		if (Double.doubleToLongBits(jawSize) != Double
				.doubleToLongBits(other.jawSize))
			return false;
		if (offspringCount != other.offspringCount)
			return false;
		if (Double.doubleToLongBits(size) != Double
				.doubleToLongBits(other.size))
			return false;
		if (Double.doubleToLongBits(weight) != Double
				.doubleToLongBits(other.weight))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		if (o.getClass().getName().equals(getClass().getName())) {
			double diff = foodSurplus - ((AntForager) o).getFoodSurplus();
			if (diff == 0)
				return 0;
			else if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
		}
		return 0;
	}
}
