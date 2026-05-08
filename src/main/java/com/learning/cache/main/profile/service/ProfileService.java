package com.learning.cache.main.profile.service;

import com.learning.cache.main.profile.controller.resources.ProfileDto;
import com.learning.cache.main.profile.repositories.ProfileEntity;
import com.learning.cache.main.profile.repositories.ProfileRepository;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileEntity addProfile(ProfileDto profileDto) {
      log.info("Adding profile in DB");
      return profileRepository.save(setProfileEntity(profileDto));
    }

    @CachePut(value = "profileCache", key = "#userId")
    public ProfileEntity updateProfile(String userId, ProfileDto profileDto) {
        ProfileEntity entity = profileRepository.getByUserId(userId);
        if(Objects.nonNull(entity)) {
            setProfileEntity(entity, profileDto);
            log.info("Updating profile for userId: {}", userId);
            return profileRepository.save(entity);
        } else {
            log.error("Profile not found for userId: {} while updating", userId);
            throw new RuntimeException("Profile not found for userId: " + userId);
        }
    }

    @CacheEvict(value = "profileCache", key = "#userId")
    public void deleteProfile(String userId) {
        ProfileEntity entity = profileRepository.getByUserId(userId);
        if(Objects.nonNull(entity)) {
            log.info("Deleting profile for userId: {}", userId);
            profileRepository.delete(entity);
        } else {
            log.error("Profile not found for userId: {} while deleting", userId);
        }
    }

    @Cacheable(value = "profileCache", key = "#userId")
    public ProfileEntity getProfileByUserId(String userId) {
        log.info("Fetching profile for userId: {}", userId);
        var entity = profileRepository.getByUserId(userId);
        if(Objects.isNull(entity)) {
            log.error("Profile not found for userId: {}", userId);
            throw new RuntimeException("Profile not found for userId: " + userId);
        }
        return entity;
    }

    private void setProfileEntity (ProfileEntity entity, ProfileDto profileDto) {
        entity.setUserId(profileDto.userId());
        entity.setName(profileDto.name());
        entity.setAge(profileDto.age());
        entity.setHobby(profileDto.hobby());
    }

    private ProfileEntity setProfileEntity (ProfileDto profileDto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setUserId(profileDto.userId());
        entity.setName(profileDto.name());
        entity.setAge(profileDto.age());
        entity.setHobby(profileDto.hobby());
        return entity;
    }
}
