package com.example.contactmanager.db;

import android.icu.text.IDNA;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactmanager.db.entity.Information;

import java.util.List;

@Dao
public interface InformationDAO {


    @Insert
    public long addInfo(Information information);

    @Update
    public void updateInfo(Information information);

    @Delete
    public void deleteInfo(Information information);


    @Query("select * from informations")
    public List<Information> getAllInfo();

    @Query("select * from informations where  info_id ==:infoId")
    public Information getInfo(long infoId);


}
