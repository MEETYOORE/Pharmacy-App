package com.example.pharmacy_app.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pharmacy_app.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pharmacy_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment
{
    private EditText profileName, profileEmail;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        profileName = root.findViewById(R.id.profile_name);
        profileEmail = root.findViewById(R.id.profile_email);

        // Fetch and display user's name and email
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null)
        {
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();

            // Set name and email to EditText fields
            profileName.setText(userName);
            profileEmail.setText(userEmail);

            Toast.makeText(getContext(), userName+", "+userEmail, Toast.LENGTH_SHORT).show();

        }
        return root;
    }
}