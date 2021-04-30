package me.willyan.ichat.database;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.xwray.groupie.GroupAdapter;

import java.util.List;

import me.willyan.ichat.model.Contact;
import me.willyan.ichat.model.Message;
import me.willyan.ichat.model.User;
import me.willyan.ichat.service.ContactItem;
import me.willyan.ichat.service.MessageItem;
import me.willyan.ichat.service.UserItem;
import me.willyan.ichat.view.ChatActivity;
import me.willyan.ichat.view.ContactsActivity;
import me.willyan.ichat.view.MessageActivity;

public class Database {

    public void fetchContacts(GroupAdapter adapter) {

        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Teste", error.getMessage(), error);
                            return;
                        }

                        List<DocumentSnapshot> docs = value.getDocuments();
                        for (DocumentSnapshot doc : docs) {
                            User user = doc.toObject(User.class);
                            adapter.add(new UserItem(user));
                            Log.d("Teste", user.getName());
                        }
                    }
                });
    }


    public void fetchMessages(GroupAdapter adapter, User me, User user) {
        if (me != null) {
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

                            if (documentChanges != null) {
                                for (DocumentChange doc : documentChanges) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        Message message = doc.getDocument().toObject(Message.class);
                                        adapter.add(new MessageItem(message, user));
                                    }
                                }
                            }
                        }
                    });
        }
    }


    public void fetchLastMessages(GroupAdapter adapter) {

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

}
