package com.CivicSystem.Model;

import lombok.Data;


@Data
public class Officer {

    private String firebaseUid;
    private String name;
    private String email;
    private String phone;
    private Role role;

    private String department;
    private String state;
    private String district;
    private String taluk;
    private String subdivision;

    private boolean active;

    public enum Role {
        SUPER_ADMIN,
        ADMIN,
        OFFICER
    }

}
