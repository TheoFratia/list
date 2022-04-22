package com.example.listedecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Toast;

import com.example.listedecourse.databinding.ActivityClockBinding;
import com.example.listedecourse.databinding.ActivityListeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClockActivity extends AppCompatActivity {
    private ActivityClockBinding binding;
    private String Nom;
    private int Heure;
    private int Minutes;
    private String Desc;
    private String Lieu;
    private boolean AllDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        setTheme(R.style.Base_Theme_AppCompat_Light);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding.time.setIs24HourView(true);

        Nom = getIntent().getExtras().getString("Nom");
        binding.set.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //récupération des données
                Heure = binding.time.getHour();
                Minutes = binding.time.getMinute();
                Desc = binding.desc.getText().toString();
                Lieu = binding.lieu.getText().toString();

                if (binding.allday.isChecked()){
                    AllDay = true;
                }else {AllDay = false;}

                //Récupération de la date de début avec les heures sous forme du calandrier Gregorian
                GregorianCalendar calDate = new GregorianCalendar(binding.day.getYear(), binding.day.getMonth(), binding.day.getDayOfMonth(), Heure, Minutes);

                //Création de l'intent
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);

                //initialisation de l'intent avec les données
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                intent.putExtra(CalendarContract.Events.ALL_DAY, AllDay);
                intent.putExtra(CalendarContract.Events.TITLE, Nom);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, Desc);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, Lieu);

                //démarrer l'activité calendrier
                startActivity(intent);
            }
        });
    }
}