package com.example.projectcookmanager.Parser;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Parser {
    private static String pathMainImage =  "img/MainImage";
    private static String pathStageImage = "img/StageRecipe";
    public static List<String> noIngrid = new ArrayList<>();
    public static List<Float> noMass = new ArrayList<>();
    public static Recipe RecOfParser(String recipeUrl){
        try {
            // Получаем HTML-страницу рецепта
            Document document = Jsoup.connect(recipeUrl).get();

            // Извлекаем название блюда
            String title = document.selectFirst("h1").text();

            if(new DBAllRecipes().Read(title) != null){
                //Если рецепт уже есть в БД
                return null;
            }
            // Извлекаем категорию блюда
            String category = document.selectFirst("span[itemprop=keywords]").text();
            String[] categ = category.split("/");
            category = categ[0].substring(0, categ[0].length()-1);

            // извлекаем описание блюда
            String info = document.selectFirst("span.detailed_full span").text();

            // Извлекаем ссылку на фото блюда
            String mainImageUrl = document.selectFirst("div.bigImgBox img").attr("src");
            downloadImage(mainImageUrl, pathMainImage);
            String[] elemUrl = mainImageUrl.split("/");
            String mainImageName = (elemUrl[elemUrl.length-1]);

            // Извлекаем время приготовления блюда
            String cookingTime = document.selectFirst("span.duration").text();
            String[] time = cookingTime.split(" ");
            int hours =Integer.parseInt(time[0]);
            int timeCooking;
            if(time.length == 4){
                int min = Integer.parseInt(time[2]);
                timeCooking = min + hours*60;
            }

            else {
                int minutes = Integer.parseInt(time[0]);
                timeCooking = minutes;
            }


            // Извлекаем рейтинг блюда
            float rating = Float.parseFloat(document.selectFirst("span[itemprop=ratingValue]").text());

            // Извлекаем имена ингредиентов
            Elements ingredientElements = document.select("span[class=name]");
            List<String> ingredientsNames = GetNameIngrid(ingredientElements);

            //Извлекаю массы ингридиентов
            Elements massIngredientElements = document.select("span[class=value]");
            List<Float> ingredientsMass = GetListMass(massIngredientElements, ingredientsNames);

            //В зависимости от типа веса устанавливаю массу
            Elements unitIngredientElements = document.select("span[class=u-unit-name]");
            SetNormalMass(unitIngredientElements, ingredientsMass);

            //Получаем список продуктов данного рецепта
            noIngrid = new ArrayList<>();
            noMass = new ArrayList<>();
            List<Product> productList = GetProductOfRecipe(ingredientsNames, ingredientsMass);

            // Извлекаем фотографии этапов приготовления
            Elements stepPhotoElements = document.select("div.detailed_step_photo_big img");
            List<String> stepPhotos = new ArrayList<>();
            for (Element stepPhotoElement : stepPhotoElements) {
                String stepPhotoUrl = stepPhotoElement.attr("src");
                downloadImage(stepPhotoUrl, pathStageImage);

                elemUrl = stepPhotoUrl.split("/");
                stepPhotos.add(elemUrl[elemUrl.length-1]);
            }

            // Извлекаем описание этапов приготовления
            Elements instructionElements = document.select("div.detailed_step_description_big");
            List<String> instructions = new ArrayList<>();
            for (Element instructionElement : instructionElements) {
                String instruction = instructionElement.text();
                instructions.add(instruction);
            }

            Recipe parceOfRecipe = new Recipe(title, info, category, timeCooking, mainImageName, productList, rating, stepPhotos, instructions);


//            // Выводим полученные данные на экран
//            System.out.println("Название: " + title);
//            System.out.println("Категория: " + category);
//            System.out.println("Описание: " + info);
//            System.out.println("Фото: " + mainImageName);
//            System.out.println("Рейтинг: " + rating);
//            System.out.println("Время приготовления: " + timeCooking);
//            System.out.println("Ингредиенты: " + ingredientsNames);
//            System.out.println("Фотографии этапов приготовления: " + stepPhotos);
//            System.out.println("Описание этапов приготовления: " + instructions);
//            System.out.println("Отсутствующие элементы: " + noIngrid);

            return parceOfRecipe;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> GetNameIngrid(Elements ingredientElements){
        List<String> ingredientsNames = new ArrayList<>();

        for (Element ingredientElement : ingredientElements) {
            String ingredient = ingredientElement.text();

            if(!ingredient.equals("Количество порций") && !ingredient.contains(",")){
                ingredientsNames.add(ingredient);
            }

            else {
                if(ingredient.contains(",")){      //Если ингридиенты в строке, но через запятую
                    String[] ingrids = ingredient.split(",");
                    for(int i = 0; i<ingrids.length; i++){
                        if(i == 0){  //Если первый элемент, то ничего не делаем, для остальных делаем верхний регистр первой буквы
                            ingredientsNames.add(ingrids[i]);
                        }
                        else {
                            String thisIngrid = ingrids[i].substring(1);
                            String cap = thisIngrid.substring(0,1).toUpperCase()+thisIngrid.substring(1).toLowerCase();
                            ingredientsNames.add(cap);
                        }

                    }
                }
            }
        }
        return ingredientsNames;
    }

    private static List<Float> GetListMass(Elements massIngredientElements, List<String> ingredientsNames){
        List<Float> ingredientsMass = new ArrayList<>();

        for (Element massElement : massIngredientElements) {
            String thisMass = massElement.text();
            if(thisMass.contains("-")){
                thisMass = thisMass.split("-")[0];
            }
            if(thisMass.contains(",")){
                thisMass = thisMass.replace(',', '.');
            }
            float mass = Float.parseFloat(thisMass);
            ingredientsMass.add(mass);
        }

        while (ingredientsNames.size() != ingredientsMass.size()){
            ingredientsMass.add(0.0f);
        }
        return ingredientsMass;

    }

    private static void SetNormalMass(Elements unitIngredientElements, List<Float> ingredientsMass){
        int i = 0;
        for (Element unitElement : unitIngredientElements) {
            String unit = unitElement.text();
            if(unit.contains("илограм")){
                ingredientsMass.set(i, ingredientsMass.get(i)*1000.0f);
            }
            else if(unit.contains("рам")){
                ingredientsMass.set(i, ingredientsMass.get(i));
            }
            else if(unit.contains("тук")){
                ingredientsMass.set(i, ingredientsMass.get(i)*30.0f);
            }
            else if(unit.contains("убч")){
                ingredientsMass.set(i, ingredientsMass.get(i)*10.0f);
            }
            else if(unit.contains("оловок")){
                ingredientsMass.set(i, ingredientsMass.get(i)*25.0f);
            }
            else if(unit.contains("айн")){
                ingredientsMass.set(i, ingredientsMass.get(i)*5.0f);
            }
            else if(unit.contains("олов")){
                ingredientsMass.set(i, ingredientsMass.get(i)*15.0f);
            }
            else if(unit.contains("акан")){
                ingredientsMass.set(i, ingredientsMass.get(i)*200.0f);
            }
            else {
                ingredientsMass.set(i, 0.0f);
            }
            i++;
        }
    }

    private static List<Product> GetProductOfRecipe(List<String> ingredientsNames, List<Float> ingredientsMass){
        List<Product> productList = new ArrayList<Product>();
        for(int i = 0; i< ingredientsNames.size(); i++){
            ProductPattern productPattern = new DBAllProducts().Read(ingredientsNames.get(i));
            if(productPattern == null){
                noMass.add(ingredientsMass.get(i));
                noIngrid.add(ingredientsNames.get(i));
                continue;
            }
            Product thisProduct = new Product(productPattern, ingredientsMass.get(i));
            productList.add(thisProduct);
        }

        return productList;
    }

    private static void downloadImage(String imageUrl, String destPath) throws IOException{
        URL url = new URL(imageUrl);
        URLConnection conn = url.openConnection();
        try(InputStream in = conn.getInputStream()){
            String[] elemUrl = imageUrl.split("/");
            String ImageName = elemUrl[elemUrl.length-1];

            Path destination = Path.of("src/main/resources", destPath, ImageName);
            Files.createDirectories(destination.getParent());

            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}

