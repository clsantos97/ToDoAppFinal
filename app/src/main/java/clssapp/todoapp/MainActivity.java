package clssapp.todoapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());
    private FloatingActionButton btnAdd;
    private ListView lvToDo;
    private SQLiteDatabase db;
    public static final int CREATE_ITEM = 1;
    public static final int UPDATE_ITEM = 2;
    private Toolbar toolbar;
    private static ArrayList<TaskItem> mockData = new ArrayList<>();
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init gui elements
        lvToDo = (ListView) findViewById(R.id.lvToDo);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTask();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


        initDb();
        initMockData();
        insertMockData();
        showAll();
    }


    public void initMockData(){
        mockData.add(new TaskItem(1,"Cita Osakidetza","Consulta","01/12/17","10:00"));
        mockData.add(new TaskItem(2,"Entrega de proyecto","Presentacion aula 21","05/12/17","09:00"));
        mockData.add(new TaskItem(3,"Devolver libro","Devolver El ultimo mohicano a biblioteca ","10/12/17","17:00"));
    }
    public void initDb() {

        // Init DB
        db = openOrCreateDatabase("ToDoListDb", Context.MODE_PRIVATE, null);

        db.execSQL("DROP TABLE item");
        db.execSQL("CREATE TABLE IF NOT EXISTS item(" +
                "item_id INTEGER PRIMARY KEY,task VARCHAR,description VARCHAR, date VARCHAR, time VARCHAR, done BOOLEAN, result BOOLEAN, resmsg VARCHAR);");
        db.execSQL("DELETE FROM item");

        //if (!isDbItemsEmpty()) {

        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CREATE_ITEM) {
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente la provincia
                //t.setText("Se ha seleccionado:\n"+data.getStringExtra("PROVINCIA"));
                try {
                    db.execSQL("INSERT INTO item (task, description, date, time) VALUES ('" + data.getStringExtra("TASK") + "','" +
                            data.getStringExtra("DESCRIPTION") + "','" +
                            data.getStringExtra("DATE") + "','" +
                            data.getStringExtra("TIME") + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("New task insert error.");
                }

                showAll();
            } else {
                logger.info("New res task insert error.");
            }
        }else if(requestCode == UPDATE_ITEM){
            if (resultCode == RESULT_OK) {
                // se seleccionó correctamente la provincia
                //t.setText("Se ha seleccionado:\n"+data.getStringExtra("PROVINCIA"));
                try {
                    db.execSQL("INSERT INTO item (task, description, date, time) VALUES ('" + data.getStringExtra("TASK") + "','" +
                            data.getStringExtra("DESCRIPTION") + "','" +
                            data.getStringExtra("DATE") + "','" +
                            data.getStringExtra("TIME") + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("New task insert error.");
                }

                showAll();
            } else {
                logger.info("New res task insert error.");
            }
        }
    }

    /**
     * Muestra listado de discos
     */
    public void showAll() {
        /*ArrayAdapter<String> adaptador;*/
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        Cursor c = db.rawQuery("SELECT * FROM item", null);

        if (c.getCount() == 0) {
            //lista.add("No hay registros");
        } else {
            while (c.moveToNext()) {
                taskList.add(new TaskItem(Integer.parseInt(c.getString(0)), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4), Boolean.parseBoolean(c.getString(5)),
                        Boolean.parseBoolean(c.getString(6)), c.getString(7)));
                System.out.println(c.getString(5));
            }
            //System.out.println(c.getString(0) + " " + c.getString(1) + " - " + c.getString(2) + "   " + c.getString(3) + "  " + c.getString(4));
        }

        // Create the adapter to convert the array to views
        TaskAdapter taskAdapter = new TaskAdapter(this, taskList);
        // Attach the adapter to a ListView

        lvToDo.setAdapter(taskAdapter);


        c.close();
    }

    public void showByQuery(String query) {
        /*ArrayAdapter<String> adaptador;*/
        ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();
        Cursor c = db.rawQuery(query, null);

        if (c.getCount() == 0) {
            //lista.add("No hay registros");
        } else {
            while (c.moveToNext()) {
                taskList.add(new TaskItem(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2), c.getString(3), c.getString(4)));
            }
            //System.out.println(c.getString(0) + " " + c.getString(1) + " - " + c.getString(2) + "   " + c.getString(3) + "  " + c.getString(4));
        }

        // Create the adapter to convert the array to views
        TaskAdapter taskAdapter = new TaskAdapter(this, taskList);
        // Attach the adapter to a ListView

        lvToDo.setAdapter(taskAdapter);


        c.close();
    }
    public boolean isDbItemsEmpty() {
        boolean res;
        Cursor c = db.rawQuery("SELECT * FROM item", null);

        if (c.getCount() == 0) {
            res = true;
        } else {
            res = false;
        }
        c.close();
        return res;
    }

    public void insertMockData() {
        try {
            String query;
            for (TaskItem mockTask:mockData) {
                query = "INSERT INTO item (task, description, date, time) " +
                        "VALUES ('"+mockTask.getTask()+"','"+mockTask.getDescription()+"','"+mockTask.getDate()+"','"+mockTask.getTime()+"')";
                try {
                    db.execSQL(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Error al insertar mockdata.");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error al añadir datos de prueba");
        }
    }

    public void openNewTask() {
        Intent intent = new Intent(this, NewTask.class);
        startActivityForResult(intent, CREATE_ITEM);
    }

    public void openUpdateTask() {
        Intent intent = new Intent(this, UpdateTask.class);
        startActivityForResult(intent, UPDATE_ITEM);
    }

    public void showDialogDone(TaskItem taskItem){
        DialogDone dialogDone = new DialogDone();
        dialogDone.setTaskItem(taskItem);
        dialogDone.show(getFragmentManager(),"dialog_done");
    }

    public void setDone(boolean value, boolean res, String msg, int id){
        try {
            String query = "UPDATE item SET done='"+value+"',result='"+res+"',resmsg='"+msg+"' " +
                        "WHERE item_id='"+id+"'";
                try {
                    db.execSQL(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Update error.");
                }


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Update error.");
        }
    }

    public static MainActivity getInstance(){
        return instance;
    }

    public void addListeners(){
        lvToDo.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }

        });
    }



}
