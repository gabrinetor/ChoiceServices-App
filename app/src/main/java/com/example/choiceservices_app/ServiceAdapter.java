package com.example.choiceservices_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {

    private List<Servico> listaDeServicos;
    private Context context;
    private LayoutInflater inflater;

    public ServiceAdapter(Context context, List<Servico> listaDeServicos) {
        this.context = context;
        this.listaDeServicos = listaDeServicos;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount() {
        return listaDeServicos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaDeServicos.get( i );
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }

    private class ItemSuporte{
        TextView tvNome, tvPreco;
        LinearLayout layout;
    }

}
