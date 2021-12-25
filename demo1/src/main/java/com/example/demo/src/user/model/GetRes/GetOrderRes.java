package com.example.demo.src.user.model.GetRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class GetOrderRes {
    private int orderIdx;
    private String storeName;
    private String menu;
    private int menuNum;
    private String orderDate;
    private int price;
    private int deliveryTip;
    private int amount;
    private String payMethod;
    private String address;
    private String phoneNum;
    private String ownReq;
    private String riderReq;
    private String orderNum;
    private String deliCom;
}
