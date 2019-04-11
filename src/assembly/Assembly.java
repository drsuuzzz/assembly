package assembly;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;

public class Assembly implements ContextBuilder {

	public Context build(Context context) {
		
		//Cell cell = new Cell();
		//context.addSubContext(cell);
       	if (RunEnvironment.getInstance().isBatch()) {
    		//RunEnvironment.getInstance().endAt(210000);
    		RunEnvironment.getInstance().endAt(300000);
    	}
		CytoNuc cn = new CytoNuc();
		context.addSubContext(cn);
		
		return context;
	}

}
