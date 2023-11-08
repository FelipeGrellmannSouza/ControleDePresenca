package com.example.controledepresenca.DAO;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.controledepresenca.activities.AlunoActivity;
import com.example.controledepresenca.activities.ProfessorActivity;
import com.example.controledepresenca.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private Context context;

    public UsuarioDAO(Context context){
        auth = FirebaseAuth.getInstance();//inicializa o auth (Cria conta Email e senha)
        firestore = FirebaseFirestore.getInstance();//incializa o fireStore (Aramazena dados)
        storage = FirebaseStorage.getInstance();//inicializa o fireStorage (armazena fotos)
        this.context = context;
    }

    // ---------------------------------------------------- Cadastro do Usuario ----------------------------------------------------
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

    //---------------------------------------------------- Logar Usuario ----------------------------------------------------
    public void logarUsuario(Usuario usuario){
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    //obtem os dados do Usuario por meio do obterDadosUsuario
                    obterDadosUsuario(usuario.getEmail(), new OnUsuarioDataReceivedListener() {
                        @Override
                        public void onUsuarioDataReceived(Usuario usuario) {
                            if(usuario != null) {
                                //Comparação do modo selecionado
                                String modo = usuario.getModo();
                                if ("aluno".equals(modo)) {
                                    //redirecinar para tela correspondente
                                    context.startActivity(new Intent(context, AlunoActivity.class));
                                } else if ("professor".equals(modo)) {
                                    //redirecinar para tela correspondente
                                    context.startActivity(new Intent(context, ProfessorActivity.class));
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

    //---------------------------------------------------- Iniciar Com o Usuario que foi Logado anteriormente ----------------------------------------------------
    public void iniciarUsuarioLogado(){
        FirebaseUser usuarioAuth = auth.getCurrentUser();
        if (usuarioAuth != null){
            obterDadosUsuario(usuarioAuth.getEmail(), new OnUsuarioDataReceivedListener() {
                @Override
                public void onUsuarioDataReceived(Usuario usuario) {
                    if (usuario != null){
                        //Comparação do modo selecionado
                        String modo = usuario.getModo();
                        Intent intent;
                        if ("aluno".equals(modo)) {
                            //redirecinar para tela correspondente
                            intent = new Intent (context, AlunoActivity.class);
                        } else if ("professor".equals(modo)) {
                            //redirecinar para tela correspondente
                            intent = new Intent (context, ProfessorActivity.class);
                        } else {
                            Toast.makeText(context, "Erro no modo do usuario", Toast.LENGTH_SHORT).show();
                            System.out.println("Modo: "+ modo);
                            return;
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "Erro ao encontrar Usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //---------------------------------------------------- Deslogar ----------------------------------------------------

    public void deslogar(){
        try{
            auth.signOut(); //Faz o logout do usuario
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //---------------------------------------------------- armazenar dados no FireStore ----------------------------------------------------
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
    //---------------------------------------------------- Obter Dados Do Usario ----------------------------------------------------
    public interface OnUsuarioDataReceivedListener {
        void onUsuarioDataReceived(Usuario usuario);

    }

    public interface OnUsuariosDataReceivedListener{
        void onUsuariosDataReceived(List<String> nomesAlunos);
    }

    public interface OnEmailDataReceivedListener {
        void onEmailDataReceived(String email);
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

    public void getEmailByNome(String nome, OnEmailDataReceivedListener listener){
        firestore.collection("usuarios")
                .whereEqualTo("nome", nome)
                .whereEqualTo("modo", "aluno") // Certifique-se de que seja um aluno
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            String email = usuario.getEmail();
                            listener.onEmailDataReceived(email);
                            return;
                        }
                    }
                    listener.onEmailDataReceived(null);
                });
    }


    //obtem ID do usuario
    public String getIdUsuairo(){
        FirebaseUser usuarioAuth = auth.getCurrentUser();
        if (usuarioAuth != null) {
            return usuarioAuth.getUid(); //retorna Id do usuario
        }else {
            return null;
        }

    }

    //obtem o nome dos alunos presentes
    public void obterAlunosPresentes(OnUsuariosDataReceivedListener listener){
        firestore.collection("usuarios")
                .whereEqualTo("modo", "aluno")
                .whereEqualTo("presenca", "presente")
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful() && task.getResult() != null){
                       List<String> nomesAlunos = new ArrayList<>();
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           Usuario usuario = document.toObject(Usuario.class);
                           nomesAlunos.add(usuario.getNome());
                       }
                       listener.onUsuariosDataReceived(nomesAlunos);
                   }
                });
    }

    //---------------------------------------------------- Atualizar  ----------------------------------------------------
    public void atualizarPresenca(List<String> idsAlunos, String presenca){
        for (String alunoId : idsAlunos) {
            DocumentReference alunoRef = firestore.collection("usuarios").document(alunoId);

            // Atualiza o campo "presenca" no Firestore
            alunoRef.update("presenca", presenca)
                    .addOnSuccessListener(aVoid -> {
                        // Atualização bem-sucedida
                        Log.d("UsuarioDAO", "Presença atualizada para 'faltou' para o aluno: " + alunoId);
                    })
                    .addOnFailureListener(e -> {
                        // Falha na atualização
                        Log.w("UsuarioDAO", "Erro ao atualizar a presença para o aluno: " + alunoId, e);
                    });
        }
    }

    public void definirPresente(String qrCodeEntrada, String qrCodeSaida) {
        FirebaseUser usuarioAuth = auth.getCurrentUser();
        if (usuarioAuth != null) {
            DocumentReference alunoRef = firestore.collection("usuarios").document(usuarioAuth.getUid());

            String presenca = "faltou"; // Define o padrão como "faltou"

            if (qrCodeEntrada.equals("pre1") && qrCodeSaida.equals("pre2")) {
                presenca = "presente"; // Se o QR Code corresponder à aula, define a presença como "presente"

            }
            // Atualiza o campo "presenca" no Firestore
            alunoRef.update("presenca", presenca)
                    .addOnSuccessListener(aVoid -> {
                        // Atualização bem-sucedida
                        Toast.makeText(context, "Você foi registrado como presente", Toast.LENGTH_SHORT).show();
                        Log.d("UsuarioDAO", "Presença atualizada com sucesso");
                    })
                    .addOnFailureListener(e -> {
                        // Falha na atualização
                        Log.w("UsuarioDAO", "Erro ao atualizar a presença", e);
                    });
        }
    }

    public void definirFalta(OnUsuariosDataReceivedListener listener){
        firestore.collection("usuarios")
                .whereEqualTo("modo", "aluno")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<String> idsAlunos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            String alunoId = document.getId();
                            idsAlunos.add(alunoId);
                        }
                        atualizarPresenca(idsAlunos, "faltou");

                        //notificar que todos os alunos com modo "aluno" tiveram a presença definida como "faltou"
                        listener.onUsuariosDataReceived(idsAlunos);
                    }else {
                        //lidar com erros
                    }
                    });
    }


    //método que atualiza a url da foto do usaurio no firestore
    public void atualizarUrlFoto(String userId, String fotoUrl){
        firestore.collection("usuarios")
                .document(userId)
                .update("urlFoto", fotoUrl)
                .addOnSuccessListener(aVoid -> {
                   Log.d("UsuarioDAO", "URL da imagem do usuário atualizado com sucesso");
                })
                .addOnFailureListener(e -> {
                    Log.w("UsuarioDAO", "Erro ao atualizar URL da imagem do usario" ,e);
                });
    }

    //---------------------------------------------------- Enviando Foto do usuario Para o Firebase Storage----------------------------------------------------

    public interface OnUploadCompleteListener{
        void onUploadComplete(String imageUrl);
        void onUploadFailed(Exception e);
    }

    public void uploadFoto(Bitmap fotoBitmap, String userId, OnUploadCompleteListener  listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        //Nome Unico da foto = ao ID do usuario
        String nomeFoto = userId + ".jpg";

        //convertendo o bitmap para um array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Definindo o caminho onde a imagem será salva no Storage
        StorageReference imagemRef = storageRef.child("fotos/" + nomeFoto);

        //Enviando a imagem para o Storage
        UploadTask uploadTask = imagemRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            storageRef.child("fotos/" + nomeFoto).getDownloadUrl().addOnSuccessListener(uri -> {
                String fotoUrl = uri.toString();

                atualizarUrlFoto(userId, fotoUrl);

                listener.onUploadComplete(fotoUrl);
            });
        });

        uploadTask.addOnFailureListener(exeception -> {
           //Lidar com erros
        });
    }
}
