package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Floor;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

}

