package com.example.contactmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactmanager.adapter.InfoAdapter;
import com.example.contactmanager.db.AppDatabase;
import com.example.contactmanager.db.entity.Contact;
import com.example.contactmanager.adapter.ContactsAdapter;
import com.example.contactmanager.db.entity.Information;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    // Variables
    private ContactsAdapter contactsAdapter;
    private InfoAdapter infoAdapter;
    private ArrayList<Contact> contactArrayList  = new ArrayList<>();
    private ArrayList<Information> informationArrayList  = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favorite Contacts");


        // RecyclerVIew
        recyclerView = findViewById(R.id.recycler_view_contacts);
        recyclerView2 = findViewById(R.id.recycler_view_info);

        // Callbacks


        // Database
        appDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class,
                        "ContactDB")
//                .addCallback(myCallback)
                .allowMainThreadQueries()
                .build();

        // display in background
        displayAllDataInBackground();

        // Displaying All Contacts List

        contactsAdapter = new ContactsAdapter(this, contactArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        infoAdapter = new InfoAdapter(this, informationArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(infoAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }
        });

        Button btn = (Button) findViewById(R.id.btnRandom);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateInfo(randomNumber());
            }
        });
    }

    public void addAndEditContacts(final boolean isUpdated,final Contact contact,final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact,null);

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alerDialogBuilder.setView(view);


        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add New Contact" : "Edit Contact");


        if (isUpdated && contact != null){
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alerDialogBuilder.setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isUpdated){
                                    DeleteContact(contact, position);
                                }else{
                                    dialogInterface.cancel();
                                }
                            }
                        }
                );

        final AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();

                    return;
                }else{
                    alertDialog.dismiss();
                }

                if (isUpdated && contact != null){
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(),position);

                }else{
                    CreateContact(newContact.getText().toString(), contactEmail.getText().toString());

                }

            }
        });

    }


    private void DeleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        appDatabase.getContactDAO().deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();


    }


    private void UpdateContact(String name, String email, int position){
        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        appDatabase.getContactDAO().updateContact(contact);

        contactArrayList.set(position, contact);
        contactsAdapter.notifyDataSetChanged();


    }


    private void CreateContact(String name, String email){

        long id = appDatabase.getContactDAO()
                .addContact(new Contact(name, email,0));

        Contact contact = appDatabase.getContactDAO().getContact(id);

        if (contact != null){
            contactArrayList.add(0, contact);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    private void CreateInfo(String number){

        long id = appDatabase.getInfoDAO()
                .addInfo(new Information(number,0));

        Information information = appDatabase.getInfoDAO().getInfo(id);

        if (information != null){
            informationArrayList.add(0, information);
            infoAdapter.notifyDataSetChanged();
        }
    }

    public void UpdateInfo(int position){
        Information information = informationArrayList.get(position);

        information.setNumber(randomNumber());

        appDatabase.getInfoDAO().updateInfo(information);

        informationArrayList.set(position, information);
        infoAdapter.notifyDataSetChanged();
    }

    public void DeleteInfo(int position) {
        Information information = informationArrayList.get(position);
        informationArrayList.remove(position);
        appDatabase.getInfoDAO().deleteInfo(information);
        infoAdapter.notifyDataSetChanged();
    }

    private void displayAllDataInBackground() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                // background work
                contactArrayList.addAll(appDatabase.getContactDAO().getContacts());
                informationArrayList.addAll(appDatabase.getInfoDAO().getAllInfo());

                // executed after background work had finished
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.notifyDataSetChanged();
                        infoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    // Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String randomNumber() {
        Random random = new Random();
        int n = random.nextInt(100);
        return String.valueOf(n);
    }

    // Callbacks
    RoomDatabase.Callback  myCallback = new RoomDatabase.Callback() {

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
            CreateContact("John", "john@o2.pl");
            CreateInfo("44");
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

//            CreateContact("John", "john@o2.pl");
//            CreateInfo("44");

            Log.i("TAG", "Create db");
        }
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.i("TAG", "Open db");
        }
    };



}