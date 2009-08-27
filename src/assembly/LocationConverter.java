package assembly;

import repast.simphony.parameter.StringConverter;
import assembly.AgentExtendCont.Loc;

public class LocationConverter implements StringConverter<Loc>{
	public Loc fromString(String strRep) {
		
		Loc ret = Loc.cytoplasm;
		if (strRep == "Nucleus") {
			ret = Loc.nucleus;
		}
		return ret;
	}

	public String toString(Loc obj) {
		
		String s = null;
		if (obj == Loc.cytoplasm) {
			s = "Cytoplasm";
		} else if (obj == Loc.nucleus) {
			s = "Nucleus";
		}
		return s;
	}
}
