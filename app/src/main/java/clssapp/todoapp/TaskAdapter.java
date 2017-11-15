package clssapp.todoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 15/11/2017.
 */

public class TaskAdapter extends ArrayAdapter<TaskItem> {
    public TaskAdapter(Context context, ArrayList<TaskItem> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskItem taskItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.default_listview, parent, false);
        }
        // Lookup view for data population
        TextView tvTask = (TextView) convertView.findViewById(R.id.tvTask);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        // Populate the data into the template view using the data object
        tvTask.setText(taskItem.getTask());
        tvDate.setText(taskItem.getDate());
        tvTime.setText(taskItem.getTime());
        tvDesc.setText(taskItem.getDescription());
        // Return the completed view to render on screen
        return convertView;
    }
}
