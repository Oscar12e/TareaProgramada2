package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tareaprogramada2.CustomViews.EducationRowView;
import com.example.tareaprogramada2.Presentations.Fragments.DatePickerFragment;
import com.example.tareaprogramada2.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditInfoActivity extends AppCompatActivity {
    List<EducationRowView> educationRows;
    TableLayout educationTable;
    int rowsAdded;

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        educationRows = new ArrayList<>();
        educationTable = findViewById(R.id.table_education);
        setContentView(R.layout.activity_edit_info);
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

        String birthday = ((TextView) findViewById(R.id.txt_birthday)).getText().toString();
        if (lastName.equals("")){
            Toast.makeText(this, "Debe de ingresar sus apellidos." , Toast.LENGTH_SHORT ).show();
            errors++;
        }

        return errors == 0;
    }

    public void updateUser(View view){
        String name = ((EditText) findViewById(R.id.tbox_name)).getText().toString();
        String birthday = ((TextView) findViewById(R.id.txt_birthday)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.tbox_lastname)).getText().toString();
        String email = ((EditText) findViewById(R.id.tbox_email)).getText().toString();
        String city = ((EditText) findViewById(R.id.tbox_city)).getText().toString();
        String phone = ((EditText) findViewById(R.id.tbox_phone)).getText().toString();
        List<String> educationList = new ArrayList<>();

        for (EducationRowView row : educationRows){
            String education = ((EditText) row.findViewById(R.id.tbox_educationField)).getText().toString();
            educationList.add(education);
        }

    }


    public void showDatePickerDialog(View v){
        DialogFragment newFragment = DatePickerFragment.newInstance(2019, 10,28, R.id.txt_birthday);
        newFragment.show(getSupportFragmentManager(), "datePicker");

        //AIzaSyBEhtYHVK9BrgQgkTncrFfCXc1Nkl9LHJw
    }
}
