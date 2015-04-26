/**
 * Class for Child function
 */
package cs580.evolution.function;
import java.util.Random;

import cs580.evolution.pojo.AntForager;

/**
 * @author Ashwin Hole
 *
 */
public class Reproduce {

	/**
	 * Reproduce function
	 * @param mom
	 * @param dad
	 * @return kid
	 */
	public AntForager produceChild(AntForager mom, AntForager dad) {
		Random r = new Random();
		
		
		double[] momarray = new double[5];
		double[] dadarray = new double[5];
		double[] childarray = new double[5];
		int as;
		
		
		momarray[0] = mom.getSize();
		momarray[1] = mom.getWeight();
		momarray[2] = mom.getJawSize();
		momarray[3] = mom.getFeromoneIntensity();
		momarray[4] = (double)mom.getAntennaeSensors();
		
		
		dadarray[0] = dad.getSize();
		dadarray[1] = dad.getWeight();
		dadarray[2] = dad.getJawSize();
		dadarray[3] = dad.getFeromoneIntensity();
		dadarray[4] = (double)dad.getAntennaeSensors();
		
		
		AntForager kid = null;
		int crossover = r.nextInt(6);
		
		if(crossover > 0)
		for(int i=0;i<crossover;i++)
		childarray[i] = momarray[i];
		
		if(crossover < 5)
		for(int i=crossover;i<5;i++)
		childarray[i] = dadarray[i];
		
		as = (int)childarray[4];
		
		kid = new AntForager(childarray[0], childarray[1], childarray[2], childarray[3], as);
		
		return kid;
	}
	
	/**
	 * Check if the resulting child has realistic AntForager
	 * @param kid
	 * @return true if pass
	 */
	public static boolean AntForagerCheck(AntForager kid){
		if(kid.getSize()>2 && kid.getSize()<=10 && kid.getWeight()>=1 && kid.getWeight()<=2)
		return true;
		else if(kid.getSize()>10 && kid.getSize()<=15 && kid.getWeight()>2 && kid.getWeight()<=3)
		return true;
		else if(kid.getSize()>15 && kid.getSize()<=20 && kid.getWeight()>3 && kid.getWeight()<=4)
		return true;
		else if(kid.getSize()>20 && kid.getSize()<=25 && kid.getWeight()>4 && kid.getWeight()<=5)
		return true;
		else return false;
	}
}
