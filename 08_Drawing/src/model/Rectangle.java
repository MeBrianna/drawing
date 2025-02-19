package model;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Brianna Yuki

public class Rectangle extends PaintObject {

	public Rectangle(Color color, Point2D from, Point2D to) {
		super(color, from, to);
	}

	@Override
	public void draw(GraphicsContext gc) {
		int x1 = Math.min((int) to.getX(), (int) from.getX());
		int x2 = Math.max((int) to.getX(), (int) from.getX());
		int y1 = Math.min((int) to.getY(), (int) from.getY());
		int y2 = Math.max((int) to.getY(), (int) from.getY());

		gc.setFill(color);
		gc.fillRect(x1, y1, x2 - x1, y2 - y1);
	}

}
