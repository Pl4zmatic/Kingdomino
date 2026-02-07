package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.Landschappen;

/**
 * De DominoTegel klasse representeert een dominotegel met vakjes en
 * eigenschappen  index en richting.
 */
public class DominoTegel implements Comparable<DominoTegel> {

	private List<Vakje> vakjes = new ArrayList<Vakje>();
	private int index;
	private String richting;

	/**
	 * Initialiseert een nieuwe instantie van de DominoTegel klasse met de opgegeven
	 * index.
	 * 
	 * @param index De index van de dominotegel.
	 */
	public DominoTegel(int index) {
		this.index = index;
		switch (this.index) {
		case 1 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 2 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 3 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 4 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 5 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 6 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 7 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 0));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 8 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 0));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 9 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 0));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 10 -> {
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 11 -> {
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 12 -> {
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
		}
		case 13 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 14 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 15 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 16 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
		}
		case 17 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 18 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 19 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 1));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 20 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 1));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 21 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 1));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 22 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 1));
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
		}
		case 23 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 1));
			vakjes.add(new Vakje(Landschappen.MIJN, 0));
		}
		case 24 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 25 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 26 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 27 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 28 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.WATER, 0));
		}
		case 29 -> {
			vakjes.add(new Vakje(Landschappen.BOS, 1));
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
		}
		case 30 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 31 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 32 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 33 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 34 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 35 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 1));
			vakjes.add(new Vakje(Landschappen.BOS, 0));
		}
		case 36 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 1));
		}
		case 37 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 1));
		}
		case 38 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 1));
		}
		case 39 -> {
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 1));
		}
		case 40 -> {
			vakjes.add(new Vakje(Landschappen.MIJN, 1));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 41 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 2));
		}
		case 42 -> {
			vakjes.add(new Vakje(Landschappen.WATER, 0));
			vakjes.add(new Vakje(Landschappen.GRAS, 2));
		}
		case 43 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 2));
		}
		case 44 -> {
			vakjes.add(new Vakje(Landschappen.GRAS, 0));
			vakjes.add(new Vakje(Landschappen.AARDE, 2));
		}
		case 45 -> {
			vakjes.add(new Vakje(Landschappen.MIJN, 2));
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
		}
		case 46 -> {
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
			vakjes.add(new Vakje(Landschappen.MIJN, 2));
		}
		case 47 -> {
			vakjes.add(new Vakje(Landschappen.AARDE, 0));
			vakjes.add(new Vakje(Landschappen.MIJN, 2));
		}
		case 48 -> {
			vakjes.add(new Vakje(Landschappen.ZAND, 0));
			vakjes.add(new Vakje(Landschappen.MIJN, 3));
		}
		}
		;
	}

	/**
	 * Initialiseert een nieuwe instantie van de DominoTegel klasse.
	 */
	public DominoTegel() {
		vakjes.add(new Vakje(Landschappen.STARTVAKJE, 0));
	}

	/**
	 * Geeft de index van de dominotegel terug.
	 * 
	 * @return De index van de dominotegel.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Zet de index van een dominotegel.
	 * 
	 * @param cijfer De index die de dominotegel moet krijgen
	 */
	public void setIndex(int cijfer) {
		this.index = cijfer;
	}

	/**
	 * Geeft de richting van de dominotegel terug.
	 * 
	 * @return String De richting van de dominotegel.
	 */
	public String getRichting() {
		return richting;
	}

	/**
	 * Zet de richting van de dominotegel.
	 * 
	 * @param richting De richting die je de dominotegel wilt geven.
	 */
	public void setRichting(String richting) {
		this.richting = richting;
	}

	/**
	 * Geeft een lijst terug met de vakjes van de dominotegel.
	 * 
	 * @return List<Vakje> De lijst met vakjes van de dominotegel.
	 */
	public List<Vakje> getVakjes() {
		return this.vakjes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DominoTegel other = (DominoTegel) obj;
		return index == other.index;
	}

	@Override
	public int compareTo(DominoTegel o) {
		int lastCmp = Integer.compare(index, o.index);
		return (lastCmp != 0 ? lastCmp : o.index);
	}
}
