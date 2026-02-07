package domein;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

import dto.DominoTegelDTO;
import dto.SpelerDTO;
import utils.Kleuren;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class DomeinController {

	private final SpelerRepository spelerRepository;
	private Spel spel;

	/**
	 * Initialiseert een nieuwe instantie van de DomeinController klasse.
	 */
	public DomeinController() {
		spelerRepository = new SpelerRepository();
	}

	/**
	 * Registreert een nieuwe speler met de opgegeven gebruikersnaam en
	 * geboortejaar.
	 *
	 * @param gebruikersnaam De gebruikersnaam van de nieuwe speler.
	 * @param geboortejaar   Het geboortejaar van de nieuwe speler.
	 */
	public void registreerSpeler(String gebruikersnaam, int geboortejaar) {
		Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
		spelerRepository.voegToe(nieuweSpeler);
	}

	/**
	 * Voegt een speler toe aan het spel met de opgegeven gebruikersnaam en kleur.
	 *
	 * @param gebruikersnaam De gebruikersnaam van de speler die aan het spel wordt
	 *                       toegevoegd.
	 * @param kleur          De kleur van de speler die aan het spel wordt
	 *                       toegevoegd.
	 */
	private void voegSpelerToe(String gebruikersnaam, Kleuren kleur) {
		Speler speler = spelerRepository.geefSpeler(gebruikersnaam);
		speler.setKleur(kleur);
		spel.voegSpelerToe(speler);
	}

	/**
	 * Voegt spelers toe aan het spel en initialiseert het spel.
	 *
	 * @param spelers De lijst van spelers die aan het spel moeten worden
	 *                toegevoegd.
	 * @param kleuren De lijst van kleuren van de spelers.
	 */
	public void voegSpelersToeEnMaakSpel(List<SpelerDTO> spelers, List<Kleuren> kleuren) {
		spel = new Spel();
		for (int i = 0; i < spelers.size(); i++) {
			voegSpelerToe(spelers.get(i).gebruikersnaam(), kleuren.get(i));
		}
	}

	/**
	 * Zet het spel op met het opgegeven aantal spelers.
	 *
	 * @param aantalSpelers Het aantal spelers waarmee het spel wordt opgezet.
	 */
	public void zetSpelOp(int aantalSpelers) {
		spel.zetSpelOp(aantalSpelers);
	}

	/**
	 * Zet de koning op de gekozen dominotegel.
	 *
	 * @param keuze          De keuze voor de koning.
	 * @param gebruikersnaam De gebruikersnaam van de speler van de koning.
	 */
	public void zetKoning(int keuze, String gebruikersnaam) {
		spel.zetKoning(keuze, gebruikersnaam);
	}

	/**
	 * Geeft een lijst van SpelerDTO-objecten die de situatie van elke speler in het
	 * spel beschrijft.
	 *
	 * @return Een lijst van SpelerDTO-objecten die de situatie van elke speler in
	 *         het spel beschrijft.
	 */
	public List<SpelerDTO> toonSpelerSituaties() {
		List<Speler> spelers = spel.getGekozenSpelers();
		List<SpelerDTO> spelersDTO = new ArrayList<>();
		for (Speler speler : spelers) {
			spelersDTO.add(new SpelerDTO(speler.getGebruikersnaam(), speler.getGeboortejaar(),
					speler.getAantalGewonnen(), speler.getAantalGespeeld(), speler.getKleur(), speler.getGekozenTegel(),
					speler.getKoninkrijk(), speler.getScore()));
		}

		return spelersDTO;
	}

	/**
	 * Geeft een lijst van SpelerDTO-objecten die alle gebruikers in het spel
	 * vertegenwoordigen.
	 *
	 * @return Een lijst van SpelerDTO-objecten die alle gebruikers in het spel
	 *         vertegenwoordigen.
	 */
	public List<SpelerDTO> geefAlleGebruikers() {
		List<SpelerDTO> alleGebruikersDTO = new ArrayList<SpelerDTO>();
		List<Speler> alleGebruikersList = spelerRepository.geefAlleSpelers();
		for (Speler s : alleGebruikersList) {
			SpelerDTO speler = new SpelerDTO(s.getGebruikersnaam(), s.getGeboortejaar(), 0, 0, null, 0, null, null);
			alleGebruikersDTO.add(speler);
		}

		return alleGebruikersDTO;
	}

	/**
	 * Geeft een lijst van SpelerDTO-objecten die de niet-gekozen spelers in het
	 * spel vertegenwoordigen.
	 *
	 * @param gekozenSpelers Een lijst van SpelerDTO-objecten die de gekozen spelers
	 *                       vertegenwoordigen.
	 * @return Een lijst van SpelerDTO-objecten die de niet-gekozen spelers in het
	 *         spel vertegenwoordigen.
	 */
	public List<SpelerDTO> geefOngekozenSpelers(List<SpelerDTO> gekozenSpelers) {
		List<SpelerDTO> resultaat = new ArrayList<SpelerDTO>();
		List<Speler> alleSpelers = spelerRepository.geefAlleSpelers();

		for (Speler speler : alleSpelers) {
			boolean isGekozen = false;
			for (SpelerDTO gekozenSpeler : gekozenSpelers)
				if (speler.getGebruikersnaam().equals(gekozenSpeler.gebruikersnaam()))
					isGekozen = true;
			if (!isGekozen)
				resultaat.add(new SpelerDTO(speler.getGebruikersnaam(), speler.getGeboortejaar(),
						speler.getAantalGewonnen(), speler.getAantalGespeeld(), speler.getKleur(),
						speler.getGekozenTegel(), speler.getKoninkrijk(), speler.getScore()));
		}

		return resultaat;

	}

	/**
	 * Geeft een lijst van Kleuren die nog niet zijn gekozen op basis van de
	 * opgegeven lijst van gekozen kleuren.
	 *
	 * @param gekozenKleuren De lijst van reeds gekozen kleuren.
	 * @return Een lijst van Kleuren die nog niet zijn gekozen.
	 */
	public List<Kleuren> geefOngekozenKleuren(List<Kleuren> gekozenKleuren) {
		List<Kleuren> resultaat = new ArrayList<Kleuren>();

		for (Kleuren kleur : Kleuren.values()) {
			boolean isGekozen = false;
			for (Kleuren gekozenKleur : gekozenKleuren)
				if (kleur.equals(gekozenKleur))
					isGekozen = true;
			if (!isGekozen)
				resultaat.add(kleur);
		}

		return resultaat;
	}

	/**
	 * Zorgt ervoor dat een ronde van het spel kan worden gespeeld.
	 *
	 * @param rondeIsOver Een boolean die aangeeft of de ronde voorbij is.
	 */
	public void speelRonde(boolean rondeIsOver) {
		spel.schuifSpelersDoor();

		if (spel.getEindKolom().size() == 0)
			spel.vulEindkolom();

		if (rondeIsOver) {
			spel.verplaatstEindKolomNaarStartKolom();
			spel.sorteerSpelers();
			spel.vulEindkolom();
		}
	}

	/**
	 * Geeft een wachtrij van DominoTegelDTO-objecten die de beschikbare tegels in
	 * het spel vertegenwoordigen.
	 *
	 * @return Een wachtrij van DominoTegelDTO-objecten die de beschikbare tegels in
	 *         het spel vertegenwoordigen.
	 */
	public Queue<DominoTegelDTO> geefBeschikbareTegels() {
		Queue<DominoTegelDTO> resultaat = new ArrayDeque<>();
		Queue<DominoTegel> beschikbareTegels = spel.geefBeschikbareTegels();

		for (DominoTegel dt : beschikbareTegels) {
			resultaat.add(new DominoTegelDTO(dt.getVakjes(), dt.getIndex(), dt.getRichting()));
		}
		return resultaat;
	}

	/**
	 * Controleert of het spel is afgelopen.
	 *
	 * @return true als het spel is afgelopen, anders false.
	 */
	public boolean isSpelEinde() {
		return spel.getSpelEinde();
	}

	/**
	 * Geeft een lijst van SpelerDTO-objecten die de gewonnen spelers in het spel
	 * vertegenwoordigen.
	 *
	 * @return Een lijst van SpelerDTO-objecten die de gewonnen spelers in het spel
	 *         vertegenwoordigen.
	 */
	public List<SpelerDTO> geefGewonnenSpelers() {
		List<SpelerDTO> gewonnenSpelers = new ArrayList<>();
		List<Speler> spelers = spel.geefGewonnenSpelers();

		for (Speler s : spelers) {
			gewonnenSpelers.add(new SpelerDTO(s.getGebruikersnaam(), s.getGeboortejaar(), s.getAantalGewonnen(),
					s.getAantalGespeeld(), s.getKleur(), s.getGekozenTegel(), s.getKoninkrijk(), s.getScore()));
		}
		return gewonnenSpelers;
	}

	/**
	 * Geeft een lijst van DominoTegelDTO-objecten die de tegels in de startkolom
	 * van het spel vertegenwoordigen.
	 *
	 * @return Een lijst van DominoTegelDTO-objecten die de tegels in de startkolom
	 *         van het spel vertegenwoordigen.
	 */
	public List<DominoTegelDTO> getStartKolom() {
		List<DominoTegelDTO> resultaat = new ArrayList<>();
		List<DominoTegel> startKolom = spel.getStartKolom();

		for (DominoTegel dt : startKolom) {
			resultaat.add(new DominoTegelDTO(dt.getVakjes(), dt.getIndex(), dt.getRichting()));
		}
		return resultaat;
	}

	/**
	 * Geeft een lijst van DominoTegelDTO-objecten die de tegels in de eindkolom van
	 * het spel vertegenwoordigen.
	 *
	 * @return Een lijst van DominoTegelDTO-objecten die de tegels in de eindkolom
	 *         van het spel vertegenwoordigen.
	 */
	public List<DominoTegelDTO> getEindKolom() {
		List<DominoTegelDTO> resultaat = new ArrayList<>();
		List<DominoTegel> eindKolom = spel.getEindKolom();

		for (DominoTegel dt : eindKolom) {
			resultaat.add(new DominoTegelDTO(dt.getVakjes(), dt.getIndex(), dt.getRichting()));
		}
		return resultaat;
	}

	/**
	 * Laat de speler een plaats en richting kiezen voor een bepaalde tegel.
	 *
	 * @param speler   De speler die de plaats en richting kiest.
	 * @param plaats1  De eerste plaats die gekozen is.
	 * @param plaats2  De tweede plaats die gekozen is.
	 * @param richting De richting die gekozen is.
	 */
	public void kiesPlaatsEnRichting(SpelerDTO speler, int rij, int kolom, String richting) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				DominoTegel tegel = spel.getStartKolom().get(spelertje.getGekozenTegel() - 1);
				tegel.setRichting(richting);
				spelertje.kiesPlaatsEnRichting(rij, kolom, richting, tegel);
			}
		}
	}

	/**
	 * Geeft de gekozen tegel van de speler als een DominoTegelDTO-object.
	 *
	 * @param speler De speler waarvan de gekozen tegel moet worden opgehaald.
	 * @return Een DominoTegelDTO-object dat de gekozen tegel van de speler
	 *         vertegenwoordigt, of null als de speler geen tegel heeft gekozen.
	 */
	public DominoTegelDTO getGekozenTegel(SpelerDTO speler) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				return new DominoTegelDTO((spel.getStartKolom().get(spelertje.getGekozenTegel() - 1).getVakjes()),
						(spel.getStartKolom().get(spelertje.getGekozenTegel() - 1).getIndex()),
						spel.getStartKolom().get(spelertje.getGekozenTegel() - 1).getRichting());
			}
		}
		return null;
	}

	/**
	 * Geeft een lijst van SpelerDTO-objecten die de gekozen spelers in het spel
	 * vertegenwoordigen.
	 *
	 * @return Een lijst van SpelerDTO-objecten die de gekozen spelers in het spel
	 *         vertegenwoordigen.
	 */
	public List<SpelerDTO> getGekozenSpelers() {
		List<SpelerDTO> gekozenSpelers = new ArrayList<SpelerDTO>();
		for (Speler speler : spel.getGekozenSpelers()) {
			SpelerDTO spelerDto = new SpelerDTO(speler.getGebruikersnaam(), speler.getGeboortejaar(),
					speler.getAantalGewonnen(), speler.getAantalGespeeld(), speler.getKleur(), speler.getGekozenTegel(),
					speler.getKoninkrijk(), speler.getScore());
			gekozenSpelers.add(spelerDto);
		}

		return gekozenSpelers;
	}

	/**
	 * Geeft een SpelerDTO object terug van de speler die aan de beurt is
	 * 
	 * @return SpelerDTO object van de speler aan beurt.
	 */
	public SpelerDTO getSpelerAanBeurt() {
		return getGekozenSpelers().get(0);
	}

	/**
	 * Zorgt ervoor dat het spel een reset.
	 */
	public void resetSpel() {
		this.spel = null;
	}

	/**
	 * Zorgt ervoor dat het spel stopt.
	 */
	public void stopSpel() {
		this.spel.setSpelEinde(true);
	}

	/**
	 * Sorteert de lijst van de gekozen spelers.
	 */
	public void sorteerGekozenSpelers() {
		spel.sorteerSpelers();
	}

	/**
	 * Zorgt ervoor dat de volgende speler aan de beurt is.
	 */
	public void schuifSpelersDoor() {
		spel.schuifSpelersDoor();
	}

	/**
	 * Controleert of een bepaalde speler een tegel kan plaatsen in het spel.
	 *
	 * @param Speler De speler waarvoor moet worden gecontroleerd of hij een tegel
	 *               kan plaatsen.
	 * @return true als de speler de tegel kan plaatsen, anders false.
	 */
	public boolean kanGeplaatstWorden(SpelerDTO speler) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				return spelertje.kanGeplaatstWorden(spel.getStartKolom().get(spelertje.getGekozenTegel() - 1));
			}
		}
		return false;
	}

	/**
	 * Bepaalt de score van de speler.
	 * 
	 * @param Speler De speler vanwie de score bepaalt moet worden.
	 */
	public void bepaalScore(SpelerDTO speler) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				spelertje.setScore(spelertje.bepaalPunten(spelertje.getKoninkrijk().getKoninkrijk()));
			}
		}

	}

	/**
	 * Geeft de score van de speler terug.
	 * @param Speler De speler vanwie die score moet teruggeven worden.
	 * @return String De score
	 */
	public String geefScore(SpelerDTO speler) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				bepaalScore(speler);
				return spelertje.scoreToString(spelertje);
			}
		}
		return null;
	}

	/**
	 * Verhoogt het aantal gewonnen spellen.
	 * @param speler De speler waarvan het aantal gewonnen spellen moet verhoogd worden
	 */
	public void verhoogAantalGewonnen(SpelerDTO speler) {
		for (Speler spelertje : spel.geefGewonnenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				spelertje.verhoogAantalGewonnen();
				spelerRepository.update(spelertje);
			}
		}
	}

	/**
	 * Verhoogt het aantal gespeelde spellen.
	 * @param speler De speler waarvan het aantal gespeelde spellen moet verhoogd worden
	 */
	public void verhoogAantalGespeeld(SpelerDTO speler) {
		for (Speler spelertje : spel.getGekozenSpelers()) {
			if (spelertje.getGebruikersnaam().equals(speler.gebruikersnaam())) {
				spelertje.verhoogAantalGespeeld();
				spelerRepository.update(spelertje);
			}
	}
}
}