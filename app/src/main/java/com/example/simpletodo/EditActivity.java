package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent receivedIntent = getIntent();
        String itemReceived = receivedIntent.getStringExtra("itemClicked");
        int position = receivedIntent.getIntExtra("position", 0);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        etItem.setText(itemReceived);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedItem = etItem.getText().toString();
                Intent toMainActivity = new Intent();
                toMainActivity.putExtra("editedItem", editedItem);
                toMainActivity.putExtra("position", position);
                setResult(RESULT_OK, toMainActivity);
                finish();
            }
        });
    }
}