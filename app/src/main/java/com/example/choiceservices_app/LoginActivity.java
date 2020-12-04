package com.example.choiceservices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCadastro;

    FirebaseAuth auth;  //busca autenticação
    FirebaseAuth.AuthStateListener authStateListener;   //verifica o estado da verificação
    FirebaseUser usuario;   //verifica usuário, caso seja null: { não está logado ainda }; caso não seja null: { estará logado }; caso esteja logado: { estará na lista };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastro = findViewById(R.id.btnCadastro);

        auth = FirebaseAuth.getInstance();  //inicializando o objeto de autenticação

        authStateListener = new FirebaseAuth.AuthStateListener() {      //inicializa verifica o que está ocorrendo, de acordo com o estado dele [autentc / não-autentic]
            @Override   //sobrescrita do método obrigatório
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = auth.getCurrentUser();    //pegar usuário atual

                if ( usuario != null ) {
                    Intent intent = new Intent(LoginActivity.this, ListaActivity.class); //referência da tela que estou e a referencia da tela pra onde queremos ir
                    startActivity ( intent );
                } else {
                    Toast.makeText(LoginActivity.this ,
                            "Erro ao realizar o login" , Toast.LENGTH_LONG).show();    //mensagem que aparece brevemente e some, passa referências (tela que estamos , "mensagem" , tempo que vai aparecer)
                }
            }
        };

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entrar();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });

    }

    private void entrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if ( !email.isEmpty() && !senha.isEmpty() ) {
            auth.signInWithEmailAndPassword( email , senha ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {    //task é a tarefa resultante dessa ação
                    if ( !task.isSuccessful() ) {
                        Toast.makeText( LoginActivity.this ,
                                "Erro ao realizar o login" , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void cadastrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (!email.isEmpty() && !senha.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Erro ao realizar o cadastrar", Toast.LENGTH_LONG).show();
                    } else {
                        usuario = auth.getCurrentUser();
                    }
                }
            });
        }
    }
}