# CreatureEvolution
GMU CS580 Spring 2015 term project: genetic algorithm demo
******************************************************************************
PRE-BUILD
Copy contents of /lib to C:/dist

BUILD
Ant build with biuld.xml

RUN
Use one of two options:
1. For random initial population
  java -Dlog4j.debug -Dlog4j.configuration=file:C:/dist/log4j.xml -jar CreatureEvolution.jar init_population_size=population size generations_number=number of generations<br>
2. For reading initial population from a file
  java -Dlog4j.debug -Dlog4j.configuration=file:C:/dist/log4j.xml -jar CreatureEvolution.jarinit_population_file=population file generations_number=number of generations<br>

RESULTS
Output in terminal and C:/evolution/log/evolution.log file