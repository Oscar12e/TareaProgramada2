package com.example.tareaprogramada2.Presentations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.tareaprogramada2.CustomViews.EducationRowView;
import com.example.tareaprogramada2.Presentations.Fragments.DatePickerFragment;
import com.example.tareaprogramada2.R;

import java.util.ArrayList;
import java.util.List;

public class EditInfoActivity extends AppCompatActivity {
    List<EducationRowView> educationRows;
    TableLayout educationTable;
    int rowsAdded;

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

    public void validateData(){

    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = DatePickerFragment.newInstance(2019, 10,28, R.id.txt_birthday);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
