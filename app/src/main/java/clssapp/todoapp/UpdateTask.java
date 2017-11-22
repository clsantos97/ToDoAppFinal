package clssapp.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class UpdateTask extends AppCompatActivity implements View.OnClickListener {

    private static final Logger logger = Logger.getLogger(UpdateTask.class.getName());
    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    private Calendar cal;

    // GUI references
    private DatePickerDialog datePickerDialog;
    private EditText etTask;
    private EditText etDate;
    private EditText etDesc;
    private ImageButton btnDate;
    private CheckBox cbDate;
    private LinearLayout lyDate;
    private TaskItem taskItem = new TaskItem();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        // Setup action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewsById();
        addListeners();

        etDate.setInputType(InputType.TYPE_NULL);
        etDate.setTextIsSelectable(false);

        cal= Calendar.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
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
        //etDate.setInputType(InputType.TYPE_NULL);
        etDate.setOnClickListener(this);

        etDesc = (EditText) findViewById(R.id.etDesc);
        etDesc.setInputType(InputType.TYPE_NULL);
        //etDesc.setOnClickListener(this);

        btnDate = (ImageButton) findViewById(R.id.btnDate);
        btnDate.setOnClickListener(this);

        cbDate = (CheckBox) findViewById(R.id.cbDate);
        lyDate = (LinearLayout) findViewById(R.id.lyDate);
    }


    @Override
    public void onClick(View view) {
        if(view == etDate || view == btnDate){
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

    public void updateTask(MenuItem item){
            // TODO EditText Validations

            if(formValid()){
                Intent i = new Intent();
                try{
                    i.putExtra("TASK", etTask.getText().toString());
                    i.putExtra("DESCRIPTION", etDesc.getText().toString());

                    i.putExtra("DATE", etDate.getText().toString());
                    i.putExtra("TIME", "15:00");


                    setResult(RESULT_OK, i);
                    finish();
                }catch(Exception ex){
                    ex.printStackTrace();
                    logger.info("Ha ocurrido un error al agregar nueva tarea.");
                }
            }
    }

    private boolean formValid(){
        boolean valid = true;

        if(etTask.getText().equals("")){
            valid=false;
            Toast toast = Toast.makeText(this, "Campo obligatorio.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            etTask.requestFocus();
        }

        return valid;
    }

    /** LISTENERS **/
    public void addListeners(){
        // CB Date
        cbDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lyDate.setVisibility(View.VISIBLE);
                }else{
                    lyDate.setVisibility(View.GONE);
                    etDate.setText("");
                }
            }
        });
    }

    public void setTaskItem(TaskItem taskItem) {
        this.taskItem = taskItem;
        etTask.setText(taskItem.getTask());
        etDate.setText(taskItem.getDate());
        etDesc.setText(taskItem.getDescription());

    }
}
