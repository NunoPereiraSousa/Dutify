package com.example.dutify.RecyclerViewAdapterProjectsTeamMember;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dutify.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TeamMembersViewAdapter extends RecyclerView.Adapter<TeamMembersViewAdapter.ViewHolder> {

    ImageView personImageHolder;
    private List<TeamMember> data;
    private final TeamMembersViewClickInterface teamMembersViewClickInterface;

    public TeamMembersViewAdapter(List<TeamMember> data, TeamMembersViewClickInterface teamMembersViewClickInterface) {
        this.teamMembersViewClickInterface = teamMembersViewClickInterface;
        this.data = data;
    }

    @Override
    public TeamMembersViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.team_member_structure, parent, false); // Define the data display layout
        return new TeamMembersViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMembersViewAdapter.ViewHolder holder, int position) {
        TeamMember myTeamMember = data.get(position);
        holder.memberNameTxt.setText(myTeamMember.getPersonName());
        Picasso.get()
                .load(myTeamMember.getPhotoUrl())
                .resize(256, 256)
                .centerCrop()
                .into(personImageHolder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView memberNameTxt;

        ViewHolder(View itemView) {
            super(itemView);

            memberNameTxt = itemView.findViewById(R.id.memberNameTxt);
            personImageHolder = itemView.findViewById(R.id.personImageHolder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    teamMembersViewClickInterface.onTeamMemberClick(getAdapterPosition());
                }
            });
        }
    }
}
