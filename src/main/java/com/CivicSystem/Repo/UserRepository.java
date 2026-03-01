package com.CivicSystem.Repo;

import com.CivicSystem.Model.User;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private static final String COLLECTION = "users";

    public void save(User user) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        db.collection("users")
                .document(user.getFirebaseUid())
                .set(user);

    }
    public User findByFirebaseUid(String firebaseUid) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot snapshot = db
                .collection(COLLECTION)
                .document(firebaseUid)
                .get()
                .get();
        if (!snapshot.exists()) {
            return null;
        }
        return snapshot.toObject(User.class);
    }


}
