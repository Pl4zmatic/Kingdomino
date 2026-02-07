package testen;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized.Parameters;

import domein.DominoTegel;
import domein.Koninkrijk;
import domein.Vakje;
import utils.Landschappen;

class KoninkrijkTest {

	@Test
	void plaatsTegel_PlaatsCorrect_MaakObject_() {
		Koninkrijk koninkrijk = new Koninkrijk();
		koninkrijk.getKoninkrijk()[4][4] = new Vakje(Landschappen.STARTVAKJE, 0);
		assertTrue(koninkrijk.isTegelGeldig(4, 5, "rechts", new DominoTegel(1)));

	}
	
	@ParameterizedTest
	@ValueSource(ints= {100,9,-1,-100})
	void plaatsTegel_RijBuitenGrenzen_WerptExcpetion(int rij) {
		Koninkrijk koninkrijk = new Koninkrijk();
		koninkrijk.getKoninkrijk()[4][4] = new Vakje(Landschappen.STARTVAKJE, 0);
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> koninkrijk.plaatsDominoTegel(rij,5,"rechts",new DominoTegel(1)));
	}
	
	@ParameterizedTest
	@ValueSource(ints= {100,9,-1,-100})
	void plaatsTegel_KolomBuitenGrenzen_WerptException(int kolom) {
		Koninkrijk koninkrijk = new Koninkrijk();
		koninkrijk.getKoninkrijk()[4][4] = new Vakje(Landschappen.STARTVAKJE, 0);
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> koninkrijk.plaatsDominoTegel(4,kolom,"rechts",new DominoTegel(1)));
	}

}
