package me.willyan.ichat;

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

import me.willyan.ichat.model.Contact;
import me.willyan.ichat.model.Message;
import me.willyan.ichat.model.User;

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
                sendMessage();
            }

        });

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        me = documentSnapshot.toObject(User.class);
                        fetchMessages();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Teste", e.getMessage(), e);
                    }
                });

    }

    private void fetchMessages() {
        if(me != null){
            String fromId = me.getUuid();
            String toId = user.getUuid();

            FirebaseFirestore.getInstance().collection("/messages")
                    .document(fromId)
                    .collection(toId)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            List<DocumentChange> documentChanges = value.getDocumentChanges();

                            if(documentChanges != null){
                                for(DocumentChange doc : documentChanges){
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        Message message = doc.getDocument().toObject(Message.class);
                                        adapter.add(new MessageItem(message));
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void sendMessage() {
        String text = editChat.getText().toString();

        editChat.setText(null);

        String fromId = FirebaseAuth.getInstance().getUid();
        String toId = user.getUuid();
        long timestamp = System.currentTimeMillis();

        Message message = new Message();
        message.setFromId(fromId);
        message.setText(text);
        message.setTimestamp(timestamp);
        message.setToId(toId);

        if (!message.getText().isEmpty()) {
            FirebaseFirestore.getInstance().collection("/messages")
                    .document(fromId)
                    .collection(toId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());

                            Contact contact = new Contact();

                            contact.setUuid(toId);
                            contact.setName(user.getName());
                            contact.setPhotoUrl(user.getPhotoUrl());
                            contact.setTimestamp(message.getTimestamp());
                            contact.setLastMessage(message.getText());

                            FirebaseFirestore.getInstance().collection("/last-messages")
                                    .document(fromId)
                                    .collection("contacts")
                                    .document(toId)
                                    .set(contact);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Test", e.getMessage(), e);
                        }
                    });

            FirebaseFirestore.getInstance().collection("/messages")
                    .document(toId)
                    .collection(fromId)
                    .add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("Teste", documentReference.getId());

                            Contact contact = new Contact();

                            contact.setUuid(toId);
                            contact.setName(user.getName());
                            contact.setPhotoUrl(user.getPhotoUrl());
                            contact.setTimestamp(message.getTimestamp());
                            contact.setLastMessage(message.getText());

                            FirebaseFirestore.getInstance().collection("/last-messages")
                                    .document(toId)
                                    .collection("contacts")
                                    .document(fromId)
                                    .set(contact);
                        }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Test", e.getMessage(), e);
                        }
            });
        }
    }


    private class MessageItem extends Item<ViewHolder> {

        private final Message msg;

        private MessageItem(Message msg) {
            this.msg = msg;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewChat = viewHolder.itemView.findViewById(R.id.textViewChat);
            ImageView imageViewChat = viewHolder.itemView.findViewById(R.id.imageChat);

            textViewChat.setText(msg.getText());

            Picasso.get().load(user.getPhotoUrl()).into(imageViewChat);

        }

        @Override
        public int getLayout() {
            return msg.getFromId().equals(FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_from_message
                    : R.layout.item_to_message;
        }
    }
}