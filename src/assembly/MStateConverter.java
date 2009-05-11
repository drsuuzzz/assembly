package assembly;

import assembly.MRNA.mState;

import repast.simphony.parameter.StringConverter;

public class MStateConverter implements StringConverter<mState> {

	public mState fromString(String strRep) {
		
		mState ms = mState.early;
		if (strRep == "Late") {
			ms = mState.late;
		} else if (strRep == "Complete") {
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
