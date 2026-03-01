package com.CivicSystem.Controller;

import com.CivicSystem.Model.Officer;
import com.CivicSystem.Service.OfficerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {

    private final OfficerService officerService;

    public SuperAdminController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Officer newAdmin) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            Officer creator =
                    officerService.getByUid(decodedToken.getUid());

            System.out.println("Creator: " + creator);
            System.out.println("Role value: " + creator.getRole());
            if (creator.getRole() != Officer.Role.SUPER_ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("ONLY_SUPER_ADMIN_ALLOWED");
            }


            newAdmin.setRole(Officer.Role.ADMIN);
            newAdmin.setFirebaseUid(null);
            newAdmin.setActive(true);

            Officer created =
                    officerService.saveOfficer(newAdmin);

            return ResponseEntity.ok(created);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("NOT_AUTHORIZED");
        }
    }
}
