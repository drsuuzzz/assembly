package assembly;

import assembly.VP123.State;
import repast.simphony.parameter.StringConverter;

public class StateConverter implements StringConverter<State> {

	public State fromString(String strRep) {
		
		State retval;
		if (strRep == "HEX" ) {
			retval = State.HEX;
		} else {
			retval = State.PENT;
		}
		return retval;
	}

	public String toString(State obj) {
		
		String s;
		if (obj == State.HEX)
			s = "HEX";
		else
			s = "PENT";
		
		return s;
	}

}
