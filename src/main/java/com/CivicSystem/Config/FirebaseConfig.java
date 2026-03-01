package com.CivicSystem.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.api.client.http.javanet.NetHttpTransport;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {

        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        InputStream serviceAccount =
                new FileInputStream("src/main/resources/firebase-service-account.json");

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccount)
                .createScoped(List.of(
                        "https://www.googleapis.com/auth/cloud-platform"
                ));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setHttpTransport(new NetHttpTransport())
                .build();

        FirebaseApp.initializeApp(options);

        warmUpFirebase();
    }

    private void warmUpFirebase() {
        System.out.println("Firebase initialized successfully (Local)");
    }
}
