package com.artworkmanagement.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.artworkmanagement.entities.Artwork;
import com.artworkmanagement.repository.ArtistRepository;
import com.artworkmanagement.repository.ArtworkRepository;

@RestController
@RequestMapping("/api/artworks")
public class ArtworkController {
	private final ArtworkRepository artworkRepository;

	private final ArtistRepository artistRepo;

	public ArtworkController(ArtworkRepository artworkRepository, ArtistRepository artistRepo) {
		this.artworkRepository = artworkRepository;
		this.artistRepo = artistRepo;
	}

	@GetMapping("/artist/{artistId}")
	public ResponseEntity<?> listArtworksByArtist(@PathVariable Long artistId) {
		List<Artwork> artworks = artworkRepository.findByArtistId(artistId);
		if (artworks.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(artworks);
	}

	@GetMapping("/available")
	public ResponseEntity<?> listAvailableArtworks() {
		List<Artwork> artworks = artworkRepository.findBySoldFalse();
		if (artworks.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(artworks);
	}

	@GetMapping("/most-expensive-sold")
	public ResponseEntity<?> listMostExpensiveSoldArtworks() {
		List<Artwork> artworks = artworkRepository.findMostExpensiveSoldArtworks();
		if (artworks.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(artworks);
	}

	@GetMapping("/average-price")
	public ResponseEntity<?> findAveragePriceOfArtworksForSale() {
		Double averagePrice = artworkRepository.findAveragePriceOfArtworksForSale();
		if (averagePrice == null) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(averagePrice);
	}

	@GetMapping("/country/{country}")
	public ResponseEntity<?> listArtworksByCountry(@PathVariable String country) {
		List<Artwork> artworks = artworkRepository.findByArtistCountryOfBirth(country);
		if (artworks.isEmpty()) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		return ResponseEntity.status(200).body(artworks);
	}

	@PostMapping
	public ResponseEntity<?> addArtwork(@Valid @RequestBody Artwork artwork) {
		try {
			Artwork savedArtwork = artworkRepository.save(artwork);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedArtwork);
		} catch (DataIntegrityViolationException ex) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: " + ex.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteArtwork(@PathVariable Long id) {
		if (!artworkRepository.existsById(id)) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");
		}
		artworkRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/price")
	public ResponseEntity<?> changePrice(@PathVariable Long id, @Valid @RequestBody double newPrice) {
		Artwork artwork = artworkRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artwork not found with id: " + id));
		if (artwork == null) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		artwork.setPrice(newPrice);
		artworkRepository.save(artwork);
		return ResponseEntity.ok(artwork);
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<?> changeStatus(@PathVariable Long id) {
		Artwork artwork = artworkRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artwork not found with id: " + id));
		if (artwork == null) {
			return ResponseEntity.status(404).body("Response Message : {No Data Found}");

		}
		artwork.setSold(true);
		artworkRepository.save(artwork);
		return ResponseEntity.ok(artwork);
	}
}