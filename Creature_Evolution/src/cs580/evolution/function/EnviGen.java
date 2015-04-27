/* Class to Generate a new Environment */
package cs580.evolution.function;

import org.apache.log4j.Logger;
import cs580.evolution.main.CreatureEvolution;
import cs580.evolution.pojo.Environment;

import java.util.Random;

/**
 * @author Ankith Raj
 *
 */

public class EnviGen{

		final static Logger log = Logger.getLogger(CreatureEvolution.class);
		
		float new_lightLevel;							
		float new_temperature;							
		int new_rainLevel;								
		int new_pollutionLevel;								
		int new_foodLevel;		
		int new_predatorLevel;	

		// These are the general maxima and minima Climatic values that is possible for the AFRICAN continent 
		private  static final int genminlight = 9;			// In Average hours per day. Range: 9-15
		private  static final int genmaxlight = 15;
		
		private  static final int genmintemp = 10;			// In Celsius. Range: 10-50 degree celsius
		private  static final int genmaxtemp = 50;
		
		private  static final int genminrain = 500;			// In mm per generation. Range: 500-5000mm
		private  static final int genmaxrain = 5000;
		
		private  static final int genminpoll = 5;			// In %. Range: 5-50%
		private  static final int genmaxpoll = 50;
		
		private  static final int genminfood = 30;			// In % available. Range: 30-80%
		private  static final int genmaxfood = 80;
		
		private  static final int genminpred = 5;			// In % present. Range: 5-70%
		private  static final int genmaxpred = 70;
		
		private  static int dflag = 0;				// Flag to check if there is a drought
		private  static int fflag = 0;				// Flag to check if there is a flood
		private  static int rflag = 0;				// Flag to check if there is a rare event such as a volcanic eruption or anything of such sort which is catastrophic
		
		Random r = new Random();
		

			public Environment EG(Environment E, int generationCount)		//The function that computes the new environment
			{
				new_lightLevel = E.getLightLevel();
				new_temperature = E.getTemperature();
				new_rainLevel = E.getrainLevel();
				new_pollutionLevel = E.getPollutionLevel();
				new_foodLevel = E.getfoodLevel();
				new_predatorLevel = E.getpredatorLevel();
		
				int gcount = generationCount;
				Random r = new Random();
				
				// These are the random variables used to compute the various variables
				float rlight = r.nextFloat();
				int rselector = r.nextInt(2);	
				int rpredator = r.nextInt(7);
				int rfood = r.nextInt(4);
				int rdrought1 = r.nextInt(200);
				int rdrought2 = r.nextInt(200);
				int rflood1 = r.nextInt(200);
				int rflood2 = r.nextInt(200);
				int rrare1 = r.nextInt(1000);
				int rrare2 = r.nextInt(1000);
		
				
				if(rdrought1 == rdrought2)		// To see whether there is a drought. The probability is 1 in 40000 generations
				{
					drought();					// The drought function is called
					dflag = 1;					// Flag indicates drought
				}
				
				if(rflood1 == rflood2 && dflag==0)	// To see whether there is a flood. The probability is 1 in 40000 generations
				{
					flood();					// The flood function is called
					fflag = 1;					// Flag indicates flood
				}
				
				if(rrare1 == rrare2)			// To see whether there is a rare event. The probability is 1 in 1000000 generations
				{
					rareevent();
					rflag = 1;
				}
				
				if(dflag == 0 && fflag == 0 && rflag == 0)		// If there is a drought or flood or rare event, the remaining computations are skipped
				{

				if(new_lightLevel < genminlight) {				// The new light level should not exceed the general boundaries
					new_lightLevel = new_lightLevel + rlight;	// rlight is the random float variable
					}
				else if(new_lightLevel > genmaxlight) {
					new_lightLevel = new_lightLevel - rlight;
					}
				else if(rselector == 1) {						// The increase or decrease of light level is done randomly using rselector random integer (0 or 1)
					new_lightLevel = new_lightLevel + rlight;
					}
				else if(rselector == 0) {
					new_lightLevel = new_lightLevel - rlight;
					}
				
				
				// The temperature depends on the light level. More the light, greater the temperature
					new_temperature = genmintemp +  (new_lightLevel-genminlight) * (genmaxtemp-genmintemp)/(genmaxlight-genminlight);
					if(new_temperature > genmaxtemp)
						new_temperature = genmaxtemp;
					if(new_temperature < genmintemp)
						new_temperature = genmintemp;
				
				// The rain level depends on the light and temperature level. Less the light and temperature, more the rain
					new_rainLevel = (int)(genminrain + (genmaxlight - new_lightLevel) * (genmaxrain-genminrain)/(genmaxlight-genminlight));
					if(new_rainLevel > genmaxrain)
						new_rainLevel = genmaxrain;
					if(new_rainLevel < genminrain)
						new_rainLevel = genminrain;
					
				// The Predator level depends on the rain and light level and the number of generations.
				// As there is more light and rain, more will be the predators. And they increase with the number of generations
					new_predatorLevel = genminpred + genminpred * rpredator  + (int) ((new_lightLevel*new_rainLevel)/(genminlight*genminrain) + Math.log(gcount));
					if(new_predatorLevel > genmaxpred)
						new_predatorLevel = genmaxpred;
					if(new_predatorLevel < genminpred)
						new_predatorLevel = genminpred;
				
				// THe pollution level depends on the light, rain, pollution level and the number of generations
				// Increases with light and predator level and the number of generations, decreases with rain
					new_pollutionLevel = genminpoll + (int) (new_lightLevel + new_predatorLevel/genminpred + Math.log(gcount) - new_rainLevel/genminrain);
					if(new_pollutionLevel > genmaxpoll)
						new_pollutionLevel = genmaxpoll;
					if(new_pollutionLevel < genminpoll)
						new_pollutionLevel = genminpoll;
					
				// The food level depends on the light, rain, pollution, predator level and the number of generations
				// increases with light and rain, decreases with pollution, predator level and number of generations 
					new_foodLevel = genminfood  + genminfood * rfood 
												+ (int) (new_lightLevel * 2 
												+ (new_rainLevel) * 2 / genminrain									
												- (new_pollutionLevel) / genminpoll
												- (new_predatorLevel) / genminpred
												- Math.log(gcount));
					
					if(new_foodLevel < genminfood)
						new_foodLevel = genminfood;
					if(new_foodLevel > genmaxfood)
						new_foodLevel = genmaxfood;
				}
			
				Environment newEnv = new Environment((int)new_lightLevel, (int)new_temperature, new_rainLevel, new_pollutionLevel, new_foodLevel, new_predatorLevel);
				return newEnv;

			}

			
			public void flood()							// All the variables are the least except rain level which is at the highest value
			{
				new_temperature = genmintemp;			
				new_lightLevel = genminlight;				   	
				new_pollutionLevel = genminpoll;			// 
				new_foodLevel = genminfood/2;						// 
				new_rainLevel = Environment.MAX_RAIN_LEVEL;						// 											                
				System.out.println("-- FLOOD --");
				log.info("-- FLOOD --");
			}
			
			public void drought()						// All the variables are the high except rain level which is at the lowest value
			{
				new_temperature = genmaxtemp;
				new_lightLevel = genminlight;
				new_pollutionLevel = genmaxpoll;
				new_foodLevel = genminfood/2;	
				new_rainLevel = Environment.MIN_RAIN_LEVEL;
				System.out.println("-- DROUGHT --");
				log.info("-- DROUGHT --");
			}
			
			public void rareevent()
			{
				int rrarepicker = r.nextInt(2);				// 2 possible events chosen at random
				if(rrarepicker == 0)						// Possible low value for everything except rain
				{
					new_temperature = Environment.MIN_TEMPERATURE_LEVEL;			
					new_lightLevel = Environment.MIN_LIGHT_LEVEL; 
					new_pollutionLevel = Environment.MIN_POLLUTION_LEVEL; 
					new_foodLevel = genminfood/2;
					new_rainLevel = Environment.MAX_RAIN_LEVEL;		 											                
					System.out.println("-- RARE EVENT--");
					log.info("-- RARE EVENT --");
				}
				if(rrarepicker == 1)						// Possible high value for everything except rain
				{
					new_temperature = Environment.MAX_TEMPERATURE_LEVEL;			
					new_lightLevel = Environment.MAX_LIGHT_LEVEL;	 
					new_pollutionLevel = Environment.MAX_POLLUTION_LEVEL; 
					new_foodLevel = genminfood/3;						 
					new_rainLevel = Environment.MAX_RAIN_LEVEL;			 											                
					System.out.println("-- RARE EVENT--");
					log.info("-- RARE EVENT --");
				}
			}
			

	}
