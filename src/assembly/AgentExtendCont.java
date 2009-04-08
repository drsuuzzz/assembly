package assembly;

import java.util.ArrayList;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class AgentExtendCont {
	
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
	
	public AgentExtendCont() {
		stop=false;
		theContext=null;
		//thetaPhiDistGen();
		//theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//phi = RepastEssentials.RandomDraw(0, 2*Math.PI);
		//distance = RepastEssentials.RandomDraw(0, 2);
		moveTick = 0;
		name=null;
	}
	//@Parameter(usageName="stop",displayName="Stopped")
	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
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
	
	public double getTheta() {
		return theta;
	}
	public void setTheta(double theta) {
		this.theta = theta;
	}
	public double getPhi() {
		return phi;
	}
	public void setPhi(double phi) {
		this.phi = phi;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
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
	public double[] normPositionToGrid(double x1, double y1, double z1) {
		
		Dimensions dim = getSpace().getDimensions();
		x1 = x1 < 0 ? -x1 : x1;
		x1 = x1 >= dim.getWidth() ? (dim.getWidth() - x1 + dim.getWidth() -2) : x1;
		y1 = y1 < 0 ? -y1 : y1;
		y1 = y1 >= dim.getHeight() ? (dim.getHeight()- y1 + dim.getHeight() - 2) : y1;
		z1 = z1 < 0 ? -z1 : z1;
		z1 = z1 >= dim.getDepth() ? (dim.getDepth() - z1 + dim.getDepth() - 2) : z1;
		double ret[]={x1,y1,z1};
		return ret;
	}
	
	public double[] normPositionToBorder(double[] pt, double dist) {
		
		Dimensions dim = getSpace().getDimensions();
		//int d = (int)Math.ceil(dist);
		double min = dist;
		double maxX = dim.getWidth()-dist;
		double maxY = dim.getHeight()-dist;
		double maxZ = dim.getDepth()-dist;
		pt[0] = pt[0] < min ? (min+(min-pt[0])) : pt[0];
		pt[0] = pt[0] > maxX ? (maxX-(pt[0]-maxX)): pt[0];
		pt[1] = pt[1] < min ? (min+(min-pt[1])) : pt[1];
		pt[1] = pt[1] > maxY ? (maxY-(pt[1]-maxY)): pt[1];
		pt[2] = pt[2] < min ? (min+(min-pt[2])) : pt[2];
		pt[2] = pt[2] > maxZ ? (maxZ-(pt[2]-maxZ)): pt[2];
		return pt;
	}
	

	
	public void thetaPhiDistGen() {
		theta = RepastEssentials.RandomDraw(0, 2*Math.PI);
		phi = RepastEssentials.RandomDraw(0, Math.PI);
		distance = RepastEssentials.RandomDraw(0, 1);
		//System.out.println(name+","+theta+","+phi+","+distance);
	}
	
	public void genXYZ() {
		X=RepastEssentials.RandomDraw(-1,1);
		Y=RepastEssentials.RandomDraw(-1,1);
		Z=RepastEssentials.RandomDraw(-1,1);
	}
	
	public void move() {
		double tick = (double) RepastEssentials.GetTickCount();
		if (tick > moveTick) {
		if (!getStop()) {

			double coord[] = {0.0,0.0,0.0};
			thetaPhiDistGen();
			coord[0] = distance*Math.sin(theta)*Math.sin(phi); //x
			coord[1] = distance*Math.cos(phi);                 //y
			coord[2] = distance*Math.cos(theta)*Math.sin(phi); //z
			space.moveByDisplacement(this, coord);

		}
		moveTick = tick;
		}
	}
	
/*	public void separation() {
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double x = 0;
		double y = 0;
		double z = 0;
		int count = 0;
		NdPoint loc = space.getLocation(this);
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			x = x - (loc.getX()-pt.getX());
			y = y - (loc.getY()-pt.getY());
			z = z - (loc.getZ()-pt.getZ());
			count++;
		}
		if (count > 0) {
			theta += Math.atan2(x,z);
			phi += Math.atan2((Math.sqrt(z*z+x*x)),y);
			distance += Math.sqrt(x*x+y*y+z*z);
		}
	}
	
	public void alignment() {
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double ptdist = 0.0;
		int count = 0;
		for (Object obj:list) {
			ptdist += ((AgentExtendCont) obj).getDistance();
			count++;
		}
		if (count > 0) {
			ptdist = ptdist/count;
			distance = (ptdist - distance)/8;
		}
	}
	
	public void cohesion() {

		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double x = 0;
		double y = 0;
		double z = 0;
		int count = 0;
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			x += pt.getX();
			y += pt.getY();
			z += pt.getZ();
			count ++;
		}
		if (count > 0) {
			x = x/count;
			y = y/count;
			z = z/count;
			NdPoint loc = space.getLocation(this);
			NdPoint newLoc = new NdPoint(loc.getX(),loc.getY(),loc.getZ());
			x += (x-loc.getX())/100;
			y += (y-loc.getY())/100;
			z += (z-loc.getZ())/100;
			theta += Math.atan2(x,z);
			phi += Math.atan2((Math.sqrt(z*z+x*x)),y);
			distance += Math.sqrt(x*x+y*y+z*z);
		}
	}*/
	
/*	public double calcDistanceBetween(ContinuousSpace space, Object o1, Object o2) {
		double value = 0.0;
		NdPoint pt1 = space.getLocation(o1);
		NdPoint pt2 = space.getLocation(o2);
		//simple euclidean distance
		value = Math.sqrt((pt1.getX()-pt2.getX())*(pt1.getX()-pt2.getX()) + 
				(pt1.getY()-pt2.getY())*(pt1.getY()-pt2.getY()) + 
				(pt1.getZ()-pt2.getZ())*(pt1.getZ()-pt2.getZ()));
		return value;
	}*/
	
/*	public void avoidCollisions() {
		ContinuousSpace space = getSpace();
		ContinuousWithin contlist = new ContinuousWithin(space,this,1);
		Iterable list = contlist.query();
		double min = 1;
		Object m = null;
		for (Object obj:list) {
			NdPoint pt = space.getLocation(obj);
			double dist = calcDistanceBetween(space,this,obj);
			if (dist < min) {
				m = obj;
				min = dist;
			}
		}
		if (m != null) {
			NdPoint pt = space.getLocation(this);
			double xy = Math.atan(Math.sqrt(pt.getX()*pt.getX()+pt.getZ()*pt.getZ())/pt.getY()) + Math.PI;
			double yz = xy;
			double xz = Math.atan2(pt.getX(), pt.getZ())+Math.PI;
	        space.moveByVector(this, 1, xy, yz, xz);
		}
	}*/
	
	
/*	public void moveContinuous() {
		
		Context context = ContextUtils.getContext("Assemble");
		ContinuousSpace space = (ContinuousSpace)context.getProjection("nucleus");
		
		double xy = RepastEssentials.RandomDraw(0, 2*Math.PI);
		double xz = RepastEssentials.RandomDraw(0, 2*Math.PI);
		double speed = RepastEssentials.RandomDraw(-2, 2);
        space.moveByDisplacement(this, speed * xy, speed * xy, speed * xz);
	}*/

}
