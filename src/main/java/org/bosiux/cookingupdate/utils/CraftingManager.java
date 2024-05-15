package org.bosiux.cookingupdate.utils;

import org.bosiux.cookingupdate.Main;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CraftingManager {
    private static final Plugin plugin = Main.plugin;

    public static void newCrafting(String[] ingredients, String result) {

        JSONObject jsonData = readData();

        JSONObject newRecipe = new JSONObject();
        JSONArray craftingArray = new JSONArray();
        for (String ingredient : ingredients) {
            craftingArray.add(ingredient);
        }
        newRecipe.put("crafting", craftingArray);
        newRecipe.put("result", result);

        JSONArray recipes = (JSONArray) jsonData.get("recipes");
        recipes.add(newRecipe);

        writeData(jsonData);
    }

    private static JSONObject readData() {
        JSONParser parser = new JSONParser();
        try {
            File file = new File(plugin.getDataFolder(), "data.json");
            if (!file.exists() || file.length() == 0) {
                return new JSONObject();
            }
            FileReader reader = new FileReader(file);
            Object obj = parser.parse(reader);
            reader.close();
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private static void writeData( JSONObject jsonData) {
        File file_ = new File(plugin.getDataFolder(), "data.json");
        try (FileWriter file = new FileWriter(file_)) {
            file.write(jsonData.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
