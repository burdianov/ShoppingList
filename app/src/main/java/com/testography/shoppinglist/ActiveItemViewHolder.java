package com.testography.shoppinglist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ActiveItemViewHolder extends RecyclerView.ViewHolder {

    CheckBox mItemStatus;
    TextView mItemName;
    TextView mItemQuantity;
    ImageView mItemAction;

    public ActiveItemViewHolder(View itemView, CheckBox itemStatus, TextView itemName, TextView itemQuantity, ImageView itemAction) {
        super(itemView);
        mItemStatus = itemStatus;
        mItemName = itemName;
        mItemQuantity = itemQuantity;
        mItemAction = itemAction;
    }
}
