package com.CivicSystem.Service;


import com.CivicSystem.Model.Officer;
import com.CivicSystem.Repo.OfficerRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OfficerService {

    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public Officer loginOfficer(String firebaseUid, String email) throws Exception {

        Officer officerByUid =
                officerRepository.findByFirebaseUid(firebaseUid);

        if (officerByUid != null) {
            return officerByUid;
        }

        Officer officerByEmail =
                officerRepository.findByEmail(email);

        if (officerByEmail == null) {
            throw new RuntimeException("NOT_REGISTERED");
        }

        if (!officerByEmail.isActive()) {
            throw new RuntimeException("INACTIVE_ACCOUNT");
        }

        // First login → link UID
        if (officerByEmail.getFirebaseUid() == null) {
            officerByEmail.setFirebaseUid(firebaseUid);
            officerRepository.save(officerByEmail);
        }

        return officerByEmail;
    }

    public Officer getByUid(String firebaseUid) throws Exception {

        Officer officer = officerRepository.findByFirebaseUid(firebaseUid);

        if (officer == null) {
            throw new RuntimeException("OFFICER_NOT_FOUND");
        }

        return officer;
    }
    public Officer saveOfficer(Officer officer) throws Exception {

        officerRepository.save(officer);

        return officer;
    }



}




//
//
//
//
//package com.CivicSystem.Service;
//
//import com.CivicSystem.Model.Officer;
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.*;
//        import com.google.firebase.cloud.FirestoreClient;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class OfficerService {
//
//    private static final String COLLECTION_NAME = "officers";
//
//    // ✅ Create Officer
//    public void createOfficer(Officer officer) throws Exception {
//
//        Firestore db = FirestoreClient.getFirestore();
//
//        // Check duplicate email
//        ApiFuture<QuerySnapshot> query =
//                db.collection(COLLECTION_NAME)
//                        .whereEqualTo("email", officer.getEmail())
//                        .get();
//
//        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
//
//        if (!documents.isEmpty()) {
//            throw new RuntimeException("Officer already exists");
//        }
//
//        officer.setId(UUID.randomUUID().toString());
//        officer.setFirebaseUid(null);
//        officer.setActive(true);
//        officer.setRole("OFFICER");
//
//        db.collection(COLLECTION_NAME)
//                .document(officer.getId())
//                .set(officer)
//                .get();
//    }
//
//    // ✅ Login Officer
//    public Officer loginOfficer(String firebaseUid, String email) throws Exception {
//
//        Firestore db = FirestoreClient.getFirestore();
//
//        // Step 1: Check by UID
//        ApiFuture<QuerySnapshot> uidQuery =
//                db.collection(COLLECTION_NAME)
//                        .whereEqualTo("firebaseUid", firebaseUid)
//                        .get();
//
//        List<QueryDocumentSnapshot> uidDocs = uidQuery.get().getDocuments();
//
//        if (!uidDocs.isEmpty()) {
//            return uidDocs.get(0).toObject(Officer.class);
//        }
//
//        // Step 2: First-time login → check by email
//        ApiFuture<QuerySnapshot> emailQuery =
//                db.collection(COLLECTION_NAME)
//                        .whereEqualTo("email", email)
//                        .get();
//
//        List<QueryDocumentSnapshot> emailDocs = emailQuery.get().getDocuments();
//
//        if (emailDocs.isEmpty()) {
//            throw new RuntimeException("Not an officer");
//        }
//
//        Officer officer = emailDocs.get(0).toObject(Officer.class);
//
//        if (!officer.isActive()) {
//            throw new RuntimeException("Officer inactive");
//        }
//
//        // Link Firebase UID
//        officer.setFirebaseUid(firebaseUid);
//
//        db.collection(COLLECTION_NAME)
//                .document(officer.getId())
//                .set(officer);
//
//        return officer;
//    }
//}
//
