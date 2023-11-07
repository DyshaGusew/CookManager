package com.example.projectcookmanager.Entity;

import com.example.projectcookmanager.DataBases.DBAllProducts;

import java.util.List;

public class Recipe extends Entity {
    //ИД, имя, основное описание, категория, время готовки, ссылка на главное фото рецепта, калории
    private String mainInfo;
    private String category;
    private int timeCooking;   //В минутах

    //БМЖ всего рецепта на 100 гр
    private float calories;
    private float fat;
    private float protein;
    private float carbohydrate;


    private String mainImageLink;

    //Продукты
    private List<Product> products;

    //Оценка
    private float rating;

    public String getMainInfo() {
        return mainInfo;
    }
    public void setMainInfo(String mainInfo) {
        this.mainInfo = mainInfo;
    }


    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }


    public int getTimeCooking() {
        return timeCooking;
    }
    public void setTimeCooking(int timeCooking) {
        this.timeCooking = timeCooking;
    }





    public float getProtein() {
        return protein;
    }
    public void setProtein(List<Product> products) {
        if(valueAllMass(products) != 0){
            this.protein = valueAspect("protein", products) / ((valueAllMass(products)/100.0f));
        }
        else{
            this.protein = 0;
        }

    }

    public float getCarbohydrate() {
        return carbohydrate;
    }
    public void setCarbohydrate(List<Product> products) {
        if(valueAllMass(products) != 0){
            this.carbohydrate = valueAspect("carbohydrate", products) / ((valueAllMass(products)/100.0f));
        }
        else{
            this.carbohydrate = 0;
        }

    }

    public float getFat() {
        return fat;
    }

    public void setFat(List<Product> products) {
        if(valueAllMass(products) != 0){
            this.fat = valueAspect("fat", products) / ((valueAllMass(products)/100.0f)); //Делю на количество сотен веса
        }
        else{
            this.fat = 0;
        }

    }

    public float getCalories() {
        return calories;
    }
    public void setCalories(float protein, float fat, float carbohydrate) {

            this.calories = (protein * 4.0f) + (fat * 9.0f) + (carbohydrate * 4.0f);

    }

    public String getMainImageLink() {
        return mainImageLink;
    }
    public void setMainImageLink(String mainImageLink) {
        this.mainImageLink = mainImageLink;
    }


    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }


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

    //Массив ссылок на картинки и их описаний (этапы приготовления)
    public String[] imagesStageLinks;
    public String[] textStages;

    public Recipe(int id, String name, String mainInfo, String category, int timeCooking, String mainImageLink, List<Product> products, float rating,
                  String[] imagesStageLinks, String[] textStages){
        this.id = id;
        this.name = name;
        setMainInfo(mainInfo);
        setCategory(category);
        setTimeCooking(timeCooking);
        setMainImageLink(mainImageLink);
        setProducts(products);
        setCarbohydrate(products);
        setFat(products);
        setProtein(products);
        setCalories(getProtein(), getFat(), getCarbohydrate());
        setRating(rating);
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }
    public Recipe(String name, String mainInfo, String category, int timeCooking, String mainImageLink, List<Product> products, float rating,
                  String[] imagesStageLinks, String[] textStages){
        this.name = name;
        setMainInfo(mainInfo);
        setCategory(category);
        setTimeCooking(timeCooking);
        setMainImageLink(mainImageLink);
        setProducts(products);
        setCarbohydrate(products);
        setFat(products);
        setProtein(products);
        setCalories(getProtein(), getFat(), getCarbohydrate());
        setRating(rating);
        this.imagesStageLinks = imagesStageLinks;
        this.textStages = textStages;
    }

    //Вес всего блюда
    static private float valueAllMass(List<Product> products){
        float valueAspect = 0;
        for(Product product : products){
            valueAspect += product.getMass();
            }
        return valueAspect;
    }
    //Подсчет углеводов, белков и жиров в конкретном рецепте
    static private float valueAspect(String nameAspect, List<Product> products){
        float valueAspect = 0;
        for(Product product : products){
            ProductPattern thisProd = new DBAllProducts().Read(product.name);
            switch (nameAspect){
                case ("protein"):
                    valueAspect += thisProd.getProtein() * product.getMass() / 100.0f; //Процент данного аспекта
                    break;
                case ("fat"):
                    valueAspect += thisProd.getFat() * product.getMass() / 100.0f ;
                    break;
                case ("carbohydrate"):
                    valueAspect += thisProd.getCarbohydrate() * product.getMass() / 100.0f;
                    break;
                default: valueAspect += 0;
            }

        }
        return valueAspect;
    }

}
