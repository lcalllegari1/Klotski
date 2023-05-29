package application.klotski.Controller;

import application.klotski.KlotskiApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DatabaseConnector {

    public record Record(
       int id,
       String date,
       String name,
       int move_count,
       String config,
       String history
    ) {}

    private static final String DB_PATH = Objects.requireNonNull(
            KlotskiApplication.class.getResource("/application/klotski/data/database/")
    ).getPath();
    private static final String DB_NAME = "klotski.db";
    private static final String DB_PREFIX = "jdbc:sqlite:";

    private static DatabaseConnector connector;
    private Connection connection = null;

    private DatabaseConnector() {}

    public static DatabaseConnector getInstance() {
        if (connector == null)
            connector = new DatabaseConnector();

        return connector;
    }

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DB_PREFIX + DB_PATH + DB_NAME
                );
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
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            return res.getInt(1) == 1;
        } catch (SQLException e) {
            System.out.println("Could not fetch the information from the database: " + e.getMessage());
            return false;
        }
    }

    public int createRecord(Record record) {
        String query = "INSERT INTO Matches (date, name, move_count, config, history) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, record.date);
            stm.setString(2, record.name);
            stm.setInt(3, record.move_count);
            stm.setString(4, record.config);
            stm.setString(5, record.history);
            stm.executeUpdate();
            return stm.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            System.out.println("Could not create a new record:" + e.getMessage());
            return 0;
        }
    }

    public Record fetch(int id) {
        String query = "SELECT * FROM Matches WHERE id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            return new Record(
                    res.getInt("id"),
                    res.getString("date"),
                    res.getString("name"),
                    res.getInt("move_count"),
                    res.getString("config"),
                    res.getString("history")
            );
        } catch (SQLException e) {
            System.out.println("Could not create a new record:" + e.getMessage());
            return null;
        }
    }

    public ArrayList<Record> fetch() {
        ArrayList<Record> records = new ArrayList<>();
        String query = "SELECT * FROM Matches";

        try {
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                records.add(new Record(
                    res.getInt("id"),
                    res.getString("date"),
                    res.getString("name"),
                    res.getInt("move_count"),
                    res.getString("config"),
                    res.getString("history")
                ));
            }
            return records;
        } catch (SQLException e) {
            System.out.println("Could not fetch data from the database");
            return null;
        }
    }

    public boolean update(int id, String date, int move_count) {
        String query = "UPDATE Matches SET date = ?, move_count = ? WHERE id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, date);
            stm.setInt(2, move_count);
            stm.setInt(3, id);

            return stm.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("Could not update the specified record: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String query = "DELETE FROM Matches WHERE id = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, id);

            return stm.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("Could not delete the specified record:" + e.getMessage());
            return false;
        }
    }

}
