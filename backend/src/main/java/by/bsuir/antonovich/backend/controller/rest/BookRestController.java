package by.bsuir.antonovich.backend.controller.rest;

import by.bsuir.antonovich.backend.data.dto.BookDto;
import by.bsuir.antonovich.backend.service.dto.BookDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
public class BookRestController {

    private final BookDtoService bookDtoService;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody final BookDto bookDto) {

        return ResponseEntity.ok(bookDtoService.create(bookDto));

    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<?> findBooksByRoom(@PathVariable final String roomId) {

        if (roomId == null || roomId.isEmpty()) {
            return ResponseEntity.badRequest().body("Room id cannot be empty");
        }

        return ResponseEntity.ok(bookDtoService.findAllByRoomId(roomId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> findBooksByUser(@PathVariable final Integer userId) {
        if (userId == null || userId < 1) {
            return ResponseEntity.badRequest().body("User id cannot be empty");
        }

        return ResponseEntity.ok(bookDtoService.findAllByUserId(userId));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable final Integer bookId) {
        if (bookId == null || bookId < 1) {
            return ResponseEntity.badRequest().body("Book id cannot be empty");
        }

        bookDtoService.deleteById(bookId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/offices/{officeId}")
    public ResponseEntity<?> getBookingAnalytics(@PathVariable final Integer officeId,
                                                 @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                 @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        return ResponseEntity.ok(bookDtoService.getBookAnalyticsDto(officeId, startDate, endDate));
    }
}
