package com.example.contactmanager.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contactmanager.db.entity.Contact;
import com.example.contactmanager.db.entity.Information;

@Database(entities = {Contact.class, Information.class},version = 2)
public abstract class ContactsAppDatabase extends RoomDatabase {

    // Linking the DAO with our Database
    public abstract ContactDAO getContactDAO();

    public abstract InformationDAO getInfoDAO();

}
