package assembly;

import java.util.Iterator;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.continuous.NdPoint;

public class MRNA extends AgentExtendCont {
	
	public enum MType {Tag, tag, vp1, vp2, vp3};	
	private MType mtype;
	public enum mState {early, late, complete};
	private mState state;
	private double moveTick;
	private double deathTick;
	private double exTick;
	private double spliceTick;
	private static int id = 0;
	private int myid;
	
	public MRNA() {
		super();
		moveTick = 0;
		deathTick = 0;
		exTick = 0;
		spliceTick = 0;
		setName("mRNA");
		mtype = MType.Tag;
		setLocation(Loc.nucleus);
		state = mState.early;
		id++;
		myid = id;
		System.out.println("MRNA"+myid);
	}
	@Parameter(usageName="mtype",displayName="MRNA Type", converter = "assembly.MTypeConverter")
	public MType getMType() {
		return mtype;
	}
	
	public void setMType(MType type) {
		this.mtype = type;
	}
	@Parameter(usageName="state",displayName = "MRNA State", converter = "assembly.MStateConverter")
	public mState getState() {
		return state;
	}

	public void setState(mState state) {
		this.state = state;
	}

	public int getMyid() {
		return myid;
	}

	public void move() {
		if (!isDead()) {
			double tick = RepastEssentials.GetTickCount();
			if (tick > moveTick) {

				double disp[] = calcDispIfCenter(Ribosome.class,Ribosome.class,MRNA.class,MRNA.class);
				if (disp[0] == 0.0f && disp[1] == 0.0f && disp[2] == 0.0f) {
					randomWalk();
				} else {
					getSpace().moveByDisplacement(this, disp);
				}
				moveTick=tick;
			}
		}
		
	}
	
	public void splice() {
		double tick = RepastEssentials.GetTickCount();
		if (tick > spliceTick) {
			if (state == mState.early) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < .2) {
					setMType(MType.tag);
					state = mState.complete;
				} else if (rand < .8) {
					setMType(MType.Tag);
					state = mState.complete;
				}
				
			} else if (state == mState.late) {
				double rand = RandomHelper.nextDoubleFromTo(0.0, 1.0);
				if (rand < .2) {
					int rand2 = RandomHelper.nextIntFromTo(0, 1);
					if (rand == 0) {
						setMType(MType.vp2);
					} else {
						setMType(MType.vp3);
					}
					state = mState.complete;
				} else if (rand < .8) {
					setMType(MType.vp1);
					state = mState.complete;
				}
			}
			spliceTick = tick;
		}
	}
	
	public void export() {
		double tick = RepastEssentials.GetTickCount();
		if (!isDead()) {
			if (tick > exTick) {
				if (getLocation() == Loc.nucleus  && !isMoving()  && state == mState.complete) {
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
					if (rand < 0.001) {
						this.die();
					}
				}
				deathTick = tick;
			}
		}
	}
}
