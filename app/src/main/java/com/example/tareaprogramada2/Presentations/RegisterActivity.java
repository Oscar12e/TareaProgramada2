package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static java.util.Arrays.asList;


public class RegisterActivity extends AppCompatActivity {
    private EditText name, lastname, email, password, passwordConfirm;
    private FirebaseAuth mAuth;

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

        //Crear referencia el usuario en la BD

        Intent intent = new Intent(this, MainActivity.class);
        Intent intent2 = new Intent(this, EditInfoActivity.class);
        finish();
        startActivity(intent);
        startActivity(intent2);
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

    }
}
