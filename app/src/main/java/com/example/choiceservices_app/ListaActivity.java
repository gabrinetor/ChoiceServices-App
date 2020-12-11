package com.example.choiceservices_app;

import android.os.Bundle;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListaActivity extends AppCompatActivity {

    private ListView lvHomeAdm;
    private ListView lvLista;
    //private List<Servico> listaDeServicos;
    //private ArrayAdapter adapter;
    private String[] listaServices = {
            "estilista", "editor", "design"
    };

    private FirebaseDatabase database;  //acessar
    private DatabaseReference reference;    //referenciar
    private ChildEventListener childEventListener;  //ramificar
    private Query query;    //objetificar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lvHomeAdm = findViewById(R.id.lvHomeAdm);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, listaServices
        );    //Adapter

        lvLista.setAdapter( adapter );  //adiciona adaptador à lista

    }

}