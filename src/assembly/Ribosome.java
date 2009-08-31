package assembly;

import java.util.Iterator;

import assembly.MRNA.MType;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;

public class Ribosome extends AgentExtendCont {
	
	private double moveTick;
	private double tranTick;
	private MRNA boundmRNA;
	
	public Ribosome() {
		super();
		moveTick=0;
		tranTick = 0;
		genXYZ();
		boundmRNA = null;
		setName("Ribosome");
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
			double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			double disp[] = calcDisplacement(MRNA.class,MRNA.class,radius,rerr,vpradius,vperr);
		
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				//getSpace().moveByDisplacement(this, disp);
				AgentMove.moveByDisplacement(this, disp);
			}
			moveTick = tick;
		}
	}
	
	public void makeProtein() {
		AgentExtendCont T=null;
		if (boundmRNA != null) {
			if (boundmRNA.getMType() == MType.Tag) {
				T = new LgTAg();
			} else if (boundmRNA.getMType() == MType.tag) {
				T = new SmTAg();
			} else if (boundmRNA.getMType() == MType.vp1) {
				T = new VP1();
			} else if (boundmRNA.getMType() == MType.vp2) {
				T = new VP2();
			} else if (boundmRNA.getMType() == MType.vp3) {
				T = new VP3();
			}
			if (T != null) {
				T.setSpace(this.getSpace());
				T.setTheContext(getTheContext());
				T.setLocation(Loc.cytoplasm);
				getTheContext().add(T);
				double[] pt = AgentMove.bounceInLocation(AgentMove.addAtRandomLocationNextTo(this),this.getLocation());
				getSpace().moveTo(T, pt);
				T.largeStepAwayFrom(this);
				this.largeStepAwayFrom(boundmRNA);
				ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
				double start = RepastEssentials.GetTickCount();
				if ((int)start %2 == 0) {
					start = start +1.0f;
				}
				ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
				T.setMove(schedule.schedule(sparams, T, "move"));
				if (T instanceof LgTAg || T instanceof VP2 || T instanceof VP3) {
					T.setExport(schedule.schedule(sparams, T, "export"));
				}
				if (boundmRNA.getNoBound()>0) {
					boundmRNA.setNoBound(boundmRNA.getNoBound()-1);
				} 
				this.setBound(false);
				boundmRNA = null;
			}
		}
	}
	
	public void translation() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > tranTick) {
			double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			ContinuousWithin cw = new ContinuousWithin(getSpace(),this,(dist+err));
			Iterator<AgentExtendCont> list = cw.query().iterator();
			while (list.hasNext()) {
				AgentExtendCont aec = list.next();
				if (aec instanceof MRNA) {
					if (!((MRNA)aec).isDead() && this.isBound()) {
						double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
						if (rand < .4) {
							boundmRNA = (MRNA)aec;
							((CytoNuc)getTheContext()).addToAddList(this);
						}
						break;
					}
				}
			}
			tranTick = tick;
		}
	}
}
