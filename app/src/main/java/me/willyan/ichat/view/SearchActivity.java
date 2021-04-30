package me.willyan.ichat.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import me.willyan.ichat.R;

public class SearchActivity extends AppCompatActivity {

    private EditText editSearch;
    private Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        buttonSearch = findViewById(R.id.buttonSearch);
        editSearch = findViewById(R.id.editSearch);


        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editSearch.getText().toString();
            }
        });

    }
}