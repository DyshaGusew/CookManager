package com.example.projectcookmanager;

import java.util.List;

public class Recipe extends Entity {
    //ИД, имя, основное описание, категория, время готовки, ссылка на главное фото рецепта, калории
    public int id;
    public String name;
    public String mainInfo;
    public String category;
    public int timeCooking;   //В минутах
    public String mainImageLink;

    //Продукты
    public List<Product> products;

    //Оценка
    private float rating;

    public float getRating(){
        return rating;
    }

    public void setRating(float rating){
        if(rating > 5.0f){
            this.rating = 5.0f;
        }
        else  {
            this.rating = rating;
        }
    }

    //Массивы названий ингридиентов и их количества
    public String[] ingredientsMass;

    //Массив ссылок на картинки и их описаний (этапы приготовления)
    public String[] imagesStageLinks;
    public String[] textStages;

    public Recipe(int id, String name, String mainInfo, String category, int timeCooking, String mainImageLink, List<Product> products, float rating, String[] ingredientsMass,
                  String[] imagesStageLinks, String[] textStages){
        this.id = id;
        this.name = name;
        this.mainInfo = mainInfo;
        this.category = category;
        this.timeCooking = timeCooking;
        this.mainImageLink = mainImageLink;
        this.products = products;
        if(rating > 5.0f){
            this.rating = 5.0f;
        }
        else  {
            this.rating = rating;
        }
        this.ingredientsMass = ingredientsMass;
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }
    public Recipe(String name, String mainInfo, String category, int timeCooking, String mainImageLink, List<Product> products, float rating, String[] ingredientsMass,
                  String[] imagesStageLinks, String[] textStages){
        this.name = name;
        this.mainInfo = mainInfo;
        this.category = category;
        this.timeCooking = timeCooking;
        this.mainImageLink = mainImageLink;
        this.products = products;
        if(rating > 5.0f){
            this.rating = 5.0f;
        }
        else  {
            this.rating = rating;
        }
        this.ingredientsMass = ingredientsMass;
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }
}
