package com.testography.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mShoppingItems;

    private Realm mRealm;

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

        }

        @Override
        public int getItemCount() {
            return 0;
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
                startActivity(i);
            }
        });

        mShoppingItems = (RecyclerView) findViewById(R.id.shopping_items);
        mShoppingItems.setLayoutManager(new LinearLayoutManager(this));
    }

}
