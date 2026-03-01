package com.CivicSystem.Repo;


import com.CivicSystem.Model.Officer;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

@Repository
public class OfficerRepository {

    private static final String COLLECTION = "officers";

    public void save(Officer officer) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        db.collection(COLLECTION)
                .document(officer.getEmail())
                .set(officer);
    }

    public Officer findByEmail(String email) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot snapshot = db
                .collection(COLLECTION)
                .document(email)
                .get()
                .get();
        if (!snapshot.exists()) {
            return null;
        }
        return snapshot.toObject(Officer.class);
    }

    public Officer findByFirebaseUid(String firebaseUid) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        return db.collection(COLLECTION)
                .whereEqualTo("firebaseUid", firebaseUid)
                .get()
                .get()
                .getDocuments()
                .stream()
                .findFirst()
                .map(doc -> doc.toObject(Officer.class))
                .orElse(null);
    }


    public boolean existsByUid(String firebaseUid) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        return db.collection(COLLECTION)
                .document(firebaseUid)
                .get()
                .get()
                .exists();
    }


}

