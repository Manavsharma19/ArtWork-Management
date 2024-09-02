package com.artworkmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.artworkmanagement.entities.Artist;
import com.artworkmanagement.repository.ArtistRepository;
import com.artworkmanagement.repository.ArtworkRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/artists")
public class ArtistController {
	private final ArtistRepository artistRepository;
	private final ArtworkRepository artworkRepo;

	public ArtistController(ArtistRepository artistRepository, ArtworkRepository artworkRepo) {
		this.artistRepository = artistRepository;
		this.artworkRepo = artworkRepo;
	}

	@GetMapping
	public ResponseEntity<?> listAllArtists() {
		List<Artist> artistData = artistRepository.findAll();
		if (artistData.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(artistData);
	}

	@PostMapping()
	public ResponseEntity<?> addArtist(@Valid @RequestBody Artist artist) {
		Artist savedArtist = artistRepository.save(artist);
		return ResponseEntity.status(201).body(savedArtist);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteArtist(@PathVariable Long id) {

		if (!artistRepository.existsById(id)) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");
		}
		artworkRepo.deleteAllByArtistId(id);
		artistRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getArtistById(@PathVariable Long id) {
		Artist artist = artistRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
		if (artist == null) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.ok(artist);
	}

}
