package by.bsuir.antonovich.backend.service.converter;

import by.bsuir.antonovich.backend.data.Book;
import by.bsuir.antonovich.backend.data.dto.BookDto;

public final class BookDtoConverter {

    private BookDtoConverter() {
    }

    public static Book convertToEntity(BookDto dto, Direction direction) {
        Book book = new Book();

        book.setId(dto.getId());
        book.setTheme(dto.getTheme());
        book.setStartDate(dto.getStartDate());
        book.setEndDate(dto.getEndDate());
        book.setUser(UserDtoConverter.convertToEntity(dto.getUser(), direction));
        book.setRoom(RoomDtoConverter.convertToEntity(dto.getRoom(), direction));

        return book;
    }

    public static BookDto convertToDto(Book entity, Direction direction) {
        BookDto dto = new BookDto();

        dto.setId(entity.getId());
        dto.setTheme(entity.getTheme());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setUser(UserDtoConverter.convertToDto(entity.getUser(), direction));
        dto.setRoom(RoomDtoConverter.convertToDto(entity.getRoom(), direction));

        return dto;
    }
}
