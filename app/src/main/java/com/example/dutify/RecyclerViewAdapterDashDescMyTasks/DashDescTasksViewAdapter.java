package com.example.dutify.RecyclerViewAdapterDashDescMyTasks;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;
import com.example.dutify.RecyleViewAdapterProjectsCard.Project;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewAdapter;
import com.example.dutify.RecyleViewAdapterProjectsCard.ProjectsViewClickInterface;

import java.util.List;

public class DashDescTasksViewAdapter extends RecyclerView.Adapter<DashDescTasksViewAdapter.ViewHolder> {
    private List<Task> data;
    private final DashDescTasksClickInterface dashDescTasksClickInterface;


    public DashDescTasksViewAdapter(List<Task> data, DashDescTasksClickInterface dashDescTasksClickInterface) {
        this.dashDescTasksClickInterface = dashDescTasksClickInterface;
        this.data = data;
    }


    @NonNull
    @Override
    public DashDescTasksViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.dashboard_mytasks_card, parent, false); // Define the data display layout
        return new DashDescTasksViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DashDescTasksViewAdapter.ViewHolder holder, int position) {
        Task myTask = data.get(position);

        if (myTask.getId_progress_status() == 1) {
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_slash, 0, 0, 0);
        } else if (myTask.getId_progress_status() == 2) {
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_correct, 0, 0, 0);
            holder.priceTxt.setTextColor(Color.parseColor("#8E96FF"));
        } else if (myTask.getId_progress_status() == 3) {
            holder.tasksTitleAndStateTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross_x, 0, 0, 0);
            holder.priceTxt.setTextColor(Color.parseColor("#E15554"));
        }
        holder.tasksTitleAndStateTxt.setText(myTask.getTaskTitle());
        holder.priceTxt.setText(String.valueOf(myTask.getTaskPrice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView priceTxt;
        TextView tasksTitleAndStateTxt;

        ViewHolder(View itemView) {
            super(itemView);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            tasksTitleAndStateTxt = itemView.findViewById(R.id.tasksTitleAndStateTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dashDescTasksClickInterface.onTaskCardClick(getAdapterPosition());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dashDescTasksClickInterface.onTaskCardLongClick(getAdapterPosition());
                    return false;
                }
            });
        }
    }


}
