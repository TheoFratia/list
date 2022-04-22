package com.example.listedecourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.listedecourse.databinding.ActivityListSauvgardeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListSauvgarde extends AppCompatActivity {
    private ActivityListSauvgardeBinding binding;
    private String Ajout;
    private String Nom;
    // Create a List from String Array elements
    List<String> random_list = new ArrayList<String>(Arrays.asList());
    private List sauvgarde;

    // 1 - FILE PURPOSE
    private static final String FILENAME = "tripBook.txt";
    private static final String FOLDERNAME = "bookTrip";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listview_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.supp){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            random_list.remove(menuInfo.position);
            Intent intent = new Intent(ListSauvgarde.this, ListSauvgarde.class);
            startActivity(intent);
            finish();
            return true;
        }if (item.getItemId() == R.id.modif){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            binding.editAjout.setText(random_list.get(menuInfo.position));
            random_list.remove(menuInfo.position);
            Intent intent = new Intent(ListSauvgarde.this, ListSauvgarde.class);
            intent.putExtra("modif", binding.editAjout.getText().toString());
            startActivity(intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSauvgardeBinding.inflate(getLayoutInflater());
        setTheme(R.style.Base_Theme_AppCompat_Light);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ListView ListView = findViewById(R.id.Test);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, random_list);
        ListView.setAdapter(arrayAdapter);
        registerForContextMenu(binding.Test);


        for (int i=0 ; i<=random_list.size();i++){
            try {
                random_list.remove(i);
                i--;
                arrayAdapter.notifyDataSetChanged();
            }catch (Exception e){

            }
        }

        try {
            binding.editAjout.setText(getIntent().getExtras().getString("modif"));

        }catch (Exception e){

        }
        readFromStorage();
        String[] words = sauvgarde.toString().split(",");
        Nom = words[0].replace("[", "").replace("]", "");
        TextView tvnom = findViewById(R.id.Nom);
        tvnom.setText(Nom);
        for ( int i=1; i<words.length; i++){
            if (!words[i].equals("[") && !words[i].equals("]")) {
                String list = words[i].trim();
                random_list.add(list.replace("[", "").replace("]", "").replace("  ", ""));
            }
        }
        arrayAdapter.notifyDataSetChanged();
        Toast.makeText(ListSauvgarde.this, "Votre dernière liste a bien été charger",Toast.LENGTH_SHORT).show();



        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {@Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3){}});



        binding.Ajouter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Ajout = binding.editAjout.getText().toString();
                Ajout = Ajout.replace(",","").trim();
                if(!Ajout.equals("")) {
                    random_list.add(Ajout);
                    arrayAdapter.notifyDataSetChanged();
                    binding.editAjout.setText("");
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });



        binding.Generer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                for (int i=0 ; i<=random_list.size();i++){
                    try {
                        random_list.remove(i);
                        i--;
                        arrayAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(ListSauvgarde.this, "Votre list a bien été réinitialiser",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.Save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.Nom.getText().toString();
                save();
            }
        });

        binding.clock.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.Nom.getText().toString();
                if (!Nom.equals("") && !Nom.equals(" ")){
                    Intent intent = new Intent(ListSauvgarde.this, ClockActivity.class);
                    intent.putExtra("Nom", Nom );
                    startActivity(intent);
                }else {Toast.makeText(ListSauvgarde.this,"Veuillez ne pas laisser le nom vide", Toast.LENGTH_LONG).show();}
            }
        });
    }
    private void update(ArrayAdapter<Object> arrayAdapter){arrayAdapter.notifyDataSetChanged();}
    private void readFromStorage() {
        // 2 - Read from internal storage
        // INTERNAL
        File directory;
        directory = getFilesDir();
        sauvgarde = Collections.singletonList(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));
    }

    private void save() {
        if (!Nom.equals("") && !Nom.equals(" ")) {
            File directory;
            directory = getFilesDir();
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom + "," + random_list.toString());
            Toast.makeText(ListSauvgarde.this, "Sauvegarde réussi", Toast.LENGTH_LONG).show();
        }else {Toast.makeText(ListSauvgarde.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();}
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nom = binding.Nom.getText().toString();
        if (!Nom.equals("") && !Nom.equals(" ")){
            save();
        }else{Toast.makeText(ListSauvgarde.this, "La liste n'a pas pu etre sauvegarder", Toast.LENGTH_LONG).show();}
    }
}