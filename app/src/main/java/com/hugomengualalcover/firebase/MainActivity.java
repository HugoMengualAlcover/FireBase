package com.hugomengualalcover.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView lblFrase;
    private EditText txtFrase;
    private Button btnGuardar;

    private ArrayList<Persona> personas;

    private FirebaseDatabase database;

    private DatabaseReference refFrase;
    private DatabaseReference refPersona;
    private DatabaseReference refPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblFrase = findViewById(R.id.lblFrase);
        txtFrase = findViewById(R.id.txtFrase);
        btnGuardar = findViewById(R.id.btnGuardar);

        personas = new ArrayList<>();
        crearPersona();

        database= FirebaseDatabase.getInstance("https://fir-41df3-default-rtdb.europe-west1.firebasedatabase.app");

        //Referencias
        refFrase = database.getReference("frase");
        refPersona = database.getReference("persona");
        refPersonas = database.getReference("personas");

        //Ecribir
        Persona p = new Persona("Edu", 2);
        //refPersona.setValue(p);

        //refPersonas.setValue(personas);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refFrase.setValue(txtFrase.getText().toString());
                personas.remove(0);
                refPersonas.setValue(personas);
            }
        });

        //Lecturas
        refPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Persona p = dataSnapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, p.getNombre(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refFrase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String frase = dataSnapshot.getValue(String.class);
                    lblFrase.setText(frase);
                }else{
                    Toast.makeText(MainActivity.this, "NO EXISTE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator< ArrayList<Persona> > gti = new GenericTypeIndicator< ArrayList<Persona> >() {};
                    personas = dataSnapshot.getValue(gti);
                    Toast.makeText(MainActivity.this, "Descargados: "+personas.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearPersona(){
        for (int i = 0; i < 100; i++) {
            personas.add(new Persona("Nombre"+i, i+20));
        }
    }
}