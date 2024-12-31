package by.bsuir.antonovich.backend.controller;

import by.bsuir.antonovich.backend.service.MailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MailService mailService;

    @GetMapping("/api/v1/test")
//    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<?> test(HttpServletRequest request) throws JsonProcessingException, MessagingException {
        mailService.sendEmail("message", "title", "jerrymous1901@gmail.com");

        return ResponseEntity.ok().build();
    }
}
