package com.example.choiceservices_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {

    private ListView lvLista;
    private ListView littlecart;
    private List<Servico> listaLittleCart;
    private List<Servico> listaDeServicos;
    private ServiceAdapter adapterService;
    private ArrayAdapter adapter;

    private FirebaseDatabase database;  //acessar
    private DatabaseReference reference;    //referenciar
    private ChildEventListener childEventListener;  //ramificar
    private Query query;    //objetificar
    private int cont;       //contador

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvLista = findViewById(R.id.lvLista);
        littlecart = findViewById(R.id.btnCarrinho);
        listaDeServicos = new ArrayList<>();
        listaDeServicos = new ArrayList<>();
        adapterService = new ServiceAdapter(ListaActivity.this, listaDeServicos);

        lvLista.setAdapter(adapterService);
        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listaDeServicos.add((Servico) parent.getItemAtPosition(position));
                cont++;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("SAIR");
        getMenuInflater().inflate(R.menu.menu_main, menu);  //conter items na barra do menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnCarrinho) {
            Log.i("Entrar", String.valueOf(listaLittleCart));
            Intent intent;
            intent = new Intent(ListaActivity.this, CarrinhoActivity.class);
            intent.putExtra("listaLittleCart", (ArrayList<Servico>) listaLittleCart);
            startActivity( intent );
            return true;
        }

        if (id == R.id.action_confg) { return true; }


        if (item.toString().equals("Sair")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if ( auth.getCurrentUser() != null ) {
                auth.signOut();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        listaDeServicos.clear();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        query = reference.child("servicos").orderByChild("nome");

        childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {
                Servico serv = new Servico();
                serv.setNome(snapshot.child("nome").getValue(String.class));
                serv.setPreco(snapshot.child("preco").getValue(Double.class));
                serv.setKey(snapshot.getKey());
                listaDeServicos.add(serv);  //adicionar cada item servico
                adapterService.notifyDataSetChanged();
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

        query.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        query.removeEventListener(childEventListener);
    }
}