package org.bosiux.cookingupdate.utils;

import org.bosiux.cookingupdate.Main;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FuelManager {

    public static File file;


    public static boolean playerExists(String username) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject players = (JSONObject) jsonObject.get("players");
            return players.containsKey(username);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Float getPlayerValue(String username) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject players = (JSONObject) jsonObject.get("players");
            if (players.containsKey(username)) {
                return ((Number) players.get(username)).floatValue();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setPlayerValue(String username, float value) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            reader.close();

            JSONObject players = (JSONObject) jsonObject.get("players");
            players.put(username, value);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void createPlayer(String username, float value) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(file);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject players = (JSONObject) jsonObject.get("players");
            players.put(username, value);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


}
