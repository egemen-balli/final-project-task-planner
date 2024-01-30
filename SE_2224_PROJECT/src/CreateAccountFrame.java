import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CreateAccountFrame extends JFrame {

    private JTextField textField1;
    private JPanel panel1;
    private JTextField textField2;
    private JButton backButton;
    private JButton signUpButton;
    private PreparedStatement preparedStatement;

    public CreateAccountFrame(Connection connection, LoginFrame frame) {
        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("CreateAccountFrame");
        setBounds(565,200,400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                frame.setVisible(true);
            }
        });
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean userInfo = false;

                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select username from users");
                    while(resultSet.next()){
                        if(resultSet.getObject(1).equals(textField1.getText())){
                            userInfo = true;
                        }
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }

                if (userInfo)
                {
                    JOptionPane.showMessageDialog(null, "This username is already in use.");
                    userInfo = false;
                } else if (textField1.getText().equals("") || textField2.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please do not leave blank.");
                } else
                {
                    JOptionPane.showMessageDialog(null, "Account created successfully.");

                    try{
                        preparedStatement = connection.prepareStatement("insert into users (username, password) values ('"+textField1.getText()+"', '"+textField2.getText()+"')");
                        preparedStatement.executeUpdate();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }

                    textField1.setText("");
                    textField2.setText("");
                }
            }
        });
    }
}
