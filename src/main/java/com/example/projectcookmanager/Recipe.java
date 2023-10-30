package com.example.projectcookmanager;

public class Recipe {
    //Имя, категория, время готовки, ссылка на главное фото рецепта, калории
    public int id;
    public String name;
    public String category;
    public int timeCooking;   //В минутах
    public String mainImageLink;
    public int totalCalories;   //В ккал

    //Белки жиры углеводы          В граммах
    public int protein;
    public int fat;
    public int carbohydrate;

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
    public String[] ingredientsName;
    public String[] ingredientsMass;

    //Массив ссылок на картинки и их описаний (этапы приготовления)
    public String[] imagesStageLinks;
    public String[] textStages;

    public Recipe(int id, String name, String category, int timeCooking, String mainImageLink, int totalCalories, int protein, int fat,
                  int carbohydrate, float rating, String[] ingredientsName, String[] ingredientsMass,
                  String[] imagesStageLinks, String[] textStages){
        this.id = id;
        this.name = name;
        this.category = category;
        this.timeCooking = timeCooking;
        this.mainImageLink = mainImageLink;
        this.totalCalories = totalCalories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        if(rating > 5.0f){
            this.rating = 5.0f;
        }
        else  {
            this.rating = rating;
        }
        this.ingredientsName = ingredientsName;
        this.ingredientsMass = ingredientsMass;
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }
    public Recipe(String name, String category, int timeCooking, String mainImageLink, int totalCalories, int protein, int fat,
                  int carbohydrate, float rating, String[] ingredientsName, String[] ingredientsMass,
                  String[] imagesStageLinks, String[] textStages){
        this.name = name;
        this.category = category;
        this.timeCooking = timeCooking;
        this.mainImageLink = mainImageLink;
        this.totalCalories = totalCalories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        if(rating > 5.0f){
            this.rating = 5.0f;
        }
        else  {
            this.rating = rating;
        }
        this.ingredientsName = ingredientsName;
        this.ingredientsMass = ingredientsMass;
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }
}
