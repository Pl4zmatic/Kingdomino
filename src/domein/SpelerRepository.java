package domein;

import java.util.List;

import exceptions.GebruikersnaamInGebruikException;
import persistentie.SpelerMapper;

/**
 * De SpelerRepository klasse beheert de opslag en manipulatie van Speler
 * objecten.
 */
public class SpelerRepository {

	private final SpelerMapper mapper;

	/**
	 * Initialiseert een nieuwe SpelerRepository met een SpelerMapper.
	 */
	public SpelerRepository() {
		mapper = new SpelerMapper();
	}

	/**
	 * Voegt een nieuwe speler toe aan de repository.
	 *
	 * @param speler De speler die moet worden toegevoegd.
	 * @throws GebruikersnaamInGebruikException Als de gebruikersnaam van de speler
	 *                                          al bestaat in de repository.
	 */
	public void voegToe(Speler speler) {
		if (bestaatSpeler(speler.getGebruikersnaam()))
			throw new GebruikersnaamInGebruikException();

		mapper.voegToe(speler);
	}

	/**
	 * Controleert of een speler met de opgegeven gebruikersnaam al bestaat in de
	 * repository.
	 *
	 * @param gebruikersnaam De gebruikersnaam van de speler.
	 * @return true als de speler bestaat, anders false.
	 */
	private boolean bestaatSpeler(String gebruikersnaam) {
		return mapper.geefSpeler(gebruikersnaam) != null;
	}

	/**
	 * Geeft de speler met de opgegeven gebruikersnaam terug.
	 *
	 * @param gebruikersnaam De gebruikersnaam van de speler.
	 * @return De speler met de opgegeven gebruikersnaam, of null als de speler niet
	 *         gevonden wordt.
	 */
	public Speler geefSpeler(String gebruikersnaam) {
		return mapper.geefSpeler(gebruikersnaam);
	}

	/**
	 * Geeft een lijst terug van alle spelers in de repository.
	 *
	 * @return Een lijst van alle spelers in de repository.
	 */
	public List<Speler> geefAlleSpelers() {
		return mapper.geefAlleSpelers();
	}
	
	public void update(Speler speler) {
		mapper.update(speler);
	}
}
