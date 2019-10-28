package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tareaprogramada2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    public void registerNewUser(View v){

        Intent intent = new Intent(this, MainActivity.class);
        Intent intent2 = new Intent(this, EditInfoActivity.class);
        finish();
        startActivity(intent);
        startActivity(intent2);
        /*
        if (!isDataValid()){
            System.out.println("Dude what he fuck?");
            return;
        }

        String nameText = name.getText().toString();
        String lastnameText = lastname.getText().toString();
        String emailText = email.getText().toString();
        String passText = pass.getText().toString();

        User user = new User();
        user.name = nameText;
        user.email = emailText;
        user.lastName = lastnameText;
        user.password = passText;

        UserService service = RetrofitClient.getRetrofitInstance().create(UserService.class);

        Call<ResponseBody> call = service.saveUser(user);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                JSONObject message;
                if (response.body() != null) {
                    try {
                        message = new JSONObject(response.body().string());
                        String status = (String) message.get("operation");
                        boolean sucessful = status.equals("sucessful");
                        if (sucessful) {
                            clearData();
                            Toast.makeText(getBaseContext(), "Restaurante agregado con exito!" , Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText(getBaseContext(), "Revise los datos" , Toast.LENGTH_SHORT ).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("On failure statment");
            }
        });*/


    }

    public void cancel(View v){
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
