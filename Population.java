import java.util.Arrays;


public class Population {
    private Individual[] individuals;

    // Initial Population
    Population(int populationSize) {
        this.individuals = new Individual[populationSize];

        for (int i = 0; i < populationSize; i++) {
            this.individuals[i] = new Individual();
        }
    }

    // Offspring
    Population(Population population, int populationSize, double mutation_step_size, double time_factor, double s) {
        this.individuals = new Individual[populationSize];

        linear_rank(population, s);
        for (int i = 0; i < populationSize; i++) {
            this.individuals[i] = new Individual(mutation_step_size, time_factor, population.select_survivor(population), population.select_survivor(population));
        }
    }

    // Survivors
    Population(Population offspring, double s, int nr_of_survivors) {
        this.individuals = new Individual[nr_of_survivors];

        linear_rank(offspring, s);
        for (int i = 0; i < nr_of_survivors; i++) {
            this.individuals[i] = select_survivor(offspring);
        }
    }

    private void linear_rank(Population offspring, double s) {
        Arrays.sort(offspring.getIndividuals(), Individual.getFitnessComparator());
        for (int i = 0; i < offspring.getPopulationSize(); i++) {
            offspring.getIndividual(i).setSelectionProbability(i, offspring.getPopulationSize(), s);
        }
        Arrays.sort(offspring.getIndividuals(), Individual.getSelectionProbabiltyComparator());
    }

    public void inverse_linear_rank(Population population, double s) {
        Arrays.sort(population.getIndividuals(), Individual.getFitnessComparator());
        for (int i = 0; i < population.getPopulationSize(); i++) {
            population.getIndividual(i).setInverseSelectionProbability(population.getPopulationSize() - 1 - i, population.getPopulationSize(), s);
        }
        Arrays.sort(population.getIndividuals(), Individual.getInverseSelectionProbabilityComparator());
    }

    // Based on https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
    private Individual select_survivor(Population offspring) {
        double index = player89.rnd_.nextDouble();
        double sum = 0.0;
        int i = 0;

        while (sum < index) {
            sum += offspring.getIndividual(i).getSelectionProbability();
            i++;
        }

        return offspring.getIndividual(Math.max(0, i - 1));
    }

    public int select_exchange(Population population) {
        double index = player89.rnd_.nextDouble();
        double sum = 0.0;
        int i = 0;

        while (sum < index) {
            sum += population.getIndividual(population.getPopulationSize() - 1 - i).getInverseSelectionProbability();
            i++;
        }

        return Math.max(0, i - 1);
    }

    Individual switchIndividual(int i, Individual new_individual) {
        Individual switchedIndividual = individuals[i];
        individuals[i] = new_individual;

        return switchedIndividual;
    }

    Individual getIndividual(int i) {
        return individuals[i];
    }

    void setIndividual(int i, Individual individual) {
        individuals[i] = individual;
    }

    private Individual[] getIndividuals() {
        return individuals;
    }

    int getPopulationSize() {
        return individuals.length;
    }
}
