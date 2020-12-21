package com.example.choiceservices_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button btnLogin, btnCadastrar;
    boolean vfCampo = true;    //verificar se campo está preenchido
    CheckBox cadAdminBox, cadUserBox;  //verificar tipo de acesso de acordo com o checkbox marcado

    FirebaseAuth auth;  //busca autenticação
    FirebaseAuth.AuthStateListener authStateListener;   //verifica o estado da verificação
    FirebaseUser usuario = auth.getCurrentUser();   //verifica usuário, caso seja null: { não está logado ainda }; caso não seja null: { estará logado }; caso esteja logado: { estará na lista };
    FirebaseFirestore colecUsuario;     //sincronizar dados através do listener, em tempo real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        email = findViewById(R.id.cadEmail);
        senha = findViewById(R.id.cadSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btnLogin = findViewById(R.id.btnIrLogar);

        cadAdminBox = findViewById(R.id.cadAdmin);
        cadUserBox = findViewById(R.id.cadUser);

        cadUserBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    cadAdminBox.setChecked(false);
                }
            }
        });

        cadAdminBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    cadUserBox.setChecked(false);
                }
            }
        });

        //botão para registrar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checarCampos(email);
                checarCampos(senha);

                //validação de checkboxs , se nenhum dos dois checkboxs estiver marcado
                if ( !(cadAdminBox.isChecked() || cadUserBox.isChecked()) ) {
                    Toast.makeText(CadastroActivity.this, "Marque uma das opções!!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (vfCampo) {   //processo de cadastrar
                    //Intent intent = new Intent(getApplicationContext(), HomeAdmActivity.class);

                    auth.createUserWithEmailAndPassword(email.getText().toString(),senha.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                         @Override
                         public void onSuccess(AuthResult authResult) {
                             Toast.makeText(CadastroActivity.this , "Perfil criado!", Toast.LENGTH_LONG).show();
                             DocumentReference docRef = colecUsuario.collection("users").document(usuario.getUid());  //grava, lê, ouve e cria uma colection no banco de dados Firestore

                             Map<String,Object> infoUsuario = new HashMap<>();   //objeto para buscar rapidamente a palavra chave
                             infoUsuario.put( "Email", email.getText().toString() );
                             //infoUsuario.put( "Senha", senha.getText().toString() );

                             //infoUsuario.put( "ehUser" , "1" );         //especficar se usuário é admin, então valor será 0

                             /* Valores */
                             if(cadAdminBox.isChecked()) {
                                 infoUsuario.put("ehAdmin", "1");
                             }

                             if(cadUserBox.isChecked()) {
                                 infoUsuario.put("ehUser", "1");
                             }

                             /* Verificar qual perfil será logado */
                             docRef.set( infoUsuario );  //enviar para a referência de documento
                             if(cadAdminBox.isChecked()) {
                                 startActivity( new Intent(getApplicationContext(), HomeAdmActivity.class) );
                                 finish();
                             }

                             if(cadUserBox.isChecked()) {
                                 startActivity( new Intent(getApplicationContext(), ListaActivity.class) );
                                 finish();
                             }

                         }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( CadastroActivity.this , "Erro ao cadastrar!!!" , Toast.LENGTH_LONG ).show();
                        }
                    });
                }

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity( new Intent(getApplicationContext(), LoginActivity.class) );
                    }
                });
            }
        });

        //botão para ir à tela de Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    public boolean checarCampos(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Erro");
            vfCampo = false;
        }else {
            vfCampo = true;
        }

        return vfCampo;
    }

}