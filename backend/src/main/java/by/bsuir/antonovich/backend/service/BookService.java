package by.bsuir.antonovich.backend.service;

import by.bsuir.antonovich.backend.data.Book;
import by.bsuir.antonovich.backend.data.Room;
import by.bsuir.antonovich.backend.data.User;
import by.bsuir.antonovich.backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final RoomService roomService;
    private final UserService userService;

    public Book create(Book newBook) {

        return bookRepository.save(newBook);
    }

    public List<Book> findAllByRoomId(String roomId) {
        Optional<Room> roomOptional = roomService.findById(roomId);

        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room not found");
        }

        List<Book> bookList = bookRepository.findAllByRoom(roomOptional.get());

        return bookList;
    }

    public List<Book> findAllByUserId(Integer userId) {
        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        return bookRepository.findAllByUser(userOptional.get());
    }

    public void deleteById(Integer id) {
        bookRepository.deleteById(id);
    }

    public List<Object[]> findMostCommonBookingTime(Integer officeId, Date startDate, Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = formatter.format(startDate);
        String formattedEndDate = formatter.format(endDate);

        return bookRepository.findMostFrequentBookingTimesByOffice(formattedStartDate, formattedEndDate, officeId);
    }

    public List<Object[]> findMostUsedRooms(Integer officeId, Date startDate, Date endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = formatter.format(startDate);
        String formattedEndDate = formatter.format(endDate);

        return bookRepository.findMostUsedRoomsByOffice(formattedStartDate, formattedEndDate, officeId);
    }
}
