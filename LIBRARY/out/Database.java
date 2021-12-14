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
        String server = Files.readAllLines(Paths.get("db.txt")).get(0);
        String database = Files.readAllLines(Paths.get("db.txt")).get(1);
        String user_name = Files.readAllLines(Paths.get("db.txt")).get(2);
        String password = Files.readAllLines(Paths.get("db.txt")).get(3);
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" 드라이버 로딩 오류 : " + e.getMessage());
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:mysql://" +
                server + "3307/" +
                database +
                "?useSSL=false", user_name, password);
        initDatabase();

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
                    "INSERT INTO book (id, isbn, number, authors, title, publisher, book_date, status, regist_date) "
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
                record[3] = String.valueOf(rs.getString("authors"));
                record[4] = String.valueOf(rs.getString("title"));
                record[5] = String.valueOf(rs.getString("publisher"));
                record[6] = String.valueOf(rs.getDate("book_date"));
                record[7] = String.valueOf(rs.getBoolean("status"));
                record[8] = String.valueOf(rs.getDate("regist_date"));

                model.addRow(record);
            }
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
                    "WHERE (id LIKE '%" + keyword + "%' OR isbn LIKE '%" + keyword + "%' OR number LIKE '%" + keyword + "%' OR authors Like '%" + keyword + "%' OR title LIKE '%" + keyword + "%' OR publisher LIKE '%" + keyword + "%' OR\n" +
                    "       book_date LIKE '%" + keyword + "%' OR status LIKE '%" + keyword + "%' OR regist_date LIKE '%" + keyword + "%');";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String[] record = new String[9];
                record[0] = Integer.toString(rs.getInt("id"));
                record[1] = String.valueOf(rs.getInt("isbn"));
                record[2] = String.valueOf(rs.getInt("number"));
                record[3] = String.valueOf(rs.getString("authors"));
                record[4] = String.valueOf(rs.getString("title"));
                record[5] = String.valueOf(rs.getString("publisher"));
                record[6] = String.valueOf(rs.getDate("book_date"));
                record[7] = String.valueOf(rs.getBoolean("status"));
                record[8] = String.valueOf(rs.getDate("regist_date"));

                model.addRow(record);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

