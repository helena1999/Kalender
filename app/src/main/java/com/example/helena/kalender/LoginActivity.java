package com.example.helena.kalender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button button_noauth, button_google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button_noauth = findViewById(R.id.button_noauth);
        button_noauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("login", true);
                startActivity(intent);
                finish();
            }
        });

        button_google = findViewById(R.id.button_google);
        button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), GoogleServices.class);
                startActivity(intent);

                // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("login", true);
                //startActivity(intent);
                //finish();
            }

        });

    }

}





