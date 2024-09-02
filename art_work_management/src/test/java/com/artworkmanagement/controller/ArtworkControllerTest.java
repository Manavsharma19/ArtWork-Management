package com.artworkmanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.artworkmanagement.entities.Artist;
import com.artworkmanagement.entities.Artwork;
import com.artworkmanagement.repository.ArtistRepository;
import com.artworkmanagement.repository.ArtworkRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ArtworkController.class)
@AutoConfigureMockMvc()
public class ArtworkControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ArtistRepository artistRepository;

	@MockBean
	private ArtworkRepository artworkRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(roles = "MANAGER")
	public void postArtwork_ValidData_ReturnsCreated() throws Exception {
		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON).content(
				"{\"title\":\"Starry Night and Owls\",\"yearOfCompletion\":1819,\"price\":9000000,\"sold\":false,\"artist\":{\"id\":1}}"))
				.andExpect(status().isCreated());
	}

	@Test
	public void testAddArtwork_UnauthenticatedUser() throws Exception {
		String artworkJson = "{ \"title\": \"Mona Lisa\", \"yearOfCompletion\": 1503, \"price\": 100000, \"sold\": false, \"artist\": { \"id\": 1 } }";
		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON).content(artworkJson))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testAddArtwork_UnauthorizedUser() throws Exception {
		String artworkJson = "{ \"title\": \"Mona Lisa\", \"yearOfCompletion\": 1503, \"price\": 100000, \"sold\": false, \"artist\": { \"id\": 1 } }";

		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON).content(artworkJson)
				.with(user("staff").password("password").roles("STAFF"))) // Simulate a STAFF user
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void postArtwork_Conflict_ReturnsConflict() throws Exception {
		Artist artist = new Artist(1L, "John", "Doe", "USA", true);
		when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
		when(artworkRepository.save(any(Artwork.class))).thenThrow(new DataIntegrityViolationException("Conflict"));
		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON).content(
				"{\"title\":\"Starry Night and Owls\",\"yearOfCompletion\":1819,\"price\":9000000,\"sold\":false,\"artist\":{\"id\":1}}"))
				.andExpect(status().isConflict());
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void postArtwork_InvalidData_ReturnsBadRequest() throws Exception {
		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\": \"\", \"year\": 1503, \"price\": -100, \"sold\": false,\"artist\":{\"id\":1}}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(roles = "MANAGER")
	public void postArtwork_NoData_ReturnsBadRequest() throws Exception {
		mockMvc.perform(post("/api/artworks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
