package com.example.controledepresenca.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controledepresenca.activities.CadastroActivity;
import com.example.controledepresenca.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class UsuarioDAO {
    private FirebaseAuth auth;
    private Context context;

    public UsuarioDAO(Context context){
        auth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void cadastrarUsuario(Usuario usuario, FirebaseAuth.AuthStateListener authListener) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authListener.onAuthStateChanged(auth);

                }else{
                    String exececao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exececao = "Digite uma senha com no mínimo 6 caracteres";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exececao = "Digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        exececao = "Essa conta já existe";
                    }catch (Exception e){
                        exececao = "Erro ao cadastrar usuario " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "Erro ao Adicionar usuario: " + exececao, Toast.LENGTH_SHORT).show();
                    Log.d("UsuarioDAO", "Erro ao Adicionar usuario: " + exececao);
                }
            }
        });
    }

}