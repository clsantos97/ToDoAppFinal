package clssapp.todoapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

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
        CheckBox cbItemDone = (CheckBox) convertView.findViewById(R.id.cbItemDone);
        //TextView tvDesc = (TextView) convertView.findViewById(R.id.tv);

        // Populate the data into the template view using the data object
        tvTask.setText(taskItem.getTask());
        tvDate.setText(taskItem.getDate());
        tvTime.setText(taskItem.getTime());
        tvDesc.setText(taskItem.getDescription());

        System.out.println("isdone: "+taskItem.isDone());
        if(taskItem.isDone()){

            cbItemDone.setChecked(true);
        }else{
            cbItemDone.setChecked(false);
        }

        // Return the completed view to render on screen

        // Styling font
        Typeface subtitleTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Bold.ttf");
        Typeface detailTypeFace =
                Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-SemiBoldItalic.ttf");
        Typeface titleTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Bold.otf");

        tvTask.setTypeface(titleTypeFace);
        tvDate.setTypeface(detailTypeFace);
        tvTime.setTypeface(subtitleTypeFace);
        tvDesc.setTypeface(detailTypeFace);

        // Add listeners
        cbItemDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try{
                        final TaskItem taskI = taskItem;
                        MainActivity.getInstance().showDialogDone(taskI);
                    }catch(Exception ex){
                        ex.printStackTrace();

                    }

                }else{
                    // TODO actualizar estado, recargar lista.
                }
            }
        });



        return convertView;
    }
}
