import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SortByPriorityFrame extends JFrame {
    private JPanel panel1;
    private JButton sortButton;
    private JTextField textField1;

    public SortByPriorityFrame(Connection connection, JTable table, int userid) {
        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("SortByPriorityFrame");
        setBounds(665,300,250,150);
        setResizable(false);
        setVisible(true);
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Statement statement;
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from TaskPlanner where deadline="+textField1.getText().replaceAll("-","")+" and userid="+userid+" ORDER BY priority DESC");

                    if (!resultSet.next()){
                        JOptionPane.showMessageDialog(null, "No such date found.");
                    }else {
                        String[] tableColumnsName = {"ID", "Task Name", "Short Description", "Deadline", "Priority", "Reminder Image", "Entry Date"};
                        DefaultTableModel defaultTableModel = (DefaultTableModel) table.getModel();
                        defaultTableModel.setRowCount(0);
                        defaultTableModel.setColumnIdentifiers(tableColumnsName);

                        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                        int colNo = resultSetMetaData.getColumnCount();

                        Object[] objects1 = new Object[colNo];
                        for (int i = 0; i < colNo; i++) {
                            objects1[i] = resultSet.getObject(i + 1);
                        }
                        defaultTableModel.addRow(objects1);

                        while (resultSet.next()) {
                            Object[] objects = new Object[colNo];
                            for (int i = 0; i < colNo; i++) {
                                objects[i] = resultSet.getObject(i + 1);
                            }
                            defaultTableModel.addRow(objects);
                        }
                        table.setModel(defaultTableModel);
                        setVisible(false);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
