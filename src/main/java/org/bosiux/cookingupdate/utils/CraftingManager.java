package org.bosiux.cookingupdate.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class CraftingManager {
    private static final Plugin plugin = Main.plugin;

    public static void newCrafting(String[] ingredients, String result, float fuel) {

        JSONObject jsonData = readData();

        JSONObject newRecipe = new JSONObject();
        JSONArray craftingArray = new JSONArray();
        for (String ingredient : ingredients) {
            craftingArray.add(ingredient);
        }
        newRecipe.put("crafting", craftingArray);
        newRecipe.put("result", result);
        newRecipe.put("fuel", fuel);

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
    public static Map<String, Float> getRecipe(String[] ingredients) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File(plugin.getDataFolder(), "data.json"));

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
                String result = recipeNode.get("result").asText();
                float fuel = recipeNode.get("fuel").floatValue();
                return Map.of(result, fuel);
            }
        }
        return null;
    }


    public static float round(float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }



}
