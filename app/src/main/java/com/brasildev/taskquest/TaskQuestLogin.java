package com.brasildev.taskquest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TaskQuestLogin extends AppCompatActivity {

    private TextView text_cadastro;
    private EditText email_login, password_login;
    private Button button_logar;
    private ProgressBar progressbar;
    String[] mensagens = {"Preencha todos os campos!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_quest_login);

        IniciarComponentes();

        text_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TaskQuestLogin.this, TaskQuestCadastro.class);
                startActivity(intent);
            }
        });

        button_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = email_login.getText().toString();
                String password = password_login.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }else {
                    AutenticarUsuario(v);
                }

            }
        });
    }


    private void AutenticarUsuario(View v){
        String email = email_login.getText().toString();
        String password = password_login.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressbar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TelaPrincipal();
                        }
                    }, 2000);
                }else {
                    String erro;

                    try {
                        throw task.getException();
                    }catch (Exception e){
                        erro = "Erro ao efetuar o usuário.";
                    }
                    Snackbar snackbar = Snackbar.make(v, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            TelaPrincipal();
        }

    }

    private void TelaPrincipal(){
        Intent intent = new Intent(TaskQuestLogin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void IniciarComponentes(){
        text_cadastro = findViewById(R.id.text_cadastro);
        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        button_logar = findViewById(R.id.button_login);
        progressbar = findViewById(R.id.progressbar);
    }
}