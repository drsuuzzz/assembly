package assembly;

//import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
//import repast.simphony.essentials.RepastEssentials;
//import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
//import repast.simphony.space.grid.GridPoint;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
//import repast.simphony.util.ContextUtils;

public class VP1 extends AgentExtendCont{

	//private static final int hexangles[] = {0,60,119,175,231,287};//{0,60,123,179,235,291};//{60,63,56,56,56,59};
	
	//private static final int pentangles[] = {0,69,138,207,276};
	
	private static final int HEXA = 6;
	private static final int PENTA = 5;
	private static final int BETA = 1;
	private static final int BETAP = 0;
	private static final int GAMMA = 5;
	private static final int ALPHAP = 4;
	private static final int ALPHAPP = 2;
	private static final int ALPHA = 3;
	private static final int ALPHA0 = 0;
	private static final int ALPHA1 = 1;
	private static final int ALPHA2 = 2;
	private static final int ALPHA3 = 3;
	private static final int ALPHA4 = 4;

	
	private static boolean nucleating = false;
	
	private int nSides = 6;
	private VP1 sides[] = new VP1[6];
	private int type;
	private boolean complete;
	private boolean nucleate;
	
	//private double rotatetheta;
	//private double rotatephi;
	private double angles[] = {0,60,119,175,231,287};
	private double phi0;
	
	private double golden;
	
	private Genome genome;

	
	public VP1() {
		super();
		type = HEXA;
		for (int i = 0; i < nSides; i++) {
			sides[i] = null;
		}
		complete = false;
		nucleate = false;
		//rotatetheta = 0;
		//rotatephi = 0;
		phi0 = 90;
		genome = null;
	}
	
	@Parameter(usageName="complete",displayName="Sides completed")
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	@Parameter(usageName="nucleate",displayName="Nucleating")
	public boolean isNucleate() {
		return nucleate;
	}

	public void setNucleate(boolean nucleate) {
		this.nucleate = nucleate;
	}
	@Parameter(usageName="type",displayName="Sides")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setSides(int index, VP1 neighbor) {
		sides[index] = neighbor;
	}
	
	public VP1 getSides(int index) {
		return sides[index];
	}
	
	public Genome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}

	public int getSidesBound() {
		int retval=0;

		for (int i = 0; i < type; i++) {
			if (sides[i] != null) {
				retval++;
			}
		}
		return retval;
	}
	
	public double getPhi() {
		return phi0;
	}

	public void setOtherSides(int index, VP1 center) {
		VP1 neighbor = null;
		if (center.getType() == HEXA) {
			switch(index) {
			case BETA:
				sides[BETAP] = center;
				neighbor = center.getSides(ALPHAPP);
				if (neighbor != null) {
					neighbor.setSides(GAMMA, this);
					sides[GAMMA] = neighbor;
				}
				neighbor = center.getSides(BETAP);
				if (neighbor != null) {
					neighbor.setSides(BETAP, this);
					sides[BETA] = neighbor;
				}
				break;
			case BETAP:
				sides[BETA] = center;
				neighbor = center.getSides(BETA);
				if (neighbor != null) {
					neighbor.setSides(BETA,this);
					sides[BETAP] = neighbor;
				}
				neighbor = center.getSides(GAMMA);
				if (neighbor != null) {
					neighbor.setSides(ALPHAP, this);
					sides[ALPHAPP] = neighbor;
				}
				break;
			case GAMMA:
				sides[GAMMA] = center;
				neighbor = center.getSides(BETAP);
				if (neighbor != null) {
					neighbor.setSides(ALPHAPP, this);
					sides[ALPHAP] = neighbor;
				}
				neighbor = center.getSides(ALPHAP);
				if (neighbor != null) {
					neighbor.setSides(BETA,this);
					sides[BETAP] = neighbor;
				}
				break;
			case ALPHAP:
				sides[ALPHAPP] = center;
				neighbor = center.getSides(GAMMA);
				if (neighbor != null) {
					neighbor.setSides(BETAP, this);
					sides[BETA] = neighbor;
				}
				neighbor = center.getSides(ALPHA);
				if (neighbor != null) {
					neighbor.setSides(ALPHA2,this);
					sides[ALPHA] = neighbor;
				}
				break;
			case ALPHA:
				sides[ALPHA1] = center;
				neighbor = center.getSides(ALPHAP);
				if (neighbor != null) {
					sides[ALPHA0] = neighbor;
					neighbor.setSides(ALPHA, this);
				}
				neighbor = center.getSides(ALPHAPP);
				if (neighbor != null) {
					neighbor.setSides(ALPHA, this);
					sides[ALPHA2] = neighbor;
				}
				break;
			case ALPHAPP:
				sides[ALPHAP] = center;
				if (center.getSides(ALPHA) != null) {
					(center.getSides(ALPHA)).setSides(ALPHA0, this);
					sides[ALPHA] = center.getSides(ALPHA);
				}
				if (center.getSides(BETA) != null) {
					(center.getSides(BETA)).setSides(GAMMA, this);
					sides[GAMMA] = center.getSides(BETA);
				}
				break;
			}
		} else { //PENT
			switch(index) {
			case ALPHA0:
				System.out.println("Pent 0 shouldn't be here");
				break;
			case ALPHA1:
				System.out.println("Pent 1 shouldn't be here");
				break;
			case ALPHA2:
				System.out.println("Pent 2 shouldn't be here");
				break;
			case ALPHA3:
				sides[ALPHA] = center;
				neighbor = center.getSides(ALPHA2);
				if (neighbor != null) {
					sides[ALPHAP] = neighbor;
					neighbor.setSides(ALPHAPP, this);
				}
				neighbor = center.getSides(ALPHA4);
				if (neighbor != null) {
					sides[ALPHAPP] = neighbor;
					neighbor.setSides(ALPHAP, this);
				}
				break;
			case ALPHA4:
				sides[ALPHA] = center;
				neighbor = center.getSides(ALPHA3);
				if (neighbor != null) {
					sides[ALPHAP] = neighbor;
					neighbor.setSides(ALPHAPP, this);
				}
				neighbor = center.getSides(ALPHA0);
				if (neighbor != null) {
					sides[ALPHAPP] = neighbor;
					neighbor.setSides(ALPHAP, this);
				}
				break;
			}			
		}
	}
	
	public int getFreeSpot() {
		
		int result = -1;
		int N = type == HEXA ? HEXA : PENTA;
		for (int i = 0; i < N; i++) {
			if (sides[i] == null) {
				result = i;
				break;
			}
		}
		
		return result;
	}

	public int boundWith(int index, int type) {
	
		int result = 0;
		if (type == HEXA) {
			switch (index) {
				case BETA:
					result = BETAP;
					break;
				case BETAP:
					result = BETA;
					break;
				case GAMMA:
					result = GAMMA;
					break;
				case ALPHAPP:
					result = ALPHAP;
					break;
				case ALPHA:  //probably dead
					result = ALPHA1;
					break;
				case ALPHAP:
					result = ALPHAPP;
					break;
			}
		} else {
			result = ALPHA;
		}
		return result;
	}
	
	public void setAngles(VP1 vp1, int index) {
		
		//translate vp1 to origin (this) 
		int hexang[] = {60,59,56,56,56,63};
		int pentang[] = {69,69,69,69,69};
		NdPoint locvp1 = this.getSpace().getLocation(vp1);
		NdPoint locthis = this.getSpace().getLocation(this);
		double x = locvp1.getX()-locthis.getX();
		double z = locvp1.getZ()-locthis.getZ();
		
		//find theta 0 to vp1--new rotation angle
		//double coord = x/z;
		double thetanew = Math.toDegrees(Math.atan2(x,z));
		
		//set the new angles
		int start = boundWith(index,vp1.type);
		int N = type == HEXA ? HEXA : PENTA;
		int j = (start+1)%N;
		int angle = type == HEXA ? hexang[start] : pentang[start];
		angles[j]=angle+thetanew;
		for (int i = 0; i < N-1; i++) {
			j = (j+1)%N;
			int prev = (j-1) < 0 ? N-1 : (j-1)%N;
			angle = (type == HEXA) ? hexang[prev] : pentang[prev];
			angles[j] = angles[prev] + angle;
		}
	}
	
	public double getAngles(int index) {
		return angles[index];
	}

	public double[] getMoveToPosition(double length, double theta, double phi, VP1 vp1) {
		
		//double d[] = getDisplacement(3,1,length,theta,phi);
		//System.out.println("from getdisplacement "+d[0]+", "+d[1]+", "+d[2]);
		double coord[] = {0.0,0.0,0.0};
		/*coord[0] = length*Math.cos(phi);
		coord[1] = length*Math.cos(theta)*Math.sin(phi);
		coord[2] = length*Math.sin(theta)*Math.sin(phi);*/
		
		//find point from 0,0,0 origin
		coord[0] = length*Math.sin(theta)*Math.sin(phi); //x
		coord[1] = length*Math.cos(phi);                 //y
		coord[2] = length*Math.cos(theta)*Math.sin(phi); //z
		ContinuousSpace space = getSpace();//(ContinuousSpace)getTheContext().getProjection("nucleus");
		NdPoint locvp1 = space.getLocation(vp1);
		NdPoint locthis = space.getLocation(this);
		//translate point in relation to origin of center protein
		coord[0] += locvp1.getX()-locthis.getX();
		coord[1] += locvp1.getY()-locthis.getY();
		coord[2] += locvp1.getZ()-locthis.getZ();
		System.out.println("Old="+locthis.getX()+", "+locthis.getY()+", "+locthis.getZ());
		System.out.println("New="+coord[0]+", "+coord[1]+", "+coord[2]);
		//NdPoint disp = new NdPoint(coord);
		//return displacement
		return coord;
	}
	
/*	private  double[] rotate(double[] plane, double angle) {
		double x = plane[0];
		double y = plane[1];
		plane[0] = x * Math.cos(angle) - y * Math.sin(angle);
		plane[1] = y * Math.cos(angle) + x * Math.sin(angle);
		return plane;
	}

	public double[] getDisplacement (int dimCount, int unitDimension, double scale,
			double... anglesInRadians) {
		
		double[] displacement = new double[dimCount];
		displacement[unitDimension] = 1;
		double[] tmp = new double[2];
		int c = 0;
		for (int i = 0; i < dimCount; i++) {
			if (i == unitDimension) {
				continue;
			} else if (i > unitDimension) {
				tmp[0] = displacement[unitDimension];
				tmp[1] = displacement[i];
				tmp = rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[0];
				displacement[i] = tmp[1];
			} else if (i < unitDimension) {
				tmp[0] = displacement[i];
				tmp[1] = displacement[unitDimension];
				tmp = rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[1];
				displacement[i] = tmp[0];
			}
			c++;
		}
		System.out.println("displacement bef="+displacement[0]);
		SpatialMath.scale(displacement, scale);
		System.out.println("displacement aft="+displacement[1]);

		return displacement;
	}*/

//	@Override
	//public void checkNeighbors() {
//		if (!getStop()) {
	//		ContinuousSpace space = getSpace();//(ContinuousSpace)getTheContext().getProjection("nucleus");
		//	ContinuousWithin contlist = new ContinuousWithin(space,this,1);
			//Iterable list = contlist.query();
//			for (Object obj:list) {
	//			if (obj instanceof Genome) {
		//			Genome g = (Genome)obj;
			//		if (!g.getStop() && !nucleate) {
				//		this.setStop(true);
					//	((Genome)obj).setStop(true);
						//nucleating = true;
//						nucleate = true;
	//					type = HEXA;
		//				break;
			//		}
				//} else if (obj instanceof VP1) {
//					VP1 vp1 = (VP1)obj;
	//				if (vp1.getStop()) {
		//				if (!vp1.isComplete() && vp1.isNucleate()) {
			//				int index = vp1.getFreeSpot();
				//			if (index > -1 && index < nSides) {
					//			setStop(true);
						//		if (vp1.getType() == PENTA) {
							//		phi0=vp1.getPhi()-15;
								//} if (index == GAMMA) {
									//phi0=vp1.getPhi()-10;
//								} else if (index == ALPHA || index == ALPHAP || index == ALPHAPP) {
	//								phi0=vp1.getPhi()-15;
		//						} else {
			//						phi0=vp1.getPhi();
				//				}
					//			double phi = Math.toRadians(phi0/*vp1.getPhi()*/);
						//		double theta = Math.toRadians(vp1.getAngles(index));
							//	double pos[] = getMoveToPosition(2,theta,phi,vp1);
								//space.moveByDisplacement(this, pos);
//								System.out.println("Side "+index+" bound.");
	//							if (index == ALPHA && vp1.type != PENTA) {
		//							setType(PENTA);
			//						//phi0 = vp1.getPhi()-15;
				//				} else {
					//				setType(HEXA);
						//			//phi0 = vp1.getPhi()-10;
							//	}
								//if (index == GAMMA && vp1.type!=PENTA) {
									//phi0 = vp1.getPhi()-10;
								//}
//								NdPoint temp = space.getLocation(this);
	//							vp1.setSides(index,this);
		//						this.setOtherSides(index,vp1);
			//					this.setAngles(vp1, index);
				//				
					//			if (index == (vp1.getType()-1)) {
						//			vp1.setComplete(true);
							//		nucleating=false;
								//}
//								break;
	//						}
		//				} else if (!vp1.isComplete() && !nucleating && vp1.getSidesBound() >=3) {
			//				int index = vp1.getFreeSpot();
				//			if (index > -1 && index < vp1.getType()) {
					//			setStop(true);
//
	//							double theta;
		//						double phi;
			//					if (vp1.getType() == HEXA) {
				//					phi = Math.toRadians(vp1.getPhi());
					//				theta = Math.toRadians(vp1.getAngles(index));
						//		} else {
							//		phi = Math.toRadians(vp1.getPhi());
								//	theta = Math.toRadians(vp1.getAngles(index));
//								}
	//							double pos[] = getMoveToPosition(2,theta,phi,vp1);
		//						space.moveByDisplacement(this, pos);
			//					
				//				if (index == ALPHA && (vp1.getType() != PENTA)) {
					//				setType(PENTA);
						//			phi0 = vp1.getPhi()-15;
							//	} else {
								//	setType(HEXA);
									//phi0 = vp1.getPhi()-10;
//								}		
	//							vp1.setSides(index, this);
		//						this.setOtherSides(index,vp1);
			//					this.setAngles(vp1,index);
				//				if (vp1.getFreeSpot() == -1) {
					//				vp1.setComplete(true);
						//		}
							//	break;
//							}
	//					}
		//			}
			//	}

//			}
	//	}

//	}
	
	public void moveMolecule() {
		if (genome != null) {
			double phi = genome.getPhi();
			double theta = genome.getTheta();
			double dist = genome.getDistance();
			double coord[] = {0.0,0.0,0.0};
			coord[0] = dist*Math.sin(theta)*Math.sin(phi); //x
			coord[1] = dist*Math.cos(phi);                 //y
			coord[2] = dist*Math.cos(theta)*Math.sin(phi); //z
			ContinuousSpace space = this.getSpace();
			space.moveByDisplacement(this, coord);
		}
	}
	
	@Override
	public void move() {
		if (genome == null) {
			super.move();
		}
	}

}
