package me.willyan.ichat.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import me.willyan.ichat.R;
import me.willyan.ichat.database.Database;
import me.willyan.ichat.model.Contact;
import me.willyan.ichat.model.Message;
import me.willyan.ichat.model.User;
import me.willyan.ichat.util.IMessage;

public class ChatActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private User user;
    private Button buttonChat;
    private EditText editChat;
    private User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        user = getIntent().getExtras().getParcelable("user");
        getSupportActionBar().setTitle(user.getName());

        buttonChat = findViewById(R.id.buttonChatMsg);
        editChat = findViewById(R.id.editChat);

        RecyclerView recyclerView = findViewById(R.id.recyclerChat);
        adapter = new GroupAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editChat.getText().toString();
                IMessage message = new IMessage();
                message.sendMessage(text, user);
                editChat.setText(null);
            }

        });

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(User.class);
                        Database database = new Database();
                        database.fetchMessages(adapter, me, user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage(), e);
                    }
                });

    }
}