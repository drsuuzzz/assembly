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
/*	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}*/

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
	
/*	public double getTheta() {
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
	}*/
	
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
			double coord[] = {0.0,0.0,0.0};
			coord[0] = this.getX();
			coord[1] = this.getY();
			coord[2] = this.getZ();
			space.moveByDisplacement(this, coord);
			moveTick = tick;
		}
	}
	public void bind() {
		
	}
	
	public void transcription() {
		
	}
	
	public void translation() {
		
	}
}
