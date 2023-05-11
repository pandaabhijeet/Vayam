package com.example.vayam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vayam.png.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import p008de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListViewHolder> {
    ArrayList<ContactObject> mContactList;

    public ContactListAdapter(ArrayList<ContactObject> mContactList2) {
        this.mContactList = mContactList2;
    }

    public ContactListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, parent, false));
    }

    public void onBindViewHolder(ContactListViewHolder holder, int position) {
        holder.mName.setText(this.mContactList.get(position).getContactName());
        holder.mPhone.setText(this.mContactList.get(position).getContactPhone());
        holder.mStatus.setText(this.mContactList.get(position).getContactStatus());
        Picasso.get().load(this.mContactList.get(position).getContactImage()).placeholder((int) R.drawable.profile_image).into((ImageView) holder.mImage);
    }

    public int getItemCount() {
        return this.mContactList.size();
    }

    public class ContactListViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView mImage;
        public TextView mName;
        public TextView mPhone;
        public TextView mStatus;

        public ContactListViewHolder(View itemView) {
            super(itemView);
            this.mName = (TextView) itemView.findViewById(R.id.contactName);
            this.mImage = (CircleImageView) itemView.findViewById(R.id.contactImage);
            this.mPhone = (TextView) itemView.findViewById(R.id.contactPhone);
            this.mStatus = (TextView) itemView.findViewById(R.id.contactStatus);
        }
    }
}
