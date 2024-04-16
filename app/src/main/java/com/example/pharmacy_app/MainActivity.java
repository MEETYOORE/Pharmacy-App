package com.example.pharmacy_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.pharmacy_app.activities.HomeActivity;
import com.example.pharmacy_app.activities.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth; // Declare FirebaseAuth instance
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth

        // Load the animation
        Animation slideInAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up_animation);

        // Find the root layout of your activity
        View rootLayout = findViewById(android.R.id.content);

        // Apply the animation to the root layout
        rootLayout.startAnimation(slideInAnimation);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_category, R.id.nav_profile, R.id.nav_offers, R.id.nav_new_products, R.id.nav_my_orders, R.id.nav_my_cart)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Check if user is logged in and adjust drawer item visibility
        adjustDrawerItemsVisibility(navigationView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // if user not logged in hides some options of drawer menu
    private void adjustDrawerItemsVisibility(NavigationView navigationView) {
        Menu navMenu = navigationView.getMenu();
        boolean isLoggedIn = mAuth.getCurrentUser() != null;
        navMenu.findItem(R.id.nav_profile).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_my_orders).setVisible(isLoggedIn);
        navMenu.findItem(R.id.nav_my_cart).setVisible(isLoggedIn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)     // handles sign out menu option click
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            // Sign out from Firebase Authentication
            mAuth.signOut();

            // Navigate to the login or home activity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optionally, finish this activity to prevent going back
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}