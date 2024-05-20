package org.bosiux.cookingupdate.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.bosiux.cookingupdate.Main;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CraftingManager {
    private static final Plugin plugin = Main.plugin;
    private static List<Map<String, Object>> recipes;

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
    public static String getRecipe(String[] ingredients) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree( new File(plugin.getDataFolder(), "data.json"));

        Set<String> ingredientSet = new HashSet<>(Arrays.asList(ingredients));

        JsonNode recipesNode = rootNode.get("recipes");
        for (JsonNode recipeNode : recipesNode) {
            Set<String> recipeSet = new HashSet<>();
            JsonNode craftingNode = recipeNode.get("crafting");
            Iterator<JsonNode> elements = craftingNode.elements();
            while (elements.hasNext()) {
                recipeSet.add(elements.next().asText());
            }

            if (recipeSet.equals(ingredientSet)) {
                return recipeNode.get("result").asText();
            }
        }
        return null;
    }




}
