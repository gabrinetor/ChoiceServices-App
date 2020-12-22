package com.example.choiceservices_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity implements FirestoreAdapter.ItemDaLista {

    private ListView lvLista;
    private ListView carrinho;
    private List<Servico> listaLittleCart;
    private List<Servico> listaDeServicos;
    private ServiceAdapter adapterService;
    private ArrayAdapter adapter;

    private FirebaseDatabase database;  //acessar
    private DatabaseReference reference;    //referenciar
    private ChildEventListener childEventListener;  //ramificar
    private Query query;    //objetificar
    private int cont;       //contador

    private FirebaseFirestore dtServc;     //sincronizar dados através do listener, em tempo real
    private RecyclerView flServc;   //firestore list de serviços
    private FirestoreAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_adm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dtServc = FirebaseFirestore.getInstance();
        flServc = findViewById(R.id.listaFirestore);

        //Query query = dtServc.collection("servicos");
        CollectionReference query1 = dtServc.collection("servicos");   //query poder consultar no firestore

        PagedList.Config config = new PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(3)
            .build();   //mostrar 3 na tela listagem

        /*FirestorePagingOptions<Servico> options = new FirestorePagingOptions.Builder<Servico>()
                .setLifecycleOwner(this)
                .setQuery(query1, config, new SnapshotParser<Servico>() {
                    @NonNull
                    @Override
                    public Servico parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Servico modelServc = snapshot.toObject(Servico.class);
                        String id = snapshot.getId();
                        modelServc.setId(id);
                        return modelServc;
                    }
                }).build();   */  //lista <>


        FirestorePagingOptions<Servico> options = new FirestorePagingOptions.Builder<Servico>()
                .setLifecycleOwner(this)
                .setQuery(query1, config, Servico.class)
                .build();


        /*adapter1 = new FirestorePagingAdapter<Servico, viewHolderServc>(op) {
            @NonNull
            @Override
            public viewHolderServc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item, parent, false);
                return new viewHolderServc(view);
            }

            @Override
            public void onBindViewHolder(@NonNull viewHolderServc holder, int position, @NonNull Servico servc) {
                holder.itemNome.setText(servc.getNome());
                holder.itemPreco.setText(servc.getId() + "");
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state) {    //para testar no Logcat
                    case LOADING_INITIAL:
                        Log.d("PAGING_LOG" , "Carregando dados iniciais");
                        break;
                    case LOADING_MORE:
                        Log.d("PAGING_LOG" , "Carregando próxima página");
                        break;
                    case FINISHED:
                        Log.d("PAGING_LOG" , "Dados carregados");
                        break;
                    case ERROR:
                        Log.d("PAGING_LOG" , "Erro ao carregar");
                        break;
                    case LOADED:
                        Log.d("PAGING_LOG" , "Itens Carregados : " + getItemCount());
                        break;
                }
            }
        }; */

        adapter1 = new FirestoreAdapter(options, this);

        flServc.setHasFixedSize(true);
        flServc.setLayoutManager(new LinearLayoutManager(this));
        flServc.setAdapter(adapter1);

        /*/
        lvLista = findViewById(R.id.lvLista);
        carrinho = findViewById(R.id.action_drawer_carrinho);
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
        }); /*/

    }

    @Override
    public void itemDaLista(DocumentSnapshot snapshot, int posicao) {
        Log.d("ITEM_CLICK", "item clicado : " + posicao + " id: " + snapshot.getId());
    }

/*    @Override
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
    protected void onStop() {
        super.onStop();
        //query.removeEventListener(childEventListener);
        adapter1.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter1.startListening();
    }*/


}