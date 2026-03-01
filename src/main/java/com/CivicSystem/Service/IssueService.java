package com.CivicSystem.Service;


import com.CivicSystem.Model.Issue;
import com.CivicSystem.Model.Officer;
import com.CivicSystem.Repo.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    private final IssueRepository issueRepository;

    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    public void createIssue(Issue issue, String userUid) {


        issue.setUserUid(userUid);
        issue.setStatus(Issue.Status.OPEN);
        issue.setVerified(false);

        issueRepository.save(issue);
    }
    public List<Issue> getIssuesByUser(String userUid) throws Exception {
        return issueRepository.findByUserUid(userUid);
    }
    public List<Issue> getAllIssues() throws Exception {
        return issueRepository.findAll();
    }

    public List<Issue> getIssuesForOfficer(Officer officer) throws Exception {

        System.out.println(officer.getSubdivision() + officer.getDepartment());
        return issueRepository.findBySubdivisionAndDepartment(
                officer.getSubdivision(),
                officer.getDepartment()
        );
    }
    public Issue getIssueById(String issueId) throws Exception {
        System.out.println(issueId);
        Issue issue = issueRepository.findById(issueId);
        System.out.println(issue);
        if (issue == null) {
            throw new RuntimeException("ISSUE_NOT_FOUND");
        }

        return issue;
    }
    public void assignIssue(
            String issueId,
            String workerEmail,
            String officerEmail) throws Exception {

        issueRepository.assignIssue(issueId, workerEmail, officerEmail);
    }

    public List<Issue> getAssignmentsForWorker(String workerEmail)
            throws Exception {

        return issueRepository.findAssignedToWorker(workerEmail);
    }

    public void markCompleted(String issueId) throws Exception {

        issueRepository.markCompleted(issueId);
    }
}
