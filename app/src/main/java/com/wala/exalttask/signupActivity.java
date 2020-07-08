package com.wala.exalttask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class signupActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText userPassword;
    private EditText userEmail;
    private TextInputLayout email_layout;
    private TextInputLayout fName_layout;
    private TextInputLayout lName_layout;
    private TextInputLayout password_layout;

    public FirebaseAuth mAuth;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName = findViewById(R.id.et_name);
        lastName= findViewById(R.id.et_last_name);
        userEmail= findViewById(R.id.et_reg_email);
        userPassword = findViewById(R.id.et_reg_password);
        signup= findViewById(R.id.btn_register);

        email_layout = findViewById(R.id.email_layout);
        fName_layout= findViewById(R.id.first_name_layout);
        lName_layout= findViewById(R.id.last_name_layout);
        password_layout= findViewById(R.id.password_layout);



        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = userEmail.getText().toString();
                String first_name= firstName.getText().toString();
                String last_name = lastName.getText().toString();
                String pass= userPassword.getText().toString();

                if (email.isEmpty()){
                    //userEmail.setError("Provide a Valid Email.");
                    email_layout.setError("Email cannot be blank.");
                    userEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    userEmail.setError("Invalid Email Address.");
                    userEmail.requestFocus();
                }
                else if (pass.isEmpty()){
                   password_layout.setError("Password cannot be blank.");
                    userPassword.requestFocus();
                }
                else if (pass.length()<=6){
                    password_layout.setError("Password too short.");
                    userPassword.requestFocus();
                }
                else if (first_name.isEmpty() ){
                    fName_layout.setError("First Name Cannot be left blank.");
                    firstName.requestFocus();
                }
                else if ( first_name.length()<=2){
                    fName_layout.setError("Name too short.");
                    firstName.requestFocus();
                }
                else if (last_name.isEmpty()){
                    lName_layout.setError("Last Name Cannot be left blank");
                    lastName.requestFocus();
                }
                else if (last_name.length()<=2){
                    lName_layout.setError("Name too short.");
                    lastName.requestFocus();
                }
                else if (pass.isEmpty() && email.isEmpty() && first_name.isEmpty() && last_name.isEmpty() ){
                    Toast.makeText(signupActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else if(!(pass.isEmpty() && email.isEmpty() && first_name.isEmpty() && last_name.isEmpty())){
                    signupFunction(first_name,last_name,email,pass);
                }
                else{
                    Toast.makeText(signupActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }//on create

    private void signupFunction(final String first_name, final String last_name, final String email, final String pass) {
        Task<AuthResult> task = FirebaseAuth.getInstance().signInAnonymously();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser= mAuth.getCurrentUser();
                            String userId= firebaseUser.getUid();
                            FirebaseDatabase db= FirebaseDatabase.getInstance();
                            DatabaseReference myReference= db.getReference();
                            myReference.child("Users").child(userId).child("FirstName").setValue(first_name);
                            myReference.child("Users").child(userId).child("LastName").setValue(last_name);
                            myReference.child("Users").child(userId).child("Email").setValue(email);
                            //myReference.child("Users").child(userId).child("Password").setValue(pass);
                            Toast.makeText(signupActivity.this, "Sign up complete.", Toast.LENGTH_SHORT).show();
                            Intent loginIntent= new Intent(signupActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                        } else {
                            if(task.getException().getClass().getCanonicalName().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException")) {
                                Toast.makeText(signupActivity.this, "This email is already used!", Toast.LENGTH_SHORT).show();
                            }
                           else{
                           Toast.makeText(signupActivity.this, "Sign up failed!", Toast.LENGTH_SHORT).show();
                           }
                        }


                    }
                });



    }//signupFunction
}//signupactivity
