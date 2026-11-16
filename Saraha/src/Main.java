import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SarahaGUI app = new SarahaGUI();
            app.setVisible(true);
        });
    }
}
