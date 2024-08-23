import java.util.ArrayList;

import javax.swing.JFrame;

public class QLearner {
    private static QTable qTable = new QTable();

    private static double ALPHA = 0.4; // Learning rate
    private static double GAMMA = 0.3; // Discount factor
    private static double EPSILON = 0.3; // Exploration rate
    private static BlackJackEnv game = new BlackJackEnv(BlackJackEnv.NONE);

    public static void plotConvergence(ArrayList<double[]> trainingHistory) {
        JFrame frame = new JFrame("Training Convergence Plot");
        Plotter plotter = new Plotter(trainingHistory);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(plotter);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        String path = "results.txt";
        
        double totalReward = 0.0;
        int numberOfGames = 0;
        ArrayList<double[]> trainingHistory = new ArrayList<>();

        while (numberOfGames < 1000) {
            totalReward += playOneGame();
            numberOfGames++;
            double avgReward = totalReward / numberOfGames;
            trainingHistory.add(new double[]{numberOfGames, avgReward});

            if (numberOfGames % 10000 == 0) {
                System.out.println("Avg reward after " + numberOfGames + " games: " + (avgReward));
            }
        }
        plotConvergence(trainingHistory);
        // Save the results to a file
        // Save the results to a file
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(path);
            for (double[] point : trainingHistory) {
                writer.println(point[0] + " " + point[1]);
            }
            writer.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static double playOneGame() {
        ArrayList<String> gameState = game.reset();

//        System.out.println("Player: " + BlackJackEnv.totalValue(BlackJackEnv.getPlayerCards(gameState)) + " " + BlackJackEnv.holdActiveAce(BlackJackEnv.getPlayerCards(gameState)) + " " + BlackJackEnv.valueOf(BlackJackEnv.getDealerCards(gameState).get(0)));

        double totalReward = 0.0;

        while (!gameState.get(0).equals("true")) {
            State currentState = getState(gameState);

//            System.out.println("Player: " + currentState.getPlayerTotal() + " " + currentState.isActiveAce() + " " + currentState.getDealerFirstCard());

            int action = selectAction(currentState);
            gameState = game.step(action);

            double reward = Double.parseDouble(gameState.get(1));

//            System.out.println("Reward: " + reward);
            totalReward += reward;

            if (!gameState.get(0).equals("true")) {
                State nextState = getState(gameState);
                double nextMaxQ = Math.max(qTable.get(nextState, 0), qTable.get(nextState, 1));
                double qValue = qTable.get(currentState, action) + ALPHA * (reward + GAMMA * nextMaxQ - qTable.get(currentState, action));
                qTable.put(currentState, action, qValue);
            }
            updateEpsilon();
        }

        return totalReward;
    }

    private static State getState(ArrayList<String> gameState) {
        int playerTotal = BlackJackEnv.totalValue(BlackJackEnv.getPlayerCards(gameState));
        boolean activeAce = BlackJackEnv.holdActiveAce(BlackJackEnv.getPlayerCards(gameState));
        int dealerFirstCard = BlackJackEnv.valueOf(BlackJackEnv.getDealerCards(gameState).get(0));

        return new State(playerTotal, activeAce, dealerFirstCard);
    }

    private static int selectAction(State state) {
        return (Math.random() < EPSILON) ? (int) (Math.random() * 2) : qTable.bestAction(state);
    }

    private static void updateEpsilon() {
        EPSILON = Math.max(0.1, EPSILON * 0.995); // Epsilon decay
    }

    public static double runTest(int NUM_GAMES, double alpha, double gamma, double epsilon) {
        EPSILON = epsilon;
        ALPHA = alpha;
        GAMMA = gamma;

        double totalReward = 0.0;
        int numberOfGames = 0;

        while (numberOfGames < NUM_GAMES) {
            totalReward += playOneGame();
            numberOfGames++;
        }
        return totalReward / numberOfGames;
    }
}
