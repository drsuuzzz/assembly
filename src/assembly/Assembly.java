package assembly;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class Assembly implements ContextBuilder {

	public Context build(Context context) {
		
		//Cell cell = new Cell();
		//context.addSubContext(cell);
		
		CytoNuc cn = new CytoNuc();
		context.addSubContext(cn);
		
		return context;
	}

}
