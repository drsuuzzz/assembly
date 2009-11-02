package assembly;

import repast.simphony.engine.environment.RunEnvironment;

public class AgentProbabilities {
	
	public static double makeVLP;
	//public static double replicate;
	public static double DNAPolDeath;
	public static double transcribeEarly;
	public static double transcribeGenome;
	public static double transcribeLate;
	public static double transcribeDNAPol;
	public static double BKVEgress;
	public static double VLPEgress;
	public static double importTag;
	public static double importVP2;
	public static double importVP3;
	public static double TagDeath;
	public static double spliceTag;
	public static double splicetag;
	public static double spliceVP1;
	public static double spliceVP23;
	public static double exportmRNA;
	public static double mRNADeath;
	public static double translation;
	public static double VLPDeath;
	
	public  static void SetAgentProbabilities() {
		makeVLP = (Double) RunEnvironment.getInstance().getParameters().getValue("makeVLP");
		//replicate = (Double) RunEnvironment.getInstance().getParameters().getValue("replicate");
		DNAPolDeath = (Double) RunEnvironment.getInstance().getParameters().getValue("DNAPolDeath");
		transcribeEarly = (Double) RunEnvironment.getInstance().getParameters().getValue("transcribeEarly");
		transcribeGenome = (Double) RunEnvironment.getInstance().getParameters().getValue("transcribeGenome");
		transcribeLate = (Double) RunEnvironment.getInstance().getParameters().getValue("transcribeLate");
		transcribeDNAPol = (Double) RunEnvironment.getInstance().getParameters().getValue("transcribeDNAPol");
		BKVEgress = (Double) RunEnvironment.getInstance().getParameters().getValue("BKVEgress");
		VLPEgress = (Double) RunEnvironment.getInstance().getParameters().getValue("VLPEgress");
		importTag = (Double) RunEnvironment.getInstance().getParameters().getValue("importTag");
		importVP2 = (Double) RunEnvironment.getInstance().getParameters().getValue("importVP2");
		importVP3 = (Double) RunEnvironment.getInstance().getParameters().getValue("importVP3");
		TagDeath = (Double) RunEnvironment.getInstance().getParameters().getValue("tagDeath");
		spliceTag = (Double) RunEnvironment.getInstance().getParameters().getValue("spliceTag");
		splicetag = (Double) RunEnvironment.getInstance().getParameters().getValue("splicetag");
		spliceVP1 = (Double) RunEnvironment.getInstance().getParameters().getValue("spliceVP1");
		spliceVP23 = (Double) RunEnvironment.getInstance().getParameters().getValue("spliceVP23");
		exportmRNA = (Double) RunEnvironment.getInstance().getParameters().getValue("exportmRNA");
		mRNADeath = (Double) RunEnvironment.getInstance().getParameters().getValue("mRNADeath");
		translation = (Double) RunEnvironment.getInstance().getParameters().getValue("translation");
		VLPDeath = (Double) RunEnvironment.getInstance().getParameters().getValue("VLPDeath");

	}

}
