package com.example.vayam;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vayam.png.R;
import java.util.ArrayList;

public class FindFriendsActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private RecyclerView contactList;
    private RecyclerView.Adapter contactListAdapter;
    private RecyclerView.LayoutManager contactListLayoutManager;
    private String image;
    private ArrayList<ContactObject> mContactList = new ArrayList<>();
    private String name;
    private String phone;
    private ArrayList<String> phoneContacts = new ArrayList<>();
    private String status;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_find_friends);
        initialiseRecyclerView();
        getContactList();
    }

    private void initialiseRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contactList);
        this.contactList = recyclerView;
        recyclerView.setNestedScrollingEnabled(false);
        this.contactList.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), 1, false);
        this.contactListLayoutManager = linearLayoutManager;
        this.contactList.setLayoutManager(linearLayoutManager);
        ContactListAdapter contactListAdapter2 = new ContactListAdapter(this.mContactList);
        this.contactListAdapter = contactListAdapter2;
        this.contactList.setAdapter(contactListAdapter2);
    }

    private void getContactList() {
        Cursor contacts = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, (String) null);
        while (contacts != null) {
            if (contacts.moveToNext()) {
                this.name = contacts.getString(contacts.getColumnIndex("display_name"));
                String string = contacts.getString(contacts.getColumnIndex("data1"));
                this.phone = string;
                String replace = string.replace(" ", "");
                this.phone = replace;
                String replace2 = replace.replace("-", "");
                this.phone = replace2;
                String replace3 = replace2.replace("(", "");
                this.phone = replace3;
                String replace4 = replace3.replace(")", "");
                this.phone = replace4;
                this.phone = replace4.trim();
                this.status = "We are on!";
                this.image = String.valueOf(R.drawable.profile_image);
                if (!this.phoneContacts.contains(this.phone) && this.phone.length() >= 10) {
                    this.phoneContacts.add(this.phone);
                    this.mContactList.add(new ContactObject(this.name, this.phone, this.status, this.image));
                }
                this.contactListAdapter.notifyDataSetChanged();
            } else {
                return;
            }
        }
        throw new AssertionError();
    }
}
