package com.artworkmanagement.Utility;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	@Before("within(com.artworkmanagement.controller..*)")
	public void logBeforeAllMethods() {
		System.out.println("A method in the controller package is being invoked");
	}

	@Before("execution(* com.artworkmanagement.controller.ArtistController.listAllArtists(..))")
	public void logBeforeListAllArtists() {
		System.out.println("ArtistController - listAllArtists method is being invoked");
	}

	@Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void logBeforePostMappingMethods() {
		System.out.println("A POST method is being invoked");
	}

	@Before("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public void logBeforeDeleteMappingMethods() {
		System.out.println("A DELETE method is being invoked");
	}
}
