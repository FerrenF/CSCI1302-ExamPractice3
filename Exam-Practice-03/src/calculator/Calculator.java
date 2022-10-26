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
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.awt.Dimension;
public class Calculator extends Application {	
	
		private boolean debug = true;
		private void dbg(String m) {
			if(!debug) {
				return;
			}
			System.out.println(m);
		}
		private calculatorOutputField outputField = new calculatorOutputField();
		
		
		private class calculatorOutputField extends TextField{
			private final String styleNormal = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #FFF, #EEE);"
					+ "-fx-border-color:  #777777;"
					+ "-fx-border-width: 1px;";
			private final int maxLength = 255; //arbitrary
			private boolean needsClear = true;
			private boolean systemOutput = false;
			private String savedText = "";
			public calculatorOutputField() {
				super();
				this.systemSetText("Output");
				
				Font customFont = new Font("Calibri",16);
				this.setFont(customFont);
				this.setStyle(styleNormal);	
				this.setOnMouseClicked(e->{
					if(needsClear){
						this.setText(this.savedText);
						savedText="";
						needsClear=false;					
						}
					});
				this.textProperty().addListener((obj, oldVal, newVal) ->{
					if(systemOutput) {
						needsClear=true;
						systemOutput=false;
						return;
					}
					if(needsClear){
						this.setText(this.savedText);
						savedText="";
						needsClear=false;					
					}
					StringBuilder newText = new StringBuilder();
					if(newVal.length()>=this.maxLength) {
						newVal = newVal.substring(0, maxLength);
					}
					char[] allowedCharacters = {'0','1','2','3','4','5','6','7','8','9','0','/','*','+','-',' ','.'};
					java.util.Arrays.sort(allowedCharacters);
					for(int i=0;i<newVal.length();i++) {
						if(java.util.Arrays.binarySearch(allowedCharacters, newVal.charAt(i))>-1) {
							newText.append(newVal.charAt(i));
						}
						else
						{
							System.out.println("Invalid character: "+newVal.charAt(i));
						}
					}	
					newVal = newText.toString();
					this.setText(newVal);					
				});
			}
			public void systemSetText(String text) {
				savedText=this.getText();
				systemOutput=true;
				this.setText(text);				
			}
			public void buttonInput(String function) {
				if(needsClear || function == "C") {
					this.setText("");
					needsClear=false;
					if(function == "C") {
						return;
					}
				}
				if(function == "=") {
					calculateOutput();
					return;
				}
				String currentText = this.getText()+function;							
				this.setText(currentText);
			}
			private void calculateOutput() {
				String[] steps = this.getText().split("((?=:|\\+|\\-|\\/|\\*)|(?<=:|\\+|\\-|\\/|\\*))");
				BigDecimal runningTotal = BigDecimal.ZERO;
				dbg("Performing steps:" + Arrays.toString(steps));
				try {
				
					String operator = "";
					double targetValue = 0;
					for(int i = 0; i < steps.length;i++) {
						String target = steps[i];
						switch(target) {
							case "+":
								if(operator=="-") {
									operator = "-";
									break;
								}
								else if(operator != "") {
									throw new java.lang.Exception("Operator error.");
								}
								operator = target;
								break;
							case "-":
								if(operator=="-") {
									operator = "+";
									break;
								}
								else if(operator != "") {
									throw new java.lang.Exception("Operator error.");
								}
								operator = target;
								break;
							case "/":
								if(operator != "") {
									throw new java.lang.Exception("Operator error.");
								}
								operator = target;
								break;
							case "*":
								if(operator != "") {
									throw new java.lang.Exception("Operator error.");
								}
								operator = target;
								break;
							default:
								BigDecimal v = BigDecimal.valueOf(Double.valueOf(target));
								switch(operator) {
									case "+":
										runningTotal=runningTotal.add(v);
										break;
									case "-":
										runningTotal=runningTotal.subtract(v);
										break;
									case "/":
										if(v.doubleValue() == 0) {
											throw new ArithmeticException("Divide by 0 ");
										}
										runningTotal=runningTotal.divide(v);
										break;
									case "*":
										runningTotal=runningTotal.multiply(v);
										break;
									default:
										runningTotal=v;
								}
								operator="";							
						}						
					}
					this.setText(String.valueOf(runningTotal));
				}
				catch(ArithmeticException e) {
					String m = "Arithmetic Error: " + e.getMessage();
					dbg(m);
					this.systemSetText(m);
				}
				catch(Exception e) {
					String m = "Exception: " + e.getMessage();
					dbg(m);
					this.systemSetText(m);
				}
				
			}
		}
		private class calculatorButton extends Button{	
			private final String styleNormal = "-fx-border-width:1px; -fx-border-color:  #777777;"
					+ "-fx-border-width: 1px;"
					+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #eeeeee, #dddddd);"
					+ "-fx-effect: dropshadow( one-pass-box , #EEEEEE , 2 , 0.0 , 2 , 0 );";
			private final String styleHover = "-fx-border-width:1px; -fx-border-color:  #999;"
					+ "-fx-border-width: 1px;"
					+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ddd, #eee);"
					+ "-fx-effect: dropshadow( one-pass-box , #EEEEEE , 2 , 0.0 , 2 , 0 );";
			public calculatorButton(String function){
				super();
				this.setText(function);
				this.setTextAlignment(TextAlignment.CENTER);
				this.setStyle(styleNormal);
				this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				
				this.onMouseEnteredProperty().set( e->this.setStyle(styleHover));
				this.onMouseExitedProperty().set(e->this.setStyle(styleNormal));
		
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
		
			String[] buttonLabels = {"1","2","3","+","4","5","6","-","7","8","9","*","0","=","C","/"};
			calculatorButton[] calculatorButtons = new calculatorButton[buttonLabels.length];
			for(int i = 0; i < calculatorButtons.length;i++) {
				calculatorButtons[i] = new calculatorButton(buttonLabels[i]);
			}
	
			gridPane.setAlignment(Pos.CENTER);
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
			
			
			
			
			root.getChildren().addAll(outputField,gridPane);
			VBox.setMargin(outputField, new Insets(5,0,5,0));
			// Process events
		
			gridPane.getChildren().stream().forEach(e -> e.onMouseClickedProperty().set(f->this.outputField.buttonInput(((calculatorButton)e).getText())));
			
			// Create a scene and place it in the stage
			Scene scene = new Scene(root, windowSize.getWidth(), windowSize.getHeight());
			primaryStage.setTitle("Calculator"); // Set title
			primaryStage.setScene(scene); // Place the scene in the stage
			primaryStage.show(); // Display the stage
		}

		/**
		 * The main method is only needed for the IDE with limited
		 * JavaFX support. Not needed for running from the command line.
		 */
		public static void main(String[] args) {
			launch(args);
		}
}
