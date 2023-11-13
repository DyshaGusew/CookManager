package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBRecipeStages{
    private Connection conn = null;
    static private String nameTable = "RecipeStages";
    //static public String nameTableControl = "ProductsRecipes";
    private Connection connect() {
        String url = "jdbc:sqlite:DB.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    //Получить все описания этапов рецепта
    public List<String> ReadAllTextStagesOfRecipe(int idRecipe){
        String sql = "SELECT * FROM " + nameTable + " WHERE idRecipe = " + idRecipe + " ORDER BY numStage";
        List<String> stagesText = new ArrayList<String>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                stagesText.add(resultSet.getString("descriptionText"));
            }

            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            stagesText = null;
        }

        //Collections.reverse(stagesText);
        return stagesText;
    }

    //Получить все изображения этапов рецепта
    public List<String> ReadAllImageStagesOfRecipe(int idRecipe){
        String sql = "SELECT * FROM " + nameTable + " WHERE idRecipe = " + idRecipe + " ORDER BY numStage";
        List<String> imagesLink = new ArrayList<String>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);


            while (resultSet.next()) {
                imagesLink.add(resultSet.getString("imageLink"));
            }

            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            imagesLink = null;
        }
        //Collections.reverse(imagesLink);
        return imagesLink;
    }

    //Запись в БД связей
    public void WriteStagesRecipe(List<String> stagesText, List<String> imagesLink, int idRecipe) {
        String sql = "INSERT INTO " + nameTable + "(idRecipe, numStage, imageLink, descriptionText) VALUES(?,?,?,?)";

        int i = 0;
        for(String textStage : stagesText){
            try{
                Connection conn = this.connect();
                PreparedStatement prepStat = conn.prepareStatement(sql);
                prepStat.setInt(1, idRecipe);
                prepStat.setInt(2, i+1);
                prepStat.setString(3, imagesLink.get(i));
                prepStat.setString(4, textStage);
                prepStat.executeUpdate();
                i++;

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void WriteStagesRecipe(String nameRecipe, List<String> stagesText, List<String> imagesLink) {
        String sql = "INSERT INTO " + nameTable + "(idRecipe, numStage, imageLink, descriptionText) VALUES(?,?,?,?)";

        int i = 0;
        for(String textStage : stagesText){
            try{
                Connection conn = this.connect();
                PreparedStatement prepStat = conn.prepareStatement(sql);
                prepStat.setInt(1, new DBAllRecipes().Read(nameRecipe).id);
                prepStat.setInt(2, i+1);
                prepStat.setString(3, imagesLink.get(i));
                prepStat.setString(4, textStage);
                prepStat.executeUpdate();
                i++;

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void DeleteStageRec(int idRec) {
        String sql = "DELETE FROM " + nameTable + " WHERE idRecipe = ?";
        try {
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setObject(1, idRec);
            // Выполняем запрос
            prepStat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateStagesRec(String nameRecipe, List<String> stagesText, List<String> imagesLink) {
        DeleteStageRec(new DBAllRecipes().Read(nameRecipe).id);
        WriteStagesRecipe(nameRecipe, stagesText, imagesLink);
    }

    public void UpdateStagesRec(int idRecipe, List<String> stagesText, List<String> imagesLink) {
        DeleteStageRec(idRecipe);
        WriteStagesRecipe(stagesText, imagesLink, idRecipe);
    }
}
