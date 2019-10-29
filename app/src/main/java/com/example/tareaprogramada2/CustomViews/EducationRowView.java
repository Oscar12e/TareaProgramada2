package com.example.tareaprogramada2.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.tareaprogramada2.R;

public class EducationRowView extends LinearLayout {
    public EditText field;
    public Button button;

    public EducationRowView(Context context) {
        super(context);
        initialize(context);
    }

    public EducationRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public EducationRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public EducationRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.education_row_view, this);
        System.out.println("Initilizided");
        setVisibility(VISIBLE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        System.out.println("Post Initilizided");

        // Sets the images for the previous and next buttons. Uses
        // built-in images so you don't need to add images, but in
        // a real application your images should be in the
        // application package so they are always available.
        field = findViewById(R.id.tbox_educationField);
        button = findViewById(R.id.btn_delete);
        System.out.println("Post Initilizided 2");
    }
}
