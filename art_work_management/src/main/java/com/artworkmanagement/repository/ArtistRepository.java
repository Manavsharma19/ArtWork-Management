package com.artworkmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artworkmanagement.entities.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
