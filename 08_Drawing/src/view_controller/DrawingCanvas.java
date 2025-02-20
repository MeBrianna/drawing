package view_controller;

import java.util.Vector;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Line;
import model.Oval;
import model.PaintObject;
import model.Picture;
import model.Rectangle;

/**
 * A GUI for NetPaint that has all PaintObjects drawn on it. This file also
 * represents the controller as it controls how paint objects are drawn and
 * sends new paint objects to the server. All Client objects also listen to the
 * server to read the Vector of paint objects and repaint every time any client
 * adds a new one.
 * 
 * @author Rick Mercer and Brianna Yuki
 * 
 */
public class DrawingCanvas extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	// Use Vector instead of ArrayList
	private Vector<PaintObject> allPaintObjects;

	enum CurrentPaintObject {
		LINE, RECTANGLE, OVAL, PICTURE
	}

	private BorderPane all = new BorderPane();
	private CurrentPaintObject currentShape = CurrentPaintObject.LINE;
	private Color currentColor = Color.RED;
	private boolean isDrawingShape = false;
	private GraphicsContext gc;
	private Point2D currentFrom;
	private Point2D currentTo;
	final static int CANVAS_WIDTH = 960;
	final static int CANVAS_HEIGHT = 600;

	@Override
	public void start(Stage primaryStage) throws Exception {
		all = new BorderPane();
		all.setPadding(new Insets(10, 0, 20, 0));
		// Put the drawing pane, a Canvas, into the center
		Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		setMouseHandlersOn(canvas);
		all.setCenter(canvas);
		layoutOptions();

		allPaintObjects = new Vector<PaintObject>();

		Scene scene = new Scene(all, 960, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void layoutOptions() {
		GridPane options = new GridPane();
		options.setHgap(20);

		ColorPicker colorPicker = new ColorPicker(Color.RED);
		colorPicker.setOnAction(event -> {
			currentColor = colorPicker.getValue();
		});

		ToggleGroup shapeOptions = new ToggleGroup();

		RadioButton lineButton = new RadioButton("Line");
		RadioButton ovalButton = new RadioButton("Oval");
		RadioButton rectangleButton = new RadioButton("Rectangle");
		RadioButton pictureButton = new RadioButton("Picture");

		lineButton.setSelected(true);

		shapeOptions.selectedToggleProperty().addListener(event -> {
			RadioButton selected = (RadioButton) shapeOptions.getSelectedToggle();
			if (selected == lineButton) {
				currentShape = CurrentPaintObject.LINE;
			} else if (selected == ovalButton) {
				currentShape = CurrentPaintObject.OVAL;
			} else if (selected == rectangleButton) {
				currentShape = CurrentPaintObject.RECTANGLE;
			} else if (selected == pictureButton) {
				currentShape = CurrentPaintObject.PICTURE;
			}
		});

		lineButton.setToggleGroup(shapeOptions);
		ovalButton.setToggleGroup(shapeOptions);
		rectangleButton.setToggleGroup(shapeOptions);
		pictureButton.setToggleGroup(shapeOptions);

		options.add(lineButton, 0, 0);
		options.add(ovalButton, 1, 0);
		options.add(rectangleButton, 2, 0);
		options.add(pictureButton, 3, 0);
		options.add(colorPicker, 4, 0);

		options.setAlignment(Pos.CENTER);

		all.setBottom(options);
	}

	private void setMouseHandlersOn(Canvas canvas) {
		canvas.setOnMouseClicked(event -> {
			if (isDrawingShape) {
				if (currentShape == CurrentPaintObject.LINE) {
					PaintObject newShape = new Line(currentColor, currentFrom, currentTo);
					allPaintObjects.add(newShape);
				} else if (currentShape == CurrentPaintObject.OVAL) {
					PaintObject newShape = new Oval(currentColor, currentFrom, currentTo);
					allPaintObjects.add(newShape);
				} else if (currentShape == CurrentPaintObject.RECTANGLE) {
					PaintObject newShape = new Rectangle(currentColor, currentFrom, currentTo);
					allPaintObjects.add(newShape);
				} else if (currentShape == CurrentPaintObject.PICTURE) {
					PaintObject newShape = new Picture(currentFrom, currentTo, "08_Drawing/doge.jpeg");
					allPaintObjects.add(newShape);
				}
				isDrawingShape = false;
			} else {
				currentFrom = new Point2D(event.getX(), event.getY());
				isDrawingShape = true;
			}
		});
		canvas.setOnMouseMoved(event -> {
			if (isDrawingShape) {
				// clear canvas
				gc.setFill(Color.WHITE);
				gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				// draw all paint objects
				for (PaintObject paintObject : allPaintObjects) {
					paintObject.draw(gc);
				}
				// draw current paint object
				currentTo = new Point2D(event.getX(), event.getY());
				if (currentShape == CurrentPaintObject.LINE) {
					PaintObject line = new Line(currentColor, currentFrom, currentTo);
					line.draw(gc);
				} else if (currentShape == CurrentPaintObject.OVAL) {
					PaintObject oval = new Oval(currentColor, currentFrom, currentTo);
					oval.draw(gc);
				} else if (currentShape == CurrentPaintObject.RECTANGLE) {
					PaintObject rectangle = new Rectangle(currentColor, currentFrom, currentTo);
					rectangle.draw(gc);
				} else if (currentShape == CurrentPaintObject.PICTURE) {
					PaintObject picture = new Picture(currentFrom, currentTo, "08_Drawing/doge.jpeg");
					picture.draw(gc);
				}
			}
		});
	}
}
