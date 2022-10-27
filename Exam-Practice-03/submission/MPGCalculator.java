

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

		VBox vb1 = new VBox(20);
		HBox hb1 = new HBox(10);
		HBox hb2 = new HBox(50);

		vb1.getChildren().addAll(hb1, hb2);

		// how to make scale with screensize
		// Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
		// double scaleRatio = 1/5.0;
		// vb1.setMaxWidth((int)(d1.getWidth()*scaleRatio));

		hb1.setStyle(" -fx-padding: 5px;");
		hb2.setStyle("-fx-padding: 5px;");
		hb2.setAlignment(Pos.CENTER);
		hb1.setAlignment(Pos.CENTER);

		// make labels and textfields
		hb1.getChildren().add(lbMiles);
		hb1.getChildren().add(tfMiles);
		hb1.getChildren().add(lbGallons);
		hb1.getChildren().add(tfGallons);

		hb2.getChildren().add(btCalculate);
		hb2.getChildren().add(lbOutput);

		// action events
		btCalculate.setOnAction(e -> calculateMPG());
		tfGallons.setOnAction(e -> calculateMPG());
		tfMiles.setOnAction(e -> calculateMPG());

		// Create a scene and place it in the stage.
		// Not specifying values forces the form to auto-size.
		Scene scene = new Scene(vb1);

		// This way, we don't actually have to program this feature.
		primaryStage.setResizable(false);

		primaryStage.setTitle("MPGCalculator"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		// hb1.maxWidthProperty().bind(primaryStage.maxWidthProperty().multiply(0.5));
	}

	private void calculateMPG() {

		boolean error = false;
		String outputText = "0";

		try {

			double miles = Double.parseDouble(tfMiles.getText());
			double gallons = Double.parseDouble(tfGallons.getText());

			if (gallons <= 0) {
				error = true;
				outputText = "Gallons used must be greater than 0.";
			} else if (miles == 0) {
				outputText = "Distance travelled must be greater than 0.";
				error = true;
			} else if (gallons == 1) {
				outputText = String.format("%.2f Miles Per Gallon", miles / gallons);
			} else {
				outputText = String.format("%.2f Miles Per Gallons", miles / gallons);
			}
		} catch (Exception e) {
			error = true;
			outputText = "Invalid input.";
		}
		finally {
			lbOutput.setText(outputText);
			tfGallons.setText("");
			tfMiles.setText("");
		}
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
