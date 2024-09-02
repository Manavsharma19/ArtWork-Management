package com.artworkmanagement.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.artworkmanagement.entities.Artist;
import com.artworkmanagement.repository.ArtistRepository;
import com.artworkmanagement.repository.ArtworkRepository;

@WebMvcTest(ArtistController.class)
@AutoConfigureMockMvc()
public class ArtistControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ArtistRepository artistRepository;

	@MockBean
	private ArtworkRepository artworkRepository;

	@Test
	public void getArtistById_Exists_ReturnsArtist() throws Exception {
		Artist artist = new Artist(1L, "John", "Doe", "USA", true);
		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

		mockMvc.perform(get("/api/artists/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.firstName").value("John"));
	}

	@Test
	public void getArtistById_DoesNotExist_Returns404() throws Exception {
		when(artistRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/artists/2")).andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteArtistById_ExistsAndDeleted_ReturnsNoContent() throws Exception {
		// Arrange
		Artist artist = new Artist(1L, "John", "Doe", "USA", true);
		when(artistRepository.existsById(1L)).thenReturn(true);
		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

		// Act & Assert
		mockMvc.perform(delete("/api/artists/1")).andExpect(status().isNoContent());

		verify(artistRepository, times(1)).deleteById(1L);
		verify(artworkRepository, times(1)).deleteAllByArtistId(1L); // Assuming artworkRepo is also mocked
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void deleteArtistById_DoesNotExist_Returns404() throws Exception {
		when(artistRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(delete("/api/artists/1")).andExpect(status().isNotFound());
	}

	@Test
	public void deleteArtistById_NotAuthenticated_ReturnsUnauthorized() throws Exception {
		when(artistRepository.existsById(1L)).thenReturn(true);
		mockMvc.perform(delete("/api/artists/1")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "STAFF")
	public void deleteArtistById_NotAuthorized_ReturnsForbidden() throws Exception {
		when(artistRepository.existsById(1L)).thenReturn(true);

		mockMvc.perform(delete("/api/artists/1")).andExpect(status().isForbidden());
	}

}
