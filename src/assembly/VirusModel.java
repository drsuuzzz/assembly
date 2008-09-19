package assembly;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class VirusModel implements ContextBuilder {

	public Context build(Context context) {
		
		Assembly model = new Assembly();
		context.add(model);
		return context;
	}

}
