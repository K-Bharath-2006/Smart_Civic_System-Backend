package com.CivicSystem.Model;


import lombok.Data;

import com.google.cloud.Timestamp;


@Data
public class User {

    private String firebaseUid;

    private String name;
    private String email;
    private String phoneNumber;

    private Role role;

    private Timestamp createdAt = Timestamp.now();

    public enum Role {
        USER,
        WORKER,
    }
}
