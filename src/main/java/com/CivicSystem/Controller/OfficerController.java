package com.CivicSystem.Controller;


import com.CivicSystem.Model.Issue;
import com.CivicSystem.Model.Officer;
import com.CivicSystem.Service.IssueService;
import com.CivicSystem.Service.OfficerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officer")
public class OfficerController {

    private final OfficerService officerService;
    private final IssueService issueService;

    public OfficerController(OfficerService officerService, IssueService issueService) {
        this.officerService = officerService;
        this.issueService = issueService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            Officer officer =
                    officerService.loginOfficer(firebaseUid, email);

            return ResponseEntity.ok(officer);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_USER");
        }
    }

    @GetMapping("/issues")
    public ResponseEntity<?> getOfficerIssues(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            Officer officer =
                    officerService.getByUid(decodedToken.getUid());
            System.out.println(decodedToken.getEmail());
            List<Issue> issues =
                    issueService.getIssuesForOfficer(officer);
            System.out.println(issues);

            return ResponseEntity.ok(issues);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/issues/{id}")
    public ResponseEntity<?> getIssueById(
            @PathVariable String id) {

        try {
            Issue issue = issueService.getIssueById(id);
            return ResponseEntity.ok(issue);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ISSUE_NOT_FOUND");
        }
    }

    @PostMapping("/assign/{issueId}")
    public ResponseEntity<?> assignIssue(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String issueId,
            @RequestParam String workerEmail) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            issueService.assignIssue(
                    issueId,
                    workerEmail,
                    decodedToken.getEmail()
            );

            return ResponseEntity.ok("ASSIGNED_SUCCESS");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("NOT_AUTHORIZED");
        }

    }
}
