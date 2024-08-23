import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Experiments {

    public static void main(String[] args) {
        double[] alphas = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        double[] gammas = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        double[] epsilons = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        String fileName = "experiment_results.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, true))) {
            // Write header if file is newly created
            pw.println("Alpha,Gamma,Epsilon,Average Reward");

            for (double alpha : alphas) {
                for (double gamma : gammas) {
                    for (double epsilon : epsilons) {
                        System.out.println("Alpha: " + alpha + " Gamma: " + gamma + " Epsilon: " + epsilon);

                        // Assuming QLearner.runTest returns the average reward for NUM_GAMES
                        double avgReward = QLearner.runTest(10000, alpha, gamma, epsilon);

                        // Write the parameters and the result to the file
                        pw.println(alpha + "," + gamma + "," + epsilon + "," + avgReward);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
