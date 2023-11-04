package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBAllProducts extends DataBase {
    private Connection conn = null;
    static private String nameTable = "AllProducts";
    static public String nameTableControl = "ProductsRecipes";

    private Connection connect() {
        String url = "jdbc:sqlite:DB.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public Product Read(int id){
        String sql = "SELECT * FROM " + nameTable + " WHERE id = ?";
        Product getProduct = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                getProduct = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("protein"),
                        resultSet.getInt("fat"),
                        resultSet.getInt("carbohydrate"));
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getProduct = null;
        }
        if(getProduct == null){
            System.out.println("В базе данных нет id: " + id);
        }

        return getProduct;
    }
    public Product Read(String name) {
        String sql = "SELECT * FROM " + nameTable + " WHERE name = ?";

        Product getProduct = null;

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = this.connect();
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                getProduct = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("protein"),
                        resultSet.getInt("fat"),
                        resultSet.getInt("carbohydrate"));
            }
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            getProduct = null;
        }
        if(getProduct == null){
            System.out.println("В базе данных нет имени: " + name);
        }

        return getProduct;
    }

    //Получить все продукты рецепта
    public List<Product> ReadAllOfRecipe(int id){
        String sql = "SELECT * FROM " + nameTableControl + " WHERE idRecipe = " + id;
        List<Integer> productsId = new ArrayList<Integer>();
        List<Product> products = new ArrayList<Product>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                productsId.add(resultSet.getInt("idProduct"));
            }

            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            productsId = null;
        }

        for(int id_ : productsId){
            products.add(Read(id_));
        }

        return products;
    }

    public void Write(Product product) {
        String sql = "INSERT INTO " + nameTable + "(name, protein, fat, carbohydrate) VALUES(?,?,?,?)";

        try{
            Connection conn = this.connect();
            PreparedStatement prepStat = conn.prepareStatement(sql);
            prepStat.setString(1, product.name);
            prepStat.setInt(2, product.protein);
            prepStat.setInt(3, product.fat);
            prepStat.setInt(4, product.carbohydrate);
            prepStat.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Запись в БД связей
    public void WriteProductRecipe(List<Product> products, String nameRecipes) {
        String sql = "INSERT INTO " + nameTableControl + "(idRecipe, idProduct) VALUES(?,?)";

        for(Product product : products){
            try{
                Connection conn = this.connect();
                PreparedStatement prepStat = conn.prepareStatement(sql);
                prepStat.setInt(1, new DBAllRecipes().Read(nameRecipes).id);
                prepStat.setInt(2, product.id);
                prepStat.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
    public void DeleteProdRec(int idRec) {
        String sql = "DELETE FROM " + nameTableControl + " WHERE idRecipe = ?";
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


    public void Update(String name, Product newProduct) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "protein = ? ,"
                + "fat = ? ,"
                + "carbohydrate = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newProduct.name);
            pstmt.setInt(2, newProduct.protein);
            pstmt.setInt(3, newProduct.fat);
            pstmt.setInt(4, newProduct.carbohydrate);
            pstmt.setInt(5, Read(name).id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void Update(int id, Product newProduct) {
        String sql = "UPDATE " + nameTable + " SET name = ? , "
                + "protein = ? ,"
                + "fat = ? ,"
                + "carbohydrate = ? "
                + "WHERE id = ?";

        try {
            Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, newProduct.name);
            pstmt.setInt(2, newProduct.protein);
            pstmt.setInt(3, newProduct.fat);
            pstmt.setInt(4, newProduct.carbohydrate);
            pstmt.setInt(5, id);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateProdRec(Recipe newRec, List<Product> products) {
        DeleteProdRec(new DBAllRecipes().Read(newRec.name).id);
        WriteProductRecipe(products, newRec.name);
    }


}
