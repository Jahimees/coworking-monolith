package by.bsuir.antonovich.backend.data.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BookAnalyticsDto {

    private Map<RoomDto, Long> mostUsedRooms;
    private Map<String, Long> mostCommonBookingTime;

}
