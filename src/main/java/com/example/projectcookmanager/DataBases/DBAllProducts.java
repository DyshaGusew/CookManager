package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//БД для работы со всеми продуктами, просто их список
public class DBAllProducts extends DataBase {
    private Connection conn = null;
    static private String nameTable = "AllProducts";
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

    public ProductPattern Read(int id){
        String sql = "SELECT * FROM " + nameTable + " WHERE id = ?";
        ProductPattern getProductPattern = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                getProductPattern = new ProductPattern(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("protein"),
                        resultSet.getFloat("fat"),
                        resultSet.getFloat("carbohydrate"));
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getProductPattern = null;
        }
        if(getProductPattern == null){
            System.out.println("В базе данных нет id: " + id);
        }

        return getProductPattern;
    }

    public ProductPattern Read(String name) {
        String sql = "SELECT * FROM " + nameTable + " WHERE name = ?";

        ProductPattern getProductPattern = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                getProductPattern = new ProductPattern(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("protein"),
                        resultSet.getFloat("fat"),
                        resultSet.getFloat("carbohydrate"));
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getProductPattern = null;
        }
        if(getProductPattern == null){
            System.out.println("В базе данных нет имени: " + name);
        }

        return getProductPattern;
    }

    public void Write(ProductPattern productPattern) {
        String sql = "INSERT INTO " + nameTable + "(name, protein, fat, carbohydrate) VALUES(?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, productPattern.name);
            prepStat.setFloat(2, productPattern.getProtein());
            prepStat.setFloat(3, productPattern.getFat());
            prepStat.setFloat(4, productPattern.getCarbohydrate());
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

    public void Update(String name, ProductPattern newProductPattern) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "protein = ? ,"
                + "fat = ? ,"
                + "carbohydrate = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newProductPattern.name);
            pstmt.setFloat(2, newProductPattern.getProtein());
            pstmt.setFloat(3, newProductPattern.getFat());
            pstmt.setFloat(4, newProductPattern.getCarbohydrate());
            pstmt.setInt(5, Read(name).id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void Update(int id, ProductPattern newProductPattern) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "protein = ? ,"
                + "fat = ? ,"
                + "carbohydrate = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newProductPattern.name);
            pstmt.setFloat(2, newProductPattern.getProtein());
            pstmt.setFloat(3, newProductPattern.getFat());
            pstmt.setFloat(4, newProductPattern.getCarbohydrate());
            pstmt.setInt(5, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ProductPattern> ReadAll() {
        String sql = "SELECT * FROM " + nameTable;
        List<ProductPattern> allProducts = new ArrayList<>();

        try (Connection conn = this.connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                ProductPattern productPattern = new ProductPattern(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getFloat("protein"),
                        resultSet.getFloat("fat"),
                        resultSet.getFloat("carbohydrate"));

                allProducts.add(productPattern);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allProducts;
    }
}
