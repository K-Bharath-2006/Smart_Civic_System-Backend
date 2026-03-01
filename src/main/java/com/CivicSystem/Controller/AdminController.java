package com.CivicSystem.Controller;

import com.CivicSystem.Model.Officer;
import com.CivicSystem.Service.OfficerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final OfficerService officerService;

    public AdminController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/create-officer")
    public ResponseEntity<?> createOfficer(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Officer newOfficer) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            Officer creator =
                    officerService.getByUid(decodedToken.getUid());

            if (creator.getRole() != Officer.Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("ONLY_ADMIN_ALLOWED");
            }

            newOfficer.setRole(Officer.Role.OFFICER);
            newOfficer.setDistrict(creator.getDistrict());
            newOfficer.setState(creator.getState());
            newOfficer.setFirebaseUid(null);
            newOfficer.setActive(true);

            Officer created =
                    officerService.saveOfficer(newOfficer);

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("NOT_AUTHORIZED");
        }
    }
}
