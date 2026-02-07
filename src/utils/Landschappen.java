package utils;

public enum Landschappen {
	STARTVAKJE, WATER, GRAS, BOS, ZAND, AARDE, MIJN;
	
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
