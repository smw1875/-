package code;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BookAddDialog extends JDialog implements ActionListener {
    //@Serial
    private static final long serialVersionUID = 1L;
    public static JTextField isbnTextField;
    public static JTextField numberTextField;
    public static JTextField authorTextField;
    public static JTextField titleTextField;
    public static JTextField publisherTextField;
    public static JTextField bookdateTextField;
    public static JTextField statusTextField;
    public static JTextField registdateTextField;

    public Connection conn;
    public ResultSet rs;
    public PreparedStatement pstmt;

    public BookAddDialog() {
        setTitle("Book Data Add");
        setLocationRelativeTo(null);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        add(panel1);

        JPanel panelCenter = new JPanel();
        panel1.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new FlowLayout());
        panelCenter.add(new JLabel("isbn         "));
        isbnTextField = new JTextField(20);
        panelCenter.add(isbnTextField);
        panelCenter.add(new JLabel("number    "));
        numberTextField = new JTextField(20);
        panelCenter.add(numberTextField);
        panelCenter.add(new JLabel("author     "));
        authorTextField = new JTextField(20);
        panelCenter.add(authorTextField);
        panelCenter.add(new JLabel("title         "));
        titleTextField = new JTextField(20);
        panelCenter.add(titleTextField);
        panelCenter.add(new JLabel("publisher"));
        publisherTextField = new JTextField(20);
        panelCenter.add(publisherTextField);
        panelCenter.add(new JLabel("book_date"));
        bookdateTextField = new JTextField(20);
        panelCenter.add(bookdateTextField);
        panelCenter.add(new JLabel("status       "));
        statusTextField = new JTextField(20);
        panelCenter.add(statusTextField);
        panelCenter.add(new JLabel("regist_date"));
        registdateTextField = new JTextField(20);
        panelCenter.add(registdateTextField);

        JPanel panel2 = new JPanel();
        panel1.add(panel2, BorderLayout.SOUTH);
        JButton okBtn = new JButton(Const.OK);
        okBtn.addActionListener(this);
        panel2.add(okBtn);
        JButton cancelBtn = new JButton(Const.CANCEL);
        cancelBtn.addActionListener(this);
        panel2.add(cancelBtn);

        setSize(350, 300);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                dispose(); 
            }
        });
    }

    public int checkTextField(String text) {
        try {
            Integer.parseInt(text);
            return 1;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int checkDateField(String text) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.parse(text);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public int checkBoolean(String text) {
        if (Integer.parseInt(text) == 0 || Integer.parseInt(text) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals(Const.OK)) {        	
            if (isbnTextField.getText().length() == 0 || checkTextField(isbnTextField.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "The isbn is wrong");
                isbnTextField.requestFocus();
            } else if (numberTextField.getText().length() == 0 || checkTextField(numberTextField.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "The number is wrong");
                numberTextField.requestFocus();
            } else if (authorTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "The author is wrong");
                authorTextField.requestFocus();
            } else if (titleTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "The title is wrong");
                titleTextField.requestFocus();
            } else if (publisherTextField.getText().length() == 0) {
                JOptionPane.showMessageDialog(null, "The publisher is wrong");
                publisherTextField.requestFocus();
            } else if (bookdateTextField.getText().length() == 0 || checkDateField(bookdateTextField.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "The book_date is wrong");
                bookdateTextField.requestFocus();
            } else if (statusTextField.getText().length() == 0 || checkBoolean(statusTextField.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "The status is wrong");
                statusTextField.requestFocus();
            } else if (registdateTextField.getText().length() == 0 || checkDateField(registdateTextField.getText()) == 0) {
                JOptionPane.showMessageDialog(null, "The regist_date is wrong");
                registdateTextField.requestFocus();
            } else {
                try {
                    Database.getInstance().insertBookData(
                            isbnTextField.getText(),
                            numberTextField.getText(),
                            authorTextField.getText(),
                            titleTextField.getText(),
                            publisherTextField.getText(),
                            bookdateTextField.getText(),
                            statusTextField.getText(),
                            registdateTextField.getText());
                    if (MenuActionListener.table != null) {
                        MenuActionListener.mainWindow.remove(MenuActionListener.scroll);
                    }
                    MenuActionListener.createBookTable();
                    DefaultTableModel model = (DefaultTableModel) MenuActionListener.table.getModel();
                    model.setRowCount(0);
                    try {
                        Database.getInstance().insertBookJTable(model);
                        MenuActionListener.mainWindow.revalidate();
                    } catch (SQLException | IOException ex) {
                        ex.printStackTrace();
                    }
                    dispose();
                } catch (SQLException | IOException b) {
                    b.printStackTrace();
                }
            }
        } else if (e.getActionCommand().equals(Const.CANCEL)) {
            dispose();
        }
    }
}

