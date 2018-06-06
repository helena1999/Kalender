package com.example.helena.kalender;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    EditText end_time, begin_time, start_time_clock, end_time_clock;
    DatePickerDialog start_event_date_dialog;
    DatePickerDialog end_event_date_dialog;
    DbEvents dbEvents = new DbEvents(this);
    Switch allday;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        begin_time = findViewById(R.id.begin_time);
        end_time = findViewById(R.id.end_time);
        start_time_clock = findViewById(R.id.start_time_clock);
        end_time_clock = findViewById(R.id.end_time_clock);
        allday = findViewById(R.id.switch_allday);

        start_time_clock.setInputType(InputType.TYPE_NULL);
        end_time_clock.setInputType(InputType.TYPE_NULL);
        begin_time.setInputType(InputType.TYPE_NULL);
        end_time.setInputType(InputType.TYPE_NULL);

        Intent changeIdent = getIntent();
        EditText name = findViewById(R.id.event);
        name.setText(changeIdent.getStringExtra("name"));

        EditText location = findViewById(R.id.location);
        location.setText(changeIdent.getStringExtra("location"));

        EditText description = findViewById(R.id.description);
        description.setText(changeIdent.getStringExtra("description"));

        final String eventId = changeIdent.getStringExtra("id");

        if (eventId != null) {
            begin_time.setText(changeIdent.getStringExtra("start_datetime").substring(0, 10));
            end_time.setText(changeIdent.getStringExtra("end_datetime").substring(0, 10));
            start_time_clock.setText(changeIdent.getStringExtra("start_datetime").substring(12, 16));
            end_time_clock.setText(changeIdent.getStringExtra("end_datetime").substring(12, 16));
        }

        begin_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                start_event_date_dialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        begin_time.setText(String.format("%04d-%02d-%02d", year, monthOfYear, dayOfMonth));
                        if(allday.isChecked()) {
                            end_time.setText(String.format("%04d-%02d-%02d", year, monthOfYear, dayOfMonth));
                        }

                    }
                }, year, month, day);

                start_event_date_dialog.show();

            }

        });

        start_time_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        start_time_clock.setText(String .format("%02d:%02d", hourOfDay, minutes));

                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                end_event_date_dialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        end_time.setText(String.format("%04d-%02d-%02d", year, monthOfYear, dayOfMonth));
                    }
                }, year, month, day);

                end_event_date_dialog.show();

            }

        });

        end_time_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        end_time_clock.setText(String .format("%02d:%02d", hourOfDay, minutes));

                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });

        allday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(allday.isChecked()) {
                    start_time_clock.setText("00:00");
                    end_time_clock.setText("23:59");
                    end_time.setEnabled(false);
                } else {
                    end_time.setEnabled(true);
                }

            }
        });



        Button save_btn = findViewById(R.id.btn_save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText eventname = findViewById(R.id.event);
                EditText location = findViewById(R.id.location);
                EditText description = findViewById(R.id.description);
                EditText start_datetime = findViewById(R.id.begin_time);
                EditText end_datetime = findViewById(R.id.end_time);

                Boolean isRequiredFielsAvailable = true;

                if (eventname.getText().toString().trim().length() == 0) {
                    isRequiredFielsAvailable = false;
                }
                if (location.getText().toString().trim().length() == 0) {
                    isRequiredFielsAvailable = false;
                }

                if (!isRequiredFielsAvailable) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Fill required fields", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    try {
                        String startdatetime = start_datetime.getText().toString() + " " + start_time_clock.getText().toString() + ":00";
                        String enddatetime = end_datetime.getText().toString() + " " + end_time_clock.getText().toString() + ":00";

                        Log.v("CALENDAR", eventname.getText().toString() + " " + location.getText().toString() + " " + description.getText().toString() + " " + startdatetime + " " + enddatetime);
                        dbEvents.open();

                        if (eventId != null){
                            dbEvents.update(Long.valueOf(eventId), eventname.getText().toString(), location.getText().toString(), description.getText().toString(), startdatetime, enddatetime);
                        }else {
                            dbEvents.insert(eventname.getText().toString(), location.getText().toString(), description.getText().toString(), startdatetime, enddatetime);
                        }


                        Toast toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG);
                        toast.show();
                        eventname.setText("");
                        start_time_clock.setText("");
                        end_time_clock.setText("");
                        location.setText("");
                        description.setText("");
                        start_datetime.setText("");
                        end_datetime.setText("");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("login", true);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e)
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error on saving to database", Toast.LENGTH_SHORT);
                        toast.show();
                        e.printStackTrace();

                    }
                }

            }



        });

    }

}
