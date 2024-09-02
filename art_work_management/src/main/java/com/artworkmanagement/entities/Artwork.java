package com.artworkmanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Artwork {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Title is mandatory")
	@Column(nullable = false)
	private String title;

	@Min(value = 0, message = "Year must be a positive number")
	@Column(nullable = false)
	private int yearOfCompletion;

	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private boolean sold;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "artist_id", nullable = false)
	private Artist artist;

	public Artwork() {
		super();
	}

	public Artwork(Long id, String title, int yearOfCompletion, double price, boolean sold, Artist artist) {
		super();
		this.id = id;
		this.title = title;
		this.yearOfCompletion = yearOfCompletion;
		this.price = price;
		this.sold = sold;
		this.artist = artist;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYearOfCompletion() {
		return yearOfCompletion;
	}

	public void setYearOfCompletion(int yearOfCompletion) {
		this.yearOfCompletion = yearOfCompletion;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

}
