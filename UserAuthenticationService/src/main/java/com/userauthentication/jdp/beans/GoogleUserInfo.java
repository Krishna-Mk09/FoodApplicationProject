package com.userauthentication.jdp.beans;

import lombok.Data;

@Data
public class GoogleUserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;
}
