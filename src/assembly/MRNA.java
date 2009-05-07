package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.NdPoint;

public class MRNA extends AgentExtendCont {
	
	private double moveTick;
	private double deathTick;
	private double exTick;
	private static int id = 0;
	private int myid;
	
	public MRNA() {
		super();
		moveTick = 0;
		deathTick = 0;
		exTick = 0;
		setName("mRNA");
		setMType(MType.Tag);
		setLocation(Loc.nucleus);
		id++;
		myid = id;
		System.out.println("MRNA"+myid);
	}

	public int getMyid() {
		return myid;
	}

	public void move() {
		if (!isDead()) {
			double tick = RepastEssentials.GetTickCount();
			if (tick > moveTick) {
				/*double distance = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBind");
				double err = (Double) RunEnvironment.getInstance().getParameters().getValue("distanceBindError");
				ContinuousWithin cw = new ContinuousWithin(getSpace(), this, (distance+err));
				Iterator<AgentExtendCont> list =cw.query().iterator();
				int count = 0;
				double align[] = {0.0,0.0,0.0};
				while(list.hasNext()) {
					AgentExtendCont aec = list.next();
					if (aec instanceof Ribosome) {
						align[0] += aec.getX();
						align[1] += aec.getY();
						align[2] += aec.getZ();
						count++;
					}
				}
				if (count > 0) {
					NdPoint thispt = getSpace().getLocation(this);
					align[0] = (align[0]/count - thispt.getX())/8;
					align[1] = (align[1]/count - thispt.getY())/8;
					align[2] = (align[2]/count - thispt.getZ())/8;
					AgentGeometry.trim(align, err/2);
					getSpace().moveByDisplacement(this, align);
					this.setX(align[0]);
					this.setY(align[1]);
					this.setZ(align[2]);
				} else {
					randomWalk();
				}*/
				double disp[] = calcDispIfCenter(Ribosome.class,MRNA.class,MRNA.class);
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
				} else {
					getSpace().moveByDisplacement(this, disp);
				}
				moveTick=tick;
			}
		}
		
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (!isDead()) {
			if (tick > exTick) {
				if (getLocation() == Loc.nucleus  && !isMoving()) {
					if (nearWall(getSpace().getLocation(this))) {
						double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
						if (rand < 0.4) {
						//remove from nucleus and add to cytoplasm
							((Nucleus)getTheContext()).getCell().addToMoveList(this);
							setMoving(true);
						}
					}
				}
				exTick = tick;
			}
		}
	}
	
	public void death() {
		if (!isDead()) {
			double tick = RepastEssentials.GetTickCount();
			if (tick > deathTick) {
				if (getLocation() == Loc.cytoplasm) {
					double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
					if (rand < 0.0001) {
						this.die();
					}
				}
				deathTick = tick;
			}
		}
	}
}
