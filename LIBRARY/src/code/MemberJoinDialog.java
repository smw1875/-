package code;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MemberJoinDialog extends JDialog implements ActionListener {
    //@Serial
    private static final long serialVersionUID = 1L;
    private JTextField nameTextField = null;
    private JPasswordField pwdTextField = null;
    public Connection conn;
    public ResultSet rs;
    public PreparedStatement pstmt;

    public MemberJoinDialog() {
        setTitle("Join Member");
        setModal(true);
        setLocationRelativeTo(null);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        add(panel1);

        JPanel panelCenter = new JPanel();
        panel1.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new FlowLayout());
        panelCenter.add(new JLabel("name       "));
        nameTextField = new JTextField(20);
        panelCenter.add(nameTextField);
        panelCenter.add(new JLabel("password"));
        pwdTextField = new JPasswordField(20);
        panelCenter.add(pwdTextField);

        JPanel panel2 = new JPanel();
        panel1.add(panel2, BorderLayout.SOUTH);
        JButton okBtn = new JButton(Const.OK);
        okBtn.addActionListener(this);
        panel2.add(okBtn);
        JButton cancelBtn = new JButton(Const.CANCEL);
        cancelBtn.addActionListener(this);
        panel2.add(cancelBtn);
        setSize(350, 200);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
        	Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException o) {
            System.err.println(" 오류발생 : " + o.getMessage());
            o.printStackTrace();
        }
        try {
        	conn = DriverManager.getConnection("jdbc:sqlite:book.db");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals(Const.OK)) {
            if (nameTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "The name is empty");
                nameTextField.requestFocus();
            } else if (pwdTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "The password is empty");
                pwdTextField.requestFocus();
            }
            String sql = "SELECT * FROM member WHERE name='" + nameTextField.getText() + "'";
            try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "The name is already");
                } else {
                    try {
                        Database.getInstance().insertMemberData(
                                nameTextField.getText(),
                                pwdTextField.getText());
                    } catch (SQLException | IOException b) {
                        b.printStackTrace();
                    }
                    dispose();
                }
            } catch (SQLException ignored) {

            }

        } else if (e.getActionCommand().equals(Const.CANCEL)) {
            dispose();
        }
    }

}

