package com.CivicSystem.Controller;

import com.CivicSystem.Model.Officer;
import com.CivicSystem.Service.OfficerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setup")
public class SetUpController {

    private final OfficerService officerService;

    public SetUpController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/create-super-admin")
    public ResponseEntity<?> createSuperAdmin(
            @RequestBody Officer officer){

        try {
            officer.setRole(Officer.Role.SUPER_ADMIN);
            officer.setFirebaseUid(null);
            officer.setActive(true);

            officerService.saveOfficer(officer);

            return ResponseEntity.ok("SUPER_ADMIN_CREATED");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("FAILED_TO_CREATE");
        }
    }
}

