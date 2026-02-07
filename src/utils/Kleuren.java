package utils;

public enum Kleuren {
	GROEN, BLAUW, ROOS, GEEL;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
