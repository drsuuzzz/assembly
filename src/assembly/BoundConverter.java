package assembly;

import assembly.VP123.Bound;
import repast.simphony.parameter.StringConverter;

public class BoundConverter implements StringConverter<Bound> {

	public Bound fromString(String strRep) {
		
		Bound b;
		if (strRep == "BOUND") 
			b = Bound.BOUND;
		else
			b = Bound.UNBOUND;
		return b;
	}

	public String toString(Bound obj) {
		
		String s;
		if (obj == Bound.BOUND) 
			s = "BOUND";
		else
			s = "UNBOUND";
		return s;
	}

}
