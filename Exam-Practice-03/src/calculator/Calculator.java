package calculator;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.awt.Toolkit;
import java.awt.Dimension;
public class Calculator extends Application {	
		private TextField outputField = new TextField("Output");
		private class calculatorButton extends Button{			
			public calculatorButton(String function){
				super();
				this.setText(function);
				this.setTextAlignment(TextAlignment.CENTER);
				this.setStyle("-fx-border-width:1px; -fx-border-color:  #777777;"
						+ "-fx-border-width: 1px;"
						+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #eeeeee, #dddddd);"
						+ "-fx-effect: dropshadow( one-pass-box , #EEEEEE , 2 , 0.0 , 2 , 0 );");
				this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			}
		}
		
		@Override // Override the start method in the Application class
		public void start(Stage primaryStage) {
			
			Dimension windowSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			windowSize.setSize((windowSize.getWidth()*.25)*1.25, windowSize.getWidth()*.25);
			primaryStage.setResizable(false);		
			
			
			VBox root = new VBox();
			root.setPadding(new Insets(5,5,5,5));
			
			// Create UI
			GridPane gridPane = new GridPane();
			gridPane.setHgap(5);
			gridPane.setVgap(5);
		
			String[] buttonLabels = {"1","2","3","+","4","5","6","-","7","8","9","*","0","C","CE","/"};
			calculatorButton[] calculatorButtons = new calculatorButton[buttonLabels.length];
			for(int i = 0; i < calculatorButtons.length;i++) {
				calculatorButtons[i] = new calculatorButton(buttonLabels[i]);
			}
			// Set properties for UI
			gridPane.setAlignment(Pos.CENTER);
			
			//GridPane.setHalignment(btCalculate, HPos.RIGHT);

			gridPane.addRow(0, Arrays.copyOfRange(calculatorButtons,0, 4));
			gridPane.addRow(1, Arrays.copyOfRange(calculatorButtons,4, 8));
			gridPane.addRow(2, Arrays.copyOfRange(calculatorButtons,8, 12));
			gridPane.addRow(3, Arrays.copyOfRange(calculatorButtons,12, 16));
			gridPane.setPrefHeight(windowSize.height);
			
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			rc.setValignment(VPos.CENTER);
			rc.setFillHeight(true);
			ColumnConstraints cc = new ColumnConstraints();
			cc.setHgrow(Priority.ALWAYS);
			cc.setHalignment(HPos.CENTER);

			// A set of constraints should be defined for each column and row
			for (int i = 0; i < gridPane.getColumnCount(); i++) {
				gridPane.getColumnConstraints().add(cc);
			}
			for (int i = 0; i < gridPane.getRowCount(); i++) {
				gridPane.getRowConstraints().add(rc);
			}
			
			
			Font customFont = new Font("Calibri",16);
			outputField.setFont(customFont);
			outputField.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #FFF, #EEE);"
								+ "-fx-border-color:  #777777;"
								+ "-fx-border-width: 1px;");	
			root.getChildren().addAll(outputField,gridPane);
			VBox.setMargin(outputField, new Insets(5,0,5,0));
			// Process events
		
			gridPane.getChildren().stream().forEach(e -> e.onMouseClickedProperty().set(f->this.buttonAction(((calculatorButton)e).getText())));
			
			// Create a scene and place it in the stage
			Scene scene = new Scene(root, windowSize.getWidth(), windowSize.getHeight());
			primaryStage.setTitle("Calculator"); // Set title
			primaryStage.setScene(scene); // Place the scene in the stage
			primaryStage.show(); // Display the stage
		}

		public void buttonAction(String button) {
			System.out.print(button);
		}

		/**
		 * The main method is only needed for the IDE with limited
		 * JavaFX support. Not needed for running from the command line.
		 */
		public static void main(String[] args) {
			launch(args);
		}
}
