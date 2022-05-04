package com.example.listedecourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedecourse.databinding.ActivityListeBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListeActivity extends AppCompatActivity {
    private ActivityListeBinding binding;
    private String Ajout;
    private String Nom;
    private boolean readable = false;
    private int indexFile = 0;
    private List sauvgarde;
    // Create a List from String Array elements
    List<String> random_list = new ArrayList<String>(Arrays.asList());

    // 1 - FILE PURPOSE
    private String FILENAME = "tripBook";
    private static final String FOLDERNAME = "bookTrip";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarlist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, random_list.toString().replace("[", "").replace("]", "").replace(",","\n").replace(" ","").trim());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.alarm:
                Nom = binding.Nom.getText().toString();
                if (!Nom.equals("") && !Nom.equals(" ")){
                    save();
                    Intent intent = new Intent(ListeActivity.this, ClockActivity.class);
                    intent.putExtra("Nom", Nom );
                    startActivity(intent);
                }else {Toast.makeText(ListeActivity.this,"Veuillez ne pas laisser le nom vide", Toast.LENGTH_SHORT).show();}
                return true;

            case R.id.refresh:
                for (int i=0 ; i<=random_list.size();i++){
                    try {
                        random_list.remove(i);
                        i--;
                        updateAdapter();
                    }catch (Exception e){
                        Toast.makeText(ListeActivity.this, "Votre list a bien été réinitialiser",Toast.LENGTH_SHORT).show();
                        setvisibility();
                    }
                }
                return true;
        }
        return false;
    }

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
            updateAdapter();
            setvisibility();
            return true;
        }if (item.getItemId() == R.id.modif){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            binding.editAjout.setText(random_list.get(menuInfo.position).trim());
            random_list.remove(menuInfo.position);
            updateAdapter();
            setvisibility();
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Section Creat View */

        binding = ActivityListeBinding.inflate(getLayoutInflater());
        setTheme(R.style.OneTheme);
        getSupportActionBar().setTitle("");
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerForContextMenu(binding.Test);

        /* End Creat View */


        Nom = getIntent().getExtras().getString("Nom");
        TextView tvnom = findViewById(R.id.Nom);
        tvnom.setText(Nom);

        binding.Ajouter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Ajout = binding.editAjout.getText().toString();
                Ajout = Ajout.replace(",","").trim();
                if(!Ajout.equals("")) {
                    random_list.add(Ajout);
                    updateAdapter();
                    binding.editAjout.setText("");
                }
                setvisibility();
            }
        });
    }

    private void updateAdapter (){
        ListView ListView = findViewById(R.id.Test);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (ListeActivity.this, R.layout.adapter, R.id.text_view, random_list);
        ListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    private void save() {
        Nom = binding.Nom.getText().toString();
        if (!Nom.equals("") && !Nom.equals(" ")) {
            File directory;
            directory = getFilesDir();
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom + "," + random_list.toString());
            Toast.makeText(ListeActivity.this, "Sauvegarde réussi", Toast.LENGTH_LONG).show();
        }else {Toast.makeText(ListeActivity.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ListeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setvisibility(){
        if(!random_list.isEmpty()) {
            binding.vide.setVisibility(View.GONE);
            binding.Test.setVisibility(View.VISIBLE);
        }else {
            binding.vide.setVisibility(View.VISIBLE);
            binding.Test.setVisibility(View.GONE);
        }
    }

    private void readFromStorage() {
        // 2 - Read from internal storage
        // INTERNAL
        File directory;
        directory = getFilesDir();
        sauvgarde = Collections.singletonList(StorageUtils.getTextFromStorage(directory, this, FILENAME, FOLDERNAME));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nom = binding.Nom.getText().toString();
        if (!Nom.equals("") && !Nom.equals(" ")){
            Nom = binding.Nom.getText().toString();
            while(readable == false){
                FILENAME = "tripBook" + String.valueOf(indexFile) + ".txt";
                readFromStorage();
                String[] words = sauvgarde.toString().split(",");
                if (!words[0].replace("[", "").replace("]", "").equals("null")) {indexFile += 1;}
                else {
                    FILENAME = "tripBook" + String.valueOf(indexFile) + ".txt";
                    readable = true;
                }
            }
            save();
            Toast.makeText(ListeActivity.this,"Sauvegarde réussi", Toast.LENGTH_LONG).show();
        }else{Toast.makeText(ListeActivity.this, "La liste n'a pas pu etre sauvegarder", Toast.LENGTH_LONG).show();}
    }
}
