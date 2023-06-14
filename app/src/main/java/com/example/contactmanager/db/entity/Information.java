package com.example.contactmanager.db.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "informations")
public class Information {

    // 2- Variables
    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "info_id")
    @PrimaryKey(autoGenerate = true)
    private int id;





    // 3- Constructors
    @Ignore
    public Information(){

    }

    public Information(String number, int id){
        this.number = number;
        this.id = id;
    }

    // 4- Getters & Setters
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

