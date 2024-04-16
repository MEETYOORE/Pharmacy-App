package com.example.pharmacy_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmacy_app.MainActivity;
import com.example.pharmacy_app.R;
import com.example.pharmacy_app.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
{
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
    }

    public void login(View view)
    {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    public void registration(View view)
    {
        startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
    }

    public void guest(View view)
    {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }
}