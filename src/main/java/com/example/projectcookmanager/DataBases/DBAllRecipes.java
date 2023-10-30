package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Recipe;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DBAllRecipes extends DataBase {
    private Connection conn = null;
    static private String nameTable = "AllRecipes";

    private Connection connect() {
        String url = "jdbc:sqlite:DB.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Recipe Read(int id){
        String sql = "SELECT * FROM " + nameTable + " WHERE id = ?";
        Recipe getRecipe = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                String[] ingredientsName = resultSet.getString("ingredientsName").split(";");
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        resultSet.getInt("totalCalories"),
                        resultSet.getInt("protein"),
                        resultSet.getInt("fat"),
                        resultSet.getInt("carbohydrate"),
                        resultSet.getFloat("rating"),
                        ingredientsName,
                        ingredientsMass,
                        imagesStageLinks,
                        textStages);
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getRecipe = null;
        }
        if(getRecipe == null){
            System.out.println("В базе данных нет id: " + id);
        }

        return getRecipe;
    }
    public Recipe Read(String name) {
        String sql = "SELECT * FROM " + nameTable + " WHERE name = ?";

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Recipe getRecipe = null;
        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                String[] ingredientsName = resultSet.getString("ingredientsName").split(";");
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        resultSet.getInt("totalCalories"),
                        resultSet.getInt("protein"),
                        resultSet.getInt("fat"),
                        resultSet.getInt("carbohydrate"),
                        resultSet.getFloat("rating"),
                        ingredientsName,
                        ingredientsMass,
                        imagesStageLinks,
                        textStages);
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getRecipe = null;
        }
        if(getRecipe == null){
            System.out.println("В базе данных нет имени: " + name);
        }
        return getRecipe;
    }
    public List<Recipe> ReadAll(){
        String sql = "SELECT * FROM " + nameTable;
        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String[] ingredientsName = rs.getString("ingredientsName").split(";");
                String[] ingredientsMass = rs.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = rs.getString("imagesStageLinks").split(";");
                String[] textStages = rs.getString("textStages").split(";");

                recipes.add(new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("timeCooking"),
                        rs.getString("mainImageLink"),
                        rs.getInt("totalCalories"),
                        rs.getInt("protein"),
                        rs.getInt("fat"),
                        rs.getInt("carbohydrate"),
                        rs.getFloat("rating"),
                        ingredientsName,
                        ingredientsMass,
                        imagesStageLinks,
                        textStages));
            }
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            recipes = null;
        }
        return recipes;
    }


    public void Write(Recipe recipe) {
        String sql = "INSERT INTO " + nameTable + "(name, category, timeCooking, mainImageLink, totalCalories, protein," +
                " fat, carbohydrate, rating, ingredientsName, ingredientsMass, imagesStageLinks, textStages) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, recipe.name);
            prepStat.setString(2, recipe.category);
            prepStat.setInt(3, recipe.timeCooking);
            prepStat.setString(4, recipe.mainImageLink);
            prepStat.setInt(5, recipe.totalCalories);
            prepStat.setInt(6, recipe.protein);
            prepStat.setInt(7, recipe.fat);
            prepStat.setInt(8, recipe.carbohydrate);
            prepStat.setFloat(9, recipe.getRating());
            prepStat.setString(10, String.join(";", recipe.ingredientsName));
            prepStat.setString(11, String.join(";", recipe.ingredientsMass));
            prepStat.setString(12, String.join(";", recipe.imagesStageLinks));
            prepStat.setString(13, String.join(";", recipe.textStages));
            prepStat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void Delete(String name) {
        String sql = "DELETE FROM " + nameTable + " WHERE name = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setObject(1, name);
            // Выполняем запрос
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void Delete(int id) {
        String sql = "DELETE FROM " + nameTable + " WHERE id = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setObject(1, id);
            // Выполняем запрос
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void Update(String name, Recipe newRecipe) {

    }


    public void Update(int id, Recipe newRecipe) {

    }

}
