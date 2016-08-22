package com.testography.shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.UUID;

import io.realm.Realm;

public class ItemActivity extends AppCompatActivity {

    private EditText mInputItemName;
    private EditText mInputItemQuantity;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        if (getIntent().hasExtra("TITLE")) {
            setTitle(getIntent().getStringExtra("TITLE"));
        }

        mInputItemName = (EditText) findViewById(R.id.input_item_name);
        mInputItemQuantity = (EditText) findViewById(R.id.input_item_quantity);

        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_action) {

            mRealm.beginTransaction();

            ShoppingItem shoppingItem = mRealm.createObject(ShoppingItem.class);

            shoppingItem.setName(mInputItemName.getText().toString());
            shoppingItem.setQuantity(mInputItemQuantity.getText().toString());
            shoppingItem.setCompleted(false);
            shoppingItem.setId(UUID.randomUUID().toString());
            shoppingItem.setTimestamp(System.currentTimeMillis());

            mRealm.commitTransaction();

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
