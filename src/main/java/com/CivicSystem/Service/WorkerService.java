package com.CivicSystem.Service;

import com.CivicSystem.Model.Worker;
import com.CivicSystem.Repo.WorkerRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    // 🔹 Create Worker
    public void createWorker(Worker worker) throws Exception {
        if (workerRepository.findByEmail(worker.getEmail()) != null) {
            throw new RuntimeException("Worker already exists");
        }
        worker.setRole(Worker.Role.WORKER);
        workerRepository.save(worker);
    }

    // 🔹 Login Worker
    public Worker loginWorker(String firebaseUid, String email) throws Exception {
        Worker worker = workerRepository.findByFirebaseUid(firebaseUid);
        if (worker != null) return worker;

        worker = workerRepository.findByEmail(email);
        if (worker == null) throw new RuntimeException("Not a worker");

//        if (!worker.isActive()) throw new RuntimeException("Worker inactive");

        // Link Firebase UID
        worker.setFirebaseUid(firebaseUid);
        workerRepository.save(worker);

        return worker;
    }
}
