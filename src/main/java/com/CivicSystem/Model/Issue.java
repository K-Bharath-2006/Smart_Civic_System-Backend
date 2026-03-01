package com.CivicSystem.Model;


import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class Issue {

    private String issueId;
    private String userUid;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private String imageUrl1;
    private Category category;
    private Status status;
    private Boolean verified;
    private String subdivision;


    private String assignedTo;
    private String assignedBy;
    private Timestamp reportedAt = Timestamp.now();

    public enum Category {
        EB,
        CORPORATION
    }

    public enum Status {
        OPEN,
        ASSIGNED,
        RESOLVED,
        REJECTED
    }
}
