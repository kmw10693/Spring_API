package com.example.demo.src.user.model.GetRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성

public class GetUserRes {
    private int userIdx;
    private String name;
    private String password;
    private String phoneNum;
    private String grade;
    private String email;
    private String mailReceive;
    private String smsReceive;
    private String address;
    private String status;
}
