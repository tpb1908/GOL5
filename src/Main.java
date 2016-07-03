import javax.swing.*;

/**
 * Created by pearson-brayt15 on 16/03/2016.
 */
public class Main {

    public static void main(final String[] arg) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Sets up the frame
                JFrame frame = new WindowManager();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                frame.setLocationRelativeTo(null);
            }
        });
    }

}
