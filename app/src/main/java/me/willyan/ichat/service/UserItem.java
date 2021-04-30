package me.willyan.ichat.service;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import me.willyan.ichat.R;
import me.willyan.ichat.model.User;

public class UserItem extends Item<ViewHolder> {


    private final User user;

    public UserItem(User user) {
        this.user = user;
    }


    @Override
    public void bind(@NonNull ViewHolder viewHolder, int position) {
        TextView textViewName = viewHolder.itemView.findViewById(R.id.textContact);
        ImageView imageContacts = viewHolder.itemView.findViewById(R.id.imgPhotoLastMessage);

        textViewName.setText(user.getName());
        Picasso.get().load(user.getPhotoUrl()).into(imageContacts);

    }

    @Override
    public int getLayout() {
        return R.layout.item_user;
    }

    public User getUser() {
        return user;
    }
}
