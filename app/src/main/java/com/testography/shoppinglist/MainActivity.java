package com.testography.shoppinglist;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private RecyclerView mShoppingItems;

    private Realm mRealm;

    private List<ShoppingItem> mDataSet;

    private RecyclerView.Adapter mShoppingItemsAdapter = new RecyclerView.Adapter() {

        private final int ACTIVE_VIEW = 1;
        private final int INACTIVE_VIEW = 2;
        private final int SUBHEADER_VIEW = 3;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ACTIVE_VIEW:
                    View v = getLayoutInflater().inflate(R.layout.active_item,
                            parent, false);
                    return new ActiveItemViewHolder(v,
                            (CheckBox) v.findViewById(R.id.item_status),
                            (TextView) v.findViewById(R.id.item_name),
                            (TextView) v.findViewById(R.id.item_quantity),
                            (ImageView) v.findViewById(R.id.item_action)
                    );
                case INACTIVE_VIEW:
                    v = getLayoutInflater().inflate(R.layout
                            .inactive_item, parent, false);
                    return new InactiveItemViewHolder(v,
                            (CheckBox) v.findViewById(R.id.item_status),
                            (TextView) v.findViewById(R.id.item_name),
                            (TextView) v.findViewById(R.id.item_quantity),
                            (ImageView) v.findViewById(R.id.item_action)
                    );
                case SUBHEADER_VIEW:
                    v = getLayoutInflater().inflate(R.layout.subheader, parent, false);
                    return new SubheaderViewHolder(v);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ShoppingItem currentItem = mDataSet.get(position);

            if (currentItem.getTimestamp() == -1) {
                return;
            }

            if (currentItem.isCompleted()) {
                InactiveItemViewHolder h = (InactiveItemViewHolder) holder;
                h.mItemName.setText(currentItem.getName());
                h.mItemName.setPaintFlags(h.mItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                h.mItemAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mRealm.beginTransaction();
                        currentItem.setCompleted(false);
                        currentItem.setTimestamp(System.currentTimeMillis());
                        mRealm.commitTransaction();

                        initializeDataSet();
                        mShoppingItemsAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                ActiveItemViewHolder h = (ActiveItemViewHolder) holder;
                h.mItemName.setText(currentItem.getName());
                h.mItemQuantity.setText(currentItem.getQuantity());
                h.mItemStatus.setChecked(false);

                h.mItemStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean checked) {
                        if (checked) {
                            mRealm.beginTransaction();
                            currentItem.setCompleted(true);
                            currentItem.setTimestamp(System.currentTimeMillis());
                            mRealm.commitTransaction();

                            initializeDataSet();
                            mShoppingItemsAdapter.notifyDataSetChanged();
                        }
                    }
                });
                h.mItemAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, ItemActivity.class);
                        i.putExtra("TITLE", "Edit item");
                        i.putExtra("ITEM_NAME", currentItem.getName());
                        i.putExtra("ITEM_QUANTITY", currentItem.getQuantity());
                        i.putExtra("ITEM_ID", currentItem.getId());
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        @Override
        public int getItemViewType(int position) {
            ShoppingItem currentItem = mDataSet.get(position);

            if (currentItem.getTimestamp() == -1) {
                return SUBHEADER_VIEW;
            }

            if (currentItem.isCompleted()) {
                return INACTIVE_VIEW;
            }

            return ACTIVE_VIEW;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmConfiguration configuration =
                new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);
        mRealm = Realm.getDefaultInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ItemActivity.class);
                i.putExtra("TITLE", "Add item");
                startActivityForResult(i, REQUEST_CODE);
            }
        });

        mShoppingItems = (RecyclerView) findViewById(R.id.shopping_items);
        mShoppingItems.setLayoutManager(new LinearLayoutManager(this));

        initializeDataSet();

        mShoppingItems.setAdapter(mShoppingItemsAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            initializeDataSet();
            mShoppingItemsAdapter.notifyDataSetChanged();
        }
    }

    private void initializeDataSet() {
        mDataSet = new ArrayList<>();

        RealmResults<ShoppingItem> activeItemResults
                = mRealm.where(ShoppingItem.class).equalTo("completed", false)
                .findAllSorted("timestamp", Sort.DESCENDING);

        RealmResults<ShoppingItem> inactiveItemResults
                = mRealm.where(ShoppingItem.class).equalTo("completed", true)
                .findAllSorted("timestamp", Sort.DESCENDING);

        ShoppingItem subheader = new ShoppingItem();
        subheader.setTimestamp(-1);

        for (ShoppingItem item : activeItemResults) {
            mDataSet.add(item);
        }
        mDataSet.add(subheader);
        for (ShoppingItem item : inactiveItemResults) {
            mDataSet.add(item);
        }
    }
}
