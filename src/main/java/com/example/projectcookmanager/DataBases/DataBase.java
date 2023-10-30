package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Recipe;

import java.sql.Connection;
import java.util.List;

public abstract class DataBase {
    private Connection conn;
    static private String nameTable;
    private Connection Connect(){
        return conn;
    };

    //Получение по имени и ID, получение вообще всего
    public abstract Recipe Select(int id);
    public abstract Recipe Select(String name);

    public abstract List<Recipe> SelectAll();

    //Добавление нового рецепта
    public abstract void Insert(Recipe recipe);

    //Удаление по имени и ID
    public abstract void Delete(Recipe recipe);
    public abstract void Delete(int id);

    //Обновление по имени и ID
    public abstract void Update(String name, Recipe newRecipe);
    public abstract void Update(int id, Recipe newRecipe);
}
