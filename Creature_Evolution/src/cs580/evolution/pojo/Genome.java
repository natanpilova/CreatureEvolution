/**
 * Class to represent a creature's genome
 */
package cs580.evolution.pojo;

/**
 * @author Natalia Anpilova (will add all the team here)
 *
 */
public class Genome {
	/**
	 * constants limiting min and max values for the variables
	 */
	public static final int MIN_EYES_NUMBER = 0;		//blind
	public static final int MAX_EYES_NUMBER = 10;
	
	public static final int MIN_EARS_NUMBER = 0;		//death
	public static final int MAX_EARS_NUMBER = 10;
	
	public static final int MIN_LEGS_NUMBER = 0;		//cannot walk/run/jump
	public static final int MAX_LEGS_NUMBER = 8;
	
	public static final int MIN_FINS_NUMBER = 0;		//cannot swim
	public static final int MAX_FINS_NUMBER = 8;
	
	public static final int MIN_WINGS_NUMBER = 0;		//cannot fly
	public static final int MAX_WINGS_NUMBER = 6;
	
	public static final int MIN_COAT_THICKNESS = 1;		//hairless thin skin
	public static final int MAX_COAT_THICKNESS = 100;
	
	public static final double MIN_HEIGHT = 0.1;		//10 cm tall
	public static final double MAX_HEIGHT = 100;		//100 m tall
	
	public static final int MIN_WEIGHT = 1;				//1 kg
	public static final int MAX_WEIGHT = 10000;			//10 ton
	
	public static final int MIN_METABOLISM = 100;		//100 kCal
	public static final int MAX_METABOLISM = 1000000;	//1 million kCal
	
	public static final int MIN_OFFSPRING_COUNT = 0;
	public static final int MAX_OFFSPRING_COUNT = 12;
	
	/**
	 * variables - genes and offspring count
	 */
	private int eyesNumber;				//gene #1: number of eyes
	private int earsNumber;				//gene #2: number of ears
	private int legsNumber;				//gene #3: number of legs/arms - for walking/running/jumping
	private int finsNumber;				//gene #4: number of fins - for swimming
	private int wingsNumber;			//gene #5: number of wings - for flying
	private int coatThickness;			//gene #6: coat thickness in cm (fur/feathers/fat layer)
	private double height;				//gene #7: height in meters
	private int weight;					//gene #8: weight in kg
	private int metabolism;				//gene #9: energy (food) required per day in kCal
	private boolean cooperationFlag;	//gene #10: true if cooperates with others, false if does not
	private int offspringCount;			//number of offspring produced
	
	/**
	 * no-argument constructor
	 */
	public Genome() {
		this.eyesNumber = MIN_EYES_NUMBER;
		this.earsNumber = MIN_EARS_NUMBER;
		this.legsNumber = MIN_LEGS_NUMBER;
		this.finsNumber = MIN_FINS_NUMBER;
		this.wingsNumber = MIN_WINGS_NUMBER;
		this.coatThickness = MIN_COAT_THICKNESS;
		this.height = MIN_HEIGHT;
		this.weight = MIN_WEIGHT;
		this.metabolism = MIN_METABOLISM;
		this.cooperationFlag = false;
		this.offspringCount = 0;
	}	
	
	/**
	 * constructor
	 * @param eyesNumber
	 * @param earsNumber
	 * @param legsNumber
	 * @param finsNumber
	 * @param wingsNumber
	 * @param coatThickness
	 * @param height
	 * @param weight
	 * @param metabolism
	 * @param cooperationFlag
	 */
	public Genome(int eyesNumber, int earsNumber, int legsNumber,
			int finsNumber, int wingsNumber, int coatThickness, double height,
			int weight, int metabolism, boolean cooperationFlag) {
		this.eyesNumber = eyesNumber;
		this.earsNumber = earsNumber;
		this.legsNumber = legsNumber;
		this.finsNumber = finsNumber;
		this.wingsNumber = wingsNumber;
		this.coatThickness = coatThickness;
		this.height = height;
		this.weight = weight;
		this.metabolism = metabolism;
		this.cooperationFlag = cooperationFlag;
		this.offspringCount = 0;	//freshly created genome(creature) does not have offspring yet
	}
	
	/**
	 * @return the eyesNumber
	 */
	public int getEyesNumber() {
		return eyesNumber;
	}
	
	/**
	 * @param eyesNumber the eyesNumber to set
	 */
	public void setEyesNumber(int eyesNumber) {
		this.eyesNumber = eyesNumber;
	}
	
	/**
	 * @return the earsNumber
	 */
	public int getEarsNumber() {
		return earsNumber;
	}
	
	/**
	 * @param earsNumber the earsNumber to set
	 */
	public void setEarsNumber(int earsNumber) {
		this.earsNumber = earsNumber;
	}
	
	/**
	 * @return the legsNumber
	 */
	public int getLegsNumber() {
		return legsNumber;
	}
	
	/**
	 * @param legsNumber the legsNumber to set
	 */
	public void setLegsNumber(int legsNumber) {
		this.legsNumber = legsNumber;
	}
	
	/**
	 * @return the finsNumber
	 */
	public int getFinsNumber() {
		return finsNumber;
	}
	
	/**
	 * @param finsNumber the finsNumber to set
	 */
	public void setFinsNumber(int finsNumber) {
		this.finsNumber = finsNumber;
	}
	
	/**
	 * @return the wingsNumber
	 */
	public int getWingsNumber() {
		return wingsNumber;
	}
	
	/**
	 * @param wingsNumber the wingsNumber to set
	 */
	public void setWingsNumber(int wingsNumber) {
		this.wingsNumber = wingsNumber;
	}
	
	/**
	 * @return the coatThickness
	 */
	public int getCoatThickness() {
		return coatThickness;
	}
	
	/**
	 * @param coatThickness the coatThickness to set
	 */
	public void setCoatThickness(int coatThickness) {
		this.coatThickness = coatThickness;
	}
	
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	
	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * @return the metabolism
	 */
	public int getMetabolism() {
		return metabolism;
	}
	
	/**
	 * @param metabolism the metabolism to set
	 */
	public void setMetabolism(int metabolism) {
		this.metabolism = metabolism;
	}
	
	/**
	 * @return the cooperationFlag
	 */
	public boolean isCooperationFlag() {
		return cooperationFlag;
	}
	
	/**
	 * @param cooperationFlag the cooperationFlag to set
	 */
	public void setCooperationFlag(boolean cooperationFlag) {
		this.cooperationFlag = cooperationFlag;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Genome [eyesNumber=" + eyesNumber + ", earsNumber="
				+ earsNumber + ", legsNumber=" + legsNumber + ", finsNumber="
				+ finsNumber + ", wingsNumber=" + wingsNumber
				+ ", coatThickness=" + coatThickness + ", height=" + height
				+ ", weight=" + weight + ", metabolism=" + metabolism
				+ ", cooperationFlag=" + cooperationFlag + ", offspringCount="
				+ offspringCount + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + coatThickness;
		result = prime * result + (cooperationFlag ? 1231 : 1237);
		result = prime * result + earsNumber;
		result = prime * result + eyesNumber;
		result = prime * result + finsNumber;
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + legsNumber;
		result = prime * result + metabolism;
		result = prime * result + offspringCount;
		result = prime * result + weight;
		result = prime * result + wingsNumber;
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
		Genome other = (Genome) obj;
		if (coatThickness != other.coatThickness)
			return false;
		if (cooperationFlag != other.cooperationFlag)
			return false;
		if (earsNumber != other.earsNumber)
			return false;
		if (eyesNumber != other.eyesNumber)
			return false;
		if (finsNumber != other.finsNumber)
			return false;
		if (Double.doubleToLongBits(height) != Double
				.doubleToLongBits(other.height))
			return false;
		if (legsNumber != other.legsNumber)
			return false;
		if (metabolism != other.metabolism)
			return false;
		if (offspringCount != other.offspringCount)
			return false;
		if (weight != other.weight)
			return false;
		if (wingsNumber != other.wingsNumber)
			return false;
		return true;
	}
	
}
