package dto;

import java.util.List;
import domein.Vakje;

public record DominoTegelDTO(List<Vakje> vakjes, int index, String richting) {
	@Override
	public String toString()
	{
		String result = "[ ";
		for(Vakje v : vakjes) {
			if(v.getKronen()>0) {
				result += String.format("%d", v.getKronen());
			}
			result += v.toString() + " ";
		}
			
		return result + "]";
	}
}
