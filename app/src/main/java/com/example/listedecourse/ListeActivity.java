package com.example.listedecourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listedecourse.databinding.ActivityListeBinding;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

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
    private boolean del = false;
    private int indexFile = 0;
    private int height = 1000;
    private List sauvgarde;
    // Create a List from String Array elements
    List<String> random_list = new ArrayList<String>(Arrays.asList());
    List<String> Save = new ArrayList<String>(Arrays.asList());

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
                sendIntent.putExtra(Intent.EXTRA_TEXT, Save.toString().replace("[", "").replace("]", "").replace(",","\n").replace("§",",").replace(" ","").trim());
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
                        Save.remove(i);
                        i--;
                        updateAdapter();
                    }catch (Exception e){
                        setvisibility();
                    }
                }
                return true;
            case R.id.delet:
                Nom = "null";
                binding.Nom.setText(Nom);
                del = true;
                Intent intent = new Intent(ListeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.meal:
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
                Intent intentMeal = new Intent(ListeActivity.this, SelectMealActivity.class);
                intentMeal.putExtra("indice", indexFile);
                startActivity(intentMeal);
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
            Save.remove(menuInfo.position);
            updateAdapter();
            setvisibility();
            return true;
        }if (item.getItemId() == R.id.modif){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            binding.editAjout.setText(random_list.get(menuInfo.position).trim());
            random_list.remove(menuInfo.position);
            Save.remove(menuInfo.position);
            updateAdapter();
            setvisibility();
            return true;
        }if (item.getItemId() == R.id.cop){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(random_list.get(menuInfo.position), random_list.get(menuInfo.position));
            clipboard.setPrimaryClip(clip);
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerForContextMenu(binding.Test);
        binding.Test.setScrollBarFadeDuration(15);
        binding.Test.setScrollBarSize(30);
        binding.Test.setDividerHeight(5);

        /* End Creat View */


        Nom = getIntent().getExtras().getString("Nom");
        TextView tvnom = findViewById(R.id.Nom);
        tvnom.setText(Nom);

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {

                if (isOpen){
                    height = 380;
                    binding.space.setVisibility(View.GONE);
                    updateAdapter();
                }
                else{
                    height = 1000;
                    binding.space.setVisibility(View.VISIBLE);
                    updateAdapter();
                }
            }
        });


        binding.Ajouter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Ajout = binding.editAjout.getText().toString();
                Ajout = Ajout.trim();
                if(!Ajout.equals("")) {
                    random_list.add(Ajout);
                    Save.add(Ajout.replace(",","§"));
                    binding.editAjout.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    binding.editAjout.clearFocus();
                }
                setvisibility();
            }
        });
    }

    private void updateAdapter (){
        ListView ListView = findViewById(R.id.Test);
        LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, height);
        ListView.setLayoutParams(mParam);
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
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom + "," + "0," + Save.toString());
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
