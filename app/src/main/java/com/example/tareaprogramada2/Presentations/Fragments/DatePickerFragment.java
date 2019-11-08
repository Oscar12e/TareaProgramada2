package com.example.tareaprogramada2.Presentations.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tareaprogramada2.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String TEXTVIEW_ID = "txt_id";
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // TODO: Rename and change types of parameters
    private int year, month, day, txt_id;



    public DatePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param year Parameter 1.
     * @param month Parameter 2.
     * @return A new instance of fragment DatePickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatePickerFragment newInstance(int year,int month, int day, int txt_id) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        System.out.println("Saving " + month);
        args.putInt(YEAR, year);
        args.putInt(MONTH, month);
        args.putInt(DAY, day);
        args.putInt(TEXTVIEW_ID, txt_id);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            System.out.println("Non null arguments ");
            year = getArguments().getInt(YEAR);
            month = getArguments().getInt(MONTH);
            day = getArguments().getInt(DAY);
        } else {
            System.out.println("Null calendar");

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        System.out.println( year + " " + month + " " + day);

        return new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog, this, year, month+1, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

        if (getArguments() == null) {
            System.out.println("Err");
            return;
        }

        txt_id = getArguments().getInt(TEXTVIEW_ID);
        TextView txt = getActivity().findViewById(txt_id);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        String dateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        txt.setText(dateString);
    }


}
