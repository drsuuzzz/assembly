package assembly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import assembly.MRNA.Loc;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;

public class Cell extends DefaultContext<AgentExtendCont> {

	private Nucleus n;
	private Cytoplasm c;
	
	private List<AgentExtendCont> mAgents;
	private List<AgentExtendCont> addList;
	private List<AgentExtendCont> remList;
	
	public Cell() {
		super("Cell");
		n = new Nucleus();
		n.setCell(this);
		this.addSubContext(n);
		c = new Cytoplasm();
		c.setCell(this);
		this.addSubContext(c);
		
		mAgents = new ArrayList<AgentExtendCont>();
		addList = new ArrayList<AgentExtendCont>();
		remList = new ArrayList<AgentExtendCont>();
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount() <= 0 ? 1 : RepastEssentials.GetTickCount();
		if ((int)start%2 != 0) {
			start = start + 1.0f;
		}
		//even schedule
		ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
		schedule.schedule(sparams,this,"moveAgents");
	}
	//should only happen on an odd tick
	public void addToMoveList(AgentExtendCont aec) {
		mAgents.add(aec);
	}
	
	public void addToAddList(AgentExtendCont aec) {
		addList.add(aec);
	}
	
	public void addToRemList(AgentExtendCont aec) {
		remList.add(aec);
	}
	
	//schedules methods
	//even tick
	public void moveAgents() {
		Iterator<AgentExtendCont> agents = mAgents.iterator();
		while (agents.hasNext()) {
			AgentExtendCont aec = agents.next();
			if (aec instanceof MRNA) {
				n.remove(aec);
				c.add(aec);
				aec.setTheContext(c);
				aec.setSpace(c.getSpace());
				((MRNA) aec).setLocation(Loc.cytoplasm);
				ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
				double start = RepastEssentials.GetTickCount();
				if ((int)start%2 == 0) {
					start = start + 1.0f;
				}
				//schedule death rule in cytoplasm
				ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
				schedule.schedule(sparams,aec,"death");
				//remove the export rule;
				aec.removeAnAction(aec.getExport());
				aec.setExport(null);
				agents.remove();
			}
		}
	}
	

}
