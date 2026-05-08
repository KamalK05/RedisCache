package com.learning.cache.main.profile.controller;

import com.learning.cache.main.profile.controller.resources.ProfileDto;
import com.learning.cache.main.profile.repositories.ProfileEntity;
import com.learning.cache.main.profile.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log4j2
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/profile")
    public ResponseEntity<ProfileDto> addProfile(@RequestBody ProfileDto profileDto) {
        log.info("Adding profile");
        var profileEntity = profileService.addProfile(profileDto);
        var response = new ProfileDto(profileEntity.getUserId(), profileEntity.getName(), profileEntity.getAge(),
            profileEntity.getHobby());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody ProfileDto profileDto) {
        log.info("Updating profile");
        var profileEntity = profileService.updateProfile(profileDto.userId(), profileDto);
        var response = new ProfileDto(profileEntity.getUserId(), profileEntity.getName(), profileEntity.getAge(),
            profileEntity.getHobby());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/profile/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String userId) {
        log.info("Deleting profile");
        profileService.deleteProfile(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfileByUserId(@RequestParam String userId) {
        log.info("Get profile");
        ProfileEntity entity = profileService.getProfileByUserId(userId);
        ProfileDto response = new ProfileDto(entity.getUserId(), entity.getName(), entity.getAge(),
            entity.getHobby());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
