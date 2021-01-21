package com.example.dutify.RecyclerViewAdapterProfileAwards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.example.dutify.R;

import java.util.List;

public class ProfileAwardsViewAdapter extends RecyclerView.Adapter<ProfileAwardsViewAdapter.ViewHolder> {
    private List<Award> data;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public ProfileAwardsViewAdapter(Context context, List<Award> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.profile_awards_card, parent, false); // Define the data display layout
        return new ViewHolder(view);
    }

    //This does the action of altering the cards
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView awardNameTxt;
        TextView priceTxt;

        ViewHolder(View itemView) {
            super(itemView);
            // Views that will display our data
            awardNameTxt = itemView.findViewById(R.id.awardNameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            //itemView.setOnClickListener((View.OnClickListener) this);
        }

        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getName(int id) {
        return data.get(id).getName();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
