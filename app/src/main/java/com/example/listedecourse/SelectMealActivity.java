package com.example.listedecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.example.listedecourse.databinding.ActivitySelectMealBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectMealActivity extends AppCompatActivity {
    private ActivitySelectMealBinding binding;

    private int Index;
    List<String> random_list = new ArrayList<String>(Arrays.asList());
    private String[] Tomate = {"Tomate", "Mozzarella"};
    private String[] Feuilleté = {"Pate feuilleté", "Jambon", "Crème épaisse", "Frommage rapé"};
    private String[] Quenelle = {"Quenelle", "Saint-Agure", "Frommage rapé"};
    private String[] Pizza = {"Pâte a pizza", "Crème épaisse", "Fromage Rapé"};
    private String[] Burger = {"Pain à burger", "Steack", "Cheddar"};
    private String[] Riz = {"Riz", "Sauce tomate", "Knacki"};
    private String[] courgette = {"Courgette", "Mozza"};
    private String[] HotDog = {"Pain a hot dog", "Knacki", "Fromage rapé"};
    private String[] Croque = {"Pain", "Fromage rapé"};
    private String[] Endive = {"Endive", "Emental", "Pomme"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectMealBinding.inflate(getLayoutInflater());
        setTheme(R.style.OneTheme);
        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Index = getIntent().getExtras().getInt("indice");
        addMealName();
        updateAdapter();

        binding.Done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String SelectSpinner = binding.Spinner.getSelectedItem().toString().trim();
                if (SelectSpinner.equals("Tomate")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Tomate);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Feuilleté au jambon")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Feuilleté);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Quenelle au saint-Agure")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Quenelle);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Pizza")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Pizza);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Burger")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Burger);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Riz au petite saucisse")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Riz);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("courgette chorizo")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",courgette);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Hot Dog")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",HotDog);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Croque Monsieur")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Croque);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }if (SelectSpinner.equals("Endive")){
                    Intent intentTomate = new Intent(SelectMealActivity.this, ListSauvgarde.class);
                    intentTomate.putExtra("Repas",Endive);
                    intentTomate.putExtra("indice", Index);
                    startActivity(intentTomate);

                }

            }
        });

    }

    private void addMealIngredient(){
    }

    private void addMealName(){
        random_list.add("-- Choisissez une recette --");
        random_list.add("Tomate");
        random_list.add("Feuilleté au jambon");
        random_list.add("Quenelle au saint-Agure");
        random_list.add("Pizza");
        random_list.add("Burger");
        random_list.add("Riz au petite saucisse");
        random_list.add("courgette chorizo");
        random_list.add("Hot Dog");
        random_list.add("Croque Monsieur");
        random_list.add("Endive");
    }

    private void updateAdapter (){

        // Create an ArrayAdapter from List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (SelectMealActivity.this, R.layout.adaptermeal, R.id.text_view, random_list);
        binding.Spinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

}