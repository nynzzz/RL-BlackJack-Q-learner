import java.util.HashMap;
import java.util.Map;

public class QTable {
    private Map<State, double[]> table;

    public QTable() {
        this.table = new HashMap<>();
    }

    public double get(State state, int action) {
        return table.computeIfAbsent(state, k -> new double[2])[action];
    }

    public void put(State state, int action, double value) {
        double[] qValues = table.computeIfAbsent(state, k -> new double[2]);
        qValues[action] = value;
    }

    public int bestAction(State state) {
        double[] actions = table.computeIfAbsent(state, k -> new double[2]);
        return actions[0] > actions[1] ? 0 : 1; // 0 for HIT, 1 for STAND
    }
}
