package com.example.dutify.RecyleViewAdapterProjectsCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;
import com.example.dutify.RecyclerViewAdapterProfileAwards.ProfileAwardsViewClickInterface;

import java.util.List;

public class ProjectsViewAdapter extends RecyclerView.Adapter<ProjectsViewAdapter.ViewHolder> {
    private List<Project> data;
    private ProjectsViewClickInterface projectsViewClickInterface;
    private ProjectsViewAdapter.ItemClickListener mClickListener;

    public ProjectsViewAdapter(List<Project> data, ProjectsViewClickInterface projectsViewClickInterface) {
        this.projectsViewClickInterface = projectsViewClickInterface;
        this.data = data;
    }

    @Override
    public ProjectsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.projects_card, parent, false); // Define the data display layout
        return new ProjectsViewAdapter.ViewHolder(view);
    }

    //This does the action of altering the cards
    @Override
    public void onBindViewHolder(@NonNull ProjectsViewAdapter.ViewHolder holder, int position) {
        Project myProject = data.get(position);
        holder.projectTitleTxt.setText(myProject.getTitle());
        holder.projectCategoryTxt.setText(String.valueOf(myProject.getCategory()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView projectTitleTxt;
        TextView projectCategoryTxt;

        ViewHolder(View itemView) {
            super(itemView);
            // Views that will display our data
            projectTitleTxt = itemView.findViewById(R.id.projectTitleTxt);
            projectCategoryTxt = itemView.findViewById(R.id.projectCategoryTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectsViewClickInterface.onProjectCardClick(getAdapterPosition());
                }
            });
        }
    }

    public String getTitle(int id) {
        return data.get(id).getTitle();
    }

    public void setClickListener(ProjectsViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
