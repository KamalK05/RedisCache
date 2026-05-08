package com.learning.cache.main.profile.service;

import com.learning.cache.main.profile.controller.resources.ProfileDto;
import com.learning.cache.main.profile.repositories.ProfileEntity;
import com.learning.cache.main.profile.repositories.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void addProfile_savesAndReturnsEntity() {
        ProfileDto dto = new ProfileDto("u1", "Alice", 30, "gardening");
        ProfileEntity saved = new ProfileEntity();
        saved.setId(1L);
        saved.setUserId("u1");
        saved.setName("Alice");
        saved.setAge(30);
        saved.setHobby("gardening");

        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(saved);

        ProfileEntity result = profileService.addProfile(dto);

        assertSame(saved, result);

        ArgumentCaptor<ProfileEntity> captor = ArgumentCaptor.forClass(ProfileEntity.class);
        verify(profileRepository).save(captor.capture());
        ProfileEntity captured = captor.getValue();
        assertEquals("u1", captured.getUserId());
        assertEquals("Alice", captured.getName());
        assertEquals(30, captured.getAge());
        assertEquals("gardening", captured.getHobby());
    }

    @Test
    void updateProfile_existing_updatesAndReturns() {
        ProfileEntity existing = new ProfileEntity();
        existing.setId(2L);
        existing.setUserId("u2");
        existing.setName("Old");
        existing.setAge(40);
        existing.setHobby("oldHobby");

        ProfileDto dto = new ProfileDto("u2", "Bob", 28, "reading");

        when(profileRepository.getByUserId("u2")).thenReturn(existing);
        when(profileRepository.save(existing)).thenReturn(existing);

        ProfileEntity result = profileService.updateProfile("u2", dto);

        assertSame(existing, result);
        assertEquals("u2", existing.getUserId());
        assertEquals("Bob", existing.getName());
        assertEquals(28, existing.getAge());
        assertEquals("reading", existing.getHobby());
        verify(profileRepository).getByUserId("u2");
        verify(profileRepository).save(existing);
    }

    @Test
    void updateProfile_missing_throwsRuntimeException() {
        ProfileDto dto = new ProfileDto("u3", "Charlie", 33, "music");

        when(profileRepository.getByUserId("u3")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profileService.updateProfile("u3", dto));
        assertTrue(ex.getMessage().contains("Profile not found for userId: u3"));
        verify(profileRepository).getByUserId("u3");
        verify(profileRepository, never()).save(any());
    }

    @Test
    void deleteProfile_existing_callsDelete() {
        ProfileEntity existing = new ProfileEntity();
        existing.setId(4L);
        existing.setUserId("u4");

        when(profileRepository.getByUserId("u4")).thenReturn(existing);

        profileService.deleteProfile("u4");

        verify(profileRepository).getByUserId("u4");
        verify(profileRepository).delete(existing);
    }

    @Test
    void deleteProfile_missing_doesNotCallDelete() {
        when(profileRepository.getByUserId("u5")).thenReturn(null);

        profileService.deleteProfile("u5");

        verify(profileRepository).getByUserId("u5");
        verify(profileRepository, never()).delete(any());
    }

    @Test
    void getProfileByUserId_existing_returnsEntity() {
        ProfileEntity existing = new ProfileEntity();
        existing.setId(6L);
        existing.setUserId("u6");
        existing.setName("Diana");

        when(profileRepository.getByUserId("u6")).thenReturn(existing);

        ProfileEntity result = profileService.getProfileByUserId("u6");

        assertSame(existing, result);
        verify(profileRepository).getByUserId("u6");
    }

    @Test
    void getProfileByUserId_missing_throwsRuntimeException() {
        when(profileRepository.getByUserId("missing")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> profileService.getProfileByUserId("missing"));
        assertTrue(ex.getMessage().contains("Profile not found for userId: missing"));
        verify(profileRepository).getByUserId("missing");
    }
}