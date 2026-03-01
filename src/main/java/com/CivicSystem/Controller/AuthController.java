package com.CivicSystem.Controller;

import com.CivicSystem.Model.User;
import com.CivicSystem.Service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/public/bb")
    public void bb() {
        System.out.println("Black");
    }
    @PostMapping("/public/register")
    public ResponseEntity<?> register(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            user.setFirebaseUid(firebaseUid);
            userService.register(user);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Firebase token");
        }
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> login(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String uid = decodedToken.getUid();

            User user = userService.findByFirebaseUid(uid);

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("USER_NOT_REGISTERED");
            }

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_TOKEN");
        }
    }
}
