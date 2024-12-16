package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookDto {

    private Integer id;
    private UserDto user;
    private Date startDate;
    private Date endDate;
    private String theme;
    private RoomDto room;

}
