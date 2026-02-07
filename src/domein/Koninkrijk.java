package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.Landschappen;

/**
 * De Koninkrijk klasse representeert het koninkrijk van een speler met vakjes
 * en dominotegels.
 */
public class Koninkrijk {
	private Vakje[][] koninkrijk;
	private DominoTegel[][] koninkrijkDominoTegel;
	private final int MAX_RIJEN = 5;
	private final int MAX_KOLOMMEN = 5;
	private int bovengrens, ondergrens, linkergrens, rechtergrens;

	/**
	 * Initialiseert een nieuw Koninkrijk object.
	 */
	public Koninkrijk() {
		koninkrijk = new Vakje[9][9];
		koninkrijkDominoTegel = new DominoTegel[9][9];
		koninkrijk[4][4] = new Vakje(Landschappen.STARTVAKJE, 0);
		koninkrijkDominoTegel[4][4] = new DominoTegel();
		bovengrens = -1;
		linkergrens = -1;
		ondergrens = 9;
		rechtergrens = 9;
	}

	/**
	 * Geeft de bovengrens van het koninkrijk terug.
	 * 
	 * @return int De bovengrens van het koninkrijk.
	 */
	public int getBovengrens() {
		return bovengrens;
	}

	/**
	 * Geeft de ondergrens van het koninkrijk terug.
	 * 
	 * @return int De bovengrens van het koninkrijk.
	 */
	public int getOndergrens() {
		return ondergrens;
	}

	/**
	 * Geeft de linkergrens van het koninkrijk terug.
	 * 
	 * @return int De bovengrens van het koninkrijk.
	 */
	public int getLinkergrens() {
		return linkergrens;
	}

	/**
	 * Geeft de ondergrens van het koninkrijk terug.
	 * 
	 * @return int De bovengrens van het koninkrijk.
	 */
	public int getRechtergrens() {
		return rechtergrens;
	}

	/**
	 * Plaatst een domino tegel en controleert of deze op een geldige plaats zou
	 * staan.
	 * 
	 * @param rij         De rij waar de tegel moet worden geplaatst (begint bij 1).
	 * 
	 * @param kolom       De kolom waar de tegel moet worden geplaatst (begint bij
	 *                    1).
	 * @param richting    De richting waarin de tegel moet worden geplaatst
	 *                    ("rechts", "links", "onder", "boven").
	 * @param dominotegel De Domino-tegel die moet worden geplaatst.
	 * @throws IllegalArgumentException Als de opgegeven plaats voor de tegel
	 *                                  ongeldig is.
	 */
	public void plaatsDominoTegel(int rij, int kolom, String richting, DominoTegel dominotegel) {
		if ((rij - 1 == this.bovengrens + 1 && richting.equals("boven")
				|| (rij - 1 == this.ondergrens - 1 && richting.equals("onder"))
				|| (kolom - 1 == this.linkergrens + 1 && richting.equals("links")
						|| (kolom - 1 == this.rechtergrens - 1 && richting.equals("rechts")
								|| (rij - 1 <= this.bovengrens || rij - 1 >= this.ondergrens
										|| kolom - 1 <= this.linkergrens || kolom - 1 >= this.rechtergrens))))) {
			throw new ArrayIndexOutOfBoundsException("Gelieve de tegel binnen het koninkrijk plaatsen");
		}
		rij--;
		kolom--;
		if (isTegelGeldig(rij, kolom, richting, dominotegel)) {
			koninkrijk[rij][kolom] = dominotegel.getVakjes().get(0);
			koninkrijkDominoTegel[rij][kolom] = dominotegel;
			switch (richting) {
			case "rechts" -> {
				koninkrijk[rij][kolom + 1] = dominotegel.getVakjes().get(1);
			}

			case "links" -> {
				koninkrijk[rij][kolom - 1] = dominotegel.getVakjes().get(1);
			}

			case "onder" -> {
				koninkrijk[rij + 1][kolom] = dominotegel.getVakjes().get(1);
			}

			case "boven" -> {
				koninkrijk[rij - 1][kolom] = dominotegel.getVakjes().get(1);
			}
			}
		} else {
			throw new IllegalArgumentException("Kies een geldige plaats voor deze tegel");
		}
		bepaalGrenzen();
	}

	/**
	 * Geeft een dubbele array terug van vakjes die het Koninkrijk samenstellen.
	 * 
	 * @return Vaje [][] Een dubbele array terug van vakjes die het Koninkrijk
	 *         samenstellen.
	 */
	public Vakje[][] getKoninkrijk() {
		return koninkrijk;
	}

	/**
	 * Geeft een dubbele array terug van dominotegels die het Koninkrijk
	 * samenstellen.
	 * 
	 * @return DominoTegel [][] Een dubbele array terug van Dominotegels die het
	 *         Koninkrijk samenstellen.
	 */
	public DominoTegel[][] getKoninkrijkDominoTegel() {
		return koninkrijkDominoTegel;
	}

	/**
	 * Controleert of het vakje op de opgegeven rij en kolom een geldig vakje is
	 * voor het plaatsen van een Domino-tegel.
	 * 
	 * @param rij       De rij waar het vakje wordt gecontroleerd.
	 * @param kolom     De kolom waar het vakje wordt gecontroleerd.
	 * @param landschap Het landschap van de Domino-tegel die wordt geplaatst.
	 * @return {@code true} als het vakje geldig is, anders {@code false}.
	 */
	private boolean isVakjeGeldig(int rij, int kolom, String landschap) {
		boolean resultaat = false;
		String boven = koninkrijk[Math.max(0, rij - 1)][kolom] != null
				? koninkrijk[Math.max(0, rij - 1)][kolom].getLandschap()
				: "";
		String onder = koninkrijk[Math.min(8, rij + 1)][kolom] != null
				? koninkrijk[Math.min(8, rij + 1)][kolom].getLandschap()
				: "";
		String links = koninkrijk[rij][Math.max(0, kolom - 1)] != null
				? koninkrijk[rij][Math.max(0, kolom - 1)].getLandschap()
				: "";
		String rechts = koninkrijk[rij][Math.min(8, kolom + 1)] != null
				? koninkrijk[rij][Math.min(8, kolom + 1)].getLandschap()
				: "";
		if (resultaat == false && (boven.equals(landschap) || boven.equals(Landschappen.STARTVAKJE.toString()))) {
			resultaat = true;
		}
		if (resultaat == false && (onder.equals(landschap) || onder.equals(Landschappen.STARTVAKJE.toString()))) {
			resultaat = true;
		}

		if (resultaat == false && (links.equals(landschap) || links.equals(Landschappen.STARTVAKJE.toString()))) {
			resultaat = true;
		}

		if (resultaat == false && (rechts.equals(landschap) || rechts.equals(Landschappen.STARTVAKJE.toString()))) {
			resultaat = true;
		}

		return resultaat;
	}

	/**
	 * Controleert of een Domino-tegel op de opgegeven positie en richting kan
	 * worden geplaatst.
	 * 
	 * @param rij         De rij waar de Domino-tegel wordt geplaatst.
	 * @param kolom       De kolom waar de Domino-tegel wordt geplaatst.
	 * @param richting    De richting waarin de Domino-tegel wordt geplaatst
	 *                    ("rechts", "links", "boven" of "onder").
	 * @param dominotegel De Domino-tegel die wordt geplaatst.
	 * @return {@code true} als de plaatsing geldig is, anders {@code false}.
	 */
	public boolean isTegelGeldig(int rij, int kolom, String richting, DominoTegel dominotegel) {
		String value = dominotegel.getVakjes().get(0).getLandschap();
		String value2 = dominotegel.getVakjes().get(1).getLandschap();
		boolean resultaat = false;
		int rij2 = 0;
		int kolom2 = 0;

		switch (richting) {
		case "rechts" -> {
			rij2 = rij;
			kolom2 = Math.min(getRechtergrens() - 1, kolom + 1);
		}

		case "links" -> {
			rij2 = rij;
			kolom2 = Math.max(getLinkergrens() + 1, kolom - 1);
		}

		case "boven" -> {
			rij2 = Math.max(getBovengrens() + 1, rij - 1);
			kolom2 = kolom;
		}

		case "onder" -> {
			rij2 = Math.min(getOndergrens() - 1, rij + 1);
			kolom2 = kolom;
		}
		}

		if (koninkrijk[rij][kolom] != null || koninkrijk[rij2][kolom2] != null || (rij == rij2 && kolom == kolom2)) {
			return false;
		}

		resultaat = isVakjeGeldig(rij, kolom, value);
		if (resultaat == false) {
			resultaat = isVakjeGeldig(rij2, kolom2, value2);
		}

		return resultaat;
	}

	/**
	 * Bepaalt de grenzen van het koninkrijk op basis van de geplaatste
	 * Domino-tegels.
	 */
	private void bepaalGrenzen() {
		boolean gevonden = false;

		for (int rij = 0; rij < this.koninkrijk.length / 2 && !gevonden; rij++) {
			for (int kolom = 0; kolom < this.koninkrijk[0].length && !gevonden; kolom++) {
				if (this.koninkrijk[rij][kolom] != null) {
					this.ondergrens = rij + 5;
					gevonden = true;
				}
			}
		}
		gevonden = false;

		for (int kolom = 0; kolom < this.koninkrijk[0].length / 2 && !gevonden; kolom++) {
			for (int rij = 0; rij < this.koninkrijk.length && !gevonden; rij++) {
				if (this.koninkrijk[rij][kolom] != null) {
					this.rechtergrens = kolom + 5;
					gevonden = true;
				}
			}
		}
		gevonden = false;

		for (int rij = this.koninkrijk.length - 1; rij > this.koninkrijk.length / 2 && !gevonden; rij--) {
			for (int kolom = 0; kolom < this.koninkrijk[0].length && !gevonden; kolom++) {
				if (this.koninkrijk[rij][kolom] != null) {
					this.bovengrens = rij - 5;
					gevonden = true;
				}
			}
		}
		gevonden = false;

		for (int kolom = this.koninkrijk[0].length - 1; kolom > this.koninkrijk[0].length / 2 && !gevonden; kolom--) {
			for (int rij = 0; rij < this.koninkrijk.length && !gevonden; rij++) {
				if (this.koninkrijk[rij][kolom] != null) {
					this.linkergrens = kolom - 5;
					gevonden = true;
				}
			}
		}

	}
}