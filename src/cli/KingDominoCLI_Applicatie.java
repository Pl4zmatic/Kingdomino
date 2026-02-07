package cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.Collection;

import domein.DomeinController;
import domein.Speler;
import exceptions.GebruikersnaamInGebruikException;
import utils.Kleuren;
import dto.DominoTegelDTO;
import dto.SpelerDTO;

public class KingDominoCLI_Applicatie {
	private final static String KEUZEMENU_ARG_EX_STRING = "Onverwachte keuze: %d, Opnieuw!";
	private final static String KEUZEMENU_MISMATCH_EX_STRING = "%nJe moet een getal invoeren (%d-%d).%n";

	private final static int MIN_GEKOZEN_SPELERS = 3;
	private final static int MAX_GEKOZEN_SPELERS = 4;

	DomeinController dc;
	private Scanner input = new Scanner(System.in);

	public KingDominoCLI_Applicatie(DomeinController dc) {
		this.dc = dc;
	}

	public void app() {
		boolean appActief = true;

		do {
			printMenu();

			try {
				int keuze = input.nextInt();

				switch (keuze) {
				case 1: {
					registreerSpeler();
				}
					break;
				case 2: {
					startSpel();
				}
					break;
				case 3: {
					appActief = false;
				}
					break;
				default:
					throw new IllegalArgumentException(String.format(KEUZEMENU_ARG_EX_STRING, keuze));
				}
			} catch (InputMismatchException e) {
				System.out.println(String.format(KEUZEMENU_MISMATCH_EX_STRING, 1, 3));
				input.nextLine();
			} catch (IllegalArgumentException | GebruikersnaamInGebruikException e) {
				System.out.println(String.format("%n%s%n", e.getMessage()));
			}

		} while (appActief);
	}

	private void printMenu() {
		final String menu = "1. Registreer nieuwe speler\n" + "2. Start nieuw spel\n" + "3. Afsluiten\n" + "Keuze: ";

		System.out.println(menu);
	}

	private void registreerSpeler() {
		try {
			System.out.println("Geef uw gebruikersnaam: ");
			String gebruikersnaam = input.next();

			System.out.println("Geef uw geboortejaar: ");
			int geboortejaar = input.nextInt();

			dc.registreerSpeler(gebruikersnaam, geboortejaar);
		} catch (InputMismatchException e) {
			System.out.println(String.format("Je moet een getal invoeren voor geboortejaar [%d, %d].",
					Speler.MINIMUM_GEBOORTEJAAR, Speler.MAXIMUM_GEBOORTEJAAR));
		}
	}

	private void startSpel() {
		boolean startSpel = true;
		kiesSpelers();

		List<Integer> tegels = new ArrayList<Integer>(
				IntStream.rangeClosed(1, dc.getGekozenSpelers().size()).boxed().collect(Collectors.toList()));

		kiesTegels(tegels);
		dc.sorteerGekozenSpelers();
		SpelerDTO spelerAanBeurt = dc.getSpelerAanBeurt();

		tegels = new ArrayList<Integer>(
				IntStream.rangeClosed(1, dc.getGekozenSpelers().size()).boxed().collect(Collectors.toList()));

		do {
			if(!startSpel)
			{
				plaatsTegel(spelerAanBeurt, dc.getStartKolom().get(spelerAanBeurt.gekozenTegel()-1));
				kiesTegel(spelerAanBeurt, tegels, dc.getEindKolom());
				dc.speelRonde(tegels.size() == 0 && !startSpel);
			}
			
			if(startSpel)
			{
				plaatsTegel(spelerAanBeurt, dc.getStartKolom().get(spelerAanBeurt.gekozenTegel()-1));
				dc.speelRonde(tegels.size() == 0 && !startSpel);
				kiesTegel(spelerAanBeurt, tegels, dc.getEindKolom());
				startSpel = false;
			}
			
			spelerAanBeurt = dc.getSpelerAanBeurt();

			if (tegels.size() == 0)
				tegels = new ArrayList<Integer>(
						IntStream.rangeClosed(1, dc.getGekozenSpelers().size()).boxed().collect(Collectors.toList()));
		} while (!(dc.isSpelEinde()));
		for(SpelerDTO spelerdto : dc.getGekozenSpelers()) {
			dc.verhoogAantalGespeeld(spelerdto);
		}
		for(SpelerDTO spelerdto : dc.geefGewonnenSpelers()) {
			dc.verhoogAantalGewonnen(spelerdto);
		}
	}

	private void kiesSpelers() {
		boolean minAantalSpelersBereikt = false;
		boolean maxAantalSpelersBereikt = false;
		boolean stopConditie = false;

		Kleuren gekozenKleur;

		List<SpelerDTO> gekozenSpelers = new ArrayList<SpelerDTO>();
		List<Kleuren> gekozenKleuren = new ArrayList<Kleuren>();

		System.out.println(String.format("\nVoeg spelers toe aan het spel. Minimum %d, maximum %d.\n",
				MIN_GEKOZEN_SPELERS, MAX_GEKOZEN_SPELERS));

		do {
			try {
				SpelerDTO gekozenSpeler = kiesOngekozenSpeler(gekozenSpelers);
				if (gekozenSpeler != null) {
					gekozenKleur = kiesOngekozenKleur(gekozenKleuren);
				} else {
					gekozenKleur = null;
				}

				stopConditie = gekozenSpeler == null || gekozenKleur == null;

				if (stopConditie && !minAantalSpelersBereikt)
					throw new IllegalArgumentException(
							String.format("Minimum aantal %d nog niet bereikt.", MIN_GEKOZEN_SPELERS));

				if (!stopConditie) {

					gekozenSpelers.add(gekozenSpeler);
					gekozenKleuren.add(gekozenKleur);
				}

				minAantalSpelersBereikt = gekozenSpelers.size() >= MIN_GEKOZEN_SPELERS;
				maxAantalSpelersBereikt = gekozenSpelers.size() >= MAX_GEKOZEN_SPELERS;
			} catch (IllegalArgumentException e) {
				System.out.println(String.format("%n%s%n", e.getMessage()));
			}

		} while (!(minAantalSpelersBereikt && stopConditie) && !maxAantalSpelersBereikt);

		dc.voegSpelersToeEnMaakSpel(gekozenSpelers, gekozenKleuren);
		dc.zetSpelOp(gekozenSpelers.size());
	}

	private void kiesTegels(List<Integer> tegels) {
		List<SpelerDTO> koningen = new ArrayList<SpelerDTO>();
		koningen.addAll(dc.getGekozenSpelers());

		for (int i = 0; i < dc.getGekozenSpelers().size(); i++) {
			int random = (int) (Math.random() * koningen.size());
			kiesTegel(koningen.get(random), tegels, dc.getStartKolom());
			koningen.remove(random);
		}
	}

	private void kiesTegel(SpelerDTO speler, List<Integer> tegels, List<DominoTegelDTO> kolom) {
		int keuze = 0;
		boolean keuzeValide = true;

		kolom.forEach(tegel -> System.out.println(kolom.indexOf(tegel)+1 + ": " + tegel.toString()));
		
		do {
			keuzeValide = true;
			try {
				System.out.printf("%s: kies een tegel %s: ", speler.gebruikersnaam(), tegels.toString());
				keuze = input.nextInt();
				if (!tegels.contains((Integer) keuze)) {
					throw new IllegalArgumentException(
							String.format("Het cijfer %d kan niet gekozen worden! Kies een cijfer tussen %s.", keuze,
									tegels.toString()));
				}

			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
				keuzeValide = false;
			}
		} while ((keuze < 1 || keuze > dc.getGekozenSpelers().size()) || !keuzeValide);
		dc.zetKoning(keuze, speler.gebruikersnaam());
		tegels.remove((Integer) keuze);
	}

	private void plaatsTegel(SpelerDTO speler, DominoTegelDTO tegel) {
		String[] richtingen = { "onder", "boven", "links", "rechts" };
		boolean isGeplaatst = false;
		boolean rijIngegeven = false;
		boolean kolomIngegeven = false;
		boolean richtingIngegeven = false;
		int rij = 0, kolom = 0;
		String richting = "";
		
		if (dc.kanGeplaatstWorden(speler)) {
			do {
				try {
					isGeplaatst = false;
					System.out.printf("%s", toonKoninkrijk(speler));
					if (!rijIngegeven) {
						System.out.printf("%s : in welke rij wil je de tegel (%s) plaatsen? ",
								speler.gebruikersnaam(), tegel.toString());
						rij = input.nextInt();
						if (rij < 1 || rij > 9) {
							throw new IllegalArgumentException(
									String.format("%nGelieve een getal geven tussen 1 en 9.%n"));
						}
						rijIngegeven = true;
					} 
					if (!kolomIngegeven) {
						System.out.printf("%s : in welke kolom wil je de tegel (%s) plaatsen? ",
								speler.gebruikersnaam(), tegel.toString());
						kolom = input.nextInt();
						if (kolom < 1 || kolom > 9) {
							throw new IllegalArgumentException(
									String.format("%nGelieve een getal te geven tussen 1 en 9.%n"));
						}
						kolomIngegeven = true;
					} 
					if (!richtingIngegeven) {
						System.out.printf(
								"%s : in welke richting wil je de tegel plaatsen (links, rechts, boven, onder)?",
								speler.gebruikersnaam());
						richting = input.next();
						if (!Arrays.asList(richtingen).contains(richting)) {
							throw new IllegalArgumentException(
									"Geef een van de 4 richtingen (links, rechts, boven, onder)");
						}
						richtingIngegeven = true;
					}
					rijIngegeven = false;
					kolomIngegeven = false;
					richtingIngegeven = false;
					dc.kiesPlaatsEnRichting(speler, rij, kolom, richting);
					System.out.printf("%s", toonKoninkrijk(speler));
					System.out.printf("%n%s%n", dc.geefScore(speler));
					isGeplaatst = true;
				} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | InputMismatchException e) {
					System.out.printf("%n%s%n%n", e.getMessage());
				}
			} while (isGeplaatst == false);
		} else {
			System.out.printf("Deze tegel kan niet gelegd worden op een plek en wordt dus verwijderd");
		}
	}

	private SpelerDTO kiesOngekozenSpeler(List<SpelerDTO> gekozenSpelers) {
		List<SpelerDTO> spelers = dc.geefOngekozenSpelers(gekozenSpelers);
		int keuze = 0;

		System.out.println("\nKies een speler.\n");

		boolean correcteKeuze = false;
		for (int i = 0; i < spelers.size(); i++) {
			System.out.printf("%d. Gebruiker: %s | Leeftijd: %d%n", i + 1, spelers.get(i).gebruikersnaam(),
					spelers.get(i).getLeeftijd());
		}

		System.out.println("-1. Stop");

		System.out.print("Keuze: ");
		do {
			try {
				keuze = input.nextInt();
				System.out.println("test");

				if (keuze == -1)
					return null;

				if (keuze < 1 || keuze > spelers.size())
					throw new IllegalArgumentException(String.format(KEUZEMENU_ARG_EX_STRING, keuze));
				else if (keuze >= 1 && keuze <= spelers.size())
					correcteKeuze = true;
			} catch (InputMismatchException e) {
				throw new InputMismatchException(String.format(KEUZEMENU_MISMATCH_EX_STRING, 1, spelers.size()));
			}

		} while (!correcteKeuze);

		return spelers.get(keuze - 1);
	}

	private Kleuren kiesOngekozenKleur(List<Kleuren> gekozenKleuren) {
		List<Kleuren> kleuren = dc.geefOngekozenKleuren(gekozenKleuren);
		int keuze = 0;

		System.out.println("\nKies een kleur voor de speler.\n");

		boolean correcteKeuze = false;
		do {
			for (int i = 0; i < kleuren.size(); i++) {
				System.out.printf("%d. %s%n", i + 1, kleuren.get(i).toString());
			}

			System.out.println("-1. Stop");

			System.out.print("Keuze: ");
			try {
				keuze = input.nextInt();

				if (keuze == -1)
					return null;

				if (keuze < 1 || keuze > kleuren.size())
					throw new IllegalArgumentException(String.format(KEUZEMENU_ARG_EX_STRING, keuze));
				else
					correcteKeuze = true;

			} catch (InputMismatchException e) {
				throw new InputMismatchException(String.format(KEUZEMENU_MISMATCH_EX_STRING, 1, kleuren.size()));
			}
		} while (!correcteKeuze);

		return kleuren.get(keuze - 1);
	}

	private String toonKoninkrijk(SpelerDTO speler) {
		String resultaat = String.format("Konikrijk van Speler: %s%n%2s", speler.gebruikersnaam(), "");

		for (int i = 0; i < 9; i++)
			resultaat += String.format("%6s%6s", String.valueOf(i+1), "");
		
		resultaat += "\n";
		
		for (int i = 0; i < 9; i++) {
			resultaat += String.format("%2s", String.valueOf(i+1));
			for (int j = 0; j < 9; j++) {
				resultaat += String.format("[%10s]", speler.koninkrijk().getKoninkrijk()[i][j] == null ? ""
						: speler.koninkrijk().getKoninkrijk()[i][j].getLandschap());
			}
			resultaat += '\n';
		}

		return resultaat;
	}
}