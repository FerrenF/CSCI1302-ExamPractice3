package mpgCalculator;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import java.awt.Toolkit;
//import java.awt.Dimension;

public class MPGCalculator extends Application {
	private TextField tfMiles = new TextField();
	private TextField tfGallons = new TextField();
	private Button btCalculate = new Button("Calculate");
	private Label lbOutput = new Label("Output");

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Create UI
		VBox vb1 = new VBox();
		HBox hb1 = new HBox();
		HBox hb2 = new HBox();
		//Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
		//double scaleRatio = 1/5.0;
		
		//vb1.setMaxWidth((int)(d1.getWidth()*scaleRatio));
		lbOutput.setMaxWidth(500);
		vb1.setStyle("-fx-border-width: 1px; -fx-border-color: black;");
		hb1.setStyle("-fx-border-width: 1px; -fx-border-color: red;");
		hb2.setStyle("-fx-border-width: 1px; -fx-border-color: blue;");
		hb2.setAlignment(Pos.TOP_LEFT);
		
		vb1.getChildren().addAll(hb1, hb2);
		
		hb2.setMaxWidth(500);

		hb1.getChildren().add(tfMiles);
		hb1.getChildren().add(tfGallons);
		hb2.getChildren().add(lbOutput);

		// Create a scene and place it in the stage
		Scene scene = new Scene(vb1, 400, 250);
		
		
		
		primaryStage.setTitle("MPGCalculator"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		hb1.setMaxWidth(primaryStage.getWidth()/2);
		hb1.maxWidthProperty().bind(primaryStage.maxWidthProperty().multiply(0.5));
	}

	private void calculateLoanPayment() {

	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
