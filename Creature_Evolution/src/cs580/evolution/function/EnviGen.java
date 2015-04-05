/* Class to Generate a new Environment */
package cs580.evolution.function;
import cs580.evolution.pojo.Environment;

/**
 * @author Ankith Raj
 *
 */

public class EnviGen {
		//TODO (optional - Ankith): replace some of the hard-coded here values (number of generations for calamity and ice age)
		//with environmental characteristics from input args - coordinate with Natalia (if she implements these input args)
		//If we do this then best is to add them as class instance variables and pass in via constructor in here
	
		int tmax=0;					   	// If there is a calamity. Initially none
		int direction = 1;				// Direction of the sine wave. 1: Increase, 0: Decrease. Initially increase
		
		int new_lightLevel;											
		int new_temperature;										
		int new_humidityLevel;										
		int new_pollutionLevel;										
		int new_foodTotalAmount;

					
			public Environment EG(Environment E, int generationCount)		//The function that computes the new environment
			{
				new_lightLevel = E.getLightLevel();
				new_temperature = E.getTemperature();
				new_humidityLevel = E.getHumidityLevel();
				new_pollutionLevel = E.getPollutionLevel();
				new_foodTotalAmount = E.getFoodTotalAmount();
				
				/*
				System.out.println("Generations : "+ generationCount);
				System.out.println("lightLevel : "+ new_lightLevel);
				System.out.println("temperature : "+ new_temperature);
				System.out.println("humidityLevel : "+ new_humidityLevel);
				System.out.println("pollutionLevel : "+ new_pollutionLevel);
				System.out.println("foodTotalAmount : "+ new_foodTotalAmount);
				*/

			/*
			 *  All the variables depend on temperature. Each factor varies accordingly 	
			 * 
			 * +35 degree C => Light = 50%
			 * Lower than -115 degree C => Light = 0%
			 * Higher than +185 degree C => Light = 100%
			 * 
			 * +35 degree C => Pollution = 50%
			 * Lower than -115 degree C => Pollution = 0%
			 * Higher than +185 degree C => Pollution = 50%
			 * 
			 * +35 degree C => Food = 1000000000
			 * Lower than -115 degree C => Food = 0
			 * Higher than +185 degree C => Food = 0
			 * 
			 * Lower than 0 degree C => Humidity = 0
			 * Higher than +100 degree C => Humidity = 0
			 * +50 degree C => Humidity = 50
			 * 
			 */
				
				//loop is not needed - you were going through all generations from the start - you need to generate environment just for current generation! -Natalia
				//for(int i=1 ; i <= generationCount ; i++)				// Loops for the specified number of generations
				//{
				int i = generationCount;
					
					if(i % 50000 == 0 && i != 10000)				// Ice age every 50000 generations
					{
						System.out.println("Generation " + generationCount);
						iceage();
						//continue;
					}
					else if(tmax == 1)									// Generally, an abrupt rise in temperature is followed by an iceage
					{
						System.out.println("Generation " + generationCount);
						iceage();
						//continue;
					}
					
					//moved calamity check under ice age check to avoid having both within the same generation
					if(i == 10000)									// A big calamity after 10000 generations that rises the temperature abruptly (volcano or meteorite)
					{
						System.out.println("Generation " + generationCount);
						calamity();
						//i++;										// It will exist for 2 generations
						//continue;
					}
					
					if(new_temperature < 0 || new_temperature > 100)
					{
						new_humidityLevel = Environment.MIN_HUMIDITY_LEVEL;			// Water exists only between 0 and 100
					}
					else
					{
						new_humidityLevel = new_temperature;
					}
					
					
					if(new_temperature > 185)
					{
						new_temperature = new_temperature - 5;			// Temperature decrease from calamity is a lot faster
						new_lightLevel = Environment.MAX_LIGHT_LEVEL;
						new_pollutionLevel = Environment.MAX_POLLUTION_LEVEL;
						new_foodTotalAmount = Environment.MIN_FOOD_AMOUNT;
						//continue;
					}
					
					if(new_temperature < -115)
					{
						new_temperature = new_temperature + 5;					// Temperature increase from Ice age is a lot faster
						new_lightLevel = Environment.MIN_LIGHT_LEVEL;			// Total darkness
						new_pollutionLevel = Environment.MIN_POLLUTION_LEVEL;
						new_foodTotalAmount = Environment.MIN_FOOD_AMOUNT;
						//continue;
					}
					
					
					if(direction == 1)									// Temperature varies in the form of a sine wave Max=185, Min= -115
					{
					new_temperature = new_temperature + 1;				// Temperature increases by 1 for each generation
					}
					
					else if(direction == 0)
					{
					new_temperature = new_temperature - 1;				// Temperature decreases by 1 for each generation
					}
					
					//TODO (optional - Ankith): this is a linear "zig-zag" dependency which is fine; but if you want real sin then use it instead
					// A temperature shift in the form of a sine wave 
					if(new_temperature == 185)							// Temperature upper boundary for other factors to exist
					{
						direction = 0;								    // Value starts to decrease after reaching maxima
					}
					
					if(new_temperature == -115)							//  Temperature lower boundary for other factors to exist
					{
						direction = 1;							      	// Value starts to increase after reaching minima
					}
					
					new_lightLevel = (new_temperature + 115) / 3;								        //Light based on temperature
					new_pollutionLevel = (new_temperature + 115) / 3; 					                //Pollution based on temperature
					new_foodTotalAmount = 1000000000 - (Math.abs(new_temperature-35)*(1000000000/150)); // food is peak at 35C, reduces with increase/decrease
													
				//}
				
				/*
				System.out.println("");
				System.out.println("new_lightLevel : "+ new_lightLevel);
				System.out.println("new_temperature : "+ new_temperature);
				System.out.println("new_humidityLevel : "+ new_humidityLevel);
				System.out.println("new_pollutionLevel : "+ new_pollutionLevel);
				System.out.println("new_foodTotalAmount : "+ new_foodTotalAmount);
				*/
				
				Environment newEnv = new Environment (new_lightLevel, new_temperature, new_humidityLevel, new_pollutionLevel, new_foodTotalAmount);
				return newEnv;
				
			}
			
			public void iceage()
			{
				new_temperature = Environment.MIN_TEMPERATURE_LEVEL;			// Ice age is generally followed the successive year of natural calamities (Nuclear Winter)
				new_lightLevel = Environment.MIN_LIGHT_LEVEL;				   	// Total darkness
				new_pollutionLevel = Environment.MIN_POLLUTION_LEVEL;			// Lower the temperature, lower the pollution
				new_foodTotalAmount = Environment.MIN_FOOD_AMOUNT;				// There would be no food at this temperature
				new_humidityLevel = Environment.MIN_HUMIDITY_LEVEL;				// No water
				tmax=0;											                // Calamity ends
				System.out.println("-- ICE AGE --");
			}
			
			public void calamity()
			{
				new_temperature = Environment.MAX_TEMPERATURE_LEVEL;			// Highest Temperature
				tmax=1;											                // Indication that the temperature is at a max (calamity)
				new_lightLevel = Environment.MAX_LIGHT_LEVEL;				    // Higher the Temperature, greater will be the light
				new_pollutionLevel = Environment.MAX_POLLUTION_LEVEL;			// Higher the temperature, higher the pollution
				new_foodTotalAmount = Environment.MIN_FOOD_AMOUNT;				// There would be no food at this temperature
				new_humidityLevel = Environment.MIN_HUMIDITY_LEVEL;				// No Water
				System.out.println("-- CALAMITY --");
			}
	
}
