package com.example.listedecourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedecourse.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String Nom;
    private boolean readable = true;
    private List sauvgarde;
    private int indexFile = 0;
    List<String> random_list = new ArrayList<String>(Arrays.asList());

    // 1 - FILE PURPOSE
    private String FILENAME;
    private static final String FOLDERNAME = "bookTrip";


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listview_main_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.supp){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String nom ="";
            indexFile=0;
            while(!binding.nameListe.getItemAtPosition(menuInfo.position).toString().trim().equals(nom)){
                FILENAME = "tripBook" + String.valueOf(indexFile) + ".txt";
                readFromStorage();
                String[] word = sauvgarde.toString().split(",");
                nom = word[0].replace("[", "").replace("]", "").trim();
                indexFile += 1;
            }
            Nom = "null";
            random_list.remove(menuInfo.position);
            save();
            setvisibility();
            updateAdapter();
            return true;
        }
        return false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.OneTheme);
        getSupportActionBar().hide();
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerForContextMenu(binding.nameListe);

        getlistname();
        setvisibility();
        updateAdapter();

        binding.valide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.idNom.getText().toString();
                Nom = Nom.replace(",","");
                TextView tvnom = findViewById(R.id.idNom);
                if(!Nom.equals("")){
                    if (!random_list.contains(Nom)) {
                        tvnom.setText("");
                        Intent intent = new Intent(MainActivity.this, ListeActivity.class);
                        intent.putExtra("Nom", Nom);
                        startActivity(intent);
                        finish();
                    }else {Toast.makeText(MainActivity.this, "La liste existe déjà", Toast.LENGTH_LONG).show();}
                }else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.saveName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.idNom.getText().toString();
                Nom = Nom.replace(",","");
                TextView tvnom = findViewById(R.id.idNom);
                if(!Nom.equals("")){
                    if (!random_list.contains(Nom)) {
                        random_list.add(Nom);
                        updateAdapter();
                        save();
                        setvisibility();
                        tvnom.setText("");
                    }else {Toast.makeText(MainActivity.this, "La liste existe déjà", Toast.LENGTH_LONG).show();}
                }else {
                    Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.nameListe.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String nom ="";
                indexFile=0;
                while(!binding.nameListe.getItemAtPosition(position).toString().trim().equals(nom)){
                    FILENAME = "tripBook" + String.valueOf(indexFile) + ".txt";
                    readFromStorage();
                    String[] words = sauvgarde.toString().split(",");
                    nom = words[0].replace("[", "").replace("]", "").trim();
                    indexFile += 1;
                }
                Intent intent = new Intent(MainActivity.this, ListSauvgarde.class);
                intent.putExtra("indice", indexFile - 1);
                startActivity(intent);
                finish();
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

    private void updateAdapter (){
        ListView ListView = findViewById(R.id.nameListe);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (MainActivity.this, R.layout.adapter, R.id.text_view, random_list);
        ListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void getlistname(){
        while (readable == true) {
            FILENAME = "tripBook" + String.valueOf(indexFile) + ".txt";
            readFromStorage();
            String[] words = sauvgarde.toString().split(",");
            if (!words[0].replace("[", "").replace("]", "").replace("\n", "").equals("null") && !random_list.contains(words[0].replace("[", "").replace("]", "").replace("\n", ""))) {
                random_list.add(words[0].replace("[", "").replace("]", "").trim());
                indexFile += 1;
            } else {
                FILENAME = "tripBook" + String.valueOf(indexFile + 1) + ".txt";
                readFromStorage();
                words = sauvgarde.toString().split(",");
                if (!words[0].replace("[", "").replace("]", "").replace("\n", "").equals("null") && !random_list.contains(words[0].replace("[", "").replace("]", "").replace("\n", ""))) {
                    random_list.add(words[0].replace("[", "").replace("]", "").trim());
                    indexFile += 2;
                } else {
                    FILENAME = "tripBook" + String.valueOf(indexFile + 2) + ".txt";
                    readFromStorage();
                    words = sauvgarde.toString().split(",");
                    if (!words[0].replace("[", "").replace("]", "").replace("\n", "").equals("null") && !random_list.contains(words[0].replace("[", "").replace("]", "").replace("\n", ""))) {
                        random_list.add(words[0].replace("[", "").replace("]", "").trim());
                        indexFile += 3;
                    } else {
                        FILENAME = "tripBook" + String.valueOf(indexFile + 3) + ".txt";
                        readFromStorage();
                        words = sauvgarde.toString().split(",");
                        if (!words[0].replace("[", "").replace("]", "").replace("\n", "").equals("null") && !random_list.contains(words[0].replace("[", "").replace("]", "").replace("\n", ""))) {
                            random_list.add(words[0].replace("[", "").replace("]", "").trim());
                            indexFile += 4;
                        } else {
                            FILENAME = "tripBook" + String.valueOf(indexFile + 4) + ".txt";
                            readFromStorage();
                            words = sauvgarde.toString().split(",");
                            if (!words[0].replace("[", "").replace("]", "").replace("\n", "").equals("null") && !random_list.contains(words[0].replace("[", "").replace("]", "").replace("\n", ""))) {
                                random_list.add(words[0].replace("[", "").replace("]", "").trim());
                                indexFile += 5;
                            } else {
                                readable = false;
                            }
                        }
                    }
                }
            }
        }
    }


    private void save() {
        if (!Nom.equals("") && !Nom.equals(" ")) {
            File directory;
            directory = getFilesDir();
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom);
        }else {Toast.makeText(MainActivity.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();}
    }

    private void setvisibility(){
        if(!random_list.isEmpty()) {
            binding.vide.setVisibility(View.GONE);
        }else {
            binding.vide.setVisibility(View.VISIBLE);
        }
    }
}