package com.example.pharmacy_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pharmacy_app.R;
import com.example.pharmacy_app.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity
{

    Button signUp;
    EditText name, email, password;
    TextView signInHere;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_2);

//        // Load the animation
//        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_to_left);
//
//        // Find the root layout of your activity
//        View rootLayout = findViewById(android.R.id.content);
//
//        // Apply the animation to the root layout
//        rootLayout.startAnimation(slideInAnimation);

        // Add this line in onCreate of RegistrationActivity after setContentView
        startAnimationWithDelay(findViewById(android.R.id.content), R.anim.slide_in_right_to_left, 100);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressbar);

        signUp = findViewById(R.id.sign_up_btn);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        signInHere = findViewById(R.id.sign_in_here);
        progressBar.setVisibility(View.GONE);   // hide progress bar intially

        signInHere.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // show progress bar while trying to create user
                progressBar.setVisibility(View.VISIBLE);

                // try creating a firebase user
                createUser();
            }
        });

    }

    /** @noinspection Since15*/
    private void createUser()
    {
        // Assuming name, email, and password are EditText fields
        String userName = name.getText().toString().strip();
        String userEmail = email.getText().toString().strip();
        String userPassword = password.getText().toString().strip();

        if (TextUtils.isEmpty(userName))
        {
            // stop showing progress bar
            progressBar.setVisibility(View.GONE);

            Toast.makeText(this,"Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (userPassword.length() <= 5)
        {
            // stop showing progress bar
            progressBar.setVisibility(View.GONE);

            Toast.makeText(this,"Password length must be greater than 5!", Toast.LENGTH_SHORT).show();
            return;
        }

        // try creating account
        // try creating account
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    UserModel userModel = new UserModel(userName, userEmail, userPassword);
                    String id = task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(id).setValue(userModel);

                    //  If successful user creation then stop showing progress
                    progressBar.setVisibility(View.GONE);

                    // If successful, show a toast with the success message
                    Toast.makeText(RegistrationActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();

                    // go to login page
                    startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));

                }
                else
                {
                    // If unsuccessful, stop progress bar
                    progressBar.setVisibility(View.GONE);

                    // If unsuccessful, retrieve and show the error message in a dialog
                    String errorMessage = task.getException().getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle("Failed to Create Account");
                    builder.setMessage(errorMessage);
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
            }
        });
    }

    private void startAnimationWithDelay(final View view, final int animationResId, final long delayMillis) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), animationResId);
                view.startAnimation(animation);
            }
        }, delayMillis);
    }

}