package assembly;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.ShapeFactory;

public class MyContinuous3DProjectionDecorator extends
		AbstractProjectionDecorator<ContinuousSpace> implements
		ProjectionDecorator3D<ContinuousSpace> {
	
	  public static Shape3D createWireframeCircle(float r, float cx, float cy, float cz, Color color, Object id) {
		  LineStripArray sphere = new LineStripArray(80, LineStripArray.COORDINATES | LineStripArray.COLOR_3);
		  double phi = 0;  //Math.pi/2;
		  double theta = 0;
		  for(int i = 0; i < 80; i++) {
			  theta = i*2*Math.PI/80;
			  double x = r*Math.cos(theta)*Math.sin(phi) + cx;
			  double y = r*Math.sin(theta)*Math.sin(phi) + cy;
			  double z = r*Math.cos(phi) + cz;
			  
			  sphere.setCoordinate(i, new Point3d(x,y,z));
		  }

		  //LineArray lineArray = new LineArray(24, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
		    //int i = 0;
		    // these are the axes
		    //for (int i == 0;)
		   // lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
		   // lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
		    //lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
		   // lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
		   // lineArray.setCoordinate(i++, new Point3f(0, 0, 0));
		   // lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));

		  //  lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
		   // lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
		    //lineArray.setCoordinate(i++, new Point3f(xdim, 0, 0));
		   // lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));

	//	    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
		//    lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
		  //  lineArray.setCoordinate(i++, new Point3f(xdim, ydim, 0));
		    //lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));

//		    lineArray.setCoordinate(i++, new Point3f(0, ydim, 0));
	//	    lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

//		    lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));
	//	    lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));
		//    lineArray.setCoordinate(i++, new Point3f(0, 0, zdim));
		  //  lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

//		    lineArray.setCoordinate(i++, new Point3f(xdim, 0, zdim));
	//	    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));

//		    lineArray.setCoordinate(i++, new Point3f(xdim, ydim, zdim));
	//	    lineArray.setCoordinate(i++, new Point3f(0, ydim, zdim));

		    Color3f colors = new Color3f(color);

		    for (int j = 0; j < 80; j++) {
		      sphere.setColor(j, colors);
		    }

		    Shape3D shape = new Shape3D(sphere);

		    shape.setUserData(id);

		    return shape;
		  }
	  
	public void init(Display3D display, Group parentGroup) {
		ContinuousSpace space = (ContinuousSpace) projection;
		float width = (float) space.getDimensions().getWidth() * unitSize;
		float height = (float) space.getDimensions().getHeight() * unitSize;
		float depth = (float) space.getDimensions().getDimension(2) * unitSize;

		if (width == Float.NEGATIVE_INFINITY || height == Float.NEGATIVE_INFINITY
						|| depth == Float.NEGATIVE_INFINITY) {
			Shape3D axes = ShapeFactory.createAxes(width, height, depth, 1000, color, "axes");
			axes.setPickable(false);
			parentGroup.addChild(axes);
		} else {
			Shape3D boundingBox = ShapeFactory.createWireframeBox(width, height, depth, color,
							"boundingbox");
			Appearance app = AppearanceFactory.setColoredAppearance(
							boundingBox.getAppearance(), color);
			boundingBox.setAppearance(app);
			boundingBox.setPickable(false);
			parentGroup.addChild(boundingBox);
		}
	}

	public void update() {}
}
