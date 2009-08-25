package assembly;

import java.util.Iterator;

import assembly.MRNA.MType;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;

public class Ribosome extends AgentExtendCont {
	
	private double moveTick;
	private double tranTick;
	
	public Ribosome() {
		super();
		moveTick=0;
		tranTick = 0;
		genXYZ();
		setName("Ribosome");
	}

	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double radius;
			double vpradius;
			double rerr;
			double vperr;
			if (RunEnvironment.getInstance().isBatch()){
				radius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				vpradius = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
				vperr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			} else {
				radius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1");
				vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceVP1Error");
			}
			double disp[] = calcDisplacement(MRNA.class,MRNA.class,radius,rerr,vpradius,vperr);
		
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
			} else {
				getSpace().moveByDisplacement(this, disp);
			}
			moveTick = tick;
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
							AgentExtendCont T=null;
							if (((MRNA) aec).getMType() == MType.Tag) {
								T = new LgTAg();
							} else if (((MRNA) aec).getMType() == MType.tag) {
								T = new SmTAg();
							} else if (((MRNA) aec).getMType() == MType.vp1) {
								T = new VP1();
							} else if (((MRNA) aec).getMType() == MType.vp2) {
								T = new VP2();
							} else if (((MRNA) aec).getMType() == MType.vp3) {
								T = new VP3();
							}
							if (T != null) {
								T.setSpace(this.getSpace());
								T.setTheContext(getTheContext());
								T.setLocation(Loc.cytoplasm);
								((Cytoplasm)getTheContext()).addToAddList(T);
								this.largeStepAwayFrom(aec);
								if (aec.getNoBound()>0) {
									aec.setNoBound(aec.getNoBound()-1);
								} 
								this.setBound(false);
							}
						}
						break;
					}
				}
			}
			tranTick = tick;
		}
	}
}
