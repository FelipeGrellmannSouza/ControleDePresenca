package com.example.controledepresenca.util;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigDb {

    private static FirebaseAuth auth;

    public static FirebaseAuth FBAuntenticacao(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

}
