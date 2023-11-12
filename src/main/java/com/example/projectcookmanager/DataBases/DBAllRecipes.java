package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");
                getRecipe = new  Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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
    public List<Recipe> ReadOfParam(String param, String valueParam){
        String sql = "SELECT * FROM " + nameTable + " WHERE " + param + " LIKE ?";
        List<Recipe> recipes = new ArrayList<Recipe>();

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setString(1, valueParam);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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

    public List<Recipe> ReadOfCategory(String valueCategory){
        return ReadOfParam("category", valueCategory);
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
                String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                String[] textStages = resultSet.getString("textStages").split(";");

                recipes.add(new Recipe(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("mainInfo"),
                        resultSet.getString("category"),
                        resultSet.getInt("timeCooking"),
                        resultSet.getString("mainImageLink"),
                        new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                        resultSet.getFloat("rating"),
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

    //Поиск по имени
    public List<Recipe> ReadOfName(String valueName){

        String sql;// = "SELECT * FROM "+ nameTable +" WHERE name LIKE '%" + valueName + "%'";
        List<Recipe> recipes = new ArrayList<Recipe>();

        valueName = valueName.substring(0,1).toUpperCase() + valueName.substring(1).toLowerCase();

        for(int i = 0; i < 2; i++){
            if(i == 1){
                valueName = valueName.toLowerCase();
            }
            sql = "SELECT * FROM "+ nameTable +" WHERE name LIKE '%" + valueName + "%'";

            try {
                Connection conn = this.connect();
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(sql);

                while (resultSet.next()) {
                    String[] imagesStageLinks = resultSet.getString("imagesStageLinks").split(";");
                    String[] textStages = resultSet.getString("textStages").split(";");

                    boolean flag = false;
                    for (Recipe recipe : recipes){
                        if(recipe.id == resultSet.getInt("id")){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        recipes.add(new Recipe(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("mainInfo"),
                                resultSet.getString("category"),
                                resultSet.getInt("timeCooking"),
                                resultSet.getString("mainImageLink"),
                                new DBRecConnectProd().ReadAllOfRecipe(resultSet.getInt("id")),
                                resultSet.getFloat("rating"),
                                imagesStageLinks,
                                textStages));
                    }

                }
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
                recipes = null;
            }
        }

        return recipes;
    }

    //Поиск по наличию указанного(ых) ингридиента(ов)
    public List<Recipe> ReadOfIngrids(String[] nameIngrids){
        List<Recipe> allRecipes = new  DBAllRecipes().ReadAll();

        List<Recipe> trueRecipes = new ArrayList<Recipe>();
        for (Recipe rec : allRecipes){

            List<Product> allProdsOfRec = new DBRecConnectProd().ReadAllOfRecipe(rec.id);
            boolean ingridsOfRec = true;
            for(int i = 0; i < nameIngrids.length; i++)
            {
                boolean ingridOfRec = false;
                for (Product prod : allProdsOfRec)
                {
                    if(nameIngrids[i].equals(prod.name)){
                        ingridOfRec = true;
                        break;
                    }
                }
                if(ingridOfRec == false){
                    ingridsOfRec = false;
                    break;
                }
            }
            if(ingridsOfRec == true){
                trueRecipes.add(rec);
            }

        }
        return trueRecipes;
    }

    public void Write(Recipe recipe) {
        String sql = "INSERT INTO " + nameTable + "(name, category, mainInfo, calories, timeCooking, mainImageLink, rating, imagesStageLinks, textStages) VALUES(?,?,?,?,?,?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, recipe.name);
            prepStat.setString(2, recipe.getCategory());
            prepStat.setString(3, recipe.getMainInfo());
            prepStat.setFloat(4, recipe.getCalories());
            prepStat.setInt(5, recipe.getTimeCooking());
            prepStat.setString(6, recipe.getMainImageLink());
            prepStat.setFloat(7, recipe.getRating());
            prepStat.setString(8, String.join(";", recipe.imagesStageLinks));
            prepStat.setString(9, String.join(";", recipe.textStages));
            prepStat.executeUpdate();


            new DBRecConnectProd().WriteProductRecipe(recipe.getProducts(),recipe.name);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Delete(String name) {
        String sql = "DELETE FROM " + nameTable + " WHERE name = ?";
        Recipe thisRec = new DBAllRecipes().Read(name);
        new  DBRecConnectProd().DeleteProdRec(thisRec.id);
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
        new  DBRecConnectProd().DeleteProdRec(id);
    }

    public void Update(String name, Recipe newRecipe) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "mainInfo = ? ,"
                + "category = ? ,"
                + "timeCooking = ? ,"
                + "mainImageLink = ? ,"
                + "rating = ? ,"
                + "imagesStageLinks = ? ,"
                + "textStages = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newRecipe.name);
            pstmt.setString(2, newRecipe.getMainInfo());
            pstmt.setString(3, newRecipe.getCategory());
            pstmt.setInt(4, newRecipe.getTimeCooking());
            pstmt.setString(5, newRecipe.getMainImageLink());
            pstmt.setFloat(6, newRecipe.getRating());
            pstmt.setString(7, String.join(";",newRecipe.imagesStageLinks));
            pstmt.setString(8, String.join(";",newRecipe.textStages));
            pstmt.setInt(9, Read(name).id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        new  DBRecConnectProd().UpdateProdRec(newRecipe, newRecipe.getProducts());
    }

    public void Update(int id, Recipe newRecipe) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "mainInfo = ? ,"
                + "category = ? ,"
                + "timeCooking = ? ,"
                + "mainImageLink = ? ,"
                + "rating = ? ,"
                + "imagesStageLinks = ? ,"
                + "textStages = ? "
                + "WHERE id = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, newRecipe.name);
            pstmt.setString(2, newRecipe.getMainInfo());
            pstmt.setString(3, newRecipe.getCategory());
            pstmt.setInt(4, newRecipe.getTimeCooking());
            pstmt.setString(5, newRecipe.getMainImageLink());
            pstmt.setFloat(6, newRecipe.getRating());
            pstmt.setString(7, String.join(";",newRecipe.imagesStageLinks));
            pstmt.setString(8, String.join(";",newRecipe.textStages));
            pstmt.setInt(9, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        new  DBRecConnectProd().UpdateProdRec(newRecipe, newRecipe.getProducts());
    }
}