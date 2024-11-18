import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    final static String url = "jdbc:sqlite:data/battleJoker.db";
    static Connection conn;

    public static void connect() throws SQLException, ClassNotFoundException {
        if (conn == null) {
//            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
        }

    }

    public static void disconnect() throws SQLException {
        if (conn != null)
            conn.close();
    }

    public static ArrayList<HashMap<String, String>> getScores() throws SQLException {
        String sql = "SELECT * FROM scores ORDER BY score DESC LIMIT 10";
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            HashMap<String, String> m = new HashMap<>();
            m.put("name", resultSet.getString("name"));
            m.put("score", resultSet.getString("score"));
            m.put("level", resultSet.getString("level"));
            m.put("time", resultSet.getString("time"));
            data.add(m);
        }
        return data;
    }

    public static void putScore(String name, int score, int level) throws SQLException {
        String sql = String.format("INSERT INTO scores ('name', 'score', 'level', 'time') VALUES ('%s', %d, %d, datetime('now'))", name, score, level);
        if (conn == null) {


        } else {

            Statement statement = conn.createStatement();
            statement.execute(sql);
        }
    }

    public static void main(String[] args) {
        try {
            connect();
            putScore("Bob", 1000, 13);
            getScores().forEach(map -> {
                System.out.println(map.get("name"));
            });
        } catch (SQLException e) {
            System.err.println("SQL 错误: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                System.err.println("断开连接时出错: " + e.getMessage());
            }
        }
    }

}
