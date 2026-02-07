package testen;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Speler;
import domein.SpelerRepository;
import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

class SpelerTest {
	private Speler speler;

	final String CORRECTE_NAAM = "avatar";
	final int CORRECTE_JAAR = 2000;
	final int CORRECTE_AANTALSPELLEN = 4;
	final int CORRECTE_AANTALGEWONNEN = 25;

	@Test
	void maakSpeler_alleGegevensCorrect_maaktObject() {
		speler = new Speler(CORRECTE_NAAM, CORRECTE_JAAR, CORRECTE_AANTALGEWONNEN, CORRECTE_AANTALSPELLEN);
		Assertions.assertEquals(CORRECTE_NAAM, speler.getGebruikersnaam());
		Assertions.assertEquals(CORRECTE_JAAR, speler.getGeboortejaar());
		Assertions.assertEquals(CORRECTE_AANTALGEWONNEN, speler.getAantalGewonnen());
		Assertions.assertEquals(CORRECTE_AANTALSPELLEN, speler.getAantalGespeeld());
	}

	@Test
	void maakSpeler_correcteGebruikersnaamGeboortejaar_maaktObject() {
		speler = new Speler(CORRECTE_NAAM, CORRECTE_JAAR);
		Assertions.assertEquals(CORRECTE_NAAM, speler.getGebruikersnaam());
		Assertions.assertEquals(CORRECTE_JAAR, speler.getGeboortejaar());
		Assertions.assertEquals(0, speler.getAantalGewonnen());
		Assertions.assertEquals(0, speler.getAantalGespeeld());
	}

	@ParameterizedTest
	@ValueSource(strings = { "      ", "avata" })
	void maakSpeler_OnjuisteGebruikersnaam_WerptException(String naam) {
		assertThrows(IllegalArgumentException.class, () -> new Speler(naam, 2003));
	}

	@Test
	void maakSpeler_DuplicateGebruikersnaam_WerptException() {
		Speler speler1 = new Speler(CORRECTE_NAAM, CORRECTE_JAAR);
		SpelerRepository sr = new SpelerRepository();
		sr.voegToe(speler1);
		assertThrows(IllegalArgumentException.class, () -> sr.voegToe(new Speler(CORRECTE_NAAM, CORRECTE_JAAR)));
	}

	@ParameterizedTest
	@ValueSource(ints = { 1, 1899, 2019, 2025, 2200 })
	void maakSpeler_OnjuistGeboorteJaar_WerptException(int geboortejaar) {
		assertThrows(IllegalArgumentException.class, () -> new Speler(CORRECTE_NAAM, geboortejaar));
	}

}
