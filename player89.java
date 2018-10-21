import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.*;

public class player89 implements ContestSubmission
{
    static Random rnd_;
	static ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private boolean isMultimodal;
    private boolean hasStructure;
	
	public player89()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

//		// Do sth with property values, e.g. specify relevant settings of your algorithm
//        if(isMultimodal){
//            // Do sth
//        }else{
//            // Do sth else
//        }
    }

    private void exchange(Population[] islands, int exchange_size, double s) {
        for (Population island : islands) {
            island.inverse_linear_rank(island, s);
        }

        int[] first_island = new int[exchange_size];
        Individual[] tmp = new Individual[exchange_size];
        for (int i = 0; i < exchange_size; i++) {
            int index = islands[0].select_exchange(islands[0]);
            first_island[i] = index;
            tmp[i] = islands[0].getIndividual(index);
        }


        for (int i = 1; i < islands.length; i++) {
            for (int j = 0; j < exchange_size; j++) {
                int index = islands[i].select_exchange(islands[i]);
                tmp[j] = islands[i].switchIndividual(index, tmp[j]);
            }
        }

        for (int i = 0; i < exchange_size; i++) {
            islands[0].switchIndividual(first_island[i], tmp[i]);
        }
    }

	public void run()
	{
		// Run your algorithm here

        int nr_of_islands = isMultimodal ? 11 : 1;
        int population_size = 60;
        int nr_of_survivors = 20;
        double mutation_step_size = !hasStructure && isMultimodal ? 0.6 : 0.05;
        double s = 2.0;
        int epoch = 140;
        int exchange_size = 9;

        double treshold_factor = 0.002;
        double mutation_step_size_after = 0.0005;

        System.out.println("Number of islands: " + nr_of_islands + "\n" +
                "Population size: " + population_size + "\n" +
                "Number of survivors: " + nr_of_survivors + "\n" +
                "Mutation step size: " + mutation_step_size + "\n" +
                "s: " + s + "\n" +
                "epoch: " + epoch + "\n" +
                "Exchange size: " + exchange_size + "\n" +
                "Treshold factor: " + treshold_factor + "\n" +
                "Matuation step size after: " + mutation_step_size_after + "\n");

        // Initialize population
        Population[] islands = new Population[nr_of_islands];
        for (int i = 0; i < nr_of_islands; i++) {
            islands[i] = new Population(population_size);
        }

        int evals = 0;
        while(evals < evaluations_limit_){
            if (!hasStructure && isMultimodal && evals > treshold_factor * evaluations_limit_) {
                mutation_step_size = mutation_step_size_after;
            }

            for (int i = 0; i < nr_of_islands; i++) {
                if (isMultimodal && evals % epoch == 0) {
                    exchange(islands, exchange_size, s);
                }

                // Apply recombination / mutation
                Population offspring = new Population(islands[i], population_size, mutation_step_size, 1.0, s);
                evals++;

                // Select survivors
                islands[i] = new Population(offspring, s, nr_of_survivors);
            }
        }
	}
}
