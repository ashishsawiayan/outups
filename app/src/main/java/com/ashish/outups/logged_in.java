package com.ashish.outups;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class logged_in extends AppCompatActivity {
    private Button flogout;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in2);
        flogout=(Button)findViewById(R.id.logout);
        firebaseAuth=FirebaseAuth.getInstance();
       getWindow().setStatusBarColor(Color.WHITE);

        flogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                //startActivity(new Intent(logged_in.this,MainActivity.class));
                updateUI();

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser ==null)
        {
           updateUI();
       }

    }

    private void updateUI() {
        Toast.makeText(logged_in.this, "you are logged out", Toast.LENGTH_LONG).show();
        //firebaseAuth.signOut();
        startActivity(new Intent(logged_in.this,MainActivity.class));
        finish();
    }
}
