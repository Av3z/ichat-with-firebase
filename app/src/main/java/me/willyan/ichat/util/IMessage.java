package me.willyan.ichat.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import me.willyan.ichat.model.Contact;
import me.willyan.ichat.model.Message;
import me.willyan.ichat.model.User;

public class IMessage {

    public void sendMessage(String text, User user) {


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
}
