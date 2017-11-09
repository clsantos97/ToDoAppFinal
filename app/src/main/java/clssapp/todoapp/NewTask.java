package clssapp.todoapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTask extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter;
    private Calendar cal;

    // GUI references
    private DatePickerDialog datePickerDialog;
    private EditText etName;
    private EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        findViewsById();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal= Calendar.getInstance();

    }

    private void findViewsById() {
        etName = (EditText) findViewById(R.id.etName);
        etName.setInputType(InputType.TYPE_NULL);
        etName.requestFocus();

        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        showDateDialog();
    }

    public void showDateDialog(){
        DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                int s=monthOfYear+1;
                String a = dayOfMonth+"/"+s+"/"+year;
                etDate.setText(""+a);
            }
        };


        DatePickerDialog d = new DatePickerDialog(this, dpd, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        d.show();
    }
}
