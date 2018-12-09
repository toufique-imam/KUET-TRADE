package com.sabertooth.kuettrade;

import java.util.ArrayList;

public class Product_class {
    String uid,id,name,address,phone1,phone2,description;
    String wid;
    Integer price,amount;
    String image_front,image_back;
    ArrayList<Boolean>size;
    String type;
    Product_class(){size=new ArrayList<>();
    size.add(false);
    size.add(false);
    size.add(false);
    size.add(false);
    size.add(false);
    size.add(false);}

    public Product_class(String uid, String id, String name, String address, String phone1, String phone2, String description, Integer price, Integer amount, String image_front, String image_back, ArrayList<Boolean> size, String type) {
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
        this.wid=null;
    }

    public Product_class(String uid, String id, String name, String address, String phone1, String phone2, String description, String wid, Integer price, Integer amount, String image_front, String image_back, ArrayList<Boolean> size, String type) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.description = description;
        this.wid = wid;
        this.price = price;
        this.amount = amount;
        this.image_front = image_front;
        this.image_back = image_back;
        this.size = size;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getImage_front() {
        return image_front;
    }

    public void setImage_front(String image_front) {
        this.image_front = image_front;
    }

    public String getImage_back() {
        return image_back;
    }

    public void setImage_back(String image_back) {
        this.image_back = image_back;
    }

    public ArrayList<Boolean> getSize() {
        return size;
    }

    public void setSize(ArrayList<Boolean> size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
