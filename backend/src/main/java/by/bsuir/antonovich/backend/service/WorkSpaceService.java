package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.WorkSpace;
import by.bsuir.antonovich.backend.repository.WorkSpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;

    public Optional<WorkSpace> findById(String id) {
        return workSpaceRepository.findById(id);
    }

    public List<WorkSpace> findAllWithUsers() {
        return workSpaceRepository.findAllByUserNotNull();
    }
}
