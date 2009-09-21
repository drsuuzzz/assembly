package assembly;

import java.util.ArrayList;
import java.util.Iterator;

import assembly.Genome.GState;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class AgentExtendCont {
	
	public enum Loc {nucleus, cytoplasm};
	private Loc location;
	public enum BoundTo {vlp, genome, none};
	private BoundTo boundTo;
	
	private Context theContext;
	private Boolean stop;
	private ContinuousSpace space;
	private double theta;
	private double phi;
	private double distance;
	private double X;
	private double Y;
	private double Z;
	private double moveTick;
	private String name;
	private boolean dead;
	private boolean moving; //moving between cytoplasm or nucleus
	private boolean bound;
	
	private int noBound;
	
	private ISchedulableAction move;
	private ISchedulableAction transcription;
	private ISchedulableAction export;
	private ISchedulableAction death;
	private ISchedulableAction splice;
	private ISchedulableAction egress;
	
	public AgentExtendCont() {
		theContext=null;
		dead = false;
		moving = false;
		noBound = 0;
		bound = false;
		moveTick = 0;
		name=null;
		move = null;
		transcription = null;
		export = null;
		death = null;
		splice = null;
		egress = null;
		location = Loc.nucleus;
		boundTo = BoundTo.none;
	}
	
	@Parameter(usageName="bound",displayName="Bound")
	public boolean isBound() {
		return bound;
	}


	public void setBound(boolean bound) {
		this.bound = bound;
	}
	@Parameter(usageName="noBound",displayName="Number Bound")
	public int getNoBound() {
		return noBound;
	}

	public void setNoBound(int noBound) {
		this.noBound = noBound;
	}
	
	@Parameter(usageName = "boundTo", displayName="Bound To", converter = "assembly.BoundToConverter")
	public BoundTo getBoundTo() {
		return boundTo;
	}

	public void setBoundTo(BoundTo boundTo) {
		this.boundTo = boundTo;
	}
	
	@Parameter(usageName = "location", displayName = "Location", converter = "assembly.LocationConverter")
	public Loc getLocation() {
		return location;
	}

	public void setLocation(Loc location) {
		this.location = location;
	}
	@Parameter(usageName="dead",displayName="Dead")
	public boolean isDead() {
		return dead;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public ISchedulableAction getDeath() {
		return death;
	}

	public void setDeath(ISchedulableAction death) {
		this.death = death;
	}

	public ISchedulableAction getExport() {
		return export;
	}

	public ISchedulableAction getMove() {
		return move;
	}


	public void setMove(ISchedulableAction move) {
		this.move = move;
	}


	public void setExport(ISchedulableAction export) {
		this.export = export;
	}

	public ISchedulableAction getSplice() {
		return splice;
	}


	public void setSplice(ISchedulableAction splice) {
		this.splice = splice;
	}
	
	public void setEgress(ISchedulableAction e) {
		this.egress = e;
	}

	public ISchedulableAction getEgress() {
		return egress;
	}
	
	public void setTranscription(ISchedulableAction t) {
		this.transcription = t;
	}
	
	public ISchedulableAction getTranscription() {
		return transcription;
	}

	public Context getTheContext() {
		return theContext;
	}

	public void setTheContext(Context theContext) {
		this.theContext = theContext;
	}
	
	public ContinuousSpace getSpace() {
		return space;
	}
	
	public void setSpace(ContinuousSpace space) {
		this.space = space;
	}
	
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public double getZ() {
		return Z;
	}
	public void setZ(double z) {
		Z = z;
	}
	@ProbeID()
	public String name() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void removeAnAction(ISchedulableAction action) {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		if (action != null) {
			schedule.removeAction(action);
		}
	}

	public double[] normPositionToBorder(double[] pt, double dist) {
		
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius");
		int cr = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		
		double[] center = new double[3];
		center[0] = cr;
		center[1] = cr;
		center[2] = cr;

		double[] v = new double[3];
		v[0] = pt[0] - center[0];
		v[1] = pt[1] - center[1];
		v[2] = pt[2] - center[2];
		
		double d = AgentGeometry.calcDistance(center, pt);
		
		double min=0;
		double max=0;
		if (this.getLocation() == Loc.cytoplasm) {
			min = r + dist;
			max = cr - dist;
		} else if (this.getLocation() == Loc.nucleus) {
			max = r-dist;
		}
		
		if (this.getLocation() == Loc.cytoplasm) {
			if (d < min) {
				double s = (min + (min-d));
				AgentGeometry.scale(v, s);
			}
			if (d > max) {
				double s = (max - (d-max));
				AgentGeometry.scale(v, s);
			}
		} else if (this.getLocation() == Loc.nucleus) {
			if (d > max) {
				double s = (max - (d-max));
				AgentGeometry.scale(v, s);
			}
		}
		pt[0] = v[0] + center[0];
		pt[1] = v[1] + center[1];
		pt[2] = v[2] + center[2];
		return pt;
	}
	
	public boolean nearWall(NdPoint pt) {
		
		boolean retval = false;
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius");
		int cr = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		
		double[] center = new double[3];
		center[0] = cr;
		center[1] = cr;
		center[2] = cr;

		double[] v = new double[3];
		v[0] = pt.getX() - center[0];
		v[1] = pt.getY() - center[1];
		v[2] = pt.getZ() - center[2];
		
		double d = AgentGeometry.calcDistance(center, pt);
		
		if (this.getLocation() == Loc.cytoplasm) {
			if ((d-r) < 1) {
				retval = true;
			}
		} else if (this.getLocation() == Loc.nucleus) {
			if ((r-d) < 1) {
				retval = true;
			}
		}
		//if (pt.getX() < 1 || pt.getY() < 1 || pt.getZ() < 1) {
			//retval = true;
		//} else if ((pt.getX() > dim.getWidth()-2) || (pt.getY() > dim.getHeight()-2) || (pt.getZ() > dim.getDepth()-2)) {
			//retval = true;
		//}
		return retval;
	}
	
	public boolean nearWallGroup(double dist, double err) {
		
		boolean retval = false;
		double[] pt = this.getSpace().getLocation(this).toDoubleArray(null);
		int r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius");
		int cr = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius");
		
		double[] center = new double[3];
		center[0] = cr;
		center[1] = cr;
		center[2] = cr;

		double[] v = new double[3];
		v[0] = pt[0] - center[0];
		v[1] = pt[1] - center[1];
		v[2] = pt[2] - center[2];
		
		double d = AgentGeometry.calcDistance(center, pt);
		if (this.getLocation() == Loc.cytoplasm) {
			if (d-r <= dist+err ) {
				retval = true;
			}
		} else if (this.getLocation() == Loc.nucleus) {
			if (r-d <= dist+err) {
				retval = true;
			}
		}

		return retval;
	}
	
	public void genXYZ() {
		X=RepastEssentials.RandomDraw(-0.5,0.5);
		Y=RepastEssentials.RandomDraw(-0.5,0.5);
		Z=RepastEssentials.RandomDraw(-0.5,0.5);
	}
	
	public void randomWalk() {

		double coord[] = {0.0,0.0,0.0};
		genXYZ();
		coord[0] = this.getX() + this.getSpace().getLocation(this).getX();
		coord[1] = this.getY() + this.getSpace().getLocation(this).getY();
		coord[2] = this.getZ() + this.getSpace().getLocation(this).getZ();
		AgentMove.bounceInLocation(coord, this.getLocation());
		space.moveTo(this, coord);
	}
	
	public void largeStepAwayFrom(AgentExtendCont agent) {
		NdPoint thisp = space.getLocation(this);
		NdPoint pt = space.getLocation(agent);
		double vector[][] = AgentGeometry.pointDisplacement(thisp.toDoubleArray(null), pt.toDoubleArray(null));
		double disp[] = {0.0,0.0,0.0};
		disp[0] = vector[0][0]*4 + thisp.getX();
		disp[1] = vector[1][0]*4 + thisp.getY();
		disp[2] = vector[2][0]*4 + thisp.getZ();
		AgentMove.bounceInLocation(disp, this.getLocation());
		space.moveTo(this, disp);
	}
	public int calcMax(Class agentType1, Class agentType2) {
		int x =0;
		if (agentType1.getName().equals(VP2.class.getName()) || agentType2.getName().equals(VP3.class.getName())) {
			x = 5;
		} else if (agentType1.getName().equals(MRNA.class.getName())){
			x = 20;
		} else if (agentType1.getName().equals(Genome.class.getName()) || agentType1.getName().equals(VLP.class.getName())) {
			x = 72;
		}
		else {
			x = 1;
		}
		return x;
	}
	
	public void makeVLP(double[] center) {
		
		double v[] = {0.0,0.0,0.0};
		v[0] = center[0] - getSpace().getLocation(this).getX();
		v[1] = center[1] - getSpace().getLocation(this).getY();
		v[2] = center[2] - getSpace().getLocation(this).getZ();
		
		double r = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadius");;
		double rerr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceRadiusError");
		
		AgentGeometry.scale(v, (r-rerr));
		v[0] = getSpace().getLocation(this).getX() + v[0];
		v[1] = getSpace().getLocation(this).getY() + v[1];
		v[2] = getSpace().getLocation(this).getZ() + v[2];
		VLP vlp = new VLP();
		vlp.setTheContext(this.getTheContext());
		vlp.setSpace(this.getSpace());
		this.getTheContext().add(vlp);
		getSpace().moveTo(vlp, v);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double start = RepastEssentials.GetTickCount() <= 0 ? 1 : RepastEssentials.GetTickCount();
		double starteven;
		if ((int)start%2==0) {
			start = start + 1;
		}
		//odd tick
		ScheduleParameters sparams = ScheduleParameters.createRepeating(start, 2);
		this.setMove(schedule.schedule(sparams,vlp,"move"));
		this.setEgress(schedule.schedule(sparams, vlp, "egress"));
	}	
	
	public boolean neighborCenterChk (AgentExtendCont center, ContinuousWithin list, double cdist, double ndist, double cerr, double nerr) {
	
		boolean retval = true;
		
		if (center instanceof VLP) {
			if (center.getNoBound() >1) {
				retval = false;
				Iterator l = list.query().iterator();
				while (l.hasNext()) {
					AgentExtendCont aec = (AgentExtendCont) l.next();
					if (aec instanceof VP123) {
						NdPoint pt1 = getSpace().getLocation(center);
						NdPoint pt2 = getSpace().getLocation(aec);
						NdPoint pt3 = getSpace().getLocation(this);
						if (AgentGeometry.calcDistanceNdPoints(pt1, pt2) < (cdist+cerr)) {
							if (AgentGeometry.calcDistanceNdPoints(pt2, pt3) < (ndist+nerr)) {
								retval = true;
								break;
							}
						}
					}
				}
			}
		}
		
		return retval;
	}
	
	public double[] calcDisplacement(Class agentType1, Class agentType2, double distc, double cerr, double distn, double nerr) {
		double retpt[] = {0.0,0.0,0.0};
		double cohesiong[]={0,0,0};
		double cohesionv[]={0,0,0};
		double separationg[]={0,0,0};
		double separationv[]={0,0,0};
		double alignmentg[]={0,0,0};
		double alignmentv[]={0,0,0};

		NdPoint thispt = this.getSpace().getLocation(this);
		
		boolean coh = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleCohesion");
		boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");

		ContinuousSpace space = getSpace();
		
		ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(space,this,(distc+cerr));
		Iterator<AgentExtendCont> l = list.query().iterator();
		boolean cAgent = false;
		double center[] = {0,0,0};
		int max = calcMax(agentType1,agentType2);
		int parcnt=0;
		while (l.hasNext()) {
			AgentExtendCont obj = l.next();
			
			if (obj.getClass().getName().equals(agentType1.getName()) || obj.getClass().getName().equals(agentType2.getName())) {
				if (obj instanceof Genome) {
					if (((Genome)obj).getState() == GState.RR) {
						if (this instanceof TranscriptionFactor) {
							((Genome)obj).setState(GState.early);
						} else if (this instanceof LgTAg) {
							//if (((CytoNuc)getTheContext()).getNoGenome() > 1) {
								///double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
								//if (rand < AgentProbabilities.replicate) {
									((Genome)obj).setState(GState.replicate);
								//} else {
									//((Genome)obj).setState(GState.late);
								//}
							//} else {
								//((Genome)obj).setState(GState.late);
							//}
						} else if (this instanceof VP123) {
							((Genome)obj).setState(GState.assembly);
						}
					}
					if (((Genome)obj).getState()==GState.replicate) {
						max = 7;
						if (!((Genome)obj).needAgent(this)) {
							break;
						} else {
							((Genome)obj).setBoundProteins(this);
						}
					}
					/*if (((Genome)obj).getState() == GState.early) {
						if (this instanceof LgTAg) {
							((Genome)obj).setState(GState.replicate);
						}
					}
					if (((Genome)obj).getState() == GState.late) {
						if (this instanceof VP123) {
							((Genome)obj).setState(GState.assembly);
						}
					}*/
					if (((Genome)obj).getState() == GState.assembly) {
						if (!((Genome)obj).needAgent(this)) {
							break;
						}
					}
				}
				//if (neighborCenterChk(obj,list,distc,distn,cerr,nerr)) {
				if (obj.getNoBound() < max || (obj.getNoBound() == max && this.isBound())) {
					cAgent = true;
					center[0] = space.getLocation(obj).getX();
					center[1] = space.getLocation(obj).getY();
					center[2] = space.getLocation(obj).getZ();
					if (coh) {
						cohesiong[0] = (center[0]-thispt.getX())/100;
						cohesiong[1] = (center[1]-thispt.getY())/100;
						cohesiong[2] = (center[2]-thispt.getZ())/100;
					}
					if (sep) {
						if (AgentGeometry.calcDistance(center, thispt) < (distc-cerr)) {
							separationg[0] = ((thispt.getX()/*+cohesiong[0]*/)-center[0])/20;
							separationg[1] = ((thispt.getY()/*+cohesiong[1]*/)-center[1])/20;
							separationg[2] = ((thispt.getZ()/*+cohesiong[2]*/)-center[2])/20;
						}
					}
					if (aln) {
						alignmentg[0] = obj.getX();
						alignmentg[1] = obj.getY();
						alignmentg[2] = obj.getZ();
					}
					parcnt=1;
					setBound(true);
					if (obj instanceof Genome) {
						setBoundTo(BoundTo.genome);
						break;
					} else if (obj instanceof VLP) {
						setBoundTo(BoundTo.vlp);
					}
				//}
				}
			}
		}

		int count=0;
		int counta = 0;
		list = new ContinuousWithin<AgentExtendCont>(space,this,(distn+nerr));
		l = list.query().iterator();
		if (cAgent || isBound()) {
			//setBound(true);
			while (l.hasNext()) {
				AgentExtendCont obj = l.next();
				if (obj.getClass().getName().equals(this.getClass().getName()) && obj.isBound()) {
					NdPoint vpt = space.getLocation(obj);
					if (AgentGeometry.calcDistance(center,vpt) < (distc+cerr)) {
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (distn-nerr)) {
								separationv[0] += (thispt.getX()-vpt.getX())/20;
								separationv[1] += (thispt.getY()-vpt.getY())/20;
								separationv[2] += (thispt.getZ()-vpt.getZ())/20;
							}
						}
						//if (aln) {
							//alignmentv[0] += obj.getX();
						//	alignmentv[1] += obj.getY();
							//alignmentv[2] += obj.getZ();
							//counta++;
						//}
					}
				}
			}
			if (count > 0) {
				if (coh) {
					cohesionv[0] = ((cohesionv[0]/count)-thispt.getX())/100;
					cohesionv[1] = ((cohesionv[1]/count)-thispt.getY())/100;
					cohesionv[2] = ((cohesionv[2]/count)-thispt.getZ())/100;
				}
				parcnt++;
			}
			if (counta > 0) {
				if (aln) {
					alignmentv[0] = ((alignmentv[0]/counta)  );
					alignmentv[1] = ((alignmentv[1]/counta));
					alignmentv[2] = ((alignmentv[2]/counta));
				}
			}
		} else if (this instanceof VP123 /*|| this instanceof VP1*/){
			while (l.hasNext()) {
				Object obj = l.next();
				if (obj instanceof VP123 /*|| this instanceof VP1*/) {
					AgentExtendCont vp = (AgentExtendCont) obj;
					NdPoint vpt = space.getLocation(vp);
					if (vp.getBoundTo()!= BoundTo.genome) {
						if (coh) {
							cohesionv[0] += vpt.getX();
							cohesionv[1] += vpt.getY();
							cohesionv[2] += vpt.getZ();
							count++;
						}
						if (sep) {
							if (AgentGeometry.calcDistanceNdPoints(vpt, thispt) < (distn-nerr)) {
								separationv[0] += (thispt.getX()-vpt.getX())/20;
								separationv[1] += (thispt.getY()-vpt.getY())/20;
								separationv[2] += (thispt.getZ()-vpt.getZ())/20;
							}
						}
						if (aln) {
							alignmentg[0] += vp.getX();
							alignmentg[1] += vp.getY();
							alignmentg[2] += vp.getZ();
							counta++;
						}
					//}
					}
				}
			}
			if (count >= 4 && !this.isBound() && this instanceof VP123) {
				center[0] = cohesionv[0]/count;
				center[1] = cohesionv[1]/count;
				center[2] = cohesionv[2]/count;
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < AgentProbabilities.makeVLP) {
					makeVLP(center);
					setBound(true);
					setBoundTo(BoundTo.vlp);
				}
			} else {
				setBound(false);
				setBoundTo(BoundTo.none);
			}
			if (count > 0) {
				if (coh) {
					double mypt[] = space.getLocation(this).toDoubleArray(null);
					cohesionv[0] = ((cohesionv[0]/count)-mypt[0])/100;
					cohesionv[1] = ((cohesionv[1]/count)-mypt[1])/100;
					cohesionv[2] = ((cohesionv[2]/count)-mypt[2])/100;
				}
				parcnt++;
			}
			if (counta > 0) {
				if (aln) {
					alignmentg[0] = alignmentg[0]/counta;
					alignmentg[1] = alignmentg[1]/counta;
					alignmentg[2] = alignmentg[2]/counta;
				}
			}
		}
		if (parcnt > 0 ) {
			retpt[0] = (cohesiong[0] + cohesionv[0])/parcnt + 
				(separationg[0] + separationv[0])/parcnt + 
				(alignmentg[0]);
			retpt[1] = (cohesiong[1] + cohesionv[1])/parcnt + 
					(separationg[1] + separationv[1])/parcnt + 
					(alignmentg[1]);
			retpt[2] = (cohesiong[2] + cohesionv[2])/parcnt + 
					(separationg[2] + separationv[2])/parcnt + 
					(alignmentg[2]);
		}
		if (retpt[0]==0 && retpt[1]==0 && retpt[2]==0) {

		} else {
			AgentGeometry.trim(retpt, nerr);//maybe hardcode this?
			this.setX(retpt[0]);
			this.setY(retpt[1]);
			this.setZ(retpt[2]);
		}
		return retpt;
	}
	
	public double[] calcDispIfCenter(Class centerType1, Class centerType2, Class agentType1,Class agentType2,
			double distc, double cerr) {
		return calcDispIfCenter(centerType1, centerType2, centerType2, agentType1, agentType2, distc, cerr);
		
	}
	
	public double[] calcDispIfCenter(Class centerType1, Class centerType2, Class centerType3, Class agentType1,Class agentType2,
			double distc, double cerr) {
		double coord[] = {0,0,0};
		NdPoint thispt = this.getSpace().getLocation(this);

		boolean aln = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleAlignment");
		boolean sep = (Boolean)RunEnvironment.getInstance().getParameters().getValue("ruleSeparation");
		
		ContinuousWithin<AgentExtendCont> list = new ContinuousWithin<AgentExtendCont>(this.getSpace(), this, (distc+cerr));
		Iterator neighbors = list.query().iterator();
		
		double align[] = {0,0,0};
		int count = 0;
		int max = 0;
		if (this instanceof VP2 || this instanceof VP3) {
			max = 5;
		} else if (this instanceof MRNA){
			max = 20;
		} else if (this instanceof Genome) {
			if (((Genome)this).getState()==GState.replicate) {
				max = 7;
			} else if (((Genome)this).getState() == GState.assembly) {
				max = 72;
			} else {
				max = 1;
			}
		} else if (this instanceof VLP) {
			max = 72;
		} else {
			max = 1;
		}
		while(neighbors.hasNext()){
			AgentExtendCont aec = (AgentExtendCont)neighbors.next();

			if (aec.getClass().getName().equals(centerType1.getName()) || 
					aec.getClass().getName().equals(centerType2.getName()) ||
					aec.getClass().getName().equals(centerType3.getName())) {
				if (aln) {
					if (getNoBound() < max || (getNoBound() == max && aec.isBound())) {
						
						if (!(this instanceof VLP && aec.getBoundTo() == BoundTo.genome)) {
							align[0] += aec.getX();
							align[1] += aec.getY();
							align[2] += aec.getZ();
							//aec.setBound(true);
							//if (this instanceof VLP && aec.getBoundTo() != BoundTo.genome) {
								//aec.setBoundTo(BoundTo.vlp);
							//} else if (this instanceof Genome) {
								//aec.setBoundTo(BoundTo.genome);
							//}
							count++;
						}
					}
				}
			}
		}
		if (count > 0) {
			double x = RandomHelper.nextDoubleFromTo(-.2, .2);
			double y = RandomHelper.nextDoubleFromTo(-.2, .2);
			double z = RandomHelper.nextDoubleFromTo(-.2, .2);
			align[0] = (align[0])/(count)/*+this.getX())/8*/;
			align[1] = (align[1])/(count)/*+this.getY())/8*/;
			align[2] = (align[2])/(count)/*+this.getZ())/8*/;
		}

		setNoBound(count);

		//keep distance from other vp2 & vp3
		double separ[] = {0.0,0.0,0.0};
		list = new ContinuousWithin(this.getSpace(), this, 2.5*(distc+cerr));
		neighbors = list.query().iterator();
		while (neighbors.hasNext()) {
			Object obj = neighbors.next();
			if (obj.getClass().getName().equals(agentType1.getName()) || obj.getClass().getName().equals(agentType2.getName())) {
				if (sep) {
					NdPoint other = this.getSpace().getLocation(obj);
					separ[0] += (thispt.getX()-other.getX())/20;
					separ[1] += (thispt.getY()-other.getY())/20;
					separ[2] += (thispt.getZ()-other.getZ())/20;
				}
			}
		}

		
		coord[0] = align[0] + separ[0];
		coord[1] = align[1] + separ[1];
		coord[2] = align[2] + separ[2];
		if (coord[0]==0 && coord[1]==0 && coord[2]==0) {
			;
		} else {
			AgentGeometry.trim(coord, cerr);
			this.setX(coord[0]);
			this.setY(coord[1]);
			this.setZ(coord[2]);
		}
		return coord;
	}
		
	public void removeScheduledActions() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		if (move != null) {
			schedule.removeAction(move);
			move = null;
		}
		if (transcription != null) {
			schedule.removeAction(transcription);
			transcription = null;
		}
		if (export != null) {
			schedule.removeAction(export);
			export = null;
		}
		if (death != null) {
			schedule.removeAction(death);
			death = null;
		}
		if (splice != null) {
			schedule.removeAction(splice);
			splice = null;
		}
		if (egress != null) {
			schedule.removeAction(egress);
			egress = null;
		}
	}
	
	public void die() {
		if (!dead) {
			((CytoNuc)getTheContext()).addToRemList(this);
			this.setDead(true);
			this.setTheContext(null);
			this.setSpace(null);
		}
	}
	
	public void bind() {
		
	}
	
	public void transcription() {
		
	}
	
	public void translation() {
		
	}
}
