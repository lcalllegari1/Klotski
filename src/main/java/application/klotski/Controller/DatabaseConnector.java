package application.klotski.Controller;

import application.klotski.KlotskiApplication;

import java.sql.*;
import java.util.Objects;

public class DatabaseConnector {

    public record Record(
            String name,
            int move_count,
            String init_config_token,
            String init_config_file,
            String init_config_img,
            String curr_config_img,
            String history_file
    ) {}

    private static final String DB_PATH = Objects.requireNonNull(KlotskiApplication.class.getResource("/application/klotski/data/database/")).getPath();
    private static final String DB_NAME = "klotski.db";
    private static final String DB_PREFIX = "jdbc:sqlite:";

    private Connection connection = null;

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("in");
                connection = DriverManager.getConnection(DB_PREFIX + DB_PATH + DB_NAME);
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database." + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Could not close the database.");
        }
    }

    public boolean contains(int id) {
        String query = "SELECT 1 FROM Matches WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet res = statement.executeQuery();

            return res.getInt(1) == 1;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public int createRecord(Record record) {
        String query = "INSERT INTO Matches (name, move_count, init_config_token, init_config_file, init_config_img, curr_config_img, history_file) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, record.name());
            statement.setInt(2, record.move_count());
            statement.setString(3, record.init_config_token());
            statement.setString(4, record.init_config_file());
            statement.setString(5, record.init_config_img());
            statement.setString(6, record.curr_config_img());
            statement.setString(7, record.history_file());

            statement.executeUpdate();
            return statement.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            System.out.println("Could not create a new record." + e.getMessage());
        }
        return 0;
    }

    public Record fetch(int id) {
        String query = "SELECT * FROM Matches WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet res = statement.executeQuery();
            return new Record(
                    res.getString("name"),
                    res.getInt("move_count"),
                    res.getString("init_config_token"),
                    res.getString("init_config_file"),
                    res.getString("init_config_img"),
                    res.getString("curr_config_img"),
                    res.getString("history_file"));

        } catch (SQLException e) {
            System.out.println("Could not fetch the specified record.");
        }
        return null;
    }

    public boolean update(int id, Record record) {
        String query = "UPDATE Matches SET name = ?, move_count = ?, curr_config_img = ?, history_file = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, record.name());
            statement.setInt(2, record.move_count());
            statement.setString(3, record.curr_config_img());
            statement.setString(4, record.history_file());
            statement.setInt(5, id);

            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("Could not update the specified record." + e.getMessage());
        }

        // exception caught, thus something went wrong
        return false;
    }
}
