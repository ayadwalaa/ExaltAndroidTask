package com.wala.exalttask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashBoardActivity extends AppCompatActivity {
    private TextView welcomingcard;
    private String userFirstName;
    private String userLastName;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        welcomingcard= findViewById(R.id.welcomeCard);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userFirstName = dataSnapshot.child("FirstName").getValue(String.class);
                userLastName = dataSnapshot.child("LastName").getValue(String.class);
                welcomingcard.setText("Hello "+ userFirstName + " " + userLastName + " !\n" +"Have a great day.");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DatabaseError", "Failed to read value.", error.toException());
            }
        });







    }
}
