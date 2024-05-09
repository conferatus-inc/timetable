package org.conferatus.timetable.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.conferatus.timetable.backend.model.entity.User;
import org.conferatus.timetable.backend.services.YandexIdService;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginDto {
    private YandexIdService.ResponseYandexId responseYandexId;
    private User user;
}
