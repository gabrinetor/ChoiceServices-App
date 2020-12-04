package com.example.choiceservices_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView lvLista;
    private List<Servico> listaDeServicos;
    private ArrayAdapter adapter;

    private FirebaseDatabase database;  //acessar
    private DatabaseReference reference;    //referenciar
    private ChildEventListener childEventListener;  //ramificar
    private Query query;    //objetificar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ListaActivity.this , FormularioActivity.class );
                startActivity( intent );
            }
        });

        listaDeServicos = new ArrayList<>();

        lvLista = findViewById(R.id.lvLista);
        adapter = new ArrayAdapter( ListaActivity.this , android.R.layout.simple_list_item_1 , listaDeServicos );
        lvLista.setAdapter( adapter );
    }

    @Override
    protected void onStart() {
        super.onStart();

        listaDeServicos.clear();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        query = reference.child("servicos").orderByChild("preco");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Servico s = new Servico();
                s.id = snapshot.getKey();
                s.nome = snapshot.child("nome").getValue( String.class );
                s.preco = snapshot.child("preco").getValue( Double.class );

                listaDeServicos.add( s );   //pegou o produto do firebase e adicionou na lista
                adapter.notifyDataSetChanged(); //atualizar a tela
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }; 

        query.addChildEventListener( childEventListener );

    }

    @Override
    protected void onStop() {
        super.onStop();

        query.removeEventListener( childEventListener );
    }

}