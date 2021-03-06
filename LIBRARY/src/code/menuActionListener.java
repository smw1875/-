package code;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class MenuActionListener implements ActionListener {

    public static MainFrame mainWindow;

    public static JTable table;
    public static JScrollPane scroll;

    public MenuActionListener(MainFrame mainWindow) {
        MenuActionListener.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getActionCommand().equals(Const.MEMBERS)) {
            if (table != null) {
                mainWindow.remove(scroll);
            }
            createTable();
            System.out.println("members... ok");
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().insertMemberJTable(model);
                mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals(Const.LOGIN)) {
            System.out.println("Log-in ...");
            new LoginWindows();
        } else if (e.getActionCommand().equals(Const.LOGOUT)) {
            int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to Logout?", "Warning", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                mainWindow.dispose();
                new LoginWindows();
            }
        } else if (e.getActionCommand().equals(Const.BOOKS)) {
            System.out.println("books... ok");
            if (table != null) {
                mainWindow.remove(scroll);
            }
            createBookTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().insertBookJTable(model);
                mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals(Const.BOOKADD)) {
            System.out.println("Add book ...");
            BookAddDialog dialog = new BookAddDialog();
            dialog.show();
            System.out.println("join");
        }
        else if (e.getActionCommand().equals(Const.BOOKDELETE)) {
            System.out.println("Book Delete ...");
            int[] row = table.getSelectedRows();
            for (int i : row) {
                String eve = (String) table.getModel().getValueAt(i, 0);
                try {
                    Database.getInstance().bookDelete(eve);
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (table != null) {
                mainWindow.remove(scroll);
            }
            createBookTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().insertBookJTable(model);
                mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getActionCommand().equals(Const.BOOKSEARCH)) {
            System.out.println("Search Book ...");
            BookSearchDialog searchDialog = new BookSearchDialog();
            searchDialog.show();
        }
        else if (e.getActionCommand().equals(Const.BOOKBORROW)) {
            System.out.println("Book borrow ...");
            int[] row = table.getSelectedRows();
            for (int i : row) {
                String eve = (String) table.getModel().getValueAt(i, 0);
                try {
                    Database.getInstance().bookborrow(eve);
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (table != null) {
                mainWindow.remove(scroll);
            }
            createBookTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().insertBookJTable(model);
                mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getActionCommand().equals(Const.BOOKRETURN)) {
            System.out.println("Book Return ...");
            int[] row = table.getSelectedRows();
            for (int i : row) {
                String eve = (String) table.getModel().getValueAt(i, 0);
                try {
                    Database.getInstance().bookReturn(eve);
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (table != null) {
                mainWindow.remove(scroll);
            }
            createBookTable();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            try {
                Database.getInstance().insertBookJTable(model);
                mainWindow.revalidate();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }   
    }

    public void createTable() {
        String[] header = {"id", "name", "password"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        mainWindow.add(scroll);
    }
    
    public static void createBookTable() {
        String[] header = {"id", "isbn", "number", "author", "title", "publisher", "bookdate", "status", "registdate"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        table = new JTable(model);
        scroll = new JScrollPane(table);
        mainWindow.add(scroll);
    }
}