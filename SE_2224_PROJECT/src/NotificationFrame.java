import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NotificationFrame extends JFrame{
    private JPanel panel1;
    private JList list1;

    public NotificationFrame(Connection connection, int userid) {
        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("NotificationFrame");
        setBounds(640,340,250,250);
        setResizable(false);

        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select taskname from TaskPlanner where deadline - CURDATE() <= 1 and deadline - CURDATE() >= 0 and userid="+userid);

            if (!resultSet.next()){
                return;
            }else {
                DefaultListModel listModel = new DefaultListModel();
                list1.setModel(listModel);
                list1.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
                listModel.addElement(resultSet.getObject(1));
                while (resultSet.next()) {
                    listModel.addElement(resultSet.getObject(1));
                }

                setVisible(true);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
