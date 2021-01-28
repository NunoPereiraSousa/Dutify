package com.example.dutify.RecyclerViewAdapterDashListTaskCard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;

import java.util.List;

public class DashListTasksViewAdapter extends  RecyclerView.Adapter<DashListTasksViewAdapter.ViewHolder> {
    private List<Task> data;
    private final DashListTasksClickInterface dashListTasksClickInterface;


    public DashListTasksViewAdapter(List<Task> data, DashListTasksClickInterface dashListTasksClickInterface) {
        this.dashListTasksClickInterface = dashListTasksClickInterface;
        this.data = data;
    }


    @NonNull
    @Override
    public DashListTasksViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dashboard_list_task_card, parent, false); // Define the data display layout
        return new DashListTasksViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashListTasksViewAdapter.ViewHolder holder, int position) {
        Task myTask = data.get(position);
        if (myTask.getId_progress_status()==1) {
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_slash,0,0,0);
        }else if (myTask.getId_progress_status()==2){
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct,0,0,0);
            holder.priceAndPermissionTxt.setTextColor(Color.parseColor("#8E96FF"));
        }else if (myTask.getId_progress_status()==3){
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross_x,0,0,0);
            holder.priceAndPermissionTxt.setTextColor(Color.parseColor("#E15554"));
        }
        holder.tasksTitleAndStateTxt.setText(myTask.getTaskTitle());
        holder.priceAndPermissionTxt.setText(String.valueOf(myTask.getTaskPrice()));

        if (myTask.getProjectProgressStatus()!=1 ||myTask.getIsPersonalTask()){
            holder.priceAndPermissionTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        }else{
            holder.priceAndPermissionTxt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_right,0);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView priceAndPermissionTxt;
        TextView tasksTitleAndStateTxt;

        ViewHolder(View itemView) {
            super(itemView);
            priceAndPermissionTxt = itemView.findViewById(R.id.priceAndPermissionTxt);
            tasksTitleAndStateTxt = itemView.findViewById(R.id.tasksTitleAndStateTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dashListTasksClickInterface.onTaskCardClick(getAdapterPosition());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dashListTasksClickInterface.onTaskCardLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }



}
