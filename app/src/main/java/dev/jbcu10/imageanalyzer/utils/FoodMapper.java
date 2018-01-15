package dev.jbcu10.imageanalyzer.utils;

import org.json.JSONException;
import org.json.JSONObject;

import dev.jbcu10.imageanalyzer.model.Food;

/**
 * Created by dev on 1/14/18.
 */

public class FoodMapper {
    
    public static Food mapFoodJSONtoFood(JSONObject jsonObject) throws JSONException {
        Food food = new Food( );

        food.setName(jsonObject.getString("item_name")  != null ? jsonObject.getString("item_name")  : "");

        food.setUpc(jsonObject.getString("upc") != null ? jsonObject.getString("upc") : "");

        food.setIngredient_statement(jsonObject.getString("ingredient_statement") != null ? jsonObject.getString("ingredient_statement") : "");

        food.setServing_qty(jsonObject.getDouble("serving_qty")>0 ? String.valueOf(jsonObject.getDouble("serving_qty")) : "0");

        food.setServing_unit( jsonObject.getString("serving_unit") != null?  jsonObject.getString("serving_unit") : "");

        food.setMetric_qty(jsonObject.getInt("metric_qty")>0 ? String.valueOf(jsonObject.getInt("metric_qty")) : "");

        food.setMetric_unit(jsonObject.getString("metric_unit") != null ? jsonObject.getString("metric_unit"): "");

        food.setCalories(jsonObject.getInt("calories")>0 ? String.valueOf(jsonObject.getInt("calories")) : "");

        food.setCalories_from_fat(jsonObject.getDouble("calories_from_fat")>0 ? String.valueOf(jsonObject.get("calories_from_fat")) : "");

        food.setTotal_fat(jsonObject.get("calories_from_fat")!=null? String.valueOf(jsonObject.get("calories_from_fat")) : "");
/*

        food.setSaturated_fat(jsonObject.getInt("saturated_fat")>0 ? String.valueOf(jsonObject.getInt("saturated_fat")) : "");

        food.setTrans_fat(jsonObject.getInt("trans_fat")>0 ? String.valueOf(jsonObject.getInt("trans_fat")) : "");

        food.setCholesterol(jsonObject.getInt("cholesterol")>0 ? String.valueOf(jsonObject.getInt("cholesterol")) : "");

        food.setSodium(jsonObject.getInt("sodium")>0 ? String.valueOf(jsonObject.getInt("sodium")) : "");

        food.setTotal_carb(jsonObject.getInt("total_carb")>0 ? String.valueOf(jsonObject.getInt("total_carb")) : "");

        food.setDietary_fiber(jsonObject.getInt("dietary_fiber")>0 ? String.valueOf(jsonObject.getInt("dietary_fiber")) : "");

        food.setSugars(jsonObject.getInt("sugars")>0 ? String.valueOf(jsonObject.getInt("sugars")) : "");

        food.setProtein(jsonObject.getInt("protein")>0 ? String.valueOf(jsonObject.getInt("protein")) : "");

        food.setPotassium(jsonObject.getInt("potassium")>0 ? String.valueOf(jsonObject.getInt("potassium")) : "");

        food.setVitamin_a(jsonObject.getInt("vitamin_a")>0 ? String.valueOf(jsonObject.getInt("vitamin_a")) : "");

        food.setVitamin_c(jsonObject.getInt("vitamin_c")>0 ? String.valueOf(jsonObject.getInt("vitamin_c")) : "");

        food.setCalcium_dv(jsonObject.getInt("calcium_dv")>0 ? String.valueOf(jsonObject.getInt("calcium_dv")) : "");

        food.setIron_dv(jsonObject.getInt("iron_dv")>0 ? String.valueOf(jsonObject.getInt("iron_dv")) : "");

*/

        return food;

    }
}
