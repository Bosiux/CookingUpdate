package org.bosiux.cookingupdate.utils;

import org.bosiux.cookingupdate.Main;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;

public class SQLiteManager {
    private static final Plugin plugin = Main.plugin;

    private static final String DATABASE_NAME = "storage.db";
    private static final String TABLE_NAME = "data";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "uuid TEXT UNIQUE," +
            "username TEXT," +
            "amount FLOAT," +
            "last_use INTEGER" +
            ")";

    private static Connection getConnection() {
        File dataFolder = plugin.getDataFolder();

        String databasePath = "jdbc:sqlite:" + dataFolder.getAbsolutePath() + File.separator + DATABASE_NAME;
        try {
            return DriverManager.getConnection(databasePath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean UUIDexist(String uuid) {
        String query = "SELECT 1 FROM data WHERE uuid = ? LIMIT 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, uuid);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
/*
                        data
+--------------+-------------+--------+--------------+
|     uuid     |  username   | amount |  last_use    |
+--------------+-------------+--------+--------------+
|    TEXT      |    TEXT     | FLOAT  |   INTEGER    |
+--------------+-------------+--------+--------------+


 */
