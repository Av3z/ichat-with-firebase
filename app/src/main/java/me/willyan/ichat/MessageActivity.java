package me.willyan.ichat;

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

        recyclerView.setAdapter(adapter);

        verifyAuth();

        fetchLastMessages();

    }

    private void fetchLastMessages() {

        String uuid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/last-messages")
                .document(uuid)
                .collection("contacts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        List<DocumentChange> documentChangeList = value.getDocumentChanges();

                        if(documentChangeList != null){
                            for(DocumentChange doc : documentChangeList){
                                if(doc.getType() == DocumentChange.Type.ADDED){
                                    Contact contact = doc.getDocument().toObject(Contact.class);
                                    adapter.add(new ContactItem(contact));
                                }
                            }
                        }
                    }
                });

    }

    private void verifyAuth() {
        if(FirebaseAuth.getInstance().getUid() == null){
            Intent i = new Intent(MessageActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
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
                verifyAuth();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ContactItem extends Item<ViewHolder> {

        private final Contact contact;

        private ContactItem(Contact contact) {
            this.contact = contact;
        }

        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {

            TextView name = viewHolder.itemView.findViewById(R.id.textContactName);
            TextView message = viewHolder.itemView.findViewById(R.id.textContact);
            ImageView photoContact = viewHolder.itemView.findViewById(R.id.imgPhotoLastMessage);

            name.setText(contact.getName());
            message.setText(contact.getLastMessage());
            Picasso.get().load(contact.getPhotoUrl())
                    .into(photoContact);

        }

        @Override
        public int getLayout() {
            return R.layout.item_user_last_messages;
        }
    }
}