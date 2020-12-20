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
    boolean vfCampo = true;    //verificar se campo está preenchido

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
        btnCadastro = findViewById(R.id.btnIrCadastrar);

        auth = FirebaseAuth.getInstance();  //inicializando o objeto de autenticação

        authStateListener = new FirebaseAuth.AuthStateListener() {      //inicializa verifica o que está ocorrendo, de acordo com o estado dele [autentc / não-autentic]

            @Override   //sobrescrita do método obrigatório
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                usuario = auth.getCurrentUser();    //pegar usuário atual

                if ( usuario != null ) {
                    Intent intent;

                    if ( usuario.getEmail().equals("admin@domin.com") ) {
                        intent = new Intent(LoginActivity.this, HomeAdmActivity.class);  //login admin
                    } else {
                        intent = new Intent(LoginActivity.this, ListaActivity.class);  //login usuario contratante
                    }
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

/*        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });*/

    }

    private void entrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if ( !email.isEmpty() && !senha.isEmpty() ) {
//            auth.signInWithEmailAndPassword( email , senha ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {    //task é a tarefa resultante dessa ação
//                    if ( !task.isSuccessful() ) {
//                        Toast.makeText( LoginActivity.this ,
//                                "Erro ao realizar o login" , Toast.LENGTH_LONG).show();
//                    } else {
//                        usuario = auth.getCurrentUser();
//                        if(usuario != null){
//                            Intent intent;
//                            if( usuario.getEmail().equals("admin@domin.com")) {
//                                intent = new Intent(LoginActivity.this , HomeAdmActivity.class);
//                            } else {
//                                intent = new Intent(LoginActivity.this , ListaActivity.class);
//                            }
//                            startActivity( intent );
//                        }
//                    }
//
//                }
//            });       //obj.metodo.evento( ref. superclasse {  @Override método(parm.) {...}  });

                            Intent intent;
                            if( email.equals("admin@domin.com")) {
                                intent = new Intent(LoginActivity.this , HomeAdmActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this , ListaActivity.class);
                            }
                            startActivity( intent );

        }

    }

    /*private void cadastrar() {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        if (!email.isEmpty() && !senha.isEmpty()) {
            auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,
                                "Erro ao cadastrar", Toast.LENGTH_LONG).show();
                    } else {
                        usuario = auth.getCurrentUser();
                        if(usuario != null) { Intent intent;
                            if( usuario.getEmail().equals("admin@domin.com")) {
                                intent = new Intent(LoginActivity.this , HomeAdmActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this , ListaActivity.class);
                            }
                            startActivity( intent ); }
                    }

                }
            });
        }

        /*
        //sobrescrita para criar instancia dentro do método cadastrar
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            etEmail = findViewById(R.id.etEmail);
            etSenha = findViewById(R.id.etSenha);
            btnEntrar = findViewById(R.id.btnEntrar);
            btnCadastro = findViewById(R.id.btnCadastro);

            //outra forma de verificar se os campos não estão vazios, fazendo referência ao método abaixo
            verCampos(etEmail);
            verCampos(etSenha);

        }*/

    //}/*

    /*public boolean verCampos(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }*/
}