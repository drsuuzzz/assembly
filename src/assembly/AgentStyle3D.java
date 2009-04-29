package assembly;

import java.awt.Color;
import java.awt.Font;
import java.util.Iterator;

import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;
import com.sun.j3d.utils.picking.PickTool;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.AppearanceFactory.PolygonDraw;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;
import repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition;

public class AgentStyle3D implements Style3D {

	private int N = 37;

	public TaggedAppearance getAppearance(Object obj, TaggedAppearance appearance, Object shapeID) {
		if (appearance == null) {
			appearance = new TaggedAppearance();
		}
		if (obj instanceof VP1) {
			double vpradius = (Double)RunEnvironment.getInstance().getParameters().getValue("distance");
			double vperr = (Double)RunEnvironment.getInstance().getParameters().getValue("distanceThreshold");
			ContinuousWithin list = new ContinuousWithin(((VP1) obj).getSpace(),obj, (vpradius+vperr));
			Iterator l = list.query().iterator();
			int size=0;
			while(l.hasNext()) {
				l.next();
				size++;
			}
			//PolygonAttributes polyatt = new PolygonAttributes();
			//polyatt.setCullFace(PolygonAttributes.CULL_NONE);
			//appearance.getAppearance().setPolygonAttributes(polyatt);
			//AppearanceFactory.setPolygonAppearance(appearance.getAppearance(), PolygonDraw.FILL);
			if (size == 5) {
				AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.magenta);

			} else {
				AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.blue);
			}
		} else if (obj instanceof VP2) {
			AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.magenta);
		} else if (obj instanceof VP3) {
			AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.green);
		} else if (obj instanceof Genome) {
			AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.yellow);
		} else {
			AppearanceFactory.setMaterialAppearance(appearance.getAppearance(), Color.white);
		}
		
		return appearance;
	}
	
	public Point3f[] getPentagonVertices(float factor) {

		Point3f vertices[] = new Point3f[N];
		
		//s1, s2, c1, c2 from mathworld.wolfram.com/Pentagon.html
		float s0 = 0.0f*factor;
		float c0 = 1.0f*factor;
		float s1 = 0.95f*factor; //1/4*(Math.sqrt(10+2*Math.sqrt(5))); 
		float c1 = 0.3f*factor; //1/4*(Math.sqrt(5)-1); //0.308017
		float s2 = 0.6f*factor; //1/4*Math.sqrt(10-2*Math.sqrt(5)); 
		float c2 = 0.8f*factor; //1/4*(Math.sqrt(5)+1);	
		float z = 0.3f*factor;
		
		vertices[0] = new Point3f(s0,c0,z);
		vertices[1] = new Point3f(-s1,c1,z);
		vertices[2]	= new Point3f(-s2,-c2,z);
		vertices[3] = new Point3f(s2,-c2,z);
		vertices[4] = new Point3f(s1,c1,z);
		vertices[5] = new Point3f(s0,c0,z);
		
		vertices[6] = new Point3f(s0,c0,-z);
		vertices[7] = new Point3f(s1,c1,-z);
		vertices[8] = new Point3f(s2,-c2,-z);
		vertices[9] = new Point3f(-s2,-c2,-z);
		vertices[10] = new Point3f(-s1,c1,-z);
		vertices[11] = new Point3f(s0,c0,-z);
		
		vertices[12] = new Point3f(s2,-c2,-z);
		vertices[13] = new Point3f(s1,c1,-z);
		vertices[14] = new Point3f(s1,c1,z);
		vertices[15] = new Point3f(s2,-c2,z);
		vertices[16] = new Point3f(s2,-c2,-z);
		
		vertices[17] = new Point3f(s1,c1,-z);
		vertices[18] = new Point3f(s0,c0,-z);
		vertices[19] = new Point3f(s0,c0,z);
		vertices[20] = new Point3f(s1,c1,z);
		vertices[21] = new Point3f(s1,c1,-z);
		
		vertices[22] = new Point3f(s0,c0,-z);
		vertices[23] = new Point3f(-s1,c1,-z);
		vertices[24] = new Point3f(-s1,c1,z);
		vertices[25] = new Point3f(s0,c0,z);
		vertices[26] = new Point3f(s0,c0,-z);
		
		vertices[27] = new Point3f(-s1,c1,-z);
		vertices[28] = new Point3f(-s2,-c2,-z);
		vertices[29] = new Point3f(-s2,-c2,z);
		vertices[30] = new Point3f(-s1,c1,z);
		vertices[31] = new Point3f(-s1,c1,-z);
		
		vertices[32] = new Point3f(-s2,-c2,-z);
		vertices[33] = new Point3f(s2,-c2,-z);
		vertices[34] = new Point3f(s2,-c2,z);
		vertices[35] = new Point3f(-s2,-c2,z);
		vertices[36] = new Point3f(-s2,-c2,-z);		

		return vertices;
	}
	
	public Shape3D createPentagon(float factor, Object id){
		//blue
		Color3f blue = new Color3f(0.0f,0.0f,1.0f);
		
		GeometryInfo geom = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		Point3f vertices[] = getPentagonVertices(factor);
		
		geom.setCoordinates(vertices);
		int[] stripCounts = {6,6,5,5,5,5,5};
		geom.setStripCounts(stripCounts);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geom);
		Stripifier st = new Stripifier();
		st.stripify(geom);
		Shape3D shape = new Shape3D();
		shape.setGeometry(geom.getGeometryArray());
		//if (!(pentagon.isLive() || pentagon.isCompiled())) {
		//	PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
		//}
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
		shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
		//shape.setCapability(Shape3D.ENABLE_PICK_REPORTING);

		shape.setUserData(id);
		return shape;
	}
	
	public TaggedBranchGroup getBranchGroup(Object obj, TaggedBranchGroup group) {
		if (group == null || group.getTag() == null) {
			group = new TaggedBranchGroup("DEFAULT");
			Shape3D shape = null;
			if (obj instanceof VP1) {
				//shape = createPentagon(0.03f,"DEFAULT");
				shape = ShapeFactory.createSphere(0.03f, "DEFAULT");
			} else if (obj instanceof VP2  || obj instanceof VP3) {
				shape = ShapeFactory.createCone(0.03f, 0.06f, "DEFAULT");
			}
			else {
				shape = ShapeFactory.createSphere(0.03f, "DEFAULT");
			}
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			group.getBranchGroup().addChild(shape);

			return group;
		}
		return null;
	}

	public String getLabel(Object obj, String currentLabel) {
		return null;
	}

	public Color getLabelColor(Object obj, Color currentColor) {
		return Color.green;
	}

	public Font getLabelFont(Object obj, Font currentFont) {
		return null;
	}

	public float getLabelOffset(Object obj) {
		return 0.035f;
	}

	public repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition getLabelPosition(
			Object obj,
			repast.simphony.visualization.visualization3D.style.Style3D.LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float[] getRotation(Object obj) {
		return null;
	}

	public float[] getScale(Object obj) {
		return null;
	}

}
