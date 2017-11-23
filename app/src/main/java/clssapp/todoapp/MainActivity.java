package clssapp.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Controller for activity_main.xml and main controller for the app.
 *
 * @author Carlos Santos
 */
public class MainActivity extends AppCompatActivity {

    private static final Logger logger = Logger.getLogger(MainActivity.class.getName());
    private FloatingActionButton btnAdd;
    private ListView lvToDo;
    private SQLiteDatabase db;
    private TextView emptyText;
    public static final int CREATE_ITEM = 1;
    public static final int UPDATE_ITEM = 2;
    private Toolbar toolbar;
    private static ArrayList<TaskItem> mockData = new ArrayList<>();
    private static MainActivity instance;
    private MenuItem config;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList = new ArrayList<TaskItem>();

    /* **************************************** INIT ******************************************** */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init gui elements
        emptyText = (TextView) findViewById(R.id.emptyText);
        lvToDo = (ListView) findViewById(R.id.lvToDo);
        lvToDo.setEmptyView(emptyText);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab);



        initDb();
        initMockData();
        insertMockData();
        getAllItems();
        addListeners();


    }

    /**
     * Creates db and tables
     */
    public void initDb() {

        //this.deleteDatabase("ToDoListDb.db");
        // Init DB
        db = openOrCreateDatabase("ToDoListDb", Context.MODE_PRIVATE, null);

        db.execSQL("DROP TABLE item");
        db.execSQL("CREATE TABLE IF NOT EXISTS item(" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT,task VARCHAR,description VARCHAR, date VARCHAR, time VARCHAR, done BOOLEAN, result BOOLEAN, resmsg VARCHAR);");
        //db.execSQL("DELETE FROM item");

    }

    /**
     * Creates mock data
     */
    public void initMockData() {
        mockData.add(new TaskItem(1, "Cita Osakidetza", "Consulta", "01/12/17", "10:00", false, false, "gg"));
        mockData.add(new TaskItem(2, "Entrega de proyecto", "Presentacion aula 21", "05/12/17", "09:00", false, false, "wp"));
        mockData.add(new TaskItem(3, "Devolver libro", "Devolver El ultimo mohicano a biblioteca ", "10/12/17", "17:00", false, false, ""));
    }

    /* ************************************** LISTENERS ***************************************** */

    /**
     * Add listeners
     */
    public void addListeners() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewTask();
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

    }

    /**
     * Inflate the menu, this adds items to the action bar if it is present.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Actions for menu item selected.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            launchBrowser();
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    /**
     * On activity result actions
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CREATE_ITEM) {
            if (resultCode == RESULT_OK) {
                insertNewTask(data);
            } else {
                logger.info("New res task insert error.");
            }
        } else if (requestCode == UPDATE_ITEM) {
            if (resultCode == RESULT_OK) {
                updateTask(data);
            } else {
                logger.info("New res task insert error.");
            }
        }
    }


    /* *************************************** ACTIONS ****************************************** */

    /**
     * Show all db records
     */
    public void getAllItems() {
        taskList.clear();
        String[] bindArgs = {String.valueOf(false)};

        /*
            IGNORE Android Studio marks "?" inside sqlite statement as an error. It's a bug.
            The application compiles ok. It's just a marking error by AS.
         */
        Cursor c = db.rawQuery("SELECT * FROM item WHERE done=?", bindArgs);

        //Cursor c = db.rawQuery("SELECT * FROM item WHERE item_id=1", null);

        if (c.getCount() == 0) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
            while (c.moveToNext()) {
                taskList.add(new TaskItem(Integer.parseInt(c.getString(0)), c.getString(1),
                        c.getString(2), c.getString(3), c.getString(4), Boolean.parseBoolean(c.getString(5)),
                        Boolean.parseBoolean(c.getString(6)), c.getString(7)));
                //System.out.println(taskList.toString());
            }
        }

        c.close();

        // Create the adapter to convert the array to views
        taskAdapter = new TaskAdapter(this, taskList);
        // Attach the adapter to a ListView
        lvToDo.setAdapter(taskAdapter);

    }

    /**
     * Show items by query
     *
     * @param query
     */
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
        c.close();
        // Create the adapter to convert the array to views
        TaskAdapter taskAdapter = new TaskAdapter(this, taskList);
        // Attach the adapter to a ListView

        lvToDo.setAdapter(taskAdapter);


    }

    /**
     * Check if db is empty
     *
     * @return
     */
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

    /**
     * Insert mock data to the database
     */
    public void insertMockData() {
        try {
            String query;
            for (TaskItem mockTask : mockData) {
                query = "INSERT INTO item (task, description, date, time, done, result, resmsg) " +
                        "VALUES ('" + mockTask.getTask() + "','" + mockTask.getDescription() +
                        "','" + mockTask.getDate() + "','" + mockTask.getTime() +
                        "','" + mockTask.isDone() + "','" + mockTask.isRes() + "','" + mockTask.getResmsg() + "')";
                try {
                    db.execSQL(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("Error al insertar mockdata.");
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("Error al añadir datos de prueba");
        }
    }

    /**
     * Launch new task activity
     */
    public void openNewTask() {
        Intent intent = new Intent(this, NewTask.class);
        startActivityForResult(intent, CREATE_ITEM);
    }

    /**
     * Launch update task activity
     */
    public void openUpdateTask(TaskItem taskItem) {
        Intent intent = new Intent(this, UpdateTask.class);
        // Passing an object to another activity
        intent.putExtra("TaskItem",taskItem);
        startActivityForResult(intent, UPDATE_ITEM);
    }

    /**
     * Show dialog for task done.
     *
     * @param taskItem task object
     */
    public void showDialogDone(TaskItem taskItem) {
        DialogDone dialogDone = new DialogDone();
        dialogDone.setTaskItem(taskItem);
        dialogDone.show(getFragmentManager(), "dialog_done");
    }

    /**
     * Set task done
     *
     * @param value is done
     * @param res
     * @param msg
     * @param id
     */
    public void setDone(boolean value, boolean res, String msg, int id) {
        try {
            String[] bindArgs = {String.valueOf(value), String.valueOf(res), msg, String.valueOf(id)};

            /*
            IGNORE Android Studio marks "?" inside sqlite statement as an error. It's a bug.
            The application compiles ok. It's just a marking error by AS.
         */
            db.execSQL("UPDATE item SET done=?, result=?, resmsg=? WHERE item_id=?", bindArgs);
            reloadListView();

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Update error.");
        }
    }

    /**
     * Get activity instance
     *
     * @return MainActivity instance
     */
    public static MainActivity getInstance() {
        return instance;
    }

    /**
     * Launch browser
     */
    public void launchBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.google.es"));
        startActivity(browserIntent);
    }

    public void reloadListView() {
        /*taskList.clear();
        taskAdapter.clear();*/
        getAllItems();
        /*taskAdapter.addAll(taskList);
        taskAdapter.notifyDataSetChanged();*/
    }

    public void insertNewTask(Intent data){
        try {
            db.execSQL("INSERT INTO item (task, description, date, time, done) VALUES ('" + data.getStringExtra("TASK") + "','" +
                    data.getStringExtra("DESCRIPTION") + "','" +
                    data.getStringExtra("DATE") + "','" +
                    data.getStringExtra("TIME") + "','"+false+"')");
            reloadListView();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("New task insert error.");
        }

    }


    public void updateTask(Intent data){
        try {
            db.execSQL("INSERT INTO item (task, description, date, time) VALUES ('" + data.getStringExtra("TASK") + "','" +
                    data.getStringExtra("DESCRIPTION") + "','" +
                    data.getStringExtra("DATE") + "','" +
                    data.getStringExtra("TIME") + "')");

            String[] bindArgs = {data.getStringExtra("TASK"), data.getStringExtra("DESCRIPTION"), data.getStringExtra("DATE"), data.getStringExtra("TIME"),data.getStringExtra("ID")};

            /*
            IGNORE Android Studio marks "?" inside sqlite statement as an error. It's a bug.
            The application compiles ok. It's just a marking error by AS.
         */
            db.execSQL("UPDATE item SET task=?, description=?, date=?, time=? WHERE item_id=?", bindArgs);

            reloadListView();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("New task update error.");
        }
    }

}
