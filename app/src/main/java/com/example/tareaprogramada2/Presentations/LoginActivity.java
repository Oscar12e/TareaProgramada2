package com.example.tareaprogramada2.Presentations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFunctions mFunctions;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFunctions = FirebaseFunctions.getInstance();
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            System.out.println("Email " + currentUser.getEmail());
            setSessionUser(currentUser.getEmail());

        }
    }

    public void loginUser(View view){
        String email = ((EditText) findViewById(R.id.tbox_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.tbox_password)).getText().toString();

        if (email.equals(" ") || password.equals(" ")){
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task task) {
                        if (task.isSuccessful()) {
                            System.out.println("Valid user");
                            FirebaseUser user = ((AuthResult) task.getResult()).getUser();
                            setSessionUser(email);
                        } else {

                        }
                    }
                });
    }

    public void registerUser(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setSessionUser(String _email){
        System.out.println("Setting the data");
        DatabaseReference usersReference = database.child("users");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                System.out.println("Other if this");

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String dsEmail = ds.child("email").getValue(String.class);
                    System.out.println("Reading email " + dsEmail);

                    if (dsEmail.equals(_email)){
                        System.out.println("Found ya");
                        String Id = ds.getKey();
                        User user = ds.getValue(User.class);
                        user._key = Id;
                        Session.instance.currentUser = user;
                        openMainActivity();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOGIN", "Failed to read value.", error.toException());
                System.out.println("LOGIN" + "Failed to read value." + error.toException());
            }
        });


    }

}
