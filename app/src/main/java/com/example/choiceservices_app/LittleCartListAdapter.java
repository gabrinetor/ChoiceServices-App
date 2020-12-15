package com.example.choiceservices_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class LittleCartListAdapter extends BaseAdapter {
    private List<Servico> listaDeServicos;
    private Context context;
    private LayoutInflater inflater;    //aumenta as visualizações dentro do método onCreateView() do Fragment, e ele abrange um arquivo de layout XML em ViewGroup e Widgets

    public LittleCartListAdapter(List<Servico> listaDeServicos, Context context) {
        this.context = context;
        this.listaDeServicos = listaDeServicos;
        this.inflater = LayoutInflater.from(context);
        Log.i("Entrarr", String.valueOf(listaDeServicos));
    }

    //polimorfismo para sobrescrever os métodos de BaseAdapter
    @Override
    public int getCount() {
        return listaDeServicos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaDeServicos.get(i);
    }

    @Override
    public long getItemId(int i) {
        //return listaDeServicos.get( i ).getId();
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Log.i("Entrarrr", String.valueOf(listaDeServicos));
        return view;
    }


    private class ItemSuporte{
        TextView itemNome, itemPreco;
        LinearLayout layout;
    }


}
