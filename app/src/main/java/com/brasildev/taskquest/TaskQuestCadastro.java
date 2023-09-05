package com.brasildev.taskquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class TaskQuestCadastro extends AppCompatActivity {

    private EditText usuario_cadastro, email_cadastro, password_cadastro;
    private Button button_cadastrar;
    String[] mensagens = {"Preencha todos os campos!", "Cadastro realizado com sucesso!"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_quest_cadastro);

        IniciarComponentes();

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = usuario_cadastro.getText().toString();
                String email = email_cadastro.getText().toString();
                String password = password_cadastro.getText().toString();

                if (usuario.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }else{
                    CadastrarUsuario(v);
                }

            }
        });
    }


    private void CadastrarUsuario(View v){

        String usuario = usuario_cadastro.getText().toString();
        String email = email_cadastro.getText().toString();
        String password = password_cadastro.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    SalvarDadosUsuario();

                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.GREEN);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }else {
                    String erro;

                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha com no mínimo 6 caracteres!";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "Este email já foi cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }catch(Exception e){
                        erro = "Erro ao cadastrar o usuário";
                    }

                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();

                }

            }
        });

    }

    private void SalvarDadosUsuario(){
        String usuario = usuario_cadastro.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("usuario", usuario);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_erro", "Erro ao salvar os dados" + e.toString());
            }
        });
    }
    private void IniciarComponentes(){
        usuario_cadastro = findViewById(R.id.usuario_cadastro);
        email_cadastro = findViewById(R.id.email_cadastro);
        password_cadastro = findViewById(R.id.password_cadastro);
        button_cadastrar = findViewById(R.id.button_cadastrar);
    }

}