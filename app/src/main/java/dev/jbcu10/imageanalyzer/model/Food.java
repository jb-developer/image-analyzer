package dev.jbcu10.imageanalyzer.model;

/**
 * Created by dev on 1/14/18.
 */

public class Food {

    private String name;
    private String upc;
    private String ingredient_statement;
    private String serving_qty;
    private String serving_unit;
    private String metric_qty;
    private String metric_unit;
    private String calories;
    private String calories_from_fat;
    private String total_fat;
    private String saturated_fat;
    private String trans_fat;
    private String cholesterol;
    private String sodium;
    private String total_carb;
    private String dietary_fiber;
    private String sugars;
    private String protein;
    private String potassium;
    private String vitamin_a;
    private String vitamin_c;
    private String calcium_dv;
    private String iron_dv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getIngredient_statement() {
        return ingredient_statement;
    }

    public void setIngredient_statement(String ingredient_statement) {
        this.ingredient_statement = ingredient_statement;
    }

    public String getServing_qty() {
        return serving_qty;
    }

    public void setServing_qty(String serving_qty) {
        this.serving_qty = serving_qty;
    }

    public String getServing_unit() {
        return serving_unit;
    }

    public void setServing_unit(String serving_unit) {
        this.serving_unit = serving_unit;
    }

    public String getMetric_qty() {
        return metric_qty;
    }

    public void setMetric_qty(String metric_qty) {
        this.metric_qty = metric_qty;
    }

    public String getMetric_unit() {
        return metric_unit;
    }

    public void setMetric_unit(String metric_unit) {
        this.metric_unit = metric_unit;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCalories_from_fat() {
        return calories_from_fat;
    }

    public void setCalories_from_fat(String calories_from_fat) {
        this.calories_from_fat = calories_from_fat;
    }

    public String getTotal_fat() {
        return total_fat;
    }

    public void setTotal_fat(String total_fat) {
        this.total_fat = total_fat;
    }

    public String getSaturated_fat() {
        return saturated_fat;
    }

    public void setSaturated_fat(String saturated_fat) {
        this.saturated_fat = saturated_fat;
    }

    public String getTrans_fat() {
        return trans_fat;
    }

    public void setTrans_fat(String trans_fat) {
        this.trans_fat = trans_fat;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getTotal_carb() {
        return total_carb;
    }

    public void setTotal_carb(String total_carb) {
        this.total_carb = total_carb;
    }

    public String getDietary_fiber() {
        return dietary_fiber;
    }

    public void setDietary_fiber(String dietary_fiber) {
        this.dietary_fiber = dietary_fiber;
    }

    public String getSugars() {
        return sugars;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

    public String getVitamin_a() {
        return vitamin_a;
    }

    public void setVitamin_a(String vitamin_a) {
        this.vitamin_a = vitamin_a;
    }

    public String getVitamin_c() {
        return vitamin_c;
    }

    public void setVitamin_c(String vitamin_c) {
        this.vitamin_c = vitamin_c;
    }

    public String getCalcium_dv() {
        return calcium_dv;
    }

    public void setCalcium_dv(String calcium_dv) {
        this.calcium_dv = calcium_dv;
    }

    public String getIron_dv() {
        return iron_dv;
    }

    public void setIron_dv(String iron_dv) {
        this.iron_dv = iron_dv;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", upc='" + upc + '\'' +
                ", ingredient_statement='" + ingredient_statement + '\'' +
                ", serving_qty='" + serving_qty + '\'' +
                ", serving_unit='" + serving_unit + '\'' +
                ", metric_qty='" + metric_qty + '\'' +
                ", metric_unit='" + metric_unit + '\'' +
                ", calories='" + calories + '\'' +
                ", calories_from_fat='" + calories_from_fat + '\'' +
                ", total_fat='" + total_fat + '\'' +
                ", saturated_fat='" + saturated_fat + '\'' +
                ", trans_fat='" + trans_fat + '\'' +
                ", cholesterol='" + cholesterol + '\'' +
                ", sodium='" + sodium + '\'' +
                ", total_carb='" + total_carb + '\'' +
                ", dietary_fiber='" + dietary_fiber + '\'' +
                ", sugars='" + sugars + '\'' +
                ", protein='" + protein + '\'' +
                ", potassium='" + potassium + '\'' +
                ", vitamin_a='" + vitamin_a + '\'' +
                ", vitamin_c='" + vitamin_c + '\'' +
                ", calcium_dv='" + calcium_dv + '\'' +
                ", iron_dv='" + iron_dv + '\'' +
                '}';
    }
}
