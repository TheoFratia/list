package com.example.listedecourse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import static java.lang.Integer.valueOf;

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

import com.example.listedecourse.databinding.ActivityListSauvgardeBinding;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListSauvgarde extends AppCompatActivity {
    private ActivityListSauvgardeBinding binding;
    private String Ajout;
    private String Nom;
    private int Repas;
    private int indexFile;
    private int height = 1000;
    private boolean del = false;
    // Create a List from String Array elements
    List<String> random_list = new ArrayList<String>(Arrays.asList());
    List<String> Save = new ArrayList<String>(Arrays.asList());
    private List sauvgarde;

    private static final DateFormat dateFormat = new SimpleDateFormat("EEEE", new Locale("FR"));
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, Save.toString().replace("[", "").replace("]", "").replace(",","\n").replace("§",",").replace(" ","").trim().trim());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.alarm:
                Nom = binding.Nom.getText().toString();
                if (!Nom.equals("") && !Nom.equals(" ")){
                    save();
                    Intent intent = new Intent(ListSauvgarde.this, ClockActivity.class);
                    intent.putExtra("Nom", Nom );
                    startActivity(intent);
                }else {Toast.makeText(ListSauvgarde.this,"Veuillez ne pas laisser le nom vide", Toast.LENGTH_SHORT).show();}
                return true;

            case R.id.refresh:
                for (int i=0 ; i<=random_list.size();i++){
                    try {
                        random_list.remove(i);
                        Save.remove(i);
                        i--;
                        updateAdapter();
                    }catch (Exception e){
                        Toast.makeText(ListSauvgarde.this, "Votre list a bien été réinitialiser",Toast.LENGTH_SHORT).show();
                        setvisibility();
                    }
                }
                return true;

            case R.id.delet:
                Nom = "null";
                binding.Nom.setText(Nom);
                del = true;
                Intent intent = new Intent(ListSauvgarde.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.meal:
                Intent intentMeal = new Intent(ListSauvgarde.this, SelectMealActivity.class);
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
            random_list.remove(menuInfo.position);
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

        binding = ActivityListSauvgardeBinding.inflate(getLayoutInflater());
        setTheme(R.style.OneTheme);
        getSupportActionBar().setTitle("");
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerForContextMenu(binding.Test);
        binding.Test.setScrollBarFadeDuration(15);
        binding.Test.setScrollBarSize(30);

        indexFile = getIntent().getExtras().getInt("indice");
        FILENAME += String.valueOf(indexFile) + ".txt";


        for (int i=0 ; i<=random_list.size();i++){
            try {
                random_list.remove(i);
                i--;
            }catch (Exception e){

            }
        }

        readFromStorage();
        String[] words = sauvgarde.toString().split(",");
        Nom = words[0].replace("[", "").replace("]", "");
        try {Repas = valueOf(words[1].replace("[", "").replace("]", ""));}catch (Exception e){}
        TextView tvnom = findViewById(R.id.Nom);
        tvnom.setText(Nom);
        for ( int i=2; i<words.length; i++){
            String list = words[i].trim();
            list = list.replace("[", "").replace("]", "").replace("  ", "");
            if (!words[i].equals("[") && !words[i].equals("]") && !list.equals("\n\n") && !list.equals("\n")) {
                list = list.replace("\n","");
                Save.add(list);
                random_list.add(list.replace("§", ","));
            }
        }
        try {
            String[] word = getIntent().getExtras().getStringArray("Repas");
            for ( int i=0; i<word.length; i++){
                String list = word[i].trim();
                list = list.replace("[", "").replace("]", "").replace("  ", "");
                if (!word[i].equals("[") && !words[i].equals("]") && !list.equals("\n\n") && !list.equals("\n")) {
                    list = list.replace("\n","");
                    Save.add(list);
                    random_list.add(list.replace("§", ","));
                }
            }
            Repas += 1;
        }catch (Exception e){}
        updateAdapter();
        setvisibility();
        if (Repas != 0){binding.countRepas.setText(GetDateMeal() +"("+Repas+")");}
        else {binding.RepasVisibility.setVisibility(View.GONE);}

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
                Ajout = Ajout.replace(",","").trim();
                if(!Ajout.equals("")) {
                    random_list.add(Ajout);
                    Save.add(Ajout.replace(",","§").trim());
                    binding.editAjout.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    binding.editAjout.clearFocus();
                }
                updateAdapter();
                setvisibility();
            }
        });

        binding.countRepas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Repas -= 1;
                if (Repas != 0){binding.countRepas.setText(GetDateMeal() +"("+Repas+")");}
                else {binding.countRepas.setVisibility(View.GONE);
                    binding.RepasVisibility.setVisibility(View.GONE);}
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

    private void save() {
        if (!Nom.equals("") && !Nom.equals(" ")) {
            File directory;
            directory = getFilesDir();
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom + "," + Repas + "," + Save.toString());
        }else {Toast.makeText(ListSauvgarde.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_SHORT).show();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ListSauvgarde.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Nom = binding.Nom.getText().toString();
        if (!Nom.equals("") && !Nom.equals(" ")){
            save();
        }else{Toast.makeText(ListSauvgarde.this, "La liste n'a pas pu etre sauvegarder", Toast.LENGTH_LONG).show();}
    }

    private void updateAdapter (){
        ListView ListView = findViewById(R.id.Test);
        LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, height);
        ListView.setLayoutParams(mParam);
        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (ListSauvgarde.this, R.layout.adapter, R.id.text_view, random_list);
        ListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
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



        public String GetDateMeal() {

            Date currentDate = new Date();
            // convert date to calendar
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);

            // manipulate date
            c.add(Calendar.DAY_OF_MONTH, Repas);
            // convert calendar to date
            Date currentDatePlusOne = c.getTime();
            String result = dateFormat.format(currentDatePlusOne);
            return result;
        }
}