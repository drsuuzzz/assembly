package assembly;

import assembly.MRNA.MType;

import repast.simphony.parameter.StringConverter;

public class MTypeConverter implements StringConverter<MType> {

	public MType fromString(String strRep) {
		
		MType mt = MType.tag;
		if (strRep.compareTo("Tag") == 0) {
			mt = MType.Tag;
		} else if (strRep.compareTo("tag") == 0) {
			mt = MType.tag;
		} else if (strRep.compareTo("VP1") == 0) {
			mt = MType.vp1;
		} else if (strRep.compareTo("VP2") == 0) {
			mt = MType.vp2;
		} else if (strRep.compareTo("VP3") == 0) {
			mt = MType.vp3;
		}
		return mt;
	}

	public String toString(MType obj) {
		
		String s = " ";
		if (obj == MType.Tag) {
			s = "Tag";
		} else if (obj == MType.tag) {
			s = "tag";
		} else if (obj ==MType.vp1) {
			s = "VP1";
		} else if (obj == MType.vp2) {
			s = "VP2";
		} else if (obj == MType.vp3) {
			s = "VP3";
		}
		return s;
	}

}
