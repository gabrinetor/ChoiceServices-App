package com.example.choiceservices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button btnLogin, btnCadastrar;
    boolean vfCampo = true;    //verificar se campo está preenchido

    FirebaseAuth auth;  //busca autenticação
    FirebaseAuth.AuthStateListener authStateListener;   //verifica o estado da verificação
    FirebaseUser usuario = auth.getCurrentUser();   //verifica usuário, caso seja null: { não está logado ainda }; caso não seja null: { estará logado }; caso esteja logado: { estará na lista };
    //FirebaseFirestore colecUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        email = findViewById(R.id.cadEmail);
        senha = findViewById(R.id.cadSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnLogin = findViewById(R.id.btnIrLogar);

        auth = FirebaseAuth.getInstance();  //inicializando o objeto de autenticação
        //colecUsuario = FirebaseFirestore.getInstance();

        if (vfCampo) {   //processo de cadastrar
            //Intent intent = new Intent(getApplicationContext(), HomeAdmActivity.class);

            auth.createUserWithEmailAndPassword(email.getText().toString() , senha.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(CadastroActivity.this , "Perfil criado!", Toast.LENGTH_LONG).show();
//                    DocumentReference docRef = colecUsuario.collection("users").document(usuario.getUid());
                    Map<String,Object> infoUsuario = new HashMap<>();
                    infoUsuario.put( "Email", email.getText().toString() );
                    //infoUsuario.put( "Senha", senha.getText().toString() );

                    infoUsuario.put( "ehUser" , "1" );         //especficar se usuário é admin, então valor será 0

//                    docRef.set( infoUsuario );  //enviar para a referência de documento

                    startActivity( new Intent(getApplicationContext(), HomeAdmActivity.class) );
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( CadastroActivity.this , "Erro ao cadastrar!!!" , Toast.LENGTH_LONG ).show();
                }
            });
        }

    }
}