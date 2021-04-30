package me.willyan.ichat.service;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import me.willyan.ichat.R;
import me.willyan.ichat.model.Message;
import me.willyan.ichat.model.User;

public class MessageItem extends Item<ViewHolder> {

    private final Message msg;

    private final User user;

    public MessageItem(Message msg, User user) {
        this.msg = msg;
        this.user = user;
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
