package testen;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Spel;
import domein.Speler;

public class SpelTest {

	public Spel spel;
	final int MIN_AANTAL_SPELERS = 3;
	final int MAX_AANTAL_SPELERS = 4;

	@ParameterizedTest
	@ValueSource(ints = { MIN_AANTAL_SPELERS, MAX_AANTAL_SPELERS })
	void maakSpel_AantalSpelersJuist_MaaktSpel(int aantal) {
		spel = new Spel();
		spel.zetSpelOp(aantal);
		assertEquals(aantal, spel.getAantalTegelsStartKolom());
	}

	@ParameterizedTest
	@ValueSource(ints = { -100, 2, 5, 100 })
	void maakSpel_AantalSpelersOnjuist_WerptException(int aantal) {
		spel = new Spel();
		assertThrows(IllegalArgumentException.class, () -> spel.zetSpelOp(aantal));
	}

	@Test
	public void voegSpelerToe_NieuweSpeler_VoegtSpelerToe() {
		spel = new Spel();
		List<Speler> speler = Arrays.asList(new Speler("Speler1", 2000), new Speler("Speler2", 2000));
		LinkedList<Speler> spelers = new LinkedList<Speler>(speler);
		spel.voegSpelerToe(new Speler("Speler1", 2000));
		spel.voegSpelerToe(new Speler("Speler2", 2000));
		assertEquals(spelers, spel.getGekozenSpelers());
	}

}
