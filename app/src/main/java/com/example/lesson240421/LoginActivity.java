package com.example.lesson240421;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerBtn = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String pass = passwordEditText.getText().toString();
            if (isEmailValid(email) && isPasswordValid(pass)) {
                login(email, pass);
            }
        });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        toMainActivity(user);
                    } else {
                        showErrorDialog("Авторизация невозможна, попробуйте зарегистрироваться!");
                    }
                });
    }

    // TODO: 24.04.2021 Need to migrate it to RegistrationActivity 
    private void register(String email, String password) {
//        mAuth.createUserWithEmailAndPassword()
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        } else {
            showErrorDialog("Вы ввели неправильную почту для авторризации!");
            return false;
        }
    }

    private boolean isPasswordValid(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        } else {
            showErrorDialog("Вы ввели направильны пароль для авторризации!");
            return false;
        }
    }

    private void showErrorDialog(String text) {
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(text)
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            toMainActivity(currentUser);
        }
    }

    private void toMainActivity(FirebaseUser user) {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("user", user);
        startActivity(mainIntent);
    }
}