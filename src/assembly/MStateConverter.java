package assembly;

import assembly.MRNA.mState;

import repast.simphony.parameter.StringConverter;

public class MStateConverter implements StringConverter<mState> {

	public mState fromString(String strRep) {
		
		mState ms = mState.early;
		if (strRep.compareTo("Late") == 0) {
			ms = mState.late;
		} else if (strRep.compareTo("Complete") == 0) {
			ms = mState.complete;
		}
		return ms;
	}

	public String toString(mState obj) {
		
		String s = "Early";
		if (obj == mState.complete) {
			s = "Complete";
		} else if (obj == mState.late) {
			s = "Late";
		}
		return s;
	}

}
