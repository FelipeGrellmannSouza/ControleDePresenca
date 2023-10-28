package com.example.controledepresenca.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controledepresenca.DAO.UsuarioDAO;
import com.example.controledepresenca.R;
import com.example.controledepresenca.model.Usuario;
import com.example.controledepresenca.util.ConfigDb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private UsuarioDAO usuarioDAO;
    Usuario usuario;
    FirebaseAuth auth;
    EditText edtEmail, edtSenha;
    Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Incializar();


    }
    //método para organização do código
    public void Incializar(){
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        usuarioDAO = new UsuarioDAO(this);
    }

    public void validarCampos(View view){
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        if(!email.isEmpty()){
            if (!senha.isEmpty()){
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);
                cadastrarUsuario(usuario);
            }else{
                Toast.makeText(this, "Preencha a senha corratamente", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Preencha o campo Email corretamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarUsuario(Usuario usuario) {
       usuarioDAO.cadastrarUsuario(usuario, new FirebaseAuth.AuthStateListener() {
           @Override
           public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               // Este método será chamado quando o estado de autenticação mudar
               // Você pode adicionar código aqui para lidar com diferentes estados de autenticação
           }
       });
    }


    private void homeAluno(){
        Intent intent = new Intent(this, MainActivity.class);//mudar para home do aluno
        startActivity(intent);
    }

}