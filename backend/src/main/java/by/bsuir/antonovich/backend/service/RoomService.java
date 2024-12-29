package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.*;
import by.bsuir.antonovich.backend.repository.RoomRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final FloorService floorService;
    private final WorkSpaceService workSpaceService;
    private final MailService mailService;

    public static final String mailMessage = "Ваше рабочее место было изменено!";
    public static final String mailTitle = "Изменение рабочего места!";

    public List<Room> findAllByRoomType(RoomType roomType) {
        return roomRepository.findAllByRoomType(roomType);
    }

    public List<Room> findAllByRoomStatus(RoomStatus roomStatus) {
        return roomRepository.findAllByRoomStatus(roomStatus);
    }

    public void deleteByFloorId(Integer floorId) {
        Optional<Floor> floorOptional = floorService.findById(floorId);

        if (floorOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid floorId");
        }

        roomRepository.deleteAllByFloor(floorOptional.get());
    }

    @Transactional
    public List<Room> saveAll(List<Room> roomList, Integer floorId) {
        deleteByFloorId(floorId);

        roomList.forEach(room -> {
                    room.setFloor(new Floor(floorId));
                    room.getWorkspaces().forEach(workspace -> {
                        Optional<WorkSpace> optionalWorkSpace = workSpaceService.findById(workspace.getId());
                        if (optionalWorkSpace.isPresent()) {
                            WorkSpace dbWorkspace = optionalWorkSpace.get();
                            User currentUser = dbWorkspace.getUser();
                            User userFromUI = workspace.getUser();

                            try {
                                if (userFromUI == null && currentUser != null) {
                                    mailService.sendEmail(mailMessage, mailTitle, currentUser.getEmail());
                                } else if (userFromUI != null && currentUser == null) {
                                    mailService.sendEmail(mailMessage, mailTitle, userFromUI.getEmail());
                                } else if (userFromUI.getId() != currentUser.getId()) {
                                    mailService.sendEmail(mailMessage, mailTitle, currentUser.getEmail());
                                    mailService.sendEmail(mailMessage, mailTitle, userFromUI.getEmail());
                                }
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        workspace.setRoom(room);
                    });
                }
        );

        return roomRepository.saveAll(roomList);
    }

    @Transactional
    public List<Room> saveAll(List<Room> roomList) {
        return roomRepository.saveAll(roomList);
    }

    public Optional<Room> findById(String roomId) {
        return roomRepository.findById(roomId);
    }

}

