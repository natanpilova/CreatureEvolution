/**
 * CS580 Spring 2015 Team 5 Term project
 */
package cs580.evolution.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cs580.evolution.function.EnviGen;
import cs580.evolution.function.Fitness;
import cs580.evolution.function.Mutation;
import cs580.evolution.function.Reproduce;
import cs580.evolution.function.Selection;
import cs580.evolution.pojo.AntForager;
import cs580.evolution.pojo.Environment;
import cs580.evolution.pojo.PopulationPair;

/**
 * @author Natalia Anpilova
 * @author Ashwin Hole
 * @author Divya Kondaviti
 * @author Ankith Raj
 * @author Manjusha Upadhye
 *
 */
public class CreatureEvolution extends JPanel implements ActionListener {
	private static final long serialVersionUID = -6211365806865371892L;
	
	//constants for output file where we dump the randomly generated initial population
	private static final String CSV_SEPARATOR = ", ";
	private static final String OUT_FILENAME_BASE = "C:/evolution/init_population";
	
	//log to C:/evolution/log/evolution.log file - configuration is in log4j.xml
	final static Logger log = Logger.getLogger(CreatureEvolution.class);
	
	private JButton btnInit;  
	private JButton btnStart;
	private JLabel  infoMsg;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    JFrame window = new JFrame("Best Ant Forager");
	    CreatureEvolution content = new CreatureEvolution();
	    window.setContentPane(content);
	    window.pack();
	    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	    window.setLocation((screensize.width - window.getWidth())/2, (screensize.height - window.getHeight())/2);
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setResizable(false);  
	    window.setVisible(true);
	}

	
	/**
	 * Constructor
	 */
	public CreatureEvolution() {
	    setLayout(null);	//custom layout
	    setPreferredSize(new Dimension(540, 600));
	    setBackground(new Color(204, 204, 204));  //light grey background
	     
	    /*
	     * Create the components
	     */
	    //TODO create inputs for population size and generations number/file
	    btnInit = new JButton("Initialize");
	    btnInit.addActionListener(this);
	    btnInit.setFont(new  Font("Courier New", Font.BOLD, 16));
	    btnInit.setBackground(new Color(51, 102, 0));
	    btnInit.setForeground(new Color(239, 235, 214));
	    btnInit.setBorder(null);
	    
	    btnStart = new JButton("Start");
	    btnStart.addActionListener(this);
	    btnStart.setFont(new  Font("Courier New", Font.BOLD, 16));
	    btnStart.setBackground(new Color(51, 102, 0));
	    btnStart.setForeground(new Color(239, 235, 214));
	    btnStart.setBorder(null);
	    
		infoMsg = new JLabel("",JLabel.LEFT);
		infoMsg.setFont(new  Font("Courier New", Font.BOLD, 16));
		infoMsg.setForeground(new Color(51, 102, 0));
		
	    add(btnInit);
	    add(btnStart);
	    add(infoMsg);
	      
	    /* 
	     * Set the position and size of each component
	     */
	    btnInit.setBounds(32, 10, 130, 30);
	    btnStart.setBounds(163, 10, 130, 30);
	    infoMsg.setBounds(32, 50, 500, 30);

	    btnInit.setEnabled(true);
	    btnStart.setEnabled(false);
	}
	
	/**
	  * Init or Start
	  */
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
		if (src == btnInit)
			init();
		else if (src == btnStart)
			start();
	}
 
    /**
     * Initialize: generate random population and display init numbers
     */
    void init() {
       infoMsg.setText("Initial state");
       btnInit.setEnabled(true);
       btnStart.setEnabled(true);
       repaint();
    }	
    
     /**
      * Start: validate inputs and run genetic algorithm
      */
     void start() {
        infoMsg.setText("Final state");
        btnInit.setEnabled(false);
        btnStart.setEnabled(false);
        repaint();

        //TODO replace args with input fields and its validation
		if (args.length > 1) {
			log.info("************** BEST ANT FORAGER START **************");
			
			int populationSize;
			int generationsNumber = Integer.parseInt(args[1].replace("generations_number=", ""));
			
			/*
			 * initial population
			 */
			List<AntForager> initPopulation = null;
			//random population generation of a given size
			if (args[0].contains("init_population_size")) {
				try {
					populationSize = Integer.parseInt(args[0].replace("init_population_size=", ""));
					if (populationSize <= 0) {
						System.err.println("Invalid population size. Please enter number greater than 0");
						System.exit(0);
					}
					initPopulation = generateRandomPopulation(populationSize);
					
				} catch (NumberFormatException e) {
					System.err.println("Invalid population size. Please enter integer value greater than 0");
					System.exit(0);
				}
				
			} else {
				//reading the population and its size from a file
				String inFilePath = args[0].replace("init_population_file=", "");
				File inFile = new File(inFilePath);
				
				if (!inFile.exists()) {
					System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
					System.exit(0);
				} else {
					PopulationPair popl;
					try {
						popl = readPopulationFromFile(inFile);
						populationSize = popl.getSize();
						initPopulation = popl.getPopul();
					} catch (IOException e) {
						System.err.println("File " + inFilePath + " does not exist. Please enther the correct full path to a file containing initial population data");
						return;
					}
				}
			}
				
			if (generationsNumber <= 0) {
				System.err.println("Invalid number of generations. Please use number greater than 0");
				System.exit(0);
			}
			
			/*
			 * initial livable environment
			 */
			Environment initEnvironment = new Environment();
			
			/*
			 * call genetic algorithm
			 */
			List<AntForager> winners = geneticAlgorithm(initPopulation, initEnvironment, generationsNumber);
			
			if (winners.isEmpty()) {
				System.out.println("\n*** NO WINNER ***");
				log.info("*** NO WINNER ***");
			} else {
				System.out.println("\n*********** WINNER" + (winners.size() == 1 ? "" : "S") + " ***********");	
				log.info("*********** WINNER" + (winners.size() == 1 ? "" : "S") + " ***********");
				int i = 1;
				for (AntForager winner : winners) {
					System.out.println("\n" + (winners.size() == 1 ? "" : i+". ") + "AntForager:");
					System.out.println(winner.toString());
					log.info((winners.size() == 1 ? "" : i+". ") + "AntForager: " + winner.toString());
					i++;
				}
			}
			
			log.info("************** BEST ANT FORAGER END **************");
			
		} else {
			System.err.println("Invalid number of input arguments. Please use 2 argumens in one of two options:");
			System.err.println("1. For random initial population, use arguments \ninit_population_size=<population size>\ngenerations_number=<number of generations>");
			System.err.println("2. For reading initial population from a file, use arguments \ninit_population_file=<population file>\ngenerations_number=<number of generations>");
		}
     }

	
	/**
	 * Main logic - genetic algorithm
	 * @param initPopulation
	 * @param initEnvironment
	 * @param generationsNumber number of generations
	 * @return winners
	 */
	public static List<AntForager> geneticAlgorithm(List<AntForager> initPopulation, Environment initEnvironment, int generationsNumber) {
		List<AntForager> population = initPopulation;		//current whole population
		List<AntForager> parentsPool = null;				//potential parents - population (with fitness levels) after discarding individuals with low fitness
		List<AntForager> parents = null;					//actual parents list
		List<AntForager> newPopulation = null;				//offspring
		AntForager mom, dad, child;
		Environment environment = initEnvironment;
		EnviGen envGenerator = new EnviGen();
		Reproduce reproduce = new Reproduce();
		Mutation mutation = new Mutation();
		Selection selection = new Selection();
		List<AntForager> winners;
		double tmp;	//to calculate log10 for generation number output
		
		//output both to terminal and log file
		System.out.println("\nInitial population size = " + population.size());
		System.out.println("Number of generations to create = " + generationsNumber);
		log.info("Initial population size = " + population.size());
		log.info("Number of generations to create = " + generationsNumber);
		
		System.out.println("\nInitial environment:");
		System.out.println(environment.toString());
		log.info("Initial environment: " + environment.toString());
		System.out.println();
		
		//output most fit individuals from initial population
		winners = getWinners(population, environment);
		if (!winners.isEmpty()) {
			System.out.println("\n*** Fittest individual" + (winners.size() == 1 ? "" : "s") + " from initial population ***");	
			log.info("*** Fittest individual" + (winners.size() == 1 ? "" : "s") + " from initial population ***");
			int i = 1;
			for (AntForager winner : winners) {
				System.out.println("\n" + (winners.size() == 1 ? "" : i+". ") + "AntForager:");
				System.out.println(winner.toString());
				log.info((winners.size() == 1 ? "" : i+". ") + "AntForager: " + winner.toString());
				i++;
			}
		}
		System.out.println("*****\n");
		log.info("*****");
		
		/*
		 * outer loop: one iteration = one generation
		 * stopping rule: required number of generations passed
		 */
		for (int generationCount = 1; generationCount <= generationsNumber; generationCount++) {
			/*
			 * output generation number - log10 scale
			 */
			tmp = Math.log10(generationCount);
			if (tmp == (int)tmp || generationCount == generationsNumber) {
				System.out.println("Generation " + generationCount);
				log.info("Generation " + generationCount);
			}
			newPopulation = new ArrayList<AntForager>();
			
			/*
			 * potential parents pre-selection based on the fitness level
			 */
			parentsPool = selection.cullPopulation(population, environment);
			
			/*
			 * inner loop: generating offspring as new population
			 */
			for (int i = 0; i < population.size(); i++) {
				//if less than two potential parents, then offspring cannot be generated
				if (parentsPool.isEmpty() || parentsPool.size() < 2) {
					System.out.println("WARNING: Not enough individuals in the parents pool: actual number is " + parentsPool.size() + ", min allowed number is 2");
					System.out.println("Final population size = " + population.size());
					System.out.println("\nFinal environment:");
					System.out.println(environment.toString());
					
					log.warn("Not enough individuals in the parents pool: actual number is " + parentsPool.size() + ", min allowed number is 2");
					log.info("Final population size = " + population.size());
					log.info("Final environment:");
					log.info(environment.toString());
					
					//output the last generation number
					if (tmp != (int)tmp && generationCount != generationsNumber) {
						System.out.println("Generation " + generationCount);
						log.info("Generation " + generationCount);
					}
					
					if (parentsPool.size() < 2) {
						System.out.println("The last survivor is the winner");
						log.info("The last survivor is the winner");
					}
					
					return parentsPool;
				}
				
				/*
				 * parents selection
				 */
				parents = selection.getParents(parentsPool, environment);
				mom = parents.get(0);
				dad = parents.get(1);
				
				/*
				 * reproduce
				 */
				child = reproduce.produceChild(mom, dad);
				mom.addOffspring(1);
				dad.addOffspring(1);
				
				//remove individuals with max possible number of offspring from parents pool
				parentsPool.removeIf(p -> (p.getOffspringCount() == AntForager.MAX_OFFSPRING_COUNT));
				
				/*
				 * mutate
				 */
				if (mutation.mutationProbability() >= 0.99)
					child = Mutation.mutate(child);
				
				//offspring added
				newPopulation.add(child);
			}
			
			//offspring can be parents in the next iteration
			population.clear();
			population.addAll(newPopulation);
			
			/*
			 * environment change with every new generation
			 */
			environment = envGenerator.EG(environment, generationCount);
		}
		
		System.out.println("Final population size = " + population.size());
		System.out.println("\nFinal environment:");
		System.out.println(environment.toString());
		
		log.info("Final population size = " + population.size());
		log.info("Final environment: " + environment.toString());
		
		//select individual with max fitness level
		winners = getWinners(population, environment);
		
		return winners;
	}
	
	
	/**
	 * Generates random population of the given size
	 * and writes this population to the CSV file
	 * @param size number of individuals in population
	 * @return population consisting of randomly generated AntForager	 */
	private static List<AntForager> generateRandomPopulation(int size) {
		//generating random population
		List<AntForager> randomPopulation = new ArrayList<AntForager>();
		for (int i = 0; i < size; i++) {
			randomPopulation.add(AntForager.generateRandomAnt());
		}
		
		//write generated population to a CSV file
		try {
			String outFileName = OUT_FILENAME_BASE + "1.csv";
			File outFile = new File(outFileName);
			//not overwriting existing file but creating a new one
			int j = 2;
			while (outFile.exists()) {
				outFileName = OUT_FILENAME_BASE + j + ".csv";
				outFile = new File(outFileName);
				j++;
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileName), "UTF-8"));
			StringBuffer oneLine = new StringBuffer();
			//header
			oneLine = new StringBuffer();
            oneLine.append("Size (mm)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Weight (mg)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Jaw size (mm)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Feromone intensity (%)");
            oneLine.append(CSV_SEPARATOR);
            oneLine.append("Antennae censory cells");
            oneLine.append("Offspring Count");
            
            bw.write(oneLine.toString());
            bw.newLine();
            
			//data
	        for (AntForager ant : randomPopulation) {
	           	oneLine = new StringBuffer();
	            oneLine.append(ant.getSize());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getWeight());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getJawSize());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getFeromoneIntensity());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getAntennaeSensors());
	            oneLine.append(CSV_SEPARATOR);
	            oneLine.append(ant.getOffspringCount());
	            
	            bw.write(oneLine.toString());
	            bw.newLine();
	        }
	        bw.flush();
	        bw.close();
	        System.out.println("Initial population saved in file " + outFileName);
	        log.info("Initial population saved in file " + outFileName);
	        
	    //if failed to write to a file - proceed anyway
		} catch (IOException e) {
			System.out.println("WARNING: unable to write generated population to CSV file. Writing to the file skipped.\n");
			log.warn("Unable to write generated population to CSV file. Writing to the file skipped.");
		}
		
		return randomPopulation;
	}


	/**
	 * Reads population from the CSV file
	 * @param inFile input file containing the list of ants
	 * @return instance containing population and its size
	 * @throws IOException 
	 */
	private static PopulationPair readPopulationFromFile(File inFile) throws IOException {
		List<AntForager> inPopulation = new ArrayList<AntForager>();
		AntForager ant;
		int inPopulationSize = -1;	//to skip the header line
		
		BufferedReader br = null;
		String line = "";
		String[] AntForager;	//to store one line as array
		br = new BufferedReader(new FileReader(inFile));
		
		//reading file line by line
		while ((line = br.readLine()) != null) {
			//skip the header line
            if (inPopulationSize >= 0) {
				AntForager = line.split(CSV_SEPARATOR);
				/*for (String s : AntForager) {
					System.out.print(s+"|");
				}*/
				ant = new AntForager();
				ant.setSize(Double.parseDouble(AntForager[0]));
				ant.setWeight(Double.parseDouble(AntForager[1]));
				ant.setJawSize(Double.parseDouble(AntForager[2]));
				ant.setFeromoneIntensity(Double.parseDouble(AntForager[3]));
				ant.setAntennaeSensors(Integer.parseInt(AntForager[4]));
				ant.setOffspringCount(Integer.parseInt(AntForager[5]));
				inPopulation.add(ant);
            }
			inPopulationSize ++;
		}
		if (br != null) 
			br.close();

		PopulationPair popl = new PopulationPair(inPopulationSize, inPopulation);
		return popl;
	}
	
	
	/**
	 * Determines the winner individuals - the ants with the highest fitness level
	 * @param population
	 * @param envmt environment
	 * @return list of individuals with the highest fitness level
	 */
	@SuppressWarnings("unchecked")
	private static List<AntForager> getWinners(List<AntForager> population, Environment envmt) {
		if (population.isEmpty() || population == null)
			return population;
		
		//calculate fitness levels for population
		Fitness fit = new Fitness();
		population = fit.calculatePopulationFitness(population, envmt);
		List<AntForager> winners = new ArrayList<AntForager>();
		
		//sort population by fitness level in asc order
		Collections.sort(population);
		
		//obtaining the top fitness level value - fitness level of the last individual in the sorted list
		double topFitness = population.get(population.size()-1).getFoodSurplus();
		
		//TODO check if need to round the double values
		for (AntForager ant : population) {
			if (ant.getFoodSurplus() == topFitness)
				winners.add(ant);
		}
		
		//get rid of duplicate AntForagers from the winners list so we have distinct AntForagers in output
		winners = new ArrayList<AntForager> (new HashSet<AntForager>(winners));
		return winners;
	}


}
