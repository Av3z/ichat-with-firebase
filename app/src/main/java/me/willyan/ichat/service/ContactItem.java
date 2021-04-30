package me.willyan.ichat.service;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import me.willyan.ichat.R;
import me.willyan.ichat.model.Contact;

public class ContactItem extends Item<ViewHolder> {

    private final Contact contact;

    public ContactItem(Contact contact) {
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
