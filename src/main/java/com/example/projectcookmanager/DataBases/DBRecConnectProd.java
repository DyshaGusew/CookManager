package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBRecConnectProd {
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

    //Получить все продукты рецепта
    public List<Product> ReadAllOfRecipe(int idRecipe){
        String sql = "SELECT * FROM " + nameTableControl + " WHERE idRecipe = " + idRecipe;
        List<Integer> productsId = new ArrayList<Integer>();
        List<Float> productsMass = new ArrayList<Float>();
        List<Product> products = new ArrayList<Product>();

        try {
            Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                productsId.add(resultSet.getInt("idProduct"));
                productsMass.add(resultSet.getFloat("mass"));
            }

            if (resultSet != null) resultSet.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            productsId = null;
        }

        int Id = 0;
        for(int id_ : productsId){
            ProductPattern productPattern = new DBAllProducts().Read(id_);
            Product product = new Product(productPattern.id, productPattern.name, productPattern.getProtein(), productPattern.getFat(), productPattern.getCarbohydrate(), productsMass.get(Id));
            products.add(product);
            Id ++;
        }

        return products;
    }

    //Запись в БД связей
    public void WriteProductRecipe(List<Product> products, String nameRecipes) {
        String sql = "INSERT INTO " + nameTableControl + "(idRecipe, idProduct, mass) VALUES(?,?,?)";

        for(Product product : products){
            try{
                Connection conn = this.connect();
                PreparedStatement prepStat = conn.prepareStatement(sql);
                prepStat.setInt(1, new DBAllRecipes().Read(nameRecipes).id);
                prepStat.setInt(2, product.id);
                prepStat.setFloat(3, product.getMass());
                prepStat.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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

    public void UpdateProdRec(Recipe newRec, List<Product> product) {
        DeleteProdRec(new DBAllRecipes().Read(newRec.name).id);
        WriteProductRecipe(product, newRec.name);
    }
}
