package clssapp.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTask extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat dateFormatter;
    private Calendar cal;

    // GUI references
    private DatePickerDialog datePickerDialog;
    private EditText etTask;
    private EditText etDate;
    private EditText etDesc;
    private Button btnAdd;
    private ImageButton btnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewsById();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal= Calendar.getInstance();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewsById() {
        etTask = (EditText) findViewById(R.id.etTask);
        etTask.setInputType(InputType.TYPE_NULL);
        etTask.requestFocus();

        etDate = (EditText) findViewById(R.id.etDate);
        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setOnClickListener(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnDate = (ImageButton) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(view == btnAdd){
            addTask();
        }else if(view == etDate || view == btnDate){
            showDateDialog();
        }

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

    public void addTask(){
            // TODO EditText Validations
            Intent i = new Intent();

            i.putExtra("TASK", etTask.getText().toString());
            i.putExtra("DATE", etDate.getText().toString());
            i.putExtra("TIME", "15:00");
            i.putExtra("DESCRIPTION", etDate.getText().toString());

            setResult(RESULT_OK, i);
            finish();
    }
}
