package me.willyan.ichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import me.willyan.ichat.model.User;

public class ContactsActivity extends AppCompatActivity {

    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        adapter = new GroupAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Item item, @NonNull View view) {
                Intent i = new Intent(ContactsActivity.this, ChatActivity.class);
                startActivity(i);
            }
        });

        fetchContacts();
    }

    private void fetchContacts() {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Teste", error.getMessage(), error);
                            return;
                        }

                        List<DocumentSnapshot> docs = value.getDocuments();
                        for(DocumentSnapshot doc : docs){
                            User user = doc.toObject(User.class);
                            adapter.add(new UserItem(user));
                            Log.d("Teste", user.getName());

                        }
                    }
                });
    }


    private class UserItem extends Item<ViewHolder>{

        private final User user;

        private UserItem(User user) {
            this.user = user;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView textViewName =  viewHolder.itemView.findViewById(R.id.textContact);
            ImageView imageContacts = viewHolder.itemView.findViewById(R.id.imgPhotoContacts);

            textViewName.setText(user.getName());
            Picasso.get().load(user.getPhotoUrl()).into(imageContacts);

        }

        @Override
        public int getLayout() {
            return R.layout.item_user;
        }
    }
}