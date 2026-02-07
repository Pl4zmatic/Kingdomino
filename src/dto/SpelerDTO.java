package dto;

import java.time.Year;
import java.util.List;

import domein.Koninkrijk;
import utils.Kleuren;

public record SpelerDTO(String gebruikersnaam, int geboortejaar, int aantalGewonnen, int aantalGespeeld, Kleuren kleur, int gekozenTegel, Koninkrijk koninkrijk, List<Integer> score) {
	
	private final static int JAAR_VANDAAG = Year.now().getValue();

	public int getLeeftijd() {
		return JAAR_VANDAAG - geboortejaar;
	}
	
	public String getGebruikersnaam()
	{
		return gebruikersnaam;
	}
	
	public int getGeboortejaar()
	{
		return geboortejaar;
	}

	public String toonSpelersituatie() {
		return String.format("-%s%n-gekozen dominotegel: %d%n", this.gebruikersnaam, this.gekozenTegel);
	}
}
