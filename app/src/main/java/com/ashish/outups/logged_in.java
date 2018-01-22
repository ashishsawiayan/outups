package com.ashish.outups;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class logged_in extends AppCompatActivity {
    private Button flogout;
    private FirebaseAuth firebaseAuth;
    Firebase mRef;
    private TextView nameD;
    private TextView emailD;
    private TextView role;
    private TextView location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in2);
        flogout=(Button)findViewById(R.id.logout);
        firebaseAuth=FirebaseAuth.getInstance();
        nameD=(TextView)findViewById(R.id.textViewname);
        emailD=(TextView)findViewById(R.id.textViewemail);
        role=(TextView)findViewById(R.id.textView3);
        location=(TextView)findViewById(R.id.textView11);


        String uid=firebaseAuth.getUid().toString();
        //String name=uid;

       //getWindow().setStatusBarColor(Color.BLUE);
        mRef=new Firebase("https://outups.firebaseio.com/User/"+uid);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map=dataSnapshot.getValue(Map.class);
                //String name=map.get("name");
                nameD.setText(map.get("name"));
                emailD.setText(map.get("email"));
                role.setText(map.get("role"));
                location.setText(map.get("location"));


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
