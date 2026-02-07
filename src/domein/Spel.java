package domein;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import dto.SpelerDTO;
import utils.Kleuren;

/**
 * De Spel klasse representeert het spel en bevat functionaliteiten om het spel
 * te beheren en te spelen.
 */
public class Spel {

	private LinkedList<Speler> gekozenSpelers;
	private List<DominoTegel> startKolom;
	private List<DominoTegel> eindKolom;
	private Queue<DominoTegel> shuffledDominoTegels;
	private boolean isSpelEinde;

	public Spel() {
		gekozenSpelers = new LinkedList<Speler>();
		startKolom = new ArrayList<DominoTegel>();
		eindKolom = new ArrayList<DominoTegel>();
		isSpelEinde = false;
	}

	/**
	 * Voegt een speler toe aan de gekozenSpelers lijst.
	 * 
	 * @param speler De speler die je wilt toevoegen.
	 */
	public void voegSpelerToe(Speler speler) {
		gekozenSpelers.add(speler);
	}

	/**
	 * Zet spel op en maakt dominotegels aan. Zet dominotegels in willekeurige
	 * volgorde. Maakt een startkolom aan.
	 * 
	 * @param aantalSpelers Dit is het aantal spelers waarmee men gaat spelen
	 */
	public void zetSpelOp(int aantalSpelers) {
		if (aantalSpelers >= 3 && aantalSpelers <= 4) {
			List<DominoTegel> dominotegels = new ArrayList<DominoTegel>();
			for (int i = 1; i <= (aantalSpelers < 4 ? 36 : 48); i++) {
				dominotegels.add(new DominoTegel(i));
			}

			Collections.shuffle(dominotegels);
			this.shuffledDominoTegels = new ArrayDeque<>(dominotegels);

			for (int i = 0; i < aantalSpelers; i++) {
				startKolom.add(shuffledDominoTegels.poll());
			}
			Collections.sort(startKolom);
		} else {
			throw new IllegalArgumentException("kies 3-4 spelers");
		}
	}

	/**
	 * Zet de koning op het juiste vakje.
	 * 
	 * @param keuze          Dit is het vakje waar de koning op moet staan.
	 * @param gebruikersnaam Dit is de gebruikersnaam van de speler van wie de konin
	 *                       is.
	 */
	public void zetKoning(int keuze, String gebruikersnaam) {
		gekozenSpelers.stream().filter(s -> s.getGebruikersnaam().equals(gebruikersnaam)).findFirst().get()
				.setGekozenTegel(keuze);
	}

	/**
	 * Getter voor het aantal tegels in de startkolom.
	 * 
	 * @return int Het aantal tegels in de startkolom.
	 */
	public int getAantalTegelsStartKolom() {
		return this.startKolom.size();
	}

	/**
	 * Getter voor het aantal tegels in de eindkolom.
	 * 
	 * @return int Het aantal tegels in de eindkolom.
	 */
	public int getAantalTegelsEindKolom() {
		return this.eindKolom.size();
	}

	/**
	 * Geeft de lijst met dominotegels van de startkolom terug.
	 * 
	 * @return List<DominoTegel> De lijst met tegels in de startkolom.
	 */
	public List<DominoTegel> getStartKolom() {
		return this.startKolom;
	}

	/**
	 * Getter voor de lijst met tegels in de eindkolom.
	 * 
	 * @return List<DominoTegel> De lijst met tegels in de eindkolom.
	 */
	public List<DominoTegel> getEindKolom() {
		return this.eindKolom;
	}

	/**
	 * Getter voor het aantal gekozen spelers.
	 * 
	 * @return int Het aantal gekozen spelers.
	 */
	public int getGekozenAantalSpelers() {
		return gekozenSpelers.size();
	}

	/**
	 * Getter voor de lijst met gekozen spelers.
	 * 
	 * @return List<Speler> De lijst met gekozen spelers.
	 */
	public List<Speler> getGekozenSpelers() {
		return this.gekozenSpelers;
	}

	/**
	 * Getter voor het totale aantal tegels.
	 * 
	 * @return int Het totale aantal tegels.
	 */
	public int getAantalTegels() {
		return shuffledDominoTegels.size();
	}

	/**
	 * Getter voor de wachtrij met beschikbare tegels.
	 * 
	 * @return Queue<DominoTegel> De wachtrij met beschikbare tegels.
	 */
	public Queue<DominoTegel> geefBeschikbareTegels() {
		return shuffledDominoTegels;
	}

	/**
	 * Geeft een lijst terug met de gewonnen spelers.
	 * 
	 * @return List<Speler> Een lijst met gewonnen spelers.
	 */
	public List<Speler> geefGewonnenSpelers() {
		Collections.sort(this.gekozenSpelers, new Comparator<Speler>() {
			@Override
			public int compare(Speler o1, Speler o2) {
				int cmp = 0;
				for (int i = 0; i < o1.getScore().size() && cmp == 0; i++)
					cmp = o2.getScore().get(i).compareTo(o1.getScore().get(i));
				return cmp;
			}
		});
		return this.gekozenSpelers;
	}

	/**
	 * Functie voor einde spel te controleren.
	 * 
	 * @return boolean Dit geeft aan of het spel al ten einde is.
	 */
	public boolean getSpelEinde() {
		return isSpelEinde;
	}

	/**
	 * Functie voor einde spel te zetten.
	 * 
	 * @param boolean
	 */
	public void setSpelEinde(boolean isSpelEinde) {
		this.isSpelEinde = isSpelEinde;
	}

	/**
	 * De volgorde van de spelers bepalen. De gekozen spelers teruggeven.
	 * 
	 * @return LinkedList<Speler> Geeft een lijst terug met gesorteerde spelers.
	 */
	public LinkedList<Speler> sorteerSpelers() {
		Collections.sort(this.gekozenSpelers);
		return this.gekozenSpelers;
	}

	/**
	 * Schuift de spelers in de wachtrij door.
	 */
	public void schuifSpelersDoor() {
		this.gekozenSpelers.offerLast(this.gekozenSpelers.poll());
	}

	/**
	 * Zet alle dominotegels van de eindkolom naar de startkolom.
	 */
	public void verplaatstEindKolomNaarStartKolom() {
		startKolom.clear();
		startKolom.addAll(eindKolom);
		eindKolom.clear();
	}

	/**
	 * Vult de eindkolom met dominotegels aan de hand van het aantal spelers.
	 * 
	 * @param aantalSpelers De hoeveelheid spelers.
	 */
	public void vulEindkolom() {
		for (int i = 0; i < gekozenSpelers.size(); i++) {
			this.eindKolom.add(shuffledDominoTegels.poll());
		}

		Collections.sort(this.eindKolom);
	}
}
