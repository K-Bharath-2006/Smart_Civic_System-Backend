package com.CivicSystem.Controller;

import com.CivicSystem.Model.Issue;
import com.CivicSystem.Service.IssueService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> reportIssue(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Issue issue) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String userUid = decodedToken.getUid();

            issueService.createIssue(issue, userUid);

            return ResponseEntity.ok("Issue reported successfully");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_TOKEN");
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyIssues(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String userUid = decodedToken.getUid();

            List<Issue> issues = issueService.getIssuesByUser(userUid);

            return ResponseEntity.ok(issues);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_TOKEN");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllIssues(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            // Optional: role check (ADMIN)
            // String role = decodedToken.getClaims().get("role").toString();

            List<Issue> issues = issueService.getAllIssues();
            return ResponseEntity.ok(issues);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_TOKEN");
        }
    }

}
