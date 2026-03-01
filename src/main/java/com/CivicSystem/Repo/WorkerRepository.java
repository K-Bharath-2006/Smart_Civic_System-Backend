package com.CivicSystem.Repo;

import com.CivicSystem.Model.Worker;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class WorkerRepository {

    private static final String COLLECTION_NAME = "users";

    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    // 🔹 Save or update worker
    public void save(Worker worker) throws ExecutionException, InterruptedException {
        CollectionReference collection = getFirestore().collection(COLLECTION_NAME);

        // Check if a document exists with this firebaseUid
        Worker existing = findByFirebaseUid(worker.getFirebaseUid());
        if (existing != null) {
            // Update existing document
            String docId = getDocumentIdByUid(worker.getFirebaseUid());
            collection.document(docId).set(worker).get();
        } else {
            // Add new document (Firestore auto ID)
            collection.add(worker).get();
        }
    }

    // 🔹 Find by Firebase UID
    public Worker findByFirebaseUid(String firebaseUid) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getFirestore()
                .collection(COLLECTION_NAME)
                .whereEqualTo("firebaseUid", firebaseUid)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Worker.class);
        }
        return null;
    }

    // 🔹 Helper to get Firestore document ID by UID
    private String getDocumentIdByUid(String firebaseUid) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getFirestore()
                .collection(COLLECTION_NAME)
                .whereEqualTo("firebaseUid", firebaseUid)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).getId();
        }
        return null;
    }

    // 🔹 Find by email
    public Worker findByEmail(String email) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = getFirestore()
                .collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        if (!documents.isEmpty()) {
            return documents.get(0).toObject(Worker.class);
        }
        return null;
    }

    // 🔹 Get all workers
    public List<Worker> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getFirestore().collection(COLLECTION_NAME).get();
        return future.get().getDocuments()
                .stream()
                .map(doc -> doc.toObject(Worker.class))
                .toList();
    }
}
