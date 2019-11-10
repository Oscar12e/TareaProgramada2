package com.example.tareaprogramada2.Presentations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tareaprogramada2.CustomViews.EducationRowView;
import com.example.tareaprogramada2.Data.GlideApp;
import com.example.tareaprogramada2.Data.MyGlideApp;
import com.example.tareaprogramada2.Models.Session;
import com.example.tareaprogramada2.Models.User;
import com.example.tareaprogramada2.Presentations.Fragments.DatePickerFragment;
import com.example.tareaprogramada2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EditInfoActivity extends AppCompatActivity {

    EditText name, lastname, city, phone, email, genre;
    TextView birthday;
    private ImageView profilePicture;

    List<EducationRowView> educationRows;
    TableLayout educationTable;
    int rowsAdded;
    private static final int GALLERY_INTENT = 101;

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    StorageReference storageReference;
    private DatabaseReference database;

    private Uri profilePictureUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        initialize();
        displayCurrentData();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void initialize(){
        profilePicture = findViewById(R.id.img_profilePic);
        educationTable = findViewById(R.id.table_education);
        educationRows = new ArrayList<>();

        database = FirebaseDatabase.getInstance().getReference("users");

        name = findViewById(R.id.tbox_name);
        lastname = findViewById(R.id.tbox_lastname);
        city = findViewById(R.id.tbox_city);
        phone = findViewById(R.id.tbox_phone);
        email = findViewById(R.id.tbox_email);
        genre = findViewById(R.id.tbox_genre);

        birthday = findViewById(R.id.txt_birthday);
    }


    private void displayCurrentData(){
        User myUser = Session.instance.currentUser;
        System.out.println(myUser.toMap());

        name.setText(myUser.name);
        lastname.setText(myUser.lastname);
        email.setText(myUser.email);
        city.setText(myUser.city);
        genre.setText(myUser.genre);
        birthday.setText(myUser.birthday);
        phone.setText(myUser.phoneNumber);

        for (int i = 0; i < myUser.education.size() ; i++){
            String item = myUser.education.get(i);
            addRow(null);

            EducationRowView row = educationRows.get(i);
            EditText field = row.findViewById(R.id.tbox_educationField);
            field.setText(item);
        }

        storageReference = FirebaseStorage.getInstance().getReference(myUser._key).child(myUser.profilePic);

        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(profilePicture);
    }

    private void saveChanges(View view){
        DatabaseReference usersReference = database.child("users");
    }

    public void addRow(View view){
        String code = "row" + rowsAdded++;
        EducationRowView row = new EducationRowView(this);
        educationRows.add(row);
        row.setTag(code);

        Button btn = row.findViewById(R.id.btn_delete);
        if (btn != null){
            btn.setOnClickListener( v -> removeRow(code));
            educationTable = findViewById(R.id.table_education);
            educationTable.addView(row);
        } else {
            System.out.println("It's null");
            row.button.setOnClickListener( v -> removeRow(code));
            educationTable.addView(row);
        }

    }

    public void removeRow(String tag){
        educationTable = findViewById(R.id.table_education);
        for (EducationRowView row : educationRows){
            if(row.getTag().toString().equals(tag)){
                educationRows.remove(row);
                educationTable.removeView(row);
                System.out.println("Removed row");
                break;
            }
        }
    }

    public boolean isDataValid(){
        String name = ((EditText) findViewById(R.id.tbox_name)).getText().toString();
        int errors = 0;
        if (name.equals("")){
            Toast.makeText(this, "Debe de ingresar un nombre." , Toast.LENGTH_SHORT ).show();
            errors++;
        }

        String lastName = ((EditText) findViewById(R.id.tbox_lastname)).getText().toString();
        if (lastName.equals("")){
            Toast.makeText(this, "Debe de ingresar sus apellidos." , Toast.LENGTH_SHORT ).show();
            errors++;
        }

        String email = this.email.getText().toString();
        if (email.equals("")){
            Toast.makeText(this, "Debe de ingresar una direccion de email." , Toast.LENGTH_SHORT ).show();
            errors++;
        }

        return errors == 0;
    }

    public void updateUser(View view){

        if (!isDataValid()){
            return;
        }

        User myUser = Session.instance.currentUser;

        myUser._key = Session.instance.currentUser._key;
        myUser.name = this.name.getText().toString();
        myUser.birthday = this.birthday.getText().toString();
        myUser.lastname = this.lastname.getText().toString();
        myUser.email = this.email.getText().toString();
        myUser.city = this.city.getText().toString();
        myUser.phoneNumber = this.phone.getText().toString();
        myUser.genre = this.genre.getText().toString();

        List<String> educationList = new ArrayList<>();

        for (EducationRowView row : educationRows){
            String education = ((EditText) row.findViewById(R.id.tbox_educationField)).getText().toString();
            educationList.add(education);
        }

        myUser.education = educationList;

        if (profilePictureUri != null && profilePictureUri.getLastPathSegment() != null){

            StorageReference profPictureRef = FirebaseStorage.getInstance().getReference(myUser._key).child(profilePictureUri.getLastPathSegment());
            UploadTask uploadTask = profPictureRef.putFile(profilePictureUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Got an issue");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myUser.profilePic = profilePictureUri.getLastPathSegment();
                    updateUser(myUser);
                }
            });
        } else {
            updateUser(myUser);
        }
    }


    public void updateUser(User user){
        Map<String, Object> map = user.toMap();
        database.child(user._key).setValue(map); //Update
        finish();
    }

    public void selectProfilePicture(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            profilePicture.setImageURI(uri);
            profilePictureUri = uri;
            System.out.println("Setted");
        } else {
            System.out.println("What??");
        }
    }


    public void showDatePickerDialog(View v){
        Date now = null;
        try {
            now = dateFormat.parse(birthday.getText().toString());
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            DialogFragment newFragment = DatePickerFragment.newInstance(c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), R.id.txt_birthday);
            newFragment.show(getSupportFragmentManager(), "datePicker");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
