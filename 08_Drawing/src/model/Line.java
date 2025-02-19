package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Brianna Yuki

public class Line extends PaintObject {

	public Line(Color color, Point2D from, Point2D to) {
		super(color, from, to);
	}

	@Override
	public void draw(GraphicsContext gc) {
		int x1 = (int) to.getX();
		int x2 = (int) from.getX();
		int y1 = (int) to.getY();
		int y2 = (int) from.getY();

		gc.setStroke(color);
		gc.strokeLine(x1, y1, x2, y2);
	}
}
