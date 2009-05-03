package assembly;

import repast.simphony.context.DefaultContext;

public class Cell extends DefaultContext<AgentExtendCont> {

	public Cell() {
		super("Cell");
		Nucleus n = new Nucleus();
		this.addSubContext(n);
		Cytoplasm c = new Cytoplasm();
		this.addSubContext(c);
	}
}
