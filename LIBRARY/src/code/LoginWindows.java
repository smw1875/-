package code;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginWindows extends JFrame {
	private static final long serialVersionUID = 1L;
	private LoginButtonListener buttonListener = null;
	public static JTextField nameTextField;
	public static JTextField pwdTextField;

	public LoginWindows() {
		buttonListener = new LoginButtonListener(this);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new FlowLayout());
		add(new JLabel("name        "));
		nameTextField = new JTextField(20);
		add(nameTextField);
		add(new JLabel("password"));
		pwdTextField = new JPasswordField(20);
		add(pwdTextField);
		
		JButton okButton = new JButton(Const.OK);
		okButton.addActionListener(buttonListener);
		add(okButton);
		JButton cancelButton = new JButton(Const.CANCEL);
		cancelButton.addActionListener(buttonListener);
		add(cancelButton);
		JButton joinButton = new JButton(Const.JOIN);
		joinButton.addActionListener(buttonListener);
		add(joinButton);
		
		setLocationRelativeTo(null);
		setSize(350, 150);
		setVisible(true);
	}
}
