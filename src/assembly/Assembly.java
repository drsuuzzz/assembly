package assembly;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;

public class Assembly implements ContextBuilder {

	public Context build(Context context) {
		
		//Nucleus model = new Nucleus();
		//context.addSubContext(model);
		//Cytoplasm cyto = new Cytoplasm();
		//context.addSubContext(cyto);
		
		Cell cell = new Cell();
		context.addSubContext(cell);
		return context;
	}

}
