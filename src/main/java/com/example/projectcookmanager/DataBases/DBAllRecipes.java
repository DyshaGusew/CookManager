package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity;
import com.example.projectcookmanager.Product;
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
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBAllProducts().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBAllProducts().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBAllProducts().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
                        ingredientsMass,
                        imagesStageLinks,
                        textStages));
            }
            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            recipes = null;
        }
        return recipes;
    }

    //Получение массива, содержащего определенный параметр
    public List<Recipe> ReadOfParam(String param, String nameParam){
        String sql = "SELECT * FROM " + nameTable + " WHERE " + param + " LIKE ?";
        List<Recipe> recipes = new ArrayList<Recipe>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setString(1, nameParam);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBAllProducts().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
                        ingredientsMass,
                        imagesStageLinks,
                        textStages));
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            recipes = null;
        }
        return recipes;
    }
    //Получение массива, отсортированного по какому-либо параметру
    public List<Recipe> ReadOfSort(String sortParam){
        String sql = "SELECT * FROM " + nameTable + " ORDER BY " + sortParam;
        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                String[] ingredientsMass = resultSet.getString("ingredientsMass").split(";");
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBAllProducts().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
                        ingredientsMass,
                        imagesStageLinks,
                        textStages));
            }
            if (resultSet != null) resultSet.close();
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
        String sql = "INSERT INTO " + nameTable + "(name, category, mainInfo, timeCooking, mainImageLink, rating, ingredientsMass, imagesStageLinks, textStages) VALUES(?,?,?,?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, recipe.name);
            prepStat.setString(2, recipe.category);
            prepStat.setString(3, recipe.mainInfo);
            prepStat.setInt(4, recipe.timeCooking);
            prepStat.setString(5, recipe.mainImageLink);
            prepStat.setFloat(6, recipe.getRating());
            prepStat.setString(7, String.join(";", recipe.ingredientsMass));
            prepStat.setString(8, String.join(";", recipe.imagesStageLinks));
            prepStat.setString(9, String.join(";", recipe.textStages));
            prepStat.executeUpdate();


            new DBAllProducts().WriteProductRecipe(recipe.products,recipe.name);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void Delete(String name) {
        String sql = "DELETE FROM " + nameTable + " WHERE name = ?";
        Recipe thisRec = new DBAllRecipes().Read(name);
        new  DBAllProducts().DeleteProdRec(thisRec.id);
        try {
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setObject(1, name);
            // Выполняем запрос
            prepStat.executeUpdate();
            if (prepStat != null) prepStat.close();
            if (conn != null) conn.close();
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
        new  DBAllProducts().DeleteProdRec(id);
    }


    public void Update(String name, Recipe newRecipe) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "mainInfo = ? ,"
                + "category = ? ,"
                + "timeCooking = ? ,"
                + "mainImageLink = ? ,"
                + "rating = ? ,"
                + "ingredientsMass = ? ,"
                + "imagesStageLinks = ? ,"
                + "textStages = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newRecipe.name);
            pstmt.setString(2, newRecipe.mainInfo);
            pstmt.setString(3, newRecipe.category);
            pstmt.setInt(4, newRecipe.timeCooking);
            pstmt.setString(5, newRecipe.mainImageLink);
            pstmt.setFloat(6, newRecipe.getRating());
            pstmt.setString(7, String.join(";", newRecipe.ingredientsMass));
            pstmt.setString(8, String.join(";",newRecipe.imagesStageLinks));
            pstmt.setString(9, String.join(";",newRecipe.textStages));
            pstmt.setInt(10, Read(name).id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        new  DBAllProducts().UpdateProdRec(newRecipe, newRecipe.products);
    }

    public void Update(int id, Recipe newRecipe) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "mainInfo = ? ,"
                + "category = ? ,"
                + "timeCooking = ? ,"
                + "mainImageLink = ? ,"
                + "rating = ? ,"
                + "ingredientsMass = ? ,"
                + "imagesStageLinks = ? ,"
                + "textStages = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, newRecipe.name);
            pstmt.setString(2, newRecipe.mainInfo);
            pstmt.setString(3, newRecipe.category);
            pstmt.setInt(4, newRecipe.timeCooking);
            pstmt.setString(5, newRecipe.mainImageLink);
            pstmt.setFloat(6, newRecipe.getRating());
            pstmt.setString(7, String.join(";", newRecipe.ingredientsMass));
            pstmt.setString(8, String.join(";",newRecipe.imagesStageLinks));
            pstmt.setString(9, String.join(";",newRecipe.textStages));
            pstmt.setInt(10, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        new DBAllProducts().UpdateProdRec(newRecipe, newRecipe.products);
    }

}
