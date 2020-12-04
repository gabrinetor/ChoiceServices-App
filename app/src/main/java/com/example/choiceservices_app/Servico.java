package com.example.choiceservices_app;

public class Servico {

    public String id;
    public String nome;
    public double preco;

    @Override
    public String toString() {  //pra criar ele vazio, pq qnd listar os serviços queremos retornar o nome e o preço
        return nome + " - " + preco;
    }
}
