package mpgCalculator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import java.awt.Toolkit;
//import java.awt.Dimension;

/* 
 * Author: Joseph Berrigan, Hunter Williams
 * Date: 10/26/2022
 * Practice assignment 3
 * Name: MPGCalculator.java
 * Course: CSCI 1302
 * Description: GUI MPG calculator for practice assignment 3
 */
public class MPGCalculator extends Application {
	private TextField tfMiles = new TextField();
	private TextField tfGallons = new TextField();
	private Button btCalculate = new Button("Calculate");
	private Label lbOutput = new Label("Output");
	private Label lbMiles = new Label("Miles driven");
	private Label lbGallons = new Label("Gallons used");

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Create UI panes
		StackPane sp1 = new StackPane();
		VBox vb1 = new VBox(20);
		HBox hb1 = new HBox(10);
		HBox hb2 = new HBox(50);

		vb1.getChildren().addAll(hb1, hb2);

		// how to make scale with screensize
		// Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
		// double scaleRatio = 1/5.0;
		// vb1.setMaxWidth((int)(d1.getWidth()*scaleRatio));

		vb1.setStyle("-fx-border-width: 1px; -fx-border-color: black;");
		hb1.setStyle("-fx-border-width: 1px; -fx-border-color: red; -fx-padding: 10px;");
		hb2.setStyle("-fx-border-width: 1px; -fx-border-color: blue;");
		hb2.setAlignment(Pos.CENTER);
		hb1.setAlignment(Pos.CENTER);

		//make labels and textfields
		hb1.getChildren().add(lbMiles);
		hb1.getChildren().add(tfMiles);
		hb1.getChildren().add(lbGallons);
		hb1.getChildren().add(tfGallons);

		hb2.getChildren().add(btCalculate);
		hb2.getChildren().add(lbOutput);

		// action events
		btCalculate.setOnAction(e -> calculateMPG());

		// Create a scene and place it in the stage
		Scene scene = new Scene(vb1, 500, 250);

		primaryStage.setTitle("MPGCalculator"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		// hb1.maxWidthProperty().bind(primaryStage.maxWidthProperty().multiply(0.5));
	}

	private void calculateMPG() {
		double miles = Double.parseDouble(tfMiles.getText());
		double gallons = Double.parseDouble(tfGallons.getText());

		lbOutput.setText(String.format("%.2f Miles Per Gallons", miles / gallons));
		tfGallons.setText("");
		tfMiles.setText("");
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
