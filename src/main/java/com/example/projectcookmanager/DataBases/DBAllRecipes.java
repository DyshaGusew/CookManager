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

    public Recipe Select(int id){
        return null;
    }


    public Recipe Select(String name) {
        return null;
    }


    public Recipe[] SelectAll(String name) {
        return new Recipe[0];
    }


    public void Insert(Recipe recipe) {

    }


    public void Delete(Recipe recipe) {

    }


    public void Delete(int id) {

    }


    public void Update(String name, Recipe newRecipe) {

    }


    public void Update(int id, Recipe newRecipe) {

    }

    public List<Recipe> SelectAll(){
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
                String[] textsStage = rs.getString("textsStage").split(";");

                recipes.add(new Recipe(rs.getInt("id"),
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
                        textsStage));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            recipes = null;
        }
        return recipes;
    }

    public void insert(String name, String category) {
        String sql = "INSERT INTO " + nameTable + "(name, category) VALUES(?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
