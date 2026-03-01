package com.CivicSystem.Controller;

import com.CivicSystem.Model.Worker;
import com.CivicSystem.Model.Issue;
import com.CivicSystem.Service.WorkerService;
import com.CivicSystem.Service.IssueService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    private final WorkerService workerService;
    private final IssueService issueService;

    public WorkerController(WorkerService workerService,IssueService issueService) {
        this.workerService = workerService;
        this.issueService = issueService;
    }

    // Worker login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            Worker worker = workerService.loginWorker(firebaseUid, email);
            return ResponseEntity.ok(worker);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("INVALID_OR_UNAUTHORIZED_WORKER");
        }
    }

    // Create Worker
    @PostMapping("/create")
    public ResponseEntity<?> createWorker(@RequestBody Worker worker) {
        try {
            workerService.createWorker(worker);
            return ResponseEntity.ok("Worker created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/assignments")
    public ResponseEntity<?> getWorkerAssignments(
            @RequestHeader("Authorization") String authHeader) {
        System.out.println("bla");
        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);
            System.out.println(decodedToken.getUid());
            String workerEmail = decodedToken.getEmail();
            System.out.println(workerEmail);
            List<Issue> assignments =
                    issueService.getAssignmentsForWorker(workerEmail);
            System.out.println(assignments);
            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("NOT_AUTHORIZED");
        }
    }

}
