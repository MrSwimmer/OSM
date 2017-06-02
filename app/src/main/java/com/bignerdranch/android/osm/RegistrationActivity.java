package com.bignerdranch.android.osm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Севастьян on 13.05.2017.
 */

public class RegistrationActivity extends Activity implements View.OnClickListener {
    String action = "auth";
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText Email;
    private EditText Password;
    //private EditText PasswordRepeat;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Intent i = new Intent(RegistrationActivity.this, SucEnter.class);
                    i.putExtra("name", email);
                    i.putExtra("action", action);
                    startActivity(i);
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        Email = (EditText) findViewById(R.id.et_email);
        Password = (EditText) findViewById(R.id.et_password);
        //PasswordRepeat = (EditText) findViewById(R.id.rep_pass);
        findViewById(R.id.btn_sign_in).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_in && Email.toString() != null && Password.toString() != null) {
            //if(Password.toString().equals(PasswordRepeat.toString()))
            Registration(Email.getText().toString(), Password.getText().toString());
//            else
//                Toast.makeText(RegistrationActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
        }
        //SignIn(Email.getText().toString(), Password.getText().toString());
//        } else if (v.getId() == R.id.btn_registration) {
//            Intent i = new Intent(RegistrationActivity.this, .class);
//            startActivity(i);
//            //Registration(Email.getText().toString(), Password.getText().toString());
//        }
    }

    public void SignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RegistrationActivity.this, SucEnter.class);
                    i.putExtra("name", Email.getText().toString());
                    i.putExtra("pass", Password.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(RegistrationActivity.this, "Ошибка входа!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Registration(final String email, String password) {
        action = "reg";
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.sendEmailVerification();
                    Toast.makeText(RegistrationActivity.this, "Регистрация прошла успешно!\n" +
                            "Письмо с подтверждением отправлено вам на почту", Toast.LENGTH_SHORT).show();
//                    mAuthListener = new FirebaseAuth.AuthStateListener() {
//                        @Override
//                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                            user = firebaseAuth.getCurrentUser();
//                            if (user != null ) {
//                                Toast.makeText(LogInActivity.this, "Аккаунт подтвержден!" ,  Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(LogInActivity.this, "Подтвердите аккаунт!" ,  Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(LogInActivity.this, MainActivity.class);
//                                startActivity(i);
//                            }
//                        }
//                    };
                } else {
                    Toast.makeText(RegistrationActivity.this, "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
