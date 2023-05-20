package com.nak.fakeblock;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nak.fakeblock.models.User;

public class LoginActivity extends FoundationActivity implements View.OnClickListener {
    private EditText mEmailField, mPasswordField;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);
        findViewById(R.id.buttonSingIn).setOnClickListener(this);
        findViewById(R.id.buttonSingUp).setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void onAuthSuccess(FirebaseUser firebaseUser) {
        String email = firebaseUser.getEmail();
        String username = email;
        if (email != null && email.contains("@")) {
            username = email.split("@")[0];
        }

        User user = new User(username, email);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(firebaseUser.getUid()).setValue(user);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void signIn() {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (validateForm(email, password)) {
            showProgressDialog();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void signUp() {
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();

        if (validateForm(email, password)) {
            showProgressDialog();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validateForm(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError(getString(R.string.required));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordField.setError(getString(R.string.required));
            return false;
        } else {
            mEmailField.setError(null);
            mPasswordField.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSingIn) {
            signIn();
        } else if (v.getId() == R.id.buttonSingUp) {
            signUp();
        }
    }
}