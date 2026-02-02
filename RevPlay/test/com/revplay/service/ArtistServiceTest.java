package com.revplay.service;

import com.revplay.model.ArtistAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ArtistService using mock DAO.
 */
public class ArtistServiceTest {

    private MockArtistDao mockArtistDao;
    private ArtistServiceImpl artistService;

    @BeforeEach
    public void setup() {
        mockArtistDao = new MockArtistDao();
        artistService = new ArtistServiceImpl();
        artistService.setArtistDao(mockArtistDao);
    }

    @Test
    public void testRegisterArtist_Success() {
        ArtistAccount artist = new ArtistAccount();
        artist.setEmail("artist@music.com");
        artist.setPasswordHash("secret");
        artist.setStageName("DJ Test");

        boolean result = artistService.registerArtist(artist);

        assertTrue(result, "Artist registration should succeed");
        assertNotNull(mockArtistDao.getArtistByEmail("artist@music.com"));
    }

    @Test
    public void testRegisterArtist_DuplicateEmail() {
        ArtistAccount artist1 = new ArtistAccount();
        artist1.setEmail("same@music.com");
        artistService.registerArtist(artist1);

        ArtistAccount artist2 = new ArtistAccount();
        artist2.setEmail("same@music.com");

        boolean result = artistService.registerArtist(artist2);

        assertFalse(result, "Should not allow duplicate email");
    }

    @Test
    public void testLogin_Success() {
        ArtistAccount artist = new ArtistAccount();
        artist.setEmail("login@music.com");
        artist.setPasswordHash("mypassword");
        mockArtistDao.registerArtist(artist);

        ArtistAccount loggedIn = artistService.login("login@music.com", "mypassword");

        assertNotNull(loggedIn, "Login should succeed with correct credentials");
    }

    @Test
    public void testLogin_WrongPassword() {
        ArtistAccount artist = new ArtistAccount();
        artist.setEmail("wrong@music.com");
        artist.setPasswordHash("correct");
        mockArtistDao.registerArtist(artist);

        ArtistAccount loggedIn = artistService.login("wrong@music.com", "incorrect");

        assertNull(loggedIn, "Login should fail with wrong password");
    }

    @Test
    public void testUpdateProfile_Success() {
        ArtistAccount artist = new ArtistAccount();
        artist.setEmail("profile@music.com");
        artist.setStageName("Original");
        mockArtistDao.registerArtist(artist);

        artist.setStageName("Updated Stage Name");
        boolean result = artistService.updateProfile(artist);

        assertTrue(result);
        assertEquals("Updated Stage Name", mockArtistDao.getArtistById(artist.getArtistId()).getStageName());
    }
}
