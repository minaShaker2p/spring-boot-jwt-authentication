package com.mina.springbootjwtauthentication.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class Response {

    private int code;

    private String message;
}
