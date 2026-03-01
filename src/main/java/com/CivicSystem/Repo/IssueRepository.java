package com.CivicSystem.Repo;

import com.CivicSystem.Model.Issue;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Repository;

import com.google.cloud.firestore.FieldValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class IssueRepository {

    private static final String COLLECTION = "Issues";

    public void save(Issue issue) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection(COLLECTION).add(issue);
    }

    public List<Issue> findByUserUid(String userUid) throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .whereEqualTo("userUid", userUid)
                .get()
                .get();

        return mapDocuments(snapshot);
    }

    public List<Issue> findAll() throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .get()
                .get();

        return mapDocuments(snapshot);
    }

//    private List<Issue> mapDocuments(QuerySnapshot snapshot) {
//        List<Issue> issues = new ArrayList<>();
//
//        for (DocumentSnapshot doc : snapshot.getDocuments()) {
//            Issue issue = doc.toObject(Issue.class);
//            issue.setIssueId(doc.getId());
//            issues.add(issue);
//        }
//        return issues;
//    }

    public List<Issue> findBySubdivisionAndDepartment(
            String subdivision,
            String department) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        return db.collection(COLLECTION)
                .whereEqualTo("subdivision", subdivision)
                .whereEqualTo("category", department)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> doc.toObject(Issue.class))
                .collect(Collectors.toList());
    }

    public Issue findById(String issueId) throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .whereEqualTo("issueId", issueId)
                .get()
                .get();

        if (snapshot.isEmpty()) {
            return null;
        }

        DocumentSnapshot doc = snapshot.getDocuments().get(0);

        Issue issue = doc.toObject(Issue.class);
//        issue.setIssueId(doc.getId());

        return issue;
    }

    public void assignIssue(
            String issueId,
            String workerEmail,
            String officerEmail) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        QuerySnapshot snapshot = db.collection(COLLECTION)
                .whereEqualTo("issueId", issueId)
                .get()
                .get();

        if (snapshot.isEmpty()) {
            throw new RuntimeException("ISSUE_NOT_FOUND");
        }

        DocumentSnapshot doc = snapshot.getDocuments().get(0);

        Map<String, Object> updates = new HashMap<>();
        updates.put("assignedTo", workerEmail);
        updates.put("assignedBy", officerEmail);
        updates.put("status", "ASSIGNED");

        db.collection(COLLECTION)
                .document(doc.getId())
                .update(updates)
                .get();
    }


    public List<Issue> findAssignedToWorker(String workerEmail)
            throws Exception {

        QuerySnapshot snapshot = FirestoreClient
                .getFirestore()
                .collection(COLLECTION)
                .whereEqualTo("assignedTo", workerEmail)
//                .whereEqualTo("status", "ASSIGNED")
                .get()
                .get();

        return mapDocuments(snapshot);
    }

    public void markCompleted(String issueId) throws Exception {

        Firestore db = FirestoreClient.getFirestore();

        db.collection(COLLECTION)
                .document(issueId)
                .update("status", "COMPLETED")
                .get();
    }
    private List<Issue> mapDocuments(QuerySnapshot snapshot) {

        List<Issue> issues = new ArrayList<>();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Issue issue = doc.toObject(Issue.class);
            issue.setIssueId(doc.getId());
            issues.add(issue);
        }

        return issues;
    }

}
