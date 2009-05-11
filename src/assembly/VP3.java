package assembly;

import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.ContextUtils;

public class VP3 extends AgentExtendCont{
	
	private double moveTick;
	private double exTick;
	
	public VP3() {
		super();
		moveTick=0;
		exTick = 0;
		genXYZ();
		setName("VP3");
	}
	
	public void move() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > moveTick) {

			double disp[] = this.calcDispIfCenter(VP1.class, VP1.class, VP2.class, VP3.class);
			if (disp[0]==0 && disp[1]==0 && disp[2]==0) {
				randomWalk();
			} else {
				getSpace().moveByDisplacement(this, disp);
			}
			
			moveTick = tick;
		}
	}
	
	public void export() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > exTick) {

			if (getNoBound() == 5) {
				if (nearWall(getSpace().getLocation(this))) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < 0.4) {
						double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
						double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
						ContinuousWithin list = new ContinuousWithin(getSpace(),this,(dist+err));
						Iterator<AgentExtendCont> l = list.query().iterator();
						while (l.hasNext()) {
							AgentExtendCont aec = l.next();
							if (aec instanceof VP1) {
								aec.setMoving(true);
								((Cytoplasm)getTheContext()).addToRemList(aec);
							}
						}
						this.setMoving(true);
						((Cytoplasm)getTheContext()).addToRemList(this);
						VP123 vp = new VP123();
						((Cytoplasm)getTheContext()).getCell().addToMoveList(vp);
					}
				}
			}
			exTick = tick;
		}
	}

}
