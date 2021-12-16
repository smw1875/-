package code;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private volatile static Database instance = null;
    private Connection connection = null;

    private Database() throws SQLException, IOException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:book.db");
            initDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println(" 오류발생 : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static Database getInstance() throws SQLException, IOException {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null)
                    instance = new Database();
            }
        }
        return instance;
    }
    public int getNext(String tableName) {
        String sql = "SELECT id FROM " + tableName + " ORDER BY id DESC";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id") + 1;
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertMemberData(String name, String password) {
        System.out.println(getNext("member"));
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate(
                    "INSERT INTO member (id, name, pwd) "
                            + "values('" + getNext("member") + "', '" + name + "', '" + password + "')");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertBookData(String isbn, String number, String author, String title, String publisher, String bookdate, String status, String registdate) {
        System.out.println(getNext("book"));
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate(
                    "INSERT INTO book (id, isbn, number, author, title, publisher, bookdate, status, registdate) "
                            + "values('" + getNext("book") + "', '" + isbn + "', '" + number + "', '" + author + "', '" + title + "', '" + publisher + "', '" + bookdate + "', '" + status + "', '" + registdate + "')");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initDatabase() {
        // create a database connection
        try {
            Statement statement = connection.createStatement();
            if (!checkExistTable("member")) {
                statement.executeUpdate(
                        "CREATE TABLE member "
                                + "(id INTEGER NOT NULL, "
                                + "name STRING NOT NULL, "
                                + "pwd STRING NOT NULL, "
                                + "PRIMARY KEY(ID AUTOINCREMENT))");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean checkExistTable(String tableName) {
        boolean retValue = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT id FROM table_name WHERE name='" + tableName + "'");
            if (rs.next())
                retValue = true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retValue;
    }

    public boolean checkExistMember(String name, String pwd) {
        boolean retValue = false;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM member WHERE name='" + name + "' AND pwd='" + pwd + "'");
            if (rs.next())
                retValue = true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retValue;
    }

    public void insertMemberJTable(DefaultTableModel model) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM member");
            while (rs.next()) {
                String[] record = new String[3];
                record[0] = Integer.toString(rs.getInt("id"));
                record[1] = rs.getString("name");
                record[2] = rs.getString("pwd");

                model.addRow(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void insertBookJTable(DefaultTableModel model) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM book");
            while (rs.next()) {
                String[] record = new String[9];
                record[0] = Integer.toString(rs.getInt("id"));
                record[1] = String.valueOf(rs.getInt("isbn"));
                record[2] = String.valueOf(rs.getInt("number"));
                record[3] = String.valueOf(rs.getString("author"));
                record[4] = String.valueOf(rs.getString("title"));
                record[5] = String.valueOf(rs.getString("publisher"));
                record[6] = String.valueOf(rs.getString("bookdate"));
                record[7] = String.valueOf(rs.getBoolean("status"));
                record[8] = String.valueOf(rs.getString("registdate"));

                model.addRow(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void bookDelete(String id) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("DELETE FROM book WHERE id =" + id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void searchBookJTable(DefaultTableModel model, String keyword) {
        System.out.println(getNext("book"));
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            String sql = "SELECT * FROM book " +
                    "WHERE (id LIKE '%" + keyword + "%' OR isbn LIKE '%" + keyword + "%' OR number LIKE '%" + keyword + "%' OR author Like '%" + keyword + "%' OR title LIKE '%" + keyword + "%' OR publisher LIKE '%" + keyword + "%' OR\n" +
                    "       bookdate LIKE '%" + keyword + "%' OR status LIKE '%" + keyword + "%' OR registdate LIKE '%" + keyword + "%');";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String[] record = new String[9];
                record[0] = Integer.toString(rs.getInt("id"));
                record[1] = String.valueOf(rs.getInt("isbn"));
                record[2] = String.valueOf(rs.getInt("number"));
                record[3] = String.valueOf(rs.getString("author"));
                record[4] = String.valueOf(rs.getString("title"));
                record[5] = String.valueOf(rs.getString("publisher"));
                record[6] = String.valueOf(rs.getString("bookdate"));
                record[7] = String.valueOf(rs.getBoolean("status"));
                record[8] = String.valueOf(rs.getString("registdate"));

                model.addRow(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void bookborrow(String id) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("UPDATE book SET status = False WHERE id =" + id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void bookReturn(String id) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("UPDATE book SET status = True WHERE id =" + id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

