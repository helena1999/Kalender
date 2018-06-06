package com.example.helena.kalender;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {



    ListView eventlist;
    DbEvents dbEvents;
    DbEvents.DbHelper dbHelper;
    EventRowAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIdent = getIntent();
        Boolean islogin = loginIdent.getBooleanExtra("login", false);

        if (!islogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String todayDate = dateFormat.format(date);
        getSupportActionBar().setTitle(todayDate);

        final String loginName = loginIdent.getStringExtra("google-login-name");
        if (loginName != null) {
            getSupportActionBar().setSubtitle(loginName);
        }

        dbEvents = new DbEvents(this);
        showEventsList();


    }

    public void showEventsList() {

        Cursor cursor = null;
        try {
            dbEvents.open();
            cursor = dbEvents.show();
        }
        catch(Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Error on opening database", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        String[] dataList = new String[]{dbHelper.NAME, dbHelper.LOCATION, dbHelper.START_DATETIME, dbHelper.ID};
        int [] destination = new int[]{R.id.event_item_name, R.id.event_item_location, R.id.event_item_time, 0};

        adapter = new EventRowAdapter(MainActivity.this,
                R.layout.customlayout, cursor, dataList, destination);

        adapter.notifyDataSetChanged();


        eventlist = findViewById(R.id.event_list);
        eventlist.setAdapter(adapter);

    }

    public void deleteEvent(int eventId){
        Log.v("Kalender", "kustutan kirje" + eventId);
        dbEvents.open();
        dbEvents.delete(Long.valueOf(eventId));
        showEventsList();

    }

    public void viewEvent(int eventId){
        Log.v("Kalender", "kuvan sündmuse info" + eventId);
        dbEvents.open();
        final Cursor event = dbEvents.getById(Long.valueOf(eventId));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(event.getString(1));
        builder.setMessage("Start time: " + event.getString(4) + "\nEnd time: " + event.getString(5) +  "\n" + "Location: " + event.getString(2) + "\n\n" + event.getString(3))
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                        intent.putExtra("id", event.getString(0));
                        intent.putExtra("name", event.getString(1));
                        intent.putExtra("location", event.getString(2));
                        intent.putExtra("description", event.getString(3));
                        intent.putExtra("start_datetime", event.getString(4));
                        intent.putExtra("end_datetime", event.getString(5));
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_addevent:
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }







}
