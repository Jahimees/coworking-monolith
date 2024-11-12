package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.repository.WorkSpaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
}
