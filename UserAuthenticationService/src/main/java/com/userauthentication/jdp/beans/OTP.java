package com.userauthentication.jdp.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * Author Name : M.V.Krishna
 * Date: 03-04-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTP {
    private int otp;
    private long timeStamp;
}
