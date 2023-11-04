package com.example.projectcookmanager.DataBases;

import com.example.projectcookmanager.Entity.Entity;

import java.sql.Connection;

public abstract class DataBase {
    private Connection conn;
    static private String nameTable;
    private Connection Connect(){
        return conn;
    };

    //Получение по имени и ID, получение вообще всего
    public abstract Entity Read(int id);
    public abstract Entity Read(String name);

    //Добавление нового рецепта
    public void Write(Entity recipe){};

    //Удаление по имени и ID
  //  public abstract void Delete(String name);
    public abstract void Delete(int id);

    //Обновление по имени и ID
    public void Update(String name, Entity newEntity){};
    public void Update(int id, Entity newEntity){};
}
