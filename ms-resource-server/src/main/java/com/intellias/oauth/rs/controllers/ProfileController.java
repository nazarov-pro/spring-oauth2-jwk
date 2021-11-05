package com.intellias.oauth.rs.controllers;

import com.intellias.oauth.rs.dto.UserProfileResponseDto;
import java.time.LocalDate;
import java.time.Month;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping
    public UserProfileResponseDto retrieveUserProfile() {
        return new UserProfileResponseDto(
                "Shahin", 25, LocalDate.of(1997, Month.APRIL, 1)
        );
    }

}
