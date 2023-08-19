package com.example.weightapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.Calendar;

public class weightEntry extends AppCompatActivity {

    protected EditText dateEntry;
    protected EditText weightEntry;
    protected String isoDate;
    UserModel _user;
    WeightDB _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_entry);

        dateEntry = findViewById(R.id.editWeightDate);
        weightEntry = findViewById(R.id.editWeightWeight);

        _user = UserModel.getUserInstance();
        _db = WeightDB.getInstance(this);

        dateEntry.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        weightEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dateEntry.setText((monthOfYear + 1) + "-" + dayOfMonth  + "-" + year);
                                isoDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" +
                                        String.valueOf(dayOfMonth);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }

        });
    }

    public void openMainForm(View view){
        Intent intent = new Intent(this, main_screen.class);
        startActivity(intent);
    }
    public void onSaveClick(View view){
        String date = dateEntry.getText().toString();
        String sWeight = weightEntry.getText().toString();
        float weight = 0;

        if (!date.equals("") && !sWeight.equals("")){
            weight = Float.valueOf(sWeight);
            WeightsClass entry = new WeightsClass(isoDate, weight);
            Boolean weightAdd = _db.addEntry(entry, _user);


            if(weight <= _user.getGoal()){
                if(_user.isTextPermission()){
                    SMSNotification.sendLongSMS(_user.getSMSText());
                }
            }
        }

        Intent intent = new Intent(this, main_screen.class);
        startActivity(intent);

    }
}
