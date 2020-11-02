package com.example.a1_project.Friends;

public class Contact {
    public int _id;
    public String _name;
    public byte[] _image;
    public String _lname;
    public String _gender;
    public String _age;
    public String _address;

    public Contact() {
    }

    public Contact(int id, String name, byte[] image, String lname, String gender, String age, String address) {
        this._id = id;
        this._name = name;
        this._image = image;
        this._lname = lname;
        this._gender = gender;
        this._age = age;
        this._address = address;
    }

    public Contact(String name, byte[] image, String lname, String gender, String age, String address) {
        this._name = name;
        this._image = image;
        this._lname = lname;
        this._gender = gender;
        this._age = age;
        this._address = address;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public byte[] getImage() {
        return this._image;
    }

    public void setImage(byte[] image) {
        this._image = image;
    }

    public String getLname() {
        return this._lname;
    }

    public void setLname(String lname) {
        this._lname = lname;
    }

    public String getGender() {
        return this._gender;
    }

    public void setGender(String gender) {
        this._gender = gender;
    }

    public String getAge() {
        return this._age;
    }

    public void setAge(String age) {
        this._age = age;
    }

    public String getAddress() {
        return this._address;
    }

    public void setAddress(String address) {
        this._address = address;
    }
}
