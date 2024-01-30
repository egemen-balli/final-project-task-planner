import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MainFrame extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton insertTaskButton;
    private JButton showAllTasksButton;
    private JTable table1;
    private JButton deleteTaskButton;
    private JButton updateTaskButton;
    private JButton sortByPriorityButton;
    private JButton filterTasksButton;
    private JButton viewATaskSButton;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private int userid;

    void updateTable() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from TaskPlanner where userid="+userid);

        String[] tableColumnsName = {"ID", "Task Name", "Short Description", "Deadline", "Priority", "Reminder Image", "Entry Date"};
        DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
        defaultTableModel.setRowCount(0);
        defaultTableModel.setColumnIdentifiers(tableColumnsName);

        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colNo = resultSetMetaData.getColumnCount();
        while (resultSet.next()) {
            Object[] objects = new Object[colNo];
            for (int i = 0; i < colNo-1; i++) {
                objects[i] = resultSet.getObject(i + 1);
            }
            defaultTableModel.addRow(objects);
        }
        table1.setModel(defaultTableModel);
    }

    MainFrame(Connection connection, int userid){
        this.userid = userid;
        this.connection = connection;
        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("MainFrame");
        setBounds(200,100,1200,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        insertTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    preparedStatement = connection.prepareStatement("insert into taskplanner (taskname, shortdescription, deadline, priority, reminderimage, entrydate, userid) values ('"+textField1.getText()+"', '"+textField2.getText()+"', "+textField3.getText().replaceAll("-","")+", ?, ?, CURDATE(), "+userid+")");
                    if(textField4.getText().equals("")){
                        preparedStatement.setNull(1, Types.NULL);
                    }else{
                        preparedStatement.setString(1, textField4.getText());
                    }
                    preparedStatement.setBoolean(2, Boolean.parseBoolean(textField5.getText()));
                    preparedStatement.executeUpdate();

                    updateTable();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        showAllTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateTable();
                } catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tblModel = (DefaultTableModel) table1.getModel();

                if (table1.getSelectedRowCount() == 1){
                    try {
                        preparedStatement = connection.prepareStatement("delete from taskplanner where id = "+table1.getModel().getValueAt(table1.getSelectedRow(), 0).toString()+" and userid="+userid);
                        preparedStatement.executeUpdate();

                        tblModel.removeRow(table1.getSelectedRow());
                        textField1.setText("");
                        textField2.setText("");
                        textField3.setText("");
                        textField4.setText("");
                        textField5.setText("");
                        updateTable();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    if (table1.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Table is empty.");
                    }else {
                        JOptionPane.showMessageDialog(null, "Please select Single Row For Delete.");
                    }
                }
            }
        });
        updateTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table1.getSelectedRowCount() == 1){
                    try {
                        preparedStatement = connection.prepareStatement("update taskplanner set taskname='"+textField1.getText()+"', shortdescription='"+textField2.getText()+"', deadline="+textField3.getText().replaceAll("-","")+", priority=?, reminderimage=? where id = "+table1.getModel().getValueAt(table1.getSelectedRow(), 0).toString()+" and userid="+userid);
                        if(textField4.getText().equals("")){
                            preparedStatement.setNull(1, Types.NULL);
                        }else{
                            preparedStatement.setString(1, textField4.getText());
                        }
                        preparedStatement.setBoolean(2, Boolean.parseBoolean(textField5.getText()));
                        preparedStatement.executeUpdate();

                        updateTable();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }else {
                    if (table1.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Table is empty.");
                    }else {
                        JOptionPane.showMessageDialog(null, "Please select Single Row For Update.");
                    }
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                DefaultTableModel tblModel = (DefaultTableModel) table1.getModel();
                int row = table1.getSelectedRow();
                textField1.setText(tblModel.getValueAt(row, 1).toString());
                textField2.setText(tblModel.getValueAt(row, 2).toString());
                textField3.setText(tblModel.getValueAt(row, 3).toString());
                if(tblModel.getValueAt(row, 4) == null){
                    textField4.setText("");
                }else{
                    textField4.setText(tblModel.getValueAt(row, 4).toString());
                }
                textField5.setText(tblModel.getValueAt(row, 5).toString());
            }
        });
        viewATaskSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel tblModel = (DefaultTableModel) table1.getModel();
                int row = table1.getSelectedRow();
                if(tblModel.getValueAt(row, 5).toString().equals("true")){
                    new ImageFrame((Integer) tblModel.getValueAt(row, 0));
                }else {
                    JOptionPane.showMessageDialog(null, "The task does not include an image.");
                }
            }
        });
        sortByPriorityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SortByPriorityFrame(connection, table1, userid);
            }
        });
        filterTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FilterTasksFrame(connection, table1, userid);
            }
        });
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                new NotificationFrame(connection, userid);
            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
}
