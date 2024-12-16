package by.bsuir.antonovich.backend.service.dto;

import by.bsuir.antonovich.backend.data.Book;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.dto.BookAnalyticsDto;
import by.bsuir.antonovich.backend.data.dto.BookDto;
import by.bsuir.antonovich.backend.data.dto.RoomDto;
import by.bsuir.antonovich.backend.service.BookService;
import by.bsuir.antonovich.backend.service.RoomService;
import by.bsuir.antonovich.backend.service.converter.BookDtoConverter;
import by.bsuir.antonovich.backend.service.converter.Direction;
import by.bsuir.antonovich.backend.service.converter.RoomDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookDtoService {

    private final BookService bookService;
    private final RoomService roomService;

    public BookDto create(BookDto bookDto) {

        Book book = BookDtoConverter.convertToEntity(bookDto, Direction.DOWN);
        return BookDtoConverter.convertToDto(bookService.create(book), Direction.DOWN);
    }

    public List<BookDto> findAllByRoomId(String roomId) {
        return bookService.findAllByRoomId(roomId)
                .stream()
                .map(book -> BookDtoConverter.convertToDto(book, Direction.DOWN))
                .toList();
    }

    public List<BookDto> findAllByUserId(Integer userId) {
        return bookService.findAllByUserId(userId)
                .stream()
                .map(book -> BookDtoConverter.convertToDto(book, Direction.DOWN)).toList();
    }

    public void deleteById(Integer id) {
        bookService.deleteById(id);
    }

    public BookAnalyticsDto getBookAnalyticsDto(Integer officeId, Date startDate, Date endDate) {
        List<Object[]> mostUsedRooms = bookService.findMostUsedRooms(officeId, startDate, endDate);

        Map<RoomDto, Long> mostUsedRoomsMap = new HashMap<>();
        if (mostUsedRooms.size() > 0) {
            mostUsedRooms.stream().forEach(value -> {
                Optional<Room> roomOptional = roomService.findById((String) value[0]);
                if (roomOptional.isEmpty()) {
                    throw new IllegalArgumentException("Room not found");
                }
                mostUsedRoomsMap.put(RoomDtoConverter.convertToDto(roomOptional.get(), Direction.DOWN), (Long) value[1]);
            });
        }

        List<Object[]> mostCommonBookingTimeList = bookService.findMostCommonBookingTime(officeId, startDate, endDate);

        Map<String, Long> mostCommonBookingTimeMap = new HashMap<>();

        mostCommonBookingTimeList.forEach(value -> {
            mostCommonBookingTimeMap.put((String) value[0], (Long) value[1]);
        });

        BookAnalyticsDto analyticsDto = new BookAnalyticsDto();
        analyticsDto.setMostUsedRooms(mostUsedRoomsMap);
        analyticsDto.setMostCommonBookingTime(mostCommonBookingTimeMap);

        return analyticsDto;
    }

}
