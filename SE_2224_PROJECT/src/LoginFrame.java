import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LoginFrame extends JFrame{
    private JPanel panel1;
    private JTextField textField1;
    private JButton loginButton;
    private JPasswordField passwordField1;
    private JButton createAccountButton;
    public SqlConnection sqlConnection = new SqlConnection();
    private Connection connection = sqlConnection.SqlConnect();
    private ArrayList<User> users = new ArrayList<>();
    private int userid;
    private final LoginFrame frame = this;

    public LoginFrame(){
        panel1.setBackground(new Color(90,90,90));
        add(panel1);
        setTitle("LoginFrame");
        setBounds(565,200,400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean userInfo = false;

                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from users");
                    while(resultSet.next()){
                        users.add(new User(Integer.parseInt(resultSet.getObject(1).toString()), resultSet.getObject(2).toString(),resultSet.getObject(3).toString()));
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }

                for(User u : users){
                    if(u.getUsername().equals(textField1.getText()) && u.getPassword().equals(new String(passwordField1.getPassword()))) {userInfo = true; userid = u.getUserid();}
                }

                if (userInfo)
                {
                    setVisible(false);
                    new MainFrame(connection, userid);
                } else if (textField1.getText().equals("") || new String(passwordField1.getPassword()).equals("")) {
                    JOptionPane.showMessageDialog(null, "Please do not leave blank.");
                } else
                {
                    JOptionPane.showMessageDialog(null, "Your username or password is incorrect.");
                    textField1.setText("");
                    passwordField1.setText("");
                }
            }
        });
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new CreateAccountFrame(connection, frame);
            }
        });
    }
}

