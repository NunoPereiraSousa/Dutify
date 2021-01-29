package com.example.dutify.RecyleViewAdapterProjectsCard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;
import com.example.dutify.RecyclerViewAdapterProfileAwards.ProfileAwardsViewClickInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProjectsViewAdapter extends RecyclerView.Adapter<ProjectsViewAdapter.ViewHolder> {
    private List<Project> data;
    private final ProjectsViewClickInterface projectsViewClickInterface;
    ImageView teamMember1;
    ImageView teamMember2;
    ImageView teamMember3;


    public ProjectsViewAdapter(List<Project> data, ProjectsViewClickInterface projectsViewClickInterface) {
        this.projectsViewClickInterface = projectsViewClickInterface;
        this.data = data;
    }

    @Override
    public ProjectsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.projects_card, parent, false); // Define the data display layout
        view.setBackgroundResource(R.drawable.shadow_categories);
        return new ProjectsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewAdapter.ViewHolder holder, int position) {
        Project myProject = data.get(position);

        int totalTask = myProject.getTotalTask();
        int totalToDoTask = myProject.getTotalTasksTodo();
        holder.progressBar.getLayoutParams().width = totalToDoTask * holder.progressBar.getLayoutParams().width / totalTask;
        holder.taskCounterTxt.setText(totalToDoTask + " / " + totalTask);
        holder.projectTitleTxt.setText(myProject.getTitle());
        holder.teamNameTxt.setText(String.valueOf(myProject.getTeamName()));
        holder.cardHolder.setBackgroundColor(Color.parseColor(myProject.getColor()));
        holder.mainLayoutProjectCard.setBackgroundResource(R.drawable.shadow_categories);
        String dayLeftSuffix = " days left";
        if (myProject.getDaysLeft().equals("1")) {
            dayLeftSuffix = " day left";
        }

        holder.daysLeftTxt.setText(myProject.getDaysLeft() + dayLeftSuffix);
        Picasso.get()
                .load(myProject.getPictureUrl1())
                .resize(256, 256)
                .centerCrop()
                .into(teamMember1);

        Picasso.get()
                .load(myProject.getPictureUrl2())
                .resize(256, 256)
                .centerCrop()
                .into(teamMember2);

        Picasso.get()
                .load(myProject.getPictureUrl3())
                .resize(256, 256)
                .centerCrop()
                .into(teamMember3);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView projectTitleTxt;
        TextView teamNameTxt;
        LinearLayout cardHolder;
        LinearLayout mainLayoutProjectCard;
        TextView taskCounterTxt;
        TextView daysLeftTxt;
        View progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            daysLeftTxt = itemView.findViewById(R.id.daysLeftTxt);
            progressBar = itemView.findViewById(R.id.progressBar);
            taskCounterTxt = itemView.findViewById(R.id.taskCounterTxt);
            cardHolder = itemView.findViewById(R.id.cardHolder);
            mainLayoutProjectCard = itemView.findViewById(R.id.mainLayoutProjectCard);
            teamMember1 = itemView.findViewById(R.id.teamMember1);
            teamMember2 = itemView.findViewById(R.id.teamMember2);
            teamMember3 = itemView.findViewById(R.id.teamMember3);
            projectTitleTxt = itemView.findViewById(R.id.projectTitleTxt);
            teamNameTxt = itemView.findViewById(R.id.teamNameTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    projectsViewClickInterface.onProjectCardClick(getAdapterPosition());
                }
            });
        }
    }
}
