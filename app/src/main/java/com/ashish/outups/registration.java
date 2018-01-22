package com.ashish.outups;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registration extends AppCompatActivity {
    private EditText name,email,pass,role,location;
    private Button signup;
    private TextView userlogin;
    private CallbackManager mCallbackManager;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final String TAG="FACELOG";
    public Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        //Firebase.setAndroidContext(this);




        mRef=new Firebase("https://outups.firebaseio.com/");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                {
                    //upload to the database
                  final String user_name=name.getText().toString();
                    final String user_email=email.getText().toString();
                    final String user_role=role.getText().toString();
                    final String user_location=location.getText().toString();
                    final String user_password=pass.getText().toString();




                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()) {
                                Toast.makeText(registration.this, "Registration sucessful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(registration.this,MainActivity.class));

                                 Firebase mRefChild=mRef.child("User");
                                // mRefChild=new Firebase("https://outups.firebaseio.com/User");
                                Firebase key=mRefChild.child(firebaseAuth.getUid().toString());
                                // Firebase mRefChild1=mRefChild.child("uid");
                                 Firebase mRefChild2=key.child("name");
                                 Firebase mRefChild3=key.child("email");
                                Firebase mRefChild4=key.child("role");
                                Firebase mRefChild5=key.child("location");

                                //key.setValue();
                                mRefChild2.setValue(user_name);
                                mRefChild3.setValue(user_email);
                                mRefChild4.setValue(user_role);
                                mRefChild5.setValue(user_location);


                            } else {
                                Toast.makeText(registration.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }
                        });

                }
            }
        });
        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registration.this,MainActivity.class));
            }
        });
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


    }
    private void setupUIViews(){
        name=(EditText)findViewById(R.id.editText);
        email=(EditText)findViewById(R.id.editText3);
        pass=(EditText)findViewById(R.id.editText5);
        signup=(Button) findViewById(R.id.button2);
        userlogin=(TextView)findViewById(R.id.textView6);
        location=(EditText) findViewById(R.id.editText7);
        role=(EditText)findViewById(R.id.editText6);
    }
    private boolean validate(){
        boolean result=false;
        String uname= name.getText().toString();
        String uemail= email.getText().toString();
        String upass= pass.getText().toString();
        String urole=role.getText().toString();
        String ulocation=location.getText().toString();


        if(uname.isEmpty()&&uemail.isEmpty()&&upass.isEmpty()&&urole.isEmpty()&&ulocation.isEmpty()) {
            Toast.makeText(this, "please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result =true;
        }
        return result;


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleFacebookAccessToken(AccessToken token) {
        progressDialog.setMessage("verifying");
        progressDialog.show();
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUI();
        }

    }
    private void updateUI() {
        Toast.makeText(registration.this, "you are logged in", Toast.LENGTH_LONG).show();
        startActivity(new Intent(registration.this,logged_in.class));
        progressDialog.dismiss();
        finish();


    }


}
