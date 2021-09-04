package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int EDIT_REQUEST_CODE = 12;
    List<String> items;

    Button btn_add;
    EditText txt_item;
    RecyclerView rv_items;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        txt_item = findViewById(R.id.txt_item);
        rv_items = findViewById(R.id.rv_items);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent toEditActivity = new Intent(MainActivity.this, EditActivity.class);
                String itemClicked = items.get(position);
                toEditActivity.putExtra("itemClicked", itemClicked);
                toEditActivity.putExtra("position", position);
                startActivityForResult(toEditActivity, EDIT_REQUEST_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rv_items.setAdapter(itemsAdapter);
        rv_items.setLayoutManager(new LinearLayoutManager(this));

        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String todoItem = txt_item.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                txt_item.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String editedItem = data.getStringExtra("editedItem");
            int position = data.getIntExtra("position", 0);

            items.remove(position);
            items.add(position, editedItem);
            itemsAdapter.notifyDataSetChanged();
            saveItems();
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}