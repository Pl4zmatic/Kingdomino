package domein;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import javax.swing.text.html.MinimalHTMLWriter;

import utils.Kleuren;
import utils.Landschappen;

/**
 * De Speler klasse representeert een speler in het spel en bevat
 * functionaliteiten om de speler te beheren.
 */
public class Speler implements Comparable<Speler> {
	private String gebruikersnaam;
	private int geboortejaar;
	private int aantalGewonnen, aantalGespeeld;
	private Kleuren kleur;
	private Koninkrijk koninkrijk;
	private int gekozenTegel;
	private List<Integer> score;

	public final static int JAAR_VANDAAG = Year.now().getValue();
	final static int GEBOORTEJAAR_OFFSET = 120;
	public final static int MINIMUM_GEBOORTEJAAR = JAAR_VANDAAG - GEBOORTEJAAR_OFFSET;
	final static int MINIMUM_LEEFTIJD = 6;
	public final static int MAXIMUM_GEBOORTEJAAR = JAAR_VANDAAG - MINIMUM_LEEFTIJD;
	final static int MINIMUM_KARAKTERS = 6;

	/**
	 * Initialiseert een nieuwe Speler met de opgegeven gebruikersnaam en
	 * geboortejaar.
	 * 
	 * @param gebruikersnaam de gebruikersnaam van de speler
	 * @param geboortejaar   het geboortejaar van de speler
	 */
	public Speler(String gebruikersnaam, int geboortejaar) {
		this.setGebruikersnaam(gebruikersnaam);
		this.setGeboortejaar(geboortejaar);
		this.aantalGespeeld = 0;
		this.aantalGewonnen = 0;
		this.koninkrijk = new Koninkrijk();
	}

	/**
	 * Initialiseert een nieuwe Speler met de opgegeven gebruikersnaam,
	 * geboortejaar, aantal gewonnen en aantal gespeelde spellen.
	 * 
	 * @param gebruikersnaam de gebruikersnaam van de speler
	 * @param geboortejaar   het geboortejaar van de speler
	 * @param aantalGewonnen het aantal gewonnen spellen van de speler
	 * @param aantalGespeeld het aantal gespeelde spellen van de speler
	 */
	public Speler(String gebruikersnaam, int geboortejaar, int aantalGewonnen, int aantalGespeeld) {
		setGebruikersnaam(gebruikersnaam);
		setGeboortejaar(geboortejaar);
		setAantalGewonnen(aantalGewonnen);
		setAantalGespeeld(aantalGespeeld);
		this.koninkrijk = new Koninkrijk();
	}

	/**
	 * Geeft de gebruikersnaam van de speler terug.
	 * 
	 * @return String De gebruikersnaam van de speler.
	 */
	public String getGebruikersnaam() {
		return gebruikersnaam;
	}

	/**
	 * Geeft het geboortejaar van de speler terug.
	 * 
	 * @return int Het geboortejaar van de speler.
	 */
	public int getGeboortejaar() {
		return geboortejaar;
	}

	/**
	 * Geeft het aantal gewonnen spellen van de speler terug.
	 * 
	 * @return int Het aantal gewonnen spellen van de speler.
	 */
	public int getAantalGewonnen() {
		return aantalGewonnen;
	}

	/**
	 * Geeft het aantal gespeelde spellen van de speler terug.
	 * 
	 * @return int Het aantal gespeelde spellen van de speler.
	 */
	public int getAantalGespeeld() {
		return aantalGespeeld;
	}

	/**
	 * Geeft de kleur van de speler terug.
	 * 
	 * @return Kleuren De kleur van de speler.
	 */
	public Kleuren getKleur() {
		return kleur;
	}

	/**
	 * Geeft de index van de gekozen tegel van de speler terug.
	 * 
	 * @return int De index van de gekozen tegel van de speler.
	 */
	public int getGekozenTegel() {
		return this.gekozenTegel;
	}

	/**
	 * Geeft het koninkrijk van de speler terug.
	 * 
	 * @return Koninkrijk Het koninkrijk van de speler.
	 */
	public Koninkrijk getKoninkrijk() {
		return this.koninkrijk;
	}

	/**
	 * Geeft een lijst met scores terug/
	 * 
	 * @return Een lijst met scores.
	 */
	public List<Integer> getScore() {
		return this.score;
	}

	/**
	 * Setter gebruikersnaam met controle op aantal karakters en soort karakters.
	 * 
	 * @param gebruikersnaam De gebruikersnaam die je aan de speler wilt geven.
	 */
	private void setGebruikersnaam(String gebruikersnaam) {
		int strLength = gebruikersnaam.length();
		if (strLength < 6)
			throw new IllegalArgumentException(
					String.format("Gebruikersnaam heeft te weinig karakters (minimum %d).", MINIMUM_KARAKTERS));

		boolean alleenSpaties = true;
		for (int i = 0; i < strLength; i++)
			if (gebruikersnaam.charAt(i) != ' ')
				alleenSpaties = false;
		if (alleenSpaties)
			throw new IllegalArgumentException("Gebruikersnaam bestaat uit alleen spaties.");

		this.gebruikersnaam = gebruikersnaam;
	}

	/**
	 * Setters geboortejaar met controle op minimum en bereik.
	 * 
	 * @param geboortejaar Het geboortejaar die je aan de speler wilt geven.
	 */
	private void setGeboortejaar(int geboortejaar) {
		int huidigeLeeftijd = JAAR_VANDAAG - geboortejaar;

		if (huidigeLeeftijd < MINIMUM_LEEFTIJD)
			throw new IllegalArgumentException(
					String.format("Huidige leeftijd is niet valide (minimum %d).", MINIMUM_LEEFTIJD));

		if (geboortejaar < MINIMUM_GEBOORTEJAAR || geboortejaar > JAAR_VANDAAG - MINIMUM_LEEFTIJD)
			throw new IllegalArgumentException(String.format("Geboortejaar is buiten mogelijk bereik [%d, %d].",
					MINIMUM_GEBOORTEJAAR, JAAR_VANDAAG - MINIMUM_LEEFTIJD));

		this.geboortejaar = geboortejaar;
	}

	/**
	 * Zet het aantal gewonnen spellen op de gewenste waarde.
	 * 
	 * @param aantalGewonnen Het aantal gewonnen spellen.
	 */
	private void setAantalGewonnen(int aantalGewonnen) {
		this.aantalGewonnen = aantalGewonnen;
	}

	/**
	 * Zet het aantal gespeelde spellen op de gewenste waarde.
	 * 
	 * @param aantalGespeeld Het aantal gespeelde spellen
	 */
	private void setAantalGespeeld(int aantalGespeeld) {
		this.aantalGespeeld = aantalGespeeld;
	}

	/**
	 * Zet het kleur van de speler op de gewenste waarde.
	 * 
	 * @param kleur Het kleur van de speler.
	 */
	public void setKleur(Kleuren kleur) {
		this.kleur = kleur;
	}

	/**
	 * Zet de tegel die de speler wilt kiezen op de gewenste tegel.
	 * 
	 * @param gekozenTegel De tegel die de speler kiest.
	 */
	public void setGekozenTegel(int gekozenTegel) {
		this.gekozenTegel = gekozenTegel;
	}

	/**
	 * Zet de score op de gewenste waarde.
	 * 
	 * @param score De gewenste waarde voor de score.
	 */
	public void setScore(List<Integer> score) {
		this.score = score;
	}

	/**
	 * Verhoogt het aantal gespeelde spellen.
	 */
	public void verhoogAantalGespeeld() {
		this.aantalGespeeld++;
	}

	/**
	 * Verhoogt het aantal gewonnen spellen.
	 */
	public void verhoogAantalGewonnen() {
		this.aantalGewonnen++;
	}

	/**
	 * Geeft een string terug met de gebruikersnaam vna de speler en de doninotegel
	 * die de speler heeft gekozen.
	 * 
	 * @return String De string met gebruikersnaam en gekozenTegel.
	 */
	public String toonSpelersituatie() {
		return String.format("-%s%n-gekozen dominotegel: %d%n", this.getGebruikersnaam(), this.gekozenTegel);
	}

	/**
	 * Plaatst de domintotegel op de gewenste plaats en richting.
	 * 
	 * @param rij         De plaats van het eerste vakje van de dominotegel.
	 * @param kolom       De plaats van het tweede vakje van de dominotegel.
	 * @param richting    De richting van de dominotegel
	 * @param dominotegel De dominotegel die de speler heeft gekozen.
	 */
	public void kiesPlaatsEnRichting(int rij, int kolom, String richting, DominoTegel dominotegel) {
		this.koninkrijk.plaatsDominoTegel(rij, kolom, richting, dominotegel);
	}

	/**
	 * Controleert of de dominotegel kan geplaatst worden.
	 * 
	 * @param tegel De tegel die men wenst te plaatsen.
	 * @return true indien de tegel geplaatst kan worden, anders false
	 */
	public boolean kanGeplaatstWorden(DominoTegel tegel) {
		boolean resultaat = false;
		for (int i = koninkrijk.getBovengrens()+1; i < koninkrijk.getOndergrens() && resultaat == false; i++) {
			for (int j = koninkrijk.getLinkergrens()+1; j < koninkrijk.getRechtergrens()
					&& resultaat == false; j++) {
				resultaat = koninkrijk.isTegelGeldig(i, j, "links", tegel);
				if (resultaat == false) {
					resultaat = koninkrijk.isTegelGeldig(i, j, "rechts", tegel);
				} else if (resultaat == false) {
					resultaat = koninkrijk.isTegelGeldig(i, j, "boven", tegel);
				} else if (resultaat == false) {
					resultaat = koninkrijk.isTegelGeldig(i, j, "onder", tegel);
				}

			}
		}
		return resultaat;
	}

	public int hashCode() {
		return Objects.hash(gekozenTegel);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Speler other = (Speler) obj;
		return gekozenTegel == other.gekozenTegel;
	}

	@Override
	public int compareTo(Speler s) {
		int lastCmp = Integer.compare(gekozenTegel, s.gekozenTegel);
		return (lastCmp != 0 ? lastCmp : s.gekozenTegel);
	}

	/**
	 * Bepaalt de punten van een koninkrijk.
	 * 
	 * @param koninkrijk Het koninkrijk waarvan men de punten wenst te weten.
	 * @return Het aantal punten van het koninkrijk.
	 */
	public List<Integer> bepaalPunten(Vakje[][] koninkrijk) {
		ArrayList<Integer> antwoord = new ArrayList<>();
		HashMap<String, Integer> scorePerLandschap = new HashMap<>();
		for (Landschappen landschap : Landschappen.values()) {
			scorePerLandschap.put(landschap.toString(), 0);
		}

		boolean[][] bezocht = new boolean[koninkrijk.length][koninkrijk[0].length];

		int grootsteGebied = 0;

		int totaalKronen = 0;

		for (int i = 0; i < koninkrijk.length; i++) {
			for (int j = 0; j < koninkrijk[i].length; j++) {
				if (!bezocht[i][j]) {
					Vakje huidigVakje = koninkrijk[i][j];
					if (huidigVakje != null) {
						List<Integer> score = berekenPunten(koninkrijk, i, j, bezocht);
						scorePerLandschap.put(huidigVakje.getLandschap(),
								scorePerLandschap.get(huidigVakje.getLandschap())
										+ ((Integer) score.get(0)).intValue());
						totaalKronen += score.get(2);
						if (((Integer) score.get(1)).intValue() >= grootsteGebied) {
							grootsteGebied = ((Integer) score.get(1)).intValue();
						}
					}
				}
			}
		}

		int totalePunten = 0;
		for (int score : scorePerLandschap.values()) {
			totalePunten += score;
		}

		antwoord.add(totalePunten);
		antwoord.add(grootsteGebied);
		antwoord.add(totaalKronen);
		return antwoord;
	}

	/**
	 * Berekent het aantal punten van aangrenzende vakjes.
	 * 
	 * @param koninkrijk Het koninkrijk waarvan men de punten wenst te weten.
	 * @param rij        De rij van het eerste vakje van het gebied.
	 * @param kolom      De kolom van het eerste vakje van het gebied.
	 * @param bezocht    De boolean die controleert of het vakje al bezocht is.
	 * @return Het aantal punten van dat gebied.
	 */
	private List<Integer> berekenPunten(Vakje[][] koninkrijk, int rij, int kolom, boolean[][] bezocht) {
		Vakje vakje = koninkrijk[rij][kolom];
		ArrayList<Integer> antwoord = new ArrayList<>();
		if (vakje == null || bezocht[rij][kolom]) {
			antwoord.add(0);
			return antwoord;
		}
		bezocht[rij][kolom] = true;

		int totaalKronen = vakje.getKronen();
		int aantalVakjes = 1;

		Queue<int[]> queue = new LinkedList<>();
		queue.add(new int[] { rij, kolom });

		while (!queue.isEmpty()) {
			int[] huidigePositie = queue.poll();
			int r = huidigePositie[0];
			int c = huidigePositie[1];

			int[][] buren = { { r - 1, c }, { r + 1, c }, { r, c - 1 }, { r, c + 1 } };
			for (int[] buur : buren) {
				int nr = buur[0];
				int nc = buur[1];
				if (nr >= 0 && nr < koninkrijk.length && nc >= 0 && nc < koninkrijk[0].length && !bezocht[nr][nc]
						&& koninkrijk[nr][nc] != null
						&& koninkrijk[nr][nc].getLandschap().equals(vakje.getLandschap())) {
					queue.add(new int[] { nr, nc });
					bezocht[nr][nc] = true;
					totaalKronen += koninkrijk[nr][nc].getKronen();
					aantalVakjes++;
				}
			}
		}

		int groepjePunten = totaalKronen * aantalVakjes;
		antwoord.add(groepjePunten);
		antwoord.add(aantalVakjes);
		antwoord.add(totaalKronen);
		return antwoord;
	}

	/**
	 * Geeft de score van een speler terug als string
	 * 
	 * @param speler De speler waarvan de punten willen geweten worden.
	 * @return De score van een speler als string
	 */
	public String scoreToString(Speler speler) {
		return String.format("%s uw huidige score is nu: %d%naantal vakje: %d%n ", speler.getGebruikersnaam(),
				speler.getScore().get(0), speler.getScore().get(1));
	}
	
	public void upload() {
		
	}

}
