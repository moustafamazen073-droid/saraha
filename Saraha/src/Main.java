import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Saraha");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("Welcome to Saraha!", SwingConstants.CENTER);
        frame.add(label);

        frame.setVisible(true);
    }
}