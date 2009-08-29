package assembly;

import assembly.Genome.GState;

import repast.simphony.parameter.StringConverter;

public class GStateConverter implements StringConverter<GState> {

	public GState fromString(String strRep) {

		GState gs = GState.early;
		if (strRep.compareTo("Late") == 0) {
			gs = GState.late;
		} else if (strRep.compareTo("Replicate") == 0) {
			gs = GState.replicate;
		} else if (strRep.compareTo("Assembly") == 0) {
			gs = GState.assembly;
		}
		return gs;
	}

	public String toString(GState obj) {
		
		String s = "Early";
		if (obj == GState.late) {
			s = "Late";
		} else if (obj == GState.replicate) {
			s = "Replicate";
		} else if (obj == GState.assembly) {
			s = "Assembly";
		}
		return s;
	}

}
