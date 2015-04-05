/**
 * Class for Child function
 */
package cs580.evolution.function;
import java.util.Random;

import cs580.evolution.pojo.Genome;

/**
 * @author Ashwin Hole
 *
 */
public class Reproduce {

	//TODO (REQUIRED - Ashwin): check if the resulting child has realistic genome:
	//adjust metabolism with weight, height and other genes like -
	//if number of eyes/wings/ears/legs/fins/height/weight increased, metabolism should be higher, if decreased - lower (having all these extra things need more energy!),
	//if height is big then the weight cannot be too small, and vice versa.
	//This is to rule out unlikely situations like height is 10cm and weight is 1 ton, and this creature eats only 1kCal per day. Impossible, right?
	//Best is to add one more private method returning boolean value - say, sanityCheck(Genome kid) for this check, and call it on the kid at the end of produceChild();
	//if didn't pass (returns false) then produce another child instead - so loop like "while (!sanityCheck(kid) {....}" is needed
	
	/**
	 * Reproduce function
	 * @param mom
	 * @param dad
	 * @return kid
	 */
	public Genome produceChild(Genome mom, Genome dad) {
		Random r = new Random();
		int crossover = r.nextInt(8);
		
		int[] momarray = new int[8];
		int[] dadarray = new int[8];
		int[] childarray = new int[8];
		double h;
		boolean coop;
		
		momarray[0] = mom.getEyesNumber();
		momarray[1] = mom.getEarsNumber();
		momarray[2] = mom.getLegsNumber();
		momarray[3] = mom.getFinsNumber();
		momarray[4] = mom.getWingsNumber();
		momarray[5] = mom.getCoatThickness();
		momarray[6] = mom.getWeight();
		momarray[7] = mom.getMetabolism();
		
		dadarray[0] = dad.getEyesNumber();
		dadarray[1] = dad.getEarsNumber();
		dadarray[2] = dad.getLegsNumber();
		dadarray[3] = dad.getFinsNumber();
		dadarray[4] = dad.getWingsNumber();
		dadarray[5] = dad.getCoatThickness();
		dadarray[6] = dad.getWeight();
		dadarray[7] = dad.getMetabolism();
		
	
		if(crossover > 0)
		for(int i=0;i<crossover;i++)
		childarray[i] = momarray[i];
		
		if(crossover < 8)
		for(int i=crossover;i<8;i++)
		childarray[i] = dadarray[i];
		
		coop = dad.isCooperationFlag();
		
		
		if(crossover == 8)
		h = mom.getHeight();	
		else
		h = dad.getHeight();
		
		Genome kid = new Genome(childarray[0], childarray[1], childarray[2], childarray[3], childarray[4], childarray[5], childarray[6], childarray[7], h, coop);

		
		return kid;
	}
}
