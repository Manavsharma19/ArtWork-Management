package com.artworkmanagement.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.artworkmanagement.entities.Artwork;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
	List<Artwork> findByArtistId(Long artistId);

	List<Artwork> findBySoldFalse();

	@Query("SELECT a FROM Artwork a WHERE a.sold = true AND a.price = (SELECT MAX(a2.price) FROM Artwork a2 WHERE a2.sold = true)")
	List<Artwork> findMostExpensiveSoldArtworks();

	@Query("SELECT AVG(a.price) FROM Artwork a WHERE a.sold = false")
	Double findAveragePriceOfArtworksForSale();

	List<Artwork> findByArtistCountryOfBirth(String country);
	
	@Transactional
	@Modifying
	void deleteAllByArtistId(Long artistId);
}
