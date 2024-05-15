package org.bosiux.cookingupdate.utils;

import org.bosiux.cookingupdate.Main;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    private static final Plugin plugin = Main.plugin;

    private static final String DATABASE_NAME = "storage.db";
    private static final String TABLE_NAME = "data";
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "uuid TEXT," +
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
}
/*

+--------------+-------------+--------+--------------+
|     uuid     |  username   | amount |  last_use    |
+--------------+-------------+--------+--------------+
|    TEXT      |    TEXT     | FLOAT  |   INTEGER    |
+--------------+-------------+--------+--------------+


 */
