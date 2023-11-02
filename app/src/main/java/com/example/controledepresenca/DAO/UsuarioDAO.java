package com.example.controledepresenca.DAO;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controledepresenca.activities.CadastroActivity;
import com.example.controledepresenca.activities.MainActivity;
import com.example.controledepresenca.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UsuarioDAO {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private Context context;

    public UsuarioDAO(Context context){
        auth = FirebaseAuth.getInstance();//inicializa o auth
        firestore = FirebaseFirestore.getInstance();//incializa o fireStore
        this.context = context;
    }

    //Cadastro FireAuth (Email E Senha)
    public void cadastrarUsuario(Usuario usuario, FirebaseAuth.AuthStateListener authListener) {
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authListener.onAuthStateChanged(auth);

                    Toast.makeText(context, "Usuario Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();
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

    //Logar com email senha e verificar o modo do usuario
    public void logarUsuario(Usuario usuario){
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    obterDadosUsuario(usuario.getEmail(), new OnUsuarioDataReceivedListener() {
                        @Override
                        public void onUsuarioDataReceived(Usuario usuario) {
                            if(usuario != null) {
                                String modo = usuario.getModo();
                                if ("aluno".equals(modo)) {
                                    //redirecinar para tela correspondente
                                    //context.startActivity(new Intent(context, CadastroActivity.class));
                                    Toast.makeText(context, "Usuario logado. Modo = "+ modo, Toast.LENGTH_SHORT).show();
                                    System.out.println("Usuario logado. Modo = "+ modo);

                                } else if ("professor".equals(modo)) {
                                    //redirecinar para tela correspondente
                                    context.startActivity(new Intent(context, MainActivity.class));
                                } else {
                                    Toast.makeText(context, "Erro no modo do usuario", Toast.LENGTH_SHORT).show();
                                    System.out.println("Modo: "+ modo);
                                }
                            }else {Toast.makeText(context, "Erro ao encontrar Usuario", Toast.LENGTH_SHORT).show();}
                        }
                    });
                }else{
                    String excessao="";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excessao = "Usuario não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excessao = "Email ou senha incorreto";
                    }catch (Exception e){
                        excessao = "Erro ao logar" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(context, excessao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //armazenar dados no FireStore
    public void adicionarDadosUsuario(Usuario usuario){
        firestore.collection("usuarios")// "usuarios" = nome da colecao
                .document(auth.getCurrentUser().getUid()) //Define o Uid do usuario autenticado coomo ID do documento
                .set(usuario) //define os dados do usuário no documento
                .addOnSuccessListener(aVoid ->{
                    Log.d("UsuarioDAO", "Dados do usuario adicionados com sucesso ao FireStore");//Log para informar que o Usuario foi cadastrado com sucesso
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Erro ao adicionar os dados do Usuario" + e.getMessage(), Toast.LENGTH_SHORT).show();//Exibe mensagem de erro na tela
                });
    }

    public interface OnUsuarioDataReceivedListener {
        void onUsuarioDataReceived(Usuario usuario);
    }

    //obtem os dados do usuario no firestore
    public void obterDadosUsuario(String email,OnUsuarioDataReceivedListener listener) {
    firestore.collection("usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener(task -> {
               if (task.isSuccessful() && task.getResult() != null){
                   for (QueryDocumentSnapshot document : task.getResult()){
                       Usuario usario = document.toObject(Usuario.class);
                       listener.onUsuarioDataReceived(usario);
                       return;
                   }
               }
               listener.onUsuarioDataReceived(null); //Usuario não encontrado
            });
    }

}
