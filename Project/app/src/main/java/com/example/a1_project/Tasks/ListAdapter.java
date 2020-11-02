package com.example.a1_project.Tasks;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.a1_project.R;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ToDoTask> implements View.OnClickListener{


    private ArrayList<ToDoTask> dataSet;
    Context mContext;
    private int lastPosition = -1;

    // shared preference values
    boolean showReminder;

    // View lookup cache
    private static class ViewHolder {
        TextView txtId;
        TextView txtTodo;
        TextView txtLocation;
        ImageView imgCompleted;

        Button task_delete;
    }

    public ListAdapter(Context context, ArrayList<ToDoTask> data) {
        super(context, R.layout.task_listview_row, data);
        this.dataSet = data;
        this.mContext = context;

        // read the Preferences
        SharedPreferences prefs = context.getSharedPreferences("com.example1.a1_project", Context.MODE_PRIVATE);
        showReminder=prefs.getBoolean("showReminder",showReminder);

        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get the data item for this position
        ToDoTask dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_listview_row, null);
            viewHolder = new ViewHolder();
            viewHolder.txtId = (TextView) convertView.findViewById(R.id.task_id);
            viewHolder.txtTodo = (TextView) convertView.findViewById(R.id.task_title);
            viewHolder.txtLocation = (TextView) convertView.findViewById(R.id.task_location);
            viewHolder.imgCompleted = (ImageView) convertView.findViewById(R.id.imgView_completed);

            //viewHolder.task_delete = (Button) convertView.findViewById(R.id.task_delete);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;


        //viewHolder.task_delete.setTag(dataModel.getId());
        viewHolder.txtTodo.setText(dataModel.getTodo().toString());
        viewHolder.txtLocation.setText(dataModel.getLocation().toString());

        //
        if (dataModel.getCompleted()==1){
            viewHolder.imgCompleted.setImageResource(R.drawable.completed); // if the task is completed show the image as checked

        } else {
            viewHolder.imgCompleted.setImageResource(R.drawable.not_completed); // if the task is incomplete show the image as unchecked.

        }
        viewHolder.imgCompleted.setOnClickListener(this); // register the check image for click listener
        viewHolder.imgCompleted.setTag(position); // get the position id of the checke item from the listview.



        return convertView;
    }

    @Override
    public void onClick(View v) { // onclick listener for the checkbox image

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        ToDoTask dataModel=(ToDoTask)object;

        switch (v.getId()) {
            case R.id.imgView_completed:

                DatabaseManager dbm = new DatabaseManager(getContext());

                if (dataModel.getCompleted() == 0) {

                    //있는 자리만큼 null더하고, 뺴는 것임
                    ToDoTask t = new ToDoTask(dataModel.getId(), null, null, 1); // if the task is incomplete, click to check the box
                    dbm.updateToDo(t);
                } else {
                    ToDoTask t = new ToDoTask(dataModel.getId(), null, null, 0); // if the task is completed, click to uncheck the box
                    dbm.updateToDo(t);
                }

                ArrayList<ToDoTask> todoList = dbm.getAllToDosAL();

                this.dataSet.clear();
                this.dataSet.addAll(todoList);
                this.notifyDataSetChanged();
                break;
        }
    }
}


