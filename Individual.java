import java.util.Arrays;
import java.util.Comparator;

public class Individual {
    private static final int dimensions = 10;
    private static final double range = 5.0;

    private double[] params;
    private double fitness;
    private double selectionProbability;
    private double inverseSelectionProbability;

    Individual() {
        this.params = genParams();
        this.fitness = (double) player89.evaluation_.evaluate(this.params);
        this.selectionProbability = 0.0;
    }

    Individual(double mutation_step_size, double time_factor, Individual parent1, Individual parent2) {
        this.params = mutate(
                recombine(parent1.getParams(), parent2.getParams()),
                mutation_step_size,
                time_factor
        );

        try {
            this.fitness = (double) player89.evaluation_.evaluate(this.params);
        } catch (NullPointerException ignored) {}
    }

    private double[] recombine(double[] parent1, double[] parent2) {
        double[] params = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            if (player89.rnd_.nextBoolean()) {
                params[i] = parent1[i];
            } else {
                params[i] = parent2[i];
            }
        }

        return params;
    }

    private double[] mutate(double[] params, double mutation_step_size, double time_factor) {
        for (int i = 0; i < params.length; i++) {
            double noise = player89.rnd_.nextGaussian() * mutation_step_size * time_factor;


            // If boolean is true: value becomes negative (to create a range between -mutation_step_size and mutation_step_size)
            if (player89.rnd_.nextBoolean()) {
                noise *= -1;
            }

            params[i] += noise;

            // Make sure parameters stay within range
            if (params[i] > range) {
                params[i] = range;
            } else if (params[i] < -range) {
                params[i] = -range;
            }
        }

        return params;
    }

    private double[] genParams() {
        // Create an individual with values for 'dimensions' paramaters
        double[] params = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            params[i] = randomInRange();
        }

        return params;
    }

    private double randomInRange() {
        // Generate double between 0 and range
        double value = player89.rnd_.nextDouble() * range;

        // If boolean is true: value becomes negative (to create a range between -range and range)
        if (player89.rnd_.nextBoolean()) {
            value *= -1;
        }

        return value;
    }

    private double[] getParams() {
        return this.params;
    }

    private double getFitness() {
        return this.fitness;
    }

    void setSelectionProbability(double i, double mu, double s) {
        this.selectionProbability = ((2.0 - s) / mu) + ((2.0 * i * (s - 1.0)) / (mu * (mu - 1.0)));

    }

    void setInverseSelectionProbability(double i, double mu, double s) {
        this.inverseSelectionProbability = 1.0 - (((2.0 - s) / mu) + ((2.0 * i * (s - 1.0)) / (mu * (mu - 1.0))));
    }

    double getSelectionProbability() {
        return this.selectionProbability;
    }

    double getInverseSelectionProbability() {
        return this.inverseSelectionProbability;
    }

    @Override
    public String toString() {
        return "{fitness: " + this.fitness + ", selection probability: " + this.selectionProbability + ", params: " + Arrays.toString(this.params) + "}";
    }

    static Comparator<Individual> getFitnessComparator() {
        return Comparator.comparingDouble(Individual::getFitness);
    }

    static Comparator<Individual> getSelectionProbabiltyComparator() {
        return Comparator.comparingDouble(Individual::getSelectionProbability);
    }

    static Comparator<Individual> getInverseSelectionProbabilityComparator() {
        return Comparator.comparingDouble(Individual::getInverseSelectionProbability);
    }
}
