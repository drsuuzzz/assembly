package assembly;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.ui.probe.ProbeID;
import repast.simphony.util.ContextUtils;

public class Genome extends AgentExtendCont{
	
	private String name;
	private static final double golden = 1.618033988749895;
	private static final double coord[][]={{0,golden,1},
		 {0,golden,-1},
		 {-golden,1,0},
		 {-1,0,golden},
		 {1,0,golden},
		 {golden,1,0},
		 {golden,-1,0},
		 {1,0,-golden},
		 {-1,0,-golden},
		 {-golden,-1,0},
		 {0,-golden,1},
		 {0,-golden,-1}, 
	};
	
	private static final double factor = 2;
	
	private VP1 vertices[]={null,null,null,null,null,null,null,null,null,null,null,null};
	
	private static double radius = 0.9510565163;
	
	private Network<VP1> network;

	public Genome() {
		super();
		name = "Genome";
		network = null;
	}
	
	
	
	public Network getNetwork() {
		return network;
	}



	public void setNetwork(Network network) {
		this.network = network;
	}



	@ProbeID()
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void genomeMove(){
		double coord[] = {0.0,0.0,0.0};
		thetaPhiDistGen();
		coord[0] = getDistance()*Math.sin(getTheta())*Math.sin(getPhi()); //x
		coord[1] = getDistance()*Math.cos(getPhi());                 //y
		coord[2] = getDistance()*Math.cos(getTheta())*Math.sin(getPhi()); //z
		NdPoint pt = getSpace().getLocation(this);
		
		double coord2[] = {0.0,0.0,0.0};
		coord2[0] = radius*factor*Math.sin(getTheta())*Math.sin(getPhi());
		coord2[1] = radius*factor*Math.cos(getPhi());
		coord2[2] = radius*factor*Math.cos(getTheta())*Math.sin(getPhi());
		
		NdPoint npt = new NdPoint(coord2[0]+pt.getX(),coord2[1]+pt.getY(),coord2[2]+pt.getZ());
		Dimensions d = getSpace().getDimensions();
		if (npt.getX() < 0 || npt.getX() >= d.getWidth()) {
			;
		} else if (npt.getY() < 0 || npt.getY() >=d.getHeight()) {
			;
		} else if (npt.getZ() < 0 || npt.getZ() >= d.getDepth()) {
			;
		} else {
			getSpace().moveByDisplacement(this, coord);
		}
	}
	
    @Override
	public void move() {
		NdPoint loc = getSpace().getLocation(this);
		System.out.println("("+loc.getX()+", "+loc.getY()+", "+loc.getZ()+")");
    	boolean rand = RepastEssentials.RandomDrawAgainstThreshold(0.5);
    	if (rand) {
    		//genomeMove();
    		super.move();
    		for (int i = 0; i < 12; i++) {
    			if (vertices[i] != null) {
    				vertices[i].moveMolecule();
    			}
    		}
    	}
	}
	
	public int findFreeVertex(Object obj) {
		
		int retval = 0;
		boolean found = false;
		for (int i = 0; i < 12; i++) {
			if (vertices[i] == null) {
				retval = i;
				found = true;
				break;
			}
		}
		if (!found)
			retval = -1;
		return retval;
	}
	
	public void setEdges(int index) {
		if (index != 0) {
		//	network.addEdge(vertices[index-1], vertices[index]);
			//if (i > 1 && i <=6) {
				//network.addEdge(vertices[0],vertices[index]);
			//}
			switch (index) {
			case 0:
				if (vertices[1] != null)
					if (network.getEdge(vertices[0],vertices[1]) == null)
						network.addEdge(vertices[0],vertices[1]);
				if (vertices[2] != null) 
					if (network.getEdge(vertices[0], vertices[2]) == null)
					network.addEdge(vertices[0], vertices[2]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[0], vertices[3]) == null)
						network.addEdge(vertices[0], vertices[3]);
				if (vertices[4] != null) 
					if (network.getEdge(vertices[0], vertices[4]) == null)
						network.addEdge(vertices[0],vertices[4]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[0], vertices[5]) == null)
						network.addEdge(vertices[0], vertices[5]);
				break;
			case 1:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[1]) == null)
					network.addEdge(vertices[0], vertices[1]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[1], vertices[2]) == null)
					network.addEdge(vertices[1], vertices[2]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[1], vertices[5]) == null)
					network.addEdge(vertices[1], vertices[5]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[1], vertices[7]) == null)
					network.addEdge(vertices[1], vertices[7]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[1], vertices[8]) == null)
					network.addEdge(vertices[1], vertices[8]);
				break;
			case 2:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[2]) == null)
					network.addEdge(vertices[0], vertices[2]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[2], vertices[1]) == null)
					network.addEdge(vertices[2], vertices[1]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[2], vertices[3]) == null)
					network.addEdge(vertices[2], vertices[3]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[2], vertices[8]) == null)
					network.addEdge(vertices[2], vertices[8]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[2], vertices[9]) == null)
					network.addEdge(vertices[2], vertices[9]);
				break;
			case 3:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[3]) == null)
					network.addEdge(vertices[0], vertices[3]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[3], vertices[2]) == null)
					network.addEdge(vertices[3], vertices[2]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[3], vertices[4]) == null)
					network.addEdge(vertices[3], vertices[4]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[3], vertices[9]) == null)
				network.addEdge(vertices[3], vertices[9]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[3], vertices[10]) == null)
				network.addEdge(vertices[3], vertices[10]);
				break;
			case 4:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[4]) == null)
					network.addEdge(vertices[0], vertices[4]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[4], vertices[3]) == null)
					network.addEdge(vertices[4], vertices[3]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[4], vertices[5]) == null)
					network.addEdge(vertices[4], vertices[5]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[4], vertices[6]) == null)
					network.addEdge(vertices[4], vertices[6]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[4], vertices[10]) == null)
					network.addEdge(vertices[4], vertices[10]);
				break;
			case 5:
				if (vertices[0] != null)
					if (network.getEdge(vertices[0], vertices[5]) == null)
				network.addEdge(vertices[0], vertices[5]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[5], vertices[4]) == null)
					network.addEdge(vertices[5], vertices[4]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[5], vertices[1]) == null)
					network.addEdge(vertices[5], vertices[1]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[5], vertices[6]) == null)
					network.addEdge(vertices[5], vertices[6]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[5], vertices[7]) == null)
					network.addEdge(vertices[5], vertices[7]);
				break;				
			case 6:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[6]) == null)
					network.addEdge(vertices[11], vertices[6]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[6], vertices[5]) == null)
				network.addEdge(vertices[6], vertices[5]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[6], vertices[7]) == null)
					network.addEdge(vertices[6], vertices[7]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[6], vertices[4]) == null)
					network.addEdge(vertices[6], vertices[4]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[6], vertices[10]) == null)
					network.addEdge(vertices[6], vertices[10]);
				break;
			case 7:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[7]) == null)
					network.addEdge(vertices[11], vertices[7]);
				if (vertices[5] != null)
					if (network.getEdge(vertices[7], vertices[5]) == null)
					network.addEdge(vertices[7], vertices[5]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[7], vertices[6]) == null)
				network.addEdge(vertices[7], vertices[6]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[7], vertices[8]) == null)
					network.addEdge(vertices[7], vertices[8]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[7], vertices[1]) == null)
					network.addEdge(vertices[7], vertices[1]);
				break;			
			case 8:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[8]) == null)
					network.addEdge(vertices[11], vertices[8]);
				if (vertices[1] != null)
					if (network.getEdge(vertices[8], vertices[1]) == null)
					network.addEdge(vertices[8], vertices[1]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[8], vertices[7]) == null)
					network.addEdge(vertices[8], vertices[7]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[8], vertices[9]) == null)
					network.addEdge(vertices[8], vertices[9]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[8], vertices[2]) == null)
					network.addEdge(vertices[8], vertices[2]);
				break;
			case 9:
				if (vertices[11] != null)
					if (network.getEdge(vertices[11], vertices[9]) == null)
					network.addEdge(vertices[11], vertices[9]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[9], vertices[3]) == null)
					network.addEdge(vertices[9], vertices[3]);
				if (vertices[2] != null)
					if (network.getEdge(vertices[9], vertices[2]) == null)
					network.addEdge(vertices[9], vertices[2]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[9], vertices[8]) == null)
					network.addEdge(vertices[9], vertices[8]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[9], vertices[10]) == null)
					network.addEdge(vertices[9], vertices[10]);
				break;
			case 10:
				if (vertices[11] != null)
					if (network.getEdge(vertices[10], vertices[11]) == null)
					network.addEdge(vertices[10], vertices[11]);
				if (vertices[3] != null)
					if (network.getEdge(vertices[10], vertices[3]) == null)
					network.addEdge(vertices[10], vertices[3]);
				if (vertices[4] != null)
					if (network.getEdge(vertices[10], vertices[4]) == null)
					network.addEdge(vertices[10], vertices[4]);
				if (vertices[6] != null)
					if (network.getEdge(vertices[10], vertices[6]) == null)
					network.addEdge(vertices[10], vertices[6]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[10], vertices[9]) == null)
					network.addEdge(vertices[10], vertices[9]);
				break;
			case 11:
				if (vertices[6] != null)
					if (network.getEdge(vertices[6], vertices[11]) == null)
					network.addEdge(vertices[6], vertices[11]);
				if (vertices[7] != null)
					if (network.getEdge(vertices[11], vertices[7]) == null)
					network.addEdge(vertices[11], vertices[7]);
				if (vertices[8] != null)
					if (network.getEdge(vertices[11], vertices[8]) == null)
					network.addEdge(vertices[11], vertices[8]);
				if (vertices[9] != null)
					if (network.getEdge(vertices[11], vertices[9]) == null)
					network.addEdge(vertices[11], vertices[9]);
				if (vertices[10] != null)
					if (network.getEdge(vertices[11], vertices[10]) == null)
					network.addEdge(vertices[11], vertices[10]);
				break;
				}
		}
	}
	@Override
	public void checkNeighbors() {
		ContinuousSpace space = getSpace();//(ContinuousSpace)getTheContext().getProjection("nucleus");
		ContinuousWithin contlist = new ContinuousWithin(space,this,factor*2);
		Iterable list = contlist.query();
		for (Object obj:list) {
			if (obj instanceof VP1) {
				VP1 vp1 = (VP1)obj;
				int i = findFreeVertex(vp1);
				if (i > -1  && vp1.getGenome() == null) {
					NdPoint locvp1 = space.getLocation(vp1);
					NdPoint locg = space.getLocation(this);
					NdPoint pt = new NdPoint((locg.getX()+factor*coord[i][0]),
							(locg.getY()+factor*coord[i][1]),
							(locg.getZ()+factor*coord[i][2]));
					double d = space.getDistance(locg, pt);
					double dist = space.getDistance(pt, locvp1);
					System.out.println("("+pt.getX()+", "+pt.getY()+", "+pt.getZ()+")");
					System.out.println("("+locvp1.getX()+", "+locvp1.getY()+", "+locvp1.getZ()+")");
					System.out.println("d = "+d+", dist = "+dist);
					if (dist <= 1) {
						double newd[] = {0,0,0};
						newd[0] = factor*coord[i][0]+locg.getX()-locvp1.getX();
						newd[1] = factor*coord[i][1]+locg.getY()-locvp1.getY();
						newd[2] = factor*coord[i][2]+locg.getZ()-locvp1.getZ();
						space.moveByDisplacement(vp1, newd);
						vertices[i] = vp1;
						vp1.setGenome(this);
						vp1.setStop(true);
						setEdges(i);
					}
				}
		}
	}
	}
}
