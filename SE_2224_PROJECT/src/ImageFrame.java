import javax.swing.*;
import java.awt.*;

public class ImageFrame extends JFrame{
    private JPanel panel1;
    private JLabel label1;
    private ImageIcon imageIcon;

    public ImageFrame(int ID) {
        imageIcon = new ImageIcon("task_images\\task"+ID+".jpg");
        label1.setIcon(imageIcon);

        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("ImageFrame");
        setBounds(600,400,150,175);
        setResizable(false);
        setVisible(true);
    }
}
