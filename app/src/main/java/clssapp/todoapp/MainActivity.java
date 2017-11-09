package clssapp.todoapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());
    private FloatingActionButton btnAdd;
    private ListView lvToDo;
    private SQLiteDatabase db;
    public static final int CREATE_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        listar();
    }

    public void initDb(){

        // Init DB
        db = openOrCreateDatabase("ToDoListDb", Context.MODE_PRIVATE, null);

        //db.execSQL("DROP TABLE item");
        db.execSQL("CREATE TABLE IF NOT EXISTS item(" +
                "item_id INTEGER PRIMARY KEY,name VARCHAR,task VARCHAR, date VARCHAR, time VARCHAR);");
        db.execSQL("DELETE FROM item");

        if (!isDbItemsEmpty()) {
            insertMockData();
        }
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
                    db.execSQL("INSERT INTO item (name, task, date, time) VALUES ('" + data.getStringExtra("NAME") + "','" +
                            data.getStringExtra("TASK") + "','" +
                            data.getStringExtra("DATE") + "','" +
                            data.getStringExtra("TIME") + "')");

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("New task insert error.");
                }

                listar();
            } else {
                logger.info("New res task insert error.");
            }
        }
    }

    /**
     * Muestra listado de discos
     */
    public void listar() {
        ArrayAdapter<String> adaptador;
        List<String> lista = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT * FROM item", null);

        if (c.getCount() == 0) {
            lista.add("No hay registros");
        } else {
            while (c.moveToNext())
                lista.add(c.getString(0) + " " + c.getString(1) + " - " + c.getString(2) + "   " + c.getString(3) + "  " + c.getString(4));
        }

        adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.default_listview, lista);
        lvToDo.setAdapter(adaptador);
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
            for (int i = 0; i < 5; i++) {
                try {
                    //db.execSQL("INSERT INTO item VALUES (" + i + 1 + ",'" + "Task" + "','" + "Task Description" + "','" + "08/11/2017" + "','" + "17:00" + "')");
                    db.execSQL("INSERT INTO item (name, task, date, time) VALUES ('Task','TaskDescription','08/11/2017','18:00')");
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

    public void openNewTask(){
        Intent intent = new Intent(this, NewTask.class);
        startActivityForResult(intent, CREATE_ITEM);
    }

}
