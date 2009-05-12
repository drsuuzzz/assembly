package assembly;

import assembly.Genome.GState;

import repast.simphony.parameter.StringConverter;

public class GStateConverter implements StringConverter<GState> {

	public GState fromString(String strRep) {

		GState gs = GState.early;
		if (strRep == "Late") {
			gs = GState.late;
		} else if (strRep == "Replicate") {
			gs = GState.replicate;
		}
		return gs;
	}

	public String toString(GState obj) {
		
		String s = "Early";
		if (obj == GState.late) {
			s = "Late";
		} else if (obj == GState.replicate) {
			s = "Replicate";
		}
		return s;
	}

}
