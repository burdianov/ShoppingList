package com.testography.shoppinglist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class InactiveItemViewHolder extends RecyclerView.ViewHolder {

    CheckBox mItemStatus;
    TextView mItemName;
    ImageView mItemAction;

    public InactiveItemViewHolder(View itemView, CheckBox itemStatus, TextView itemName, TextView itemQuantity, ImageView itemAction) {
        super(itemView);
        mItemStatus = itemStatus;
        mItemName = itemName;
        mItemAction = itemAction;
    }
}
