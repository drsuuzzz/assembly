package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;

public class HostGenome extends AgentExtendCont {
	
	private double moveTick;
	private double xcriptTick;
	
	public HostGenome() {
		super();
		setName("HostGenome");
		moveTick = 0;
		xcriptTick = 0;
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			
			double disp[] = this.calcDispIfCenter(LgTAg.class, LgTAg.class, HostGenome.class, HostGenome.class);
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else
				getSpace().moveByDisplacement(this, disp);
			moveTick = tick;
		}
	}
	
	public void transcription() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > xcriptTick) {
			double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			ContinuousWithin<AgentExtendCont> list = new ContinuousWithin(getSpace(), this, (dist+err));
			Iterator<AgentExtendCont> l = list.query().iterator();
			while (l.hasNext()) {
				AgentExtendCont aec = l.next();
				if (aec instanceof LgTAg) {
					if (aec.isBound()) {
						double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
						if (rand < 0.001) {
							DNAPol pol = new DNAPol();
							pol.setTheContext(this.getTheContext());
							pol.setSpace(this.getSpace());
							((Nucleus)getTheContext()).addToAddList(pol);
							aec.largeStepAwayFrom(this);
							aec.setBound(false);
							this.setNoBound(0);
							break;
						}
					}
				}
			}
			xcriptTick = tick;
		}
	}
}
