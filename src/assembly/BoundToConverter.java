package assembly;

import assembly.AgentExtendCont.BoundTo;
import repast.simphony.parameter.StringConverter;

public class BoundToConverter implements StringConverter<BoundTo> {

	public BoundTo fromString(String strRep) {
		
		BoundTo ret = BoundTo.none;
		if (strRep.compareTo("VLP") == 0) {
			ret = BoundTo.vlp;
		} else if (strRep.compareTo("Genome") == 0) {
			ret = BoundTo.genome;
		}
		return ret;
	}

	public String toString(BoundTo obj) {
		
		String s;
		if (obj == BoundTo.vlp) {
			s = "VLP";
		} else if (obj == BoundTo.genome) {
			s = "Genome";
		} else {
			s = "None";
		}
		return s;
	}

}
