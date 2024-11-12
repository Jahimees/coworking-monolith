package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.repository.FloorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FloorService {

    private final FloorRepository floorRepository;
}
