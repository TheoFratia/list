package com.example.listedecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedecourse.databinding.ActivityMainBinding;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String Nom;
    private List sauvgarde;

    // 1 - FILE PURPOSE
    private static final String FILENAME = "tripBook.txt";
    private static final String FOLDERNAME = "bookTrip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.Base_Theme_AppCompat_Light);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        binding.valide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.idNom.getText().toString();
                Nom = Nom.replace(",","");
                TextView tvnom = findViewById(R.id.idNom);
                tvnom.setText("");
                if(!Nom.equals("")){
                    Intent intent = new Intent(MainActivity.this, Liste.class);
                    intent.putExtra("Nom", Nom );
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.Read.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                readFromStorage();
                String[] words = sauvgarde.toString().split(",");
                if (!words[0].replace("[", "").replace("]", "").equals("null")) {
                    TextView tvnom = findViewById(R.id.idNom);
                    tvnom.setText("");
                    Intent intent = new Intent(MainActivity.this, ListSauvgarde.class);
                    startActivity(intent);
                }else {Toast.makeText(MainActivity.this,"Vous n'avez pas de liste enregistrée", Toast.LENGTH_LONG).show();}
            }
        });

        binding.Clock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.idNom.getText().toString();
                Nom = Nom.replace(",","");
                if (!Nom.equals("")) {
                    TextView tvnom = findViewById(R.id.idNom);
                    tvnom.setText("");
                    Intent intent = new Intent(MainActivity.this, ClockActivity.class);
                    intent.putExtra("Nom", Nom);
                    startActivity(intent);
                }else {Toast.makeText(MainActivity.this,"Veuillez entrer un nom pour le rappel", Toast.LENGTH_LONG).show();}
            }
        });
    }
    private void readFromStorage() {
        // 2 - Read from internal storage
        // INTERNAL
        File directory;
        directory = getFilesDir();
        sauvgarde = Collections.singletonList(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));
    }
}