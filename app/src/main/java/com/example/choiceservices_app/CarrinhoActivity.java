package com.example.choiceservices_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class CarrinhoActivity extends AppCompatActivity {

    private ListView lvLista;
    private List<Servico> listaDeServicos;
    private LittleCartListAdapter littleCartListAdapter;
    private Context context;
    private ArrayAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ChildEventListener childEventListener;
    private Query query;
    private int cont = 1;
    private ImageView plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvLista = findViewById(R.id.lvServicosCarrinho);
        listaDeServicos = new ArrayList<>();
        listaDeServicos = (ArrayList<Servico>) getIntent().getSerializableExtra("listaCarrinho");
        Log.i("Entrarr", String.valueOf(listaDeServicos));
        littleCartListAdapter = new LittleCartListAdapter(listaDeServicos, context);
        Log.i("Entrarrrr", String.valueOf(listaDeServicos));
        lvLista.setAdapter(littleCartListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Sair");
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confg) {
            return true;
        }

        if (item.toString().equals("SAIR")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth.getCurrentUser() != null) {
                auth.signOut();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Servico serv = new Servico();
                serv.setNome(snapshot.child("nome").getValue(String.class));
                serv.setPreco(snapshot.child("preco").getValue(Double.class));
                serv.setKey(snapshot.getKey());
                listaDeServicos.add(serv);
                Log.i("Entraarr", String.valueOf(listaDeServicos));
                littleCartListAdapter.notifyDataSetChanged();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}