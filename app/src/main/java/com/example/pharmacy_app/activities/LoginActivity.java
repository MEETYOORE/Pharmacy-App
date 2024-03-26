package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.example.pharmacy_app.MainActivity;
import com.example.pharmacy_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{

    Button logIn;
    EditText email, password;
    TextView signUpHere;

    FirebaseAuth auth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressbar);

        logIn = findViewById(R.id. login_btn);
        email = findViewById(R.id. email_login);
        password = findViewById(R.id.password_login);
        signUpHere = findViewById(R.id.sign_in_here);
        progressBar.setVisibility(View.GONE);

        signUpHere.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // show progress bar while authenticating logging in
                progressBar.setVisibility(View.VISIBLE);

                // Call the login method
                logInUser();
            }
        });
    }

    private void logInUser()
    {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        if (TextUtils.isEmpty(userEmail))
        {
            // stop showing progress bar
            progressBar.setVisibility(View.GONE);

            Toast.makeText(this,"Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userPassword))
        {
            // stop showing progress bar
            progressBar.setVisibility(View.GONE);

            Toast.makeText(this,"Password is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            //  If successful login then stop showing progress bar
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Login Successful!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                        }
                        else
                        {
                            //  If unsuccessful user login then stop showing progress
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"Incorrect email or password!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}