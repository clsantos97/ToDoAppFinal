package clssapp.todoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Created by Carlos on 11/22/2017.
 */
public class DialogDone extends DialogFragment implements Button.OnClickListener{
    DialogDoneInterface ddi;
    TaskItem taskItem;
    private EditText etMsg;
    private RadioGroup rbgDone;
    private RadioButton rbPos;
    private RadioButton rbNeg;
    private Button btnAccept;
    private Button btnCancel;

    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_layout_done, container, false);

        findViewsById(rootView);
        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


        getDialog().setTitle("Simple Dialog");
        return rootView;
    }

    private void findViewsById(View rootView) {

        etMsg = (EditText) rootView.findViewById(R.id.etMsg);
        rbgDone = (RadioGroup) rootView.findViewById(R.id.rbgDone);
        rbPos = (RadioButton) rootView.findViewById(R.id.rbPos);
        rbNeg = (RadioButton) rootView.findViewById(R.id.rbNeg);
        btnAccept = (Button) rootView.findViewById(R.id.btnAccept);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
    }


    public void setTaskItem(TaskItem taskItem) {
        this.taskItem = taskItem;
    }

    @Override
    public void onClick(View view) {
        boolean res = false;
        if((Button) view == btnAccept){
            if(rbgDone.getCheckedRadioButtonId()==0){
                res=true;

            }
            MainActivity.getInstance().setDone(true, res, etMsg.getText().toString(),taskItem.getId());
            MainActivity.getInstance().showAll();
        }

        dismiss();
    }

    public interface DialogDoneInterface{
        public void onFinishDialog(boolean value);
    }
}
