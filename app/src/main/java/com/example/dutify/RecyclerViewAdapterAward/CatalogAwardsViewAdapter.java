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
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatalogAwardsViewAdapter extends RecyclerView.Adapter<CatalogAwardsViewAdapter.ViewHolder> {
    private List<CatalogAward> data;
    private LayoutInflater mInflater;
    private final CatalogAwardClickInterface catalogAwardClickInterface;
    private ImageView imageAward;

    public CatalogAwardsViewAdapter(List<CatalogAward> data, CatalogAwardClickInterface catalogAwardClickInterface) {
        this.catalogAwardClickInterface = catalogAwardClickInterface;
        this.data = data;
    }

    @Override
    public CatalogAwardsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.award_card, parent, false);
        return new CatalogAwardsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogAwardsViewAdapter.ViewHolder holder, int position) {
        CatalogAward myAward = data.get(position);

        Picasso.get()
                .load(myAward.getPicture())
                .resize(256, 256)
                .centerCrop()
                .into(imageAward);
        holder.awardName.setText(String.valueOf(myAward.getName()));
        holder.awardPrice.setText(" - " + myAward.getPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView awardName;
        TextView awardPrice;

        ViewHolder(View itemView) {
            super(itemView);
            imageAward = itemView.findViewById(R.id.imageAward);
            awardName = itemView.findViewById(R.id.awardName);
            awardPrice = itemView.findViewById(R.id.awardPrice);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    catalogAwardClickInterface.onAwardLongClick(getAdapterPosition());
                    return false;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    catalogAwardClickInterface.onAwardClick(getAdapterPosition());
                }
            });
        }
    }
}
