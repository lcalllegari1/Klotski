package com.klotski.Controller;

// Class to handle database related operations.

import com.klotski.KlotskiApplication;

import java.util.ArrayList;
import java.util.Objects;
import java.sql.*;

// Singleton design pattern used to guarantee
// only one instance of the class to prevent
// multiple connections to the database.
public class DatabaseConnector {

    // Records to help database data transfer

    // refers to a record of the "Configs" table.
    public record Config(
            int config_id,
            String token
    ) {}

    // refers to a record of the "Matches" table.
    public record Match(
            int match_id,
            int score,
            String date,
            String history,
            String img,
            int config_id
    ) {}

    // data member
    private static final String DB_DIR_PATH = Objects.requireNonNull(
            KlotskiApplication.class.getResource("/com/klotski/data/database/")
    ).getPath();

    private static final String DB_NAME = "klotski.db";
    private static final String DEFAULT_DB_PREFIX = "jdbc:sqlite:";
    private static final String JAR_DB_PREFIX = DEFAULT_DB_PREFIX + ":resource:";

    // static instance of this class to implement Singleton design pattern
    private static DatabaseConnector connector;

    private Connection connection;

    // private constructor to guarantee only one instance of this class (Singleton)
    private DatabaseConnector() {}

    // method to access the instance of this class
    public static DatabaseConnector getInstance() {
        if (connector == null)
            connector = new DatabaseConnector();
        return connector;
    }

    public void connect() {
        try {
            // Try to connect (default ide build)
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        DEFAULT_DB_PREFIX + DB_DIR_PATH + DB_NAME
                );
            }
        } catch (SQLException tmp) {
            try {
                // Try to connect (jar build)
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(
                            JAR_DB_PREFIX + "com/klotski/data/database/" + DB_NAME
                    );
                }
            } catch (SQLException e) {
                System.out.println("Could not connect to the database: " + e.getMessage());
            }
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

    // Fetch data from the database to find the record with
    // the specified "match_id" from "Matches" table.
    public Match getMatch(int match_id) {
        String query = "SELECT * FROM Matches WHERE match_id = ?";

        try {
            // prepare the query & set the parameters
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, match_id);
            // execute the query and return the record
            ResultSet res = stm.executeQuery();
            if (res.isBeforeFirst()) {
                // Then there is a match with the specified "match_id".
                return new Match(
                        res.getInt("match_id"),
                        res.getInt("score"),
                        res.getString("date"),
                        res.getString("history"),
                        res.getString("img"),
                        res.getInt("config_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }
        // there is not a match with the specified "match_id" in
        // the database table "Matches" or an exception was thrown,
        // thus return null.
        return null;
    }

    public ArrayList<Match> fetchAllMatches() {
        ArrayList<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM Matches";

        try {
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                matches.add(new Match(
                        res.getInt("match_id"),
                        res.getInt("score"),
                        res.getString("date"),
                        res.getString("history"),
                        res.getString("img"),
                        res.getInt("config_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }
        return matches;
    }

    // Creates a new record in the "Matches" table filled
    // with data specified in "match".
    public int createMatch(Match match) {
        String query = "INSERT INTO Matches (score, date, history, img, config_id) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, match.score);
            stm.setString(2, match.date);
            stm.setString(3, match.history);
            stm.setString(4, match.img);
            stm.setInt(5, match.config_id);

            stm.executeUpdate();
            // return the match_id given to the new record.
            return stm.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }
        // could not create a match, thus return 0 (invalid id)
        return 0;
    }

    public boolean updateMatch(int match_id, int score, String date) {
        String query = "UPDATE Matches SET score = ?, date = ? WHERE match_id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, score);
            stm.setString(2, date);
            stm.setInt(3, match_id);

            return stm.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }

        // the query could not be completed, thus return false.
        return false;
    }

    public boolean deleteMatch(int match_id) {
        String query = "DELETE FROM Matches WHERE match_id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, match_id);

            return stm.executeUpdate() != 0;
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }

        // the query could not be completed, thus return false.
        return false;
    }

    // Fetch data from the database to find the record with
    // the specified "config_id" from "Configs" table.
    public Config getConfig(int config_id) {
        String query = "SELECT * FROM Configs WHERE config_id = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, config_id);
            ResultSet res = stm.executeQuery();
            if (res.isBeforeFirst()) {
                return new Config(
                  res.getInt("config_id"),
                  res.getString("token")
                );
            }
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }

        // The config with the specified "config_id" could not be found
        // in the database table "Configs", thus return null.
        return null;
    }

    public ArrayList<Config> fetchAllConfigs() {
        ArrayList<Config> configs = new ArrayList<>();
        String query = "SELECT * FROM Configs ORDER BY config_id";

        try {
            Statement stm = connection.createStatement();
            ResultSet res = stm.executeQuery(query);

            while (res.next()) {
                configs.add(new Config(
                        res.getInt("config_id"), res.getString("token")
                ));
            }
        } catch (SQLException e) {
            System.out.println("The query could not be completed: " + e.getMessage());
        }
        return configs;
    }
}
