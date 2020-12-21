package com.example.choiceservices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCadastro;
    boolean vfCampo = true;    //verificar se campo está preenchido

    FirebaseAuth auth;  //busca autenticação
    FirebaseAuth.AuthStateListener authStateListener;   //verifica o estado da verificação
    FirebaseUser usuario;   //verifica usuário, caso seja null: { não está logado ainda }; caso não seja null: { estará logado }; caso esteja logado: { estará na lista };
    FirebaseFirestore colecUsuario; //sincronizar dados através do listener, em tempo real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastro = findViewById(R.id.btnIrCadastrar);

        auth = FirebaseAuth.getInstance();  //inicializando o objeto de autenticação
        colecUsuario = FirebaseFirestore.getInstance();  //inicializando o objeto para autenticar usuario armazenado no colection do firebase em tempo real

        //logar
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checarCampos(etEmail);
                checarCampos(etSenha);
                Log.d( "TAG", "Entrar: " + etEmail.getText().toString() );

                if(vfCampo) {
                    auth.signInWithEmailAndPassword(etEmail.getText().toString(),etSenha.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginActivity.this, "Logado com sucesso", Toast.LENGTH_SHORT).show();
                            checarNivelDeAcesso(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //ir ao cadastrar
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent (getApplicationContext() , CadastroActivity.class) );
            }
        });

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

    }

    private void checarNivelDeAcesso(String uid) {
        DocumentReference docRef = colecUsuario.collection("users").document(uid);  //objeto para ouvir os dados do doccumento de banco de dados Firebase
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "Ok " + documentSnapshot.getData());

                if ( documentSnapshot.getString("ehAdmin") != null ) {     //perfil admin
                    startActivity(new Intent(getApplicationContext(), HomeAdmActivity.class));
                    finish();
                }

                if ( documentSnapshot.getString("ehUser") != null ) {      //perfil user contratante
                    startActivity(new Intent(getApplicationContext(), ListaActivity.class));
                    finish();
                }
            }
        });
    }

    public boolean checarCampos(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Erro");
            vfCampo = false;
        } else {
            vfCampo = true;
        }

        return vfCampo;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null) {
            final DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(auth.getCurrentUser().getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("ehAdmin") != null) {
                        startActivity(new Intent(getApplicationContext(), HomeAdmActivity.class));
                        finish();
                    }

                    if(documentSnapshot.getString("ehUser") != null) {
                        startActivity(new Intent(getApplicationContext(), ListaActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    auth.signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
            });
        }
    }
}