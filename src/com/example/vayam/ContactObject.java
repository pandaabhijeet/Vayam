package com.example.vayam;

public class ContactObject {
    private final String contactImage;
    private final String contactName;
    private final String contactPhone;
    private final String contactStatus;

    public ContactObject(String name, String phone, String status, String image) {
        this.contactName = name;
        this.contactImage = image;
        this.contactPhone = phone;
        this.contactStatus = status;
    }

    public String getContactName() {
        return this.contactName;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public String getContactImage() {
        return this.contactImage;
    }

    public String getContactStatus() {
        return this.contactStatus;
    }
}
