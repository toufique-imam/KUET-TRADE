package com.sabertooth.kuettrade;

import java.util.ArrayList;

public class Product_class {
    String uid,id,name,address,phone1,phone2,description;
    Integer price,amount;
    String image_front,image_back;
    ArrayList<String>size;
    String type;
    Product_class(){}

    public Product_class(String uid, String id, String name, String address, String phone1, String phone2, String description, Integer price, Integer amount, String image_front, String image_back, ArrayList<String> size, String type) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.image_front = image_front;
        this.image_back = image_back;
        this.size = size;
        this.type = type;
    }
}
