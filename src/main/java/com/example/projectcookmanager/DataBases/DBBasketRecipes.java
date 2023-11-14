package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBBasketRecipes{
    private Connection conn = null;
    static private String nameTable = "BasketRecipes";
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

    public List<Recipe> ReadAllOnBasket(){
        String sql = "SELECT * FROM " + nameTable;
        List<Recipe> recipes = new ArrayList<Recipe>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                recipes.add(new DBAllRecipes().Read(resultSet.getInt("idRecipe")));
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

    public void WriteInBasket(Recipe recipe) {
        String sql = "INSERT INTO " + nameTable + "(idRecipe) VALUES(?)";
        boolean flag = false;
        for(Recipe recipe1 : ReadAllOnBasket()){
            if(recipe1.id == recipe.id){
                flag = true;
                break;
            }
        }

        if(!flag){
            try {
                Connection conn = this.connect();
                PreparedStatement prepStat = conn.prepareStatement(sql);
                prepStat.setInt(1, recipe.id);

                prepStat.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

    public void Delete(int id) {
        String sql = "DELETE FROM " + nameTable + " WHERE idRecipe = ?";
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
}
