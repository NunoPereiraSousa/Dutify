package com.example.dutify.RecyclerViewAdapterTaskCard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;

import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.ViewHolder> {
    private List<Task> data;
    private LayoutInflater mInflater;
    private TaskViewAdapter.ItemClickListener mClickListener;

    public TaskViewAdapter(Context context, List<Task> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public TaskViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tasks_card, parent, false); // Define the data display layout
        return new TaskViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewAdapter.ViewHolder holder, int position) {
        Task myTask = data.get(position);
        holder.projectNameTxt.setText(myTask.getTitle());
        holder.taskDescriptionTxt.setText(String.valueOf(myTask.getDescription()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView taskDescriptionTxt;
        TextView projectNameTxt;

        ViewHolder(View itemView) {
            super(itemView);
            taskDescriptionTxt = itemView.findViewById(R.id.taskDescriptionTxt);
            projectNameTxt = itemView.findViewById(R.id.projectNameTxt);
        }

        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getTitle(int id) {
        return data.get(id).getTitle();
    }

    public void setClickListener(TaskViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
