package me.willyan.ichat.controller;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import me.willyan.ichat.view.LoginActivity;
import me.willyan.ichat.view.MessageActivity;

public class LoginController {

    private MessageActivity messageAct;

    public LoginController(MessageActivity main){
        this.messageAct = main;
    }

    public void verifyAuth() {
        if(FirebaseAuth.getInstance().getUid() == null){
            Intent i = new Intent(messageAct, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            messageAct.startActivity(i);
        }
    }


}
