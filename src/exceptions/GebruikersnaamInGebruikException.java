package exceptions;

public class GebruikersnaamInGebruikException extends RuntimeException {

	public GebruikersnaamInGebruikException() {
		super("Gebruikersnaam reeds in gebruik.");
	}

	public GebruikersnaamInGebruikException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
