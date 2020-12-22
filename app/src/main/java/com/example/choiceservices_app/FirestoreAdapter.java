package com.example.choiceservices_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

//testar se seria necessária uma classe para o adapter do Firestore , pois já tem o adapter dos servicos
public class FirestoreAdapter extends FirestorePagingAdapter<Servico, FirestoreAdapter.viewHolderServc> {

    private ItemDaLista itemDaLista;

    public FirestoreAdapter(@NonNull FirestorePagingOptions<Servico> options, ItemDaLista itemDaLista) {
        super(options);
        this.itemDaLista = itemDaLista;
    }

    @NonNull
    @Override
    public viewHolderServc onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item, parent, false);
        return new viewHolderServc(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderServc holder, int position, @NonNull Servico servc) {
        holder.itemNome.setText(servc.getNome());
        holder.itemPreco.setText(servc.getPreco() + "");
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


    public class viewHolderServc extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView itemNome;
        private TextView itemPreco;

        public viewHolderServc(@NonNull View itemView) {
            super(itemView);

            itemNome = itemView.findViewById(R.id.itemNome);
            itemPreco = itemView.findViewById(R.id.itemPreco);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemDaLista.itemDaLista(getItem(getAdapterPosition()), getAdapterPosition());
        }
    }

    public interface ItemDaLista{
        void itemDaLista(DocumentSnapshot snapshot, int posicao);
    }

}
