package domein;

import utils.Landschappen;

/**
 * Representeert een vakje op een domino tegel met een landschap en een aantal
 * kronen.
 */
public class Vakje {
	private int kronen;
	private String landschap;

	/**
	 * Initialiseert een vakje met het opgegeven landschap en aantal kronen.
	 *
	 * @param landschap Het landschap van het vakje.
	 * @param kronen    Het aantal kronen op het vakje.
	 */
	public Vakje(Landschappen landschap, int kronen) {
		setLandschap(landschap);
		setKronen(kronen);
	}

	/**
	 * Geeft het aantal kronen op het vakje terug.
	 *
	 * @return Het aantal kronen op het vakje.
	 */
	public int getKronen() {
		return kronen;
	}

	/**
	 * Stelt het aantal kronen op het vakje in.
	 *
	 * @param kroon Het aantal kronen dat moet worden ingesteld.
	 */
	public void setKronen(int kroon) {
		this.kronen = kroon;
	}

	/**
	 * Geeft het landschap van het vakje terug.
	 *
	 * @return Het landschap van het vakje.
	 */
	public String getLandschap() {
		return landschap;
	}

	/**
	 * Stelt het landschap van het vakje in.
	 *
	 * @param landschap Het landschap dat moet worden ingesteld.
	 */
	public void setLandschap(Landschappen landschap) {
		this.landschap = landschap.toString();
	}
	
	/**
	 * Geeft het landschap terug als string.
	 */
	@Override
	public String toString()
	{
		return landschap;
	}
}



