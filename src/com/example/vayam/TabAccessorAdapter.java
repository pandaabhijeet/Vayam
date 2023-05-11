package com.example.vayam;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {
    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int i) {
        if (i == 0) {
            return new ChatsFragment();
        }
        if (i == 1) {
            return new GroupsFragment();
        }
        if (i != 2) {
            return null;
        }
        return new ContactsFragment();
    }

    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Chats";
        }
        if (position == 1) {
            return "Groups";
        }
        if (position != 2) {
            return null;
        }
        return "Contacts";
    }
}
