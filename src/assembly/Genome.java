package assembly;


import java.util.ArrayList;
import java.util.Iterator;

import assembly.MRNA.mState;
import assembly.VP123.VPType;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class Genome extends AgentExtendCont{
	
	public enum GState {early, replicate, late, assembly};
	private GState state;
	private double neighborTick;
	private double lmoveTick;
	private double moveTick;
	private double move2Tick;
	private double xcriptTick;
	private double repTick;
	private double egressTick;
	
	private AgentExtendCont bind1;
	private AgentExtendCont bind2;

	public Genome() {
		super();
		//name = "Genome";
		//network = null;
		neighborTick = 0;
		lmoveTick = 0;
		moveTick = 0;
		move2Tick = 0;
		xcriptTick = 0;
		egressTick = 0;
		setName("Genome");
		state = GState.early;
		this.genXYZ();
		bind1 = null;
		bind2 = null;
	}
	
	public void setBoundProteins (AgentExtendCont agent) {
		//routine only necessary for Tag recruitment of DNA Pol
		if (bind1 == null) {
			bind1 = agent;
		} else if (bind1 instanceof LgTAg) {
			if (agent instanceof DNAPol) {
				bind2 = agent;
			}
		} else if (bind1 instanceof DNAPol) {
			if (agent instanceof LgTAg) {
				bind2 = agent;
			}
		}
	}
	
	public void clearBoundProteins () {
		bind1 = null;
		bind2 = null;
	}
	
	public boolean needAgent (AgentExtendCont agent) {
		
		boolean need = false;
		if (state == GState.assembly) {
			if (agent instanceof VP123) {
				need = true;
			}
		} else {
		if (bind1 != null) {
			if (bind1 instanceof LgTAg && bind2 == null) {
				if (agent instanceof DNAPol) {
					need = true;
				} else if (bind1.equals(agent)) {
					need = true;
				}
			} else if (bind1 instanceof DNAPol && bind2 == null) {
				if (agent instanceof LgTAg) {
					need = true;
				} else if (bind1.equals(agent)) {
					need = true;
				}
			} else if (bind1.equals(agent) || bind2.equals(agent)) {
				need = true;
			}
		} else if (agent instanceof DNAPol || agent instanceof LgTAg){
			need = true;
		}
		}
		
		return need;
	}
	

	@Parameter(usageName="state",displayName="Genome State", converter = "assembly.GStateConverter")
	public GState getState() {
		return state;
	}



	public void setState(GState state) {
		this.state = state;
	}



	//Scheduled methods
	
	public void move() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > moveTick) {
			double disp[] = {0.0,0.0,0.0};
			double r=0;
			double rerr=0;
			
			if (state == GState.early) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(TranscriptionFactor.class, LgTAg.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.replicate) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(LgTAg.class, DNAPol.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.late) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBind");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				}
				disp = this.calcDispIfCenter(LgTAg.class, VP123.class, Genome.class, HostGenome.class,r,rerr);
				
			} else if (state == GState.assembly) {
				if (RunEnvironment.getInstance().isBatch()){
					r = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					rerr = (Float)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				} else {
					r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
				}
				disp = this.calcDispIfCenter(VP123.class, VP123.class, Genome.class, HostGenome.class,r,rerr);
				//disp = move2();
			}
			if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
				randomWalk();
				clearBoundProteins();
			} else {
				double tmp[] = new double[3];
				NdPoint thispt = getSpace().getLocation(this);
				tmp[0] = disp[0]+thispt.getX();
				tmp[1] = disp[1]+thispt.getY();
				tmp[2] = disp[2]+thispt.getZ();
				//NdPoint pt = getSpace().moveByDisplacement(this, disp);
				//if (pt != null) {
					//this.setX(pt.getX()-thispt.getX());
					//this.setY(pt.getY()-thispt.getY());
					//this.setZ(pt.getZ() - thispt.getZ());
				//}
				this.normPositionToBorder(tmp, r);
				getSpace().moveTo(this, tmp);
				this.setX(tmp[0]-thispt.getX());
				this.setY(tmp[1]-thispt.getY());
				this.setZ(tmp[2]-thispt.getZ());
			}
			moveTick = tick;
		}
	}
	
	public void transcription() {
		double tick = RepastEssentials.GetTickCount();
		if (xcriptTick < tick) {
			double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
			double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
			ContinuousWithin<AgentExtendCont> list = new ContinuousWithin(getSpace(), this, (dist+err));
			Iterator<AgentExtendCont> l = list.query().iterator();
			if (state == GState.early) {
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof TranscriptionFactor) {
						if (aec.isBound()) {
							double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
							if (rand < 0.1) {
								MRNA mrna = new MRNA();
								//mrna.setMType(MType.Tag);
								mrna.setState(mState.early);
								mrna.setLocation(Loc.nucleus);
								mrna.setTheContext(this.getTheContext());
								mrna.setSpace(this.getSpace());
								((Nucleus)getTheContext()).addToAddList(mrna);
								aec.largeStepAwayFrom(this);
								aec.setBound(false);
								this.setNoBound(0);
								//state = GState.replicate;
								break;
							}
						}
					} 
				}
			} else if (state == GState.replicate) {
				boolean dfound = false;
				boolean lfound = false;
				AgentExtendCont daec = null;
				AgentExtendCont laec = null;
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof DNAPol && !dfound) {
						if (aec.isBound()) {
							daec = aec;
							dfound = true;
						}
					} else if (aec instanceof LgTAg && !lfound) {
						if (aec.isBound()) {
							laec = aec;
							lfound = true;
						}
					}
				}
				if (dfound && lfound) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < .2) {
						Genome g = new Genome();
						g.setState(GState.early);
						state = GState.late;
						g.setSpace(this.getSpace());
						g.setTheContext(this.getTheContext());
						g.setLocation(Loc.nucleus);
						((Nucleus)getTheContext()).addToAddList(g);
						daec.largeStepAwayFrom(this);
						daec.setBound(false);
						laec.largeStepAwayFrom(this);
						laec.setBound(false);
						this.setNoBound(0);
						this.clearBoundProteins();
						state = GState.late;
					}
				}
			} else if (state == GState.late) {
				while (l.hasNext()) {
					AgentExtendCont aec = l.next();
					if (aec instanceof LgTAg) {
						MRNA m = new MRNA();
						m.setState(mState.late);
						m.setLocation(Loc.nucleus);
						m.setSpace(this.getSpace());
						m.setTheContext(this.getTheContext());
						((Nucleus)getTheContext()).addToAddList(m);
						aec.largeStepAwayFrom(this);
						aec.setBound(false);
						this.setNoBound(0);
					}
				}
			}
			xcriptTick = tick;
		}
	}
	
	public void egress() {
		double tick = (double)RepastEssentials.GetTickCount();
		if (tick > egressTick) {
			if (getNoBound() == 72) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < 0.5) {
					double dist = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadius");
					double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
					ContinuousWithin list = new ContinuousWithin(getSpace(),this,(dist+err));
					Iterator<AgentExtendCont> l = list.query().iterator();
					while (l.hasNext()) {
						AgentExtendCont aec = l.next();
						if (aec instanceof VP123 && aec.isBound()) {
								//aec.setMoving(true);
							((Nucleus)getTheContext()).addToRemList(aec);
						}
					}
						//this.setMoving(true);
					((Nucleus)getTheContext()).addToRemList(this);
						//VP123 vp = new VP123();
						//vp.setVptype(VPType.VP12);
						//((Cytoplasm)getTheContext()).getCell().addToMoveList(vp);
					((Nucleus)getTheContext()).getCell().addVirions();
				}
			}
			egressTick = tick;

		}
	}
		
}
