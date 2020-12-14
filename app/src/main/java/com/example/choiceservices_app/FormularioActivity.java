package com.example.choiceservices_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormularioActivity extends AppCompatActivity {

    private EditText etNome, etPreco;
    private Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        etNome = findViewById(R.id.etNome);
        etPreco = findViewById(R.id.etPreco);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvar();
            }
        });
    }

    private void salvar() {
        String nome = etNome.getText().toString();
        String preco = etPreco.getText().toString();

        if ( !nome.isEmpty() ) {
            Servico serv = new Servico();
            serv.setNome(nome);

            if ( preco.isEmpty() ) {
                serv.setPreco(0.00);
            } else {
                preco = preco.replace( "," , "." );  //senão procuro por uma vírgula ou por ponto no preço, ao cadastrar serviço
                serv.setPreco(Double.valueOf( preco ));
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance(); //comunicação com o firebase
            DatabaseReference reference = database.getReference();  //referência do bd
            reference.child("servicos").push().setValue( serv );    //colocar valores (child = ramificação do banco)
            finish();

        } else {
        Toast.makeText(FormularioActivity.this ,
                "Você deve preencher o nome do serviço!" , Toast.LENGTH_LONG).show();
        }
    }

}