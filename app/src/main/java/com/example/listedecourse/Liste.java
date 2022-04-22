package com.example.listedecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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

public class Liste extends AppCompatActivity {
    private ActivityListeBinding binding;
    private String Ajout;
    private String Nom;
    // Create a List from String Array elements
    List<String> random_list = new ArrayList<String>(Arrays.asList());

    // 1 - FILE PURPOSE
    private static final String FILENAME = "tripBook.txt";
    private static final String FOLDERNAME = "bookTrip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListeBinding.inflate(getLayoutInflater());
        setTheme(R.style.Base_Theme_AppCompat_Light);
        View view = binding.getRoot();
        setContentView(view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Nom = getIntent().getExtras().getString("Nom");
        TextView tvnom = findViewById(R.id.Nom);
        tvnom.setText(Nom);


        ListView ListView = findViewById(R.id.Test);
        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, random_list);

        ListView.setAdapter(arrayAdapter);
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
            }
        });


        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                android.app.AlertDialog.Builder builder = new android.app
                        .AlertDialog.Builder(Liste.this,R.style.AlertDialog);
                builder.setTitle("Confirm");
                builder.setMessage("Etes vous sûr de vouloir supprimer cet élément?");
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        random_list.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Modifier", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        binding.editAjout.setText(random_list.get(position));
                        random_list.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
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
                        Toast.makeText(Liste.this, "Votre list a bien été réinitialiser",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        binding.Save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.Nom.getText().toString();
                save();
                Toast.makeText(Liste.this,"Sauvegarde réussi", Toast.LENGTH_LONG).show();
            }
        });

        binding.alarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Nom = binding.Nom.getText().toString();
                if (!Nom.equals("") && !Nom.equals(" ")){
                    save();
                    Intent intent = new Intent(Liste.this, ClockActivity.class);
                    intent.putExtra("Nom", Nom );
                    startActivity(intent);
                }else {Toast.makeText(Liste.this,"Veuillez ne pas laisser le nom vide", Toast.LENGTH_LONG).show();}
            }
        });
    }

    private void save() {
        if (!Nom.equals("") && !Nom.equals(" ")) {
            File directory;
            directory = getFilesDir();
            StorageUtils.setTextInStorage(directory, this, FILENAME, FOLDERNAME, Nom + "," + random_list.toString());
            Toast.makeText(Liste.this, "Sauvegarde réussi", Toast.LENGTH_LONG).show();
        }else {Toast.makeText(Liste.this, "Veuillez entrer un nom pour votre liste", Toast.LENGTH_LONG).show();}
    }
}
