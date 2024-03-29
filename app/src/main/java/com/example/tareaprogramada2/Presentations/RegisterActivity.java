package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static java.util.Arrays.asList;


public class RegisterActivity extends AppCompatActivity {
    private EditText name, lastname, email, password, passwordConfirm;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    public boolean isDataValid(){
        int errors = 0;


        ArrayList<EditText> textBoxes = new ArrayList<>();
        textBoxes.addAll(asList(name, lastname, email, password, passwordConfirm));

        for (EditText textBox : textBoxes){
            if (textBox.getText().toString().equals("")){
                errors++;
                Toast.makeText(this, textBox.getTag().toString() + " no puede quedar vacio", Toast.LENGTH_SHORT).show();
            }
        }

        String passText = password.getText().toString();
        String cPassText = passwordConfirm.getText().toString();

        if (passText.length() < 6){
            errors++;
            Toast.makeText(this, "La contraseña ingresada es muy corta.", Toast.LENGTH_SHORT).show();
        }

        if (!passText.equals(cPassText)){
            errors++;
            Toast.makeText(this, "Contraseña y confirmación de contraseña no coinciden.", Toast.LENGTH_SHORT).show();
        }

        return errors == 0;
    }

    public void registerNewUser(View v){

        if (!isDataValid()){
            System.out.println("Solucione todos los errores antes de intentar de nuevo.");
            //acceptUser();
            return;
        }

        String email = ((EditText) findViewById(R.id.tbox_email)).getText().toString();
        String password = ((EditText) findViewById(R.id.tbox_password)).getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task task) {

                if (!task.isSuccessful()){
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Log.d("TAG", "onComplete: exist_email");
                    } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                        Log.d("TAG", "onComplete: malformed_email");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    acceptUser();
                }
            }
        });


    }

    public void acceptUser(){
        User user = new User();


        user.email = email.getText().toString();
        user.name = name.getText().toString();
        user.lastname = lastname.getText().toString();


        String key = database.push().getKey();
        user._key = key;
        Map<String, Object> map = user.toMap();
        if (key != null){
            database.child(key).setValue(map);
        } else {
            database.push().setValue(map);
        }

        System.out.println("Check the db");
        //Crear referencia el usuario en la BD
        setSessionUser(user.email, key);

    }

    public void setSessionUser(String _email, String _key) {
        System.out.println("Setting the data");
        DatabaseReference usersReference = database.child("users").child(_key);
        final Context context = this;

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                System.out.println("Setting user");
                User user = dataSnapshot.getValue(User.class);
                Session.instance.currentUser = user;
                Intent intent = new Intent(context, MainActivity.class);
                Intent intent2 = new Intent(context, EditInfoActivity.class);
                finish();
                startActivity(intent);
                startActivity(intent2);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOGIN", "Failed to read value.", error.toException());
                System.out.println("LOGIN" + "Failed to read value." + error.toException());
            }
        });
    }

    public void cancel(View v){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.tbox_name);
        lastname = findViewById(R.id.tbox_lastname);
        email = findViewById(R.id.tbox_email);
        password = findViewById(R.id.tbox_password);
        passwordConfirm = findViewById(R.id.tbox_confirmPass);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");
    }
}
