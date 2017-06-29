package com.bignerdranch.android.osm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    String action = "auth";
    FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText Email;
    private EditText Password;

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
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Intent i = new Intent(LogInActivity.this, SucEnter.class);
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
        findViewById(R.id.btn_sign_in).setOnClickListener(this);
        findViewById(R.id.btn_registration).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sign_in && Email.toString() != null && Password.toString() != null) {
            SignIn(Email.getText().toString(), Password.getText().toString());
        } else if (v.getId() == R.id.btn_registration) {
            Intent i = new Intent(LogInActivity.this, RegistrationActivity.class);
            startActivity(i);
            //Registration(Email.getText().toString(), Password.getText().toString());
        }
    }

    public void SignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LogInActivity.this, SucEnter.class);
                    i.putExtra("name", Email.getText().toString());
                    i.putExtra("pass", Password.getText().toString());
                    startActivity(i);
                } else {
                    Toast.makeText(LogInActivity.this, "Ошибка входа!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Registration(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.sendEmailVerification();
                    action = "reg";
                    Toast.makeText(LogInActivity.this, "Регистрация прошла успешно!\n" +
                            "Письмо с подтверждением отправлено вам на почту", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LogInActivity.this, SucEnter.class);
                    i.putExtra("name", email);
                    i.putExtra("action", action);
                    startActivity(i);
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
                    Toast.makeText(LogInActivity.this, "Такой пользователь уже существует!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
