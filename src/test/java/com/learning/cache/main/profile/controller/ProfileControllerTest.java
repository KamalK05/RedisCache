package com.learning.cache.main.profile.controller;

import com.learning.cache.main.profile.controller.resources.ProfileDto;
import com.learning.cache.main.profile.repositories.ProfileEntity;
import com.learning.cache.main.profile.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @InjectMocks
    private ProfileController controller;

    @Test
    void addProfile_success_returnsOkAndBody() {
        ProfileDto dto = new ProfileDto("user1", "Alice", 30, "gardening");
        ProfileEntity entity = mock(ProfileEntity.class);

        when(profileService.addProfile(dto)).thenReturn(entity);
        when(entity.getUserId()).thenReturn("user1");
        when(entity.getName()).thenReturn("Alice");
        when(entity.getAge()).thenReturn(30);
        when(entity.getHobby()).thenReturn("gardening");

        ResponseEntity<ProfileDto> response = controller.addProfile(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user1", response.getBody().userId());
        assertEquals("Alice", response.getBody().name());
        assertEquals(30, response.getBody().age());
        assertEquals("gardening", response.getBody().hobby());
        verify(profileService).addProfile(dto);
    }

    @Test
    void updateProfile_success_returnsOkAndBody() {
        ProfileDto dto = new ProfileDto("user2", "Bob", 28, "reading");
        ProfileEntity entity = mock(ProfileEntity.class);

        when(profileService.updateProfile("user2", dto)).thenReturn(entity);
        when(entity.getUserId()).thenReturn("user2");
        when(entity.getName()).thenReturn("Bob");
        when(entity.getAge()).thenReturn(28);
        when(entity.getHobby()).thenReturn("reading");

        ResponseEntity<ProfileDto> response = controller.updateProfile(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user2", response.getBody().userId());
        assertEquals("Bob", response.getBody().name());
        assertEquals(28, response.getBody().age());
        assertEquals("reading", response.getBody().hobby());
        verify(profileService).updateProfile("user2", dto);
    }

    @Test
    void deleteProfile_success_returnsOk() {
        doNothing().when(profileService).deleteProfile("user3");

        ResponseEntity<Void> response = controller.deleteProfile("user3");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(profileService).deleteProfile("user3");
    }

    @Test
    void getProfileByUserId_success_returnsOkAndBody() {
        ProfileEntity entity = mock(ProfileEntity.class);

        when(profileService.getProfileByUserId("user4")).thenReturn(entity);
        when(entity.getUserId()).thenReturn("user4");
        when(entity.getName()).thenReturn("Carol");
        when(entity.getAge()).thenReturn(35);
        when(entity.getHobby()).thenReturn("hiking");

        ResponseEntity<ProfileDto> response = controller.getProfileByUserId("user4");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("user4", response.getBody().userId());
        assertEquals("Carol", response.getBody().name());
        assertEquals(35, response.getBody().age());
        assertEquals("hiking", response.getBody().hobby());
        verify(profileService).getProfileByUserId("user4");
    }

    @Test
    void getProfileByUserId_serviceThrows_propagatesException() {
        when(profileService.getProfileByUserId("missing")).thenThrow(new RuntimeException("not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.getProfileByUserId("missing"));
        assertEquals("not found", ex.getMessage());
        verify(profileService).getProfileByUserId("missing");
    }
}