package com.example.dutify.RecyclerViewAdapterProfileAwards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;

import java.util.List;

public class ProfileAwardsViewAdapter extends RecyclerView.Adapter<ProfileAwardsViewAdapter.ViewHolder> {
    private List<Award> data;

    public ProfileAwardsViewAdapter(List<Award> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.profile_awards_card, parent, false); // Define the data display layout
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Award myAward = data.get(position);
        holder.awardNameTxt.setText(myAward.getName());
        holder.priceTxt.setText(String.valueOf(myAward.getPrice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView awardNameTxt;
        TextView priceTxt;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            awardNameTxt = itemView.findViewById(R.id.awardNameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
        }
    }
}
