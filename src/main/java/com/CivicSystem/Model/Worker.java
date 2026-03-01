package com.CivicSystem.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Worker extends User {

    private String assignedDistrict;
    private String assignedTaluk;
    private String assignedSubdivision;

    public Worker() {
        this.setRole(Role.WORKER); // automatically set role to WORKER
    }
}
