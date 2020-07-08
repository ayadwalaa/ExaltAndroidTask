package com.wala.exalttask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText userName; //userName
    private EditText userPassword;
    public FirebaseAuth mAuth;
    private Button Login;
    private Button signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.et_email);
        userPassword = findViewById(R.id.et_password);
        Login= findViewById(R.id.btn_login);
        signup=findViewById(R.id.tv_register);

        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent= new Intent (MainActivity.this, signupActivity.class);
                startActivity(signupintent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = userName.getText().toString();
                String pass= userPassword.getText().toString();
                if (email.isEmpty()){
                    userName.setError("Provide a Valid Email.");
                    userName.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userName.setError("Invalid Email Address.");
                    userName.requestFocus();
                }
                else if (pass.isEmpty()){
                    userPassword.setError("Please enter your password");
                    userPassword.requestFocus();
                }
                else if (pass.isEmpty() && email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!(pass.isEmpty() && email.isEmpty()) ){
                    loginFunction(email, pass);
                    //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void loginFunction(String email, String pass){
        Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent dashBoardIntent = new Intent(MainActivity.this, dashBoardActivity.class);
                    startActivity(dashBoardIntent);
                    AuthResult result = task.getResult();
                } else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    Exception exception = task.getException();
                }

            }
        });

    }



}
