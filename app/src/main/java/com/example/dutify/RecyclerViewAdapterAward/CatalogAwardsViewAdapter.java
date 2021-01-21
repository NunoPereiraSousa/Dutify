package com.example.dutify.RecyclerViewAdapterAward;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dutify.R;
import com.example.dutify.RecyclerViewAdapterProfileAwards.Award;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatalogAwardsViewAdapter extends RecyclerView.Adapter<CatalogAwardsViewAdapter.ViewHolder> {
    private List<CatalogAward> data;
    private LayoutInflater mInflater;
    private CatalogAwardsViewAdapter.ItemClickListener mClickListener;

    public CatalogAwardsViewAdapter(Context context, List<CatalogAward> data) {
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public CatalogAwardsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.award_card, parent, false);
        return new CatalogAwardsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAwardsViewAdapter.ViewHolder holder, int position) {
        CatalogAward myAward = data.get(position);


        /*holder.imageAward.setText(myAward.getPicture());
        Picasso.get()
                .load(myAward.getPicture())
                .resize(256, 256)
                .centerCrop()
                .into(userPicture);*/
        holder.awardName.setText(String.valueOf(myAward.getName()));
        holder.awardPrice.setText(String.valueOf(myAward.getPrice()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageAward;
        TextView awardName;
        TextView awardPrice;

        ViewHolder(View itemView) {
            super(itemView);
            //imageAward = itemView.findViewById(R.id.imageAward);
            awardName = itemView.findViewById(R.id.awardName);
            awardPrice = itemView.findViewById(R.id.awardPrice);
            //itemView.setOnClickListener((View.OnClickListener) this);
        }

        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getName(int id) {
        return data.get(id).getName();
    }

    public void setClickListener(CatalogAwardsViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
