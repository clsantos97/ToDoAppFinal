package clssapp.todoapp;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom Adapter for List of TaskItem.
 * Created by Carlos on 15/11/2017.
 *
 * @author Carlos Santos
 */
public class TaskAdapter extends ArrayAdapter<TaskItem> {


    public TaskAdapter(Context context, ArrayList<TaskItem> users) {
        super(context, 0, users);
    }

    /**
     * Setup view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final TaskItem taskItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_listview, parent, false);
        }

        // Lookup view for data population
        TextView tvTask = (TextView) convertView.findViewById(R.id.tvTask);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        final CheckBox cbItemDone = (CheckBox) convertView.findViewById(R.id.cbItemDone);

        // Populate the data into the template view using the data object
        tvTask.setText(taskItem.getTask());
        tvDate.setText(taskItem.getDate());
        tvTime.setText(taskItem.getTime());
        tvDesc.setText(taskItem.getDescription());

        //System.out.println(taskItem.toString());
        if (taskItem.isDone()) {
            cbItemDone.setChecked(true);
        } else {
            cbItemDone.setChecked(false);
        }

        // Styling font
        Typeface subtitleTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Bold.ttf");
        Typeface detailTypeFace =
                Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        Typeface titleTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Bold.otf");

        tvTask.setTypeface(titleTypeFace);
        tvDate.setTypeface(detailTypeFace);
        tvTime.setTypeface(subtitleTypeFace);
        tvDesc.setTypeface(detailTypeFace);

        cbItemDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbItemDone.isChecked()) {
                    try {
                        final TaskItem taskI = taskItem;
                        MainActivity.getInstance().showDialogDone(taskI);
                        cbItemDone.setChecked(false);
                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }
        });

        tvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getInstance().openUpdateTask(taskItem);
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });


        return convertView;
    }

}
