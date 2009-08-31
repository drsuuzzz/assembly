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

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.visualization.decorator.AbstractProjectionDecorator;
import repast.simphony.visualization.decorator.ProjectionDecorator3D;
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.Display3D;
import repast.simphony.visualization.visualization3D.ShapeFactory;

public class MyContinuous3DProjectionDecorator extends
		AbstractProjectionDecorator<ContinuousSpace> implements
		ProjectionDecorator3D<ContinuousSpace> {
	
	  public static Shape3D createWireframeCircle(float r, float r2, float cx, float cy, float cz, Color color, Object id) {
		  int N = 80;
		  int points = 6*N;
		  int n;
		  double a;
		  int[] strips = {N,N,N,N,N,N};
		  LineStripArray sphere = new LineStripArray(points, LineStripArray.COORDINATES | LineStripArray.COLOR_3,strips);
		  Color3f colors = new Color3f(color);
		  for(a = 0, n = 0; n < N; a = 2.0*Math.PI/(N-1) * ++n) {
			  double x = r * Math.cos(a) + cx;
			  double y = r * Math.sin(a) + cy;
			  double z = 0 + cz;
			  sphere.setCoordinate(n, new Point3d(x,y,z));
			  sphere.setColor(n,colors);
		  }
		  
		  for (a = 0, n = 0; n <N; a = 2.0*Math.PI/(N-1)*++n) {
			  double x = r*Math.cos(a) + cx;
			  double y = cy;
			  double z = r * Math.sin(a) + cz;
			  sphere.setCoordinate(n+N, new Point3d(x,y,z));
			  sphere.setColor(n+N, colors);
		  }
		  
		  for (a = 0, n = 0; n <N; a = 2.0*Math.PI/(N-1)*++n) {
			  double x = cx;
			  double y = r*Math.cos(a) + cy;
			  double z = r * Math.sin(a) + cz;
			  sphere.setCoordinate(n+2*N, new Point3d(x,y,z));
			  sphere.setColor(n+2*N, colors);
		  }
		  for(a = 0, n = 0; n < N; a = 2.0*Math.PI/(N-1) * ++n) {
			  double x = r2 * Math.cos(a) + cx;
			  double y = r2 * Math.sin(a) + cy;
			  double z = 0 + cz;
			  sphere.setCoordinate(n+3*N, new Point3d(x,y,z));
			  sphere.setColor(n+3*N,colors);
		  }
		  
		  for (a = 0, n = 0; n <N; a = 2.0*Math.PI/(N-1)*++n) {
			  double x = r2*Math.cos(a) + cx;
			  double y = cy;
			  double z = r2 * Math.sin(a) + cz;
			  sphere.setCoordinate(n+4*N, new Point3d(x,y,z));
			  sphere.setColor(n+4*N, colors);
		  }
		  
		  for (a = 0, n = 0; n <N; a = 2.0*Math.PI/(N-1)*++n) {
			  double x = cx;
			  double y = r2*Math.cos(a) + cy;
			  double z = r2 * Math.sin(a) + cz;
			  sphere.setCoordinate(n+5*N, new Point3d(x,y,z));
			  sphere.setColor(n+5*N, colors);
		  }			  
		  
		    Shape3D shape = new Shape3D(sphere);

		    shape.setUserData(id);

		    return shape;
		  }
	  
	public void init(Display3D display, Group parentGroup) {
		ContinuousSpace space = (ContinuousSpace) projection;
		float width = (float) space.getDimensions().getWidth() * unitSize/2;
		float height = (float) space.getDimensions().getHeight() * unitSize/2;
		float depth = (float) space.getDimensions().getDimension(2) * unitSize/2;
		float r = (Integer)RunEnvironment.getInstance().getParameters().getValue("nucleusRadius") * unitSize;
		float r2 = (Integer)RunEnvironment.getInstance().getParameters().getValue("cellRadius") * unitSize;

		if (width == Float.NEGATIVE_INFINITY || height == Float.NEGATIVE_INFINITY
						|| depth == Float.NEGATIVE_INFINITY) {
			Shape3D axes = ShapeFactory.createAxes(width, height, depth, 1000, color, "axes");
			axes.setPickable(false);
			parentGroup.addChild(axes);
		} else {
			//Shape3D boundingBox = ShapeFactory.createWireframeBox(width, height, depth, color,
				//			"boundingbox");
			Shape3D boundingBox = createWireframeCircle(r, r2, width, height, depth, color, "boundingbox");
			Appearance app = AppearanceFactory.setColoredAppearance(
							boundingBox.getAppearance(), color);
			boundingBox.setAppearance(app);
			boundingBox.setPickable(false);
			parentGroup.addChild(boundingBox);
		}
	}

	public void update() {}
}
