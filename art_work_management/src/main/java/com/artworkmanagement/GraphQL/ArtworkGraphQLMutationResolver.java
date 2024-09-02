package com.artworkmanagement.GraphQL;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.artworkmanagement.entities.Artist;
import com.artworkmanagement.entities.Artwork;
import com.artworkmanagement.entities.ArtworkInput;
import com.artworkmanagement.repository.ArtistRepository;
import com.artworkmanagement.repository.ArtworkRepository;
import com.artworkmanagement.repository.EmployeeRepository;

import graphql.kickstart.tools.GraphQLMutationResolver;

@Component
public class ArtworkGraphQLMutationResolver implements GraphQLMutationResolver {
	private final ArtworkRepository artworkRepository;
	private final ArtistRepository artistRepository;
	private final EmployeeRepository employeeRepository;

	public ArtworkGraphQLMutationResolver(ArtworkRepository artworkRepository, ArtistRepository artistRepository,
			EmployeeRepository employeeRepository) {
		this.artworkRepository = artworkRepository;
		this.artistRepository = artistRepository;
		this.employeeRepository = employeeRepository;
	}

	public Artwork addArtwork(ArtworkInput input) {
		Artist artist = artistRepository.findById(input.getArtistId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Artwork not found with id: " + input.getArtistId()));
		Artwork artwork = new Artwork();
		artwork.setTitle(input.getTitle());
		artwork.setYearOfCompletion(input.getYearOfCompletion());
		artwork.setPrice(input.getPrice());
		artwork.setSold(input.isSold());
		artwork.setArtist(artist);
		return artworkRepository.save(artwork);
	}

	public Boolean deleteEmployee(Long id) {
		if (employeeRepository.existsById(id)) {
			employeeRepository.deleteById(id);
			return true;
		} else {
			return false; // Or throw a custom exception
		}
	}
}
