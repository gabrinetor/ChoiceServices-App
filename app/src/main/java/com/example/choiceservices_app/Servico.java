package com.example.choiceservices_app;

import java.io.Serializable;

public class Servico implements Serializable {  //modelagem dos itens trabalhados

    private String id;
    private String nome;
    private double preco;
    private String key;

    public Servico(String id, String nome, double preco, String key) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.key = key;
    }

    public Servico() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {  //pra criar ele vazio, pq qnd listar os serviços queremos retornar o nome e o preço
        return nome + " - Preço: " + preco;
    }
}
