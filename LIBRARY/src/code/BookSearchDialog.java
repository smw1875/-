package code;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class BookSearchDialog extends JDialog implements ActionListener {
    @Serial
    private static final long serialVersionUID = 1L;
    public static JTextField searchTextField;

    public BookSearchDialog() {
        setTitle("Search Book");
        setLocationRelativeTo(null);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        add(panel1);

        JPanel panelCenter = new JPanel();
        panel1.add(panelCenter, BorderLayout.CENTER);
        panelCenter.setLayout(new FlowLayout());
        panelCenter.add(new JLabel("Keyword "));
        searchTextField = new JTextField(20);
        panelCenter.add(searchTextField);

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
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals(Const.OK)) {
            if (MenuActionListener.table != null) {
                MenuActionListener.mainWindow.remove(MenuActionListener.scroll);
            }
            MenuActionListener.createBookTable();
            DefaultTableModel model = (DefaultTableModel) MenuActionListener.table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().searchBookJTable(model, searchTextField.getText());
                MenuActionListener.mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals(Const.CANCEL)) {
            dispose();
        }
    }
}
