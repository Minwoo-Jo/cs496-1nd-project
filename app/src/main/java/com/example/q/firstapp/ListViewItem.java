package com.example.q.firstapp;

public class ListViewItem {
    private String id;
    private String name;
    private String phone;
    private String gender;
    private String email;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public boolean contains(CharSequence text) {
        if(getName().contains(text)||getEmail().contains(text)||getPhone().contains(text))
            return true;
        return false;
    }
}
