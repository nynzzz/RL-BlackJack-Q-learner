import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Plotter extends JPanel {

    private ArrayList<double[]> trainingHistory;

    public Plotter(ArrayList<double[]> trainingHistory) {
        this.trainingHistory = trainingHistory;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (trainingHistory == null || trainingHistory.size() == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(1));

        // Determine min and max values for scaling
        double maxReward = Double.MIN_VALUE;
        double minReward = Double.MAX_VALUE;
        for (double[] point : trainingHistory) {
            if (point[1] > maxReward) maxReward = point[1];
            if (point[1] < minReward) minReward = point[1];
        }

        // Adjust starting x-coordinate for axes and labels to move plot to the right
        int startX = 100; // Increased from 50 to 100

        // Draw axes
        g2.drawLine(startX, this.getHeight() - 50, startX, 50);
        g2.drawLine(startX, this.getHeight() - 50, this.getWidth() - 50, this.getHeight() - 50);

        // Axis labels
        g2.drawString("Games", (this.getWidth() / 2) , this.getHeight() - 10); // Adjusted for new starting X
        // Draw rotated string at y axis
        g2.rotate(-Math.PI / 2);
        g2.drawString("Average Reward", -(this.getHeight() / 2), startX - 60); // Adjusted for new starting X
        
        g2.rotate(Math.PI / 2);

        // Ticks and labels for X axis
        int numberOfXTicks = 10;
        for (int i = 0; i <= numberOfXTicks; i++) {
            int x = startX + i * (this.getWidth() - startX - 50) / numberOfXTicks; // Adjusted for new starting X
            g2.drawLine(x, this.getHeight() - 50, x, this.getHeight() - 45);
            g2.drawString(String.valueOf(i * (trainingHistory.size() / numberOfXTicks)), x - 10, this.getHeight() - 30);
        }

        // Ticks and labels for Y axis
        int numberOfYTicks = 10;
        for (int i = 0; i <= numberOfYTicks; i++) {
            int y = 50 + i * (this.getHeight() - 100) / numberOfYTicks;
            g2.drawLine(startX - 5, this.getHeight() - y, startX, this.getHeight() - y); // Adjusted for new starting X
            String label = String.format("%.2f", minReward + i * (maxReward - minReward) / numberOfYTicks);
            g2.drawString(label, startX - 45, this.getHeight() - y + 5); // Adjusted for new starting X
        }

        // Chnage color to red
        g2.setColor(Color.ORANGE);

        // Plot points
        int prevX = -1;
        int prevY = -1;
        for (int i = 0; i < trainingHistory.size(); i++) {
            double[] point = trainingHistory.get(i);
            int x = startX + (int) ((point[0] / trainingHistory.size()) * (this.getWidth() - startX - 50)); // Adjusted for new starting X
            int y = (int) (((point[1] - minReward) / (maxReward - minReward)) * (this.getHeight() - 100));
            y = this.getHeight() - 50 - y; // Invert y to match the Cartesian plane

            if (prevX != -1 && prevY != -1) {
                g2.drawLine(prevX, prevY, x, y);
            }

            prevX = x;
            prevY = y;
        }

        // Change color to black
        g2.setColor(Color.BLACK);
    }


    public static void plotConvergence(ArrayList<double[]> trainingHistory) {
        JFrame frame = new JFrame("Training Convergence Plot");
        Plotter plotter = new Plotter(trainingHistory);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(plotter);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Example usage
    public static void main(String[] args) {
        // Example data
        ArrayList<double[]> trainingHistory = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            trainingHistory.add(new double[]{i, Math.sin(i * 0.1) * 100 + 100}); // Example data
        }
        
        plotConvergence(trainingHistory);
    }
}
