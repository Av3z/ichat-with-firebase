package me.willyan.ichat.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import me.willyan.ichat.R;
import me.willyan.ichat.controller.LoginController;
import me.willyan.ichat.database.Database;
import me.willyan.ichat.model.Contact;

public class MessageActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        RecyclerView recyclerView = findViewById(R.id.reclyclerLastMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupAdapter();

        Database database = new Database();

        recyclerView.setAdapter(adapter);

        LoginController login = new LoginController(this);

        login.verifyAuth();

        database.fetchLastMessages(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Intent it = new Intent(MessageActivity.this, SearchActivity.class);
                startActivity(it);
                break;
            case R.id.contatcs:
                Intent i = new Intent(MessageActivity.this, ContactsActivity.class);
                startActivity(i);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                LoginController login = new LoginController(this);
                login.verifyAuth();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}