package com.sabertooth.kuettrade;

class User_class {
    String Name, Email, Address, Phone1, Phone2;
    String proPicUrl, coverPicUrl;
    String uid;
    String password;

    User_class() {
        uid = Name = Email = Address = Phone1 = Phone2 = proPicUrl = coverPicUrl = null;
    }
    public User_class(String uid, String name, String email, String address, String phone1, String phone2, String proPicUrl, String coverPicUrl,String P) {
        this.uid = uid;
        Name = name;
        Email = email;
        Address = address;
        Phone1 = phone1;
        Phone2 = phone2;
        this.proPicUrl = proPicUrl;
        this.coverPicUrl = coverPicUrl;
        password=P;
    }
}
