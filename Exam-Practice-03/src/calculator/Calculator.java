package calculator;

import java.util.Arrays;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.awt.Dimension;

public class Calculator extends Application {

	// Set to false to disable console debug output.
	private boolean debug = true;

	private void dbg(String m) {
		if (!debug) {
			return;
		}
		System.out.println(m);
	}

	private calculatorOutputField outputField = new calculatorOutputField();

	private class calculatorOutputField extends TextField {

		// class properties
		private final String styleNormal = "-fx-background-color: #FFF;"
				+ "-fx-border-color:  #777777;" + "-fx-border-width: 1px;";
		private final int maxLength = 255; // arbitrary
		private boolean needsClear = true;
		private boolean systemOutput = false;
		private String savedText = "";

		// Constructors;
		public calculatorOutputField() {
			super();
			this.systemSetText("Output");

			Font customFont = new Font("Calibri", 16);
			this.setFont(customFont);
			this.setStyle(styleNormal);
		
			// Event handlers
			this.setOnMouseClicked(e -> {
				if (needsClear) {
					this.setText(this.savedText);
					savedText = "";
					needsClear = false;
				}
			});
			this.textProperty().addListener((obj, oldVal, newVal) -> {
				if (systemOutput) {
					needsClear = true;
					systemOutput = false;
					return;
				}
				if (needsClear) {
					this.setText(this.savedText);
					savedText = "";
					needsClear = false;
				}

				StringBuilder newText = new StringBuilder();
				if (newVal.length() >= this.maxLength) {
					newVal = newVal.substring(0, maxLength);
				}

				for (int i = 0; i < newVal.length(); i++) {
					if (charAllowed(newVal.charAt(i))) {
						newText.append(newVal.charAt(i));
					} else if (newVal.charAt(i) == '=') {
						this.setText(newVal.substring(0, i));
						this.calculateOutput();
						return;
					} else {
						System.out.println("Invalid character: " + newVal.charAt(i));
					}
				}
				newVal = newText.toString();
				this.setText(newVal);
			});
			// end event handlers
		}

		// This sets the text of the input field, saving the previous value and
		// returning to it once the text field is cleared again. Great for error
		// messages.
		public void systemSetText(String text) {
			savedText = this.getText();
			systemOutput = true;
			this.setText(text);
		}

		// This method is triggered when a button on the main form is pressed, or a key
		// is pressed while the window is in focus.
		public void buttonInput(String function) {
			if (needsClear || function == "C") {
				this.setText("");
				needsClear = false;
				if (function == "C") {
					return;
				}
			}
			if (function == "=") {
				calculateOutput();
				return;
			}
			String currentText = this.getText() + function;
			this.setText(currentText);
		}

		// Instruct the input field to calculate the equation currently in it's text
		// area.
		private void calculateOutput() {
			String[] steps = this.getText().split("((?=:|\\+|\\-|\\/|\\*)|(?<=:|\\+|\\-|\\/|\\*))");
			BigDecimal runningTotal = BigDecimal.ZERO;
			dbg("Performing steps:" + Arrays.toString(steps));
			try {

				String operator = "";
				for (int i = 0; i < steps.length; i++) {
					String target = steps[i];
					switch (target) {
					case "+":
						if (operator.equals("-")) {
							operator = "-";
							break;
						} else if (operator.equals("+")) {
							operator = target;
							break;
						} else if (!operator.isEmpty()) {
							throw new java.lang.Exception("Operator error.");
						}
						operator = target;
						break;
					case "-":
						if (operator.equals("-")) {
							operator = "+";
							break;
						} else if (!operator.isEmpty()) {
							throw new java.lang.Exception("Operator error.");
						}
						operator = target;
						break;
					case "/":
						if (!operator.isEmpty()) {
							throw new java.lang.Exception("Operator error.");
						}
						operator = target;
						break;
					case "*":
						if (!operator.isEmpty()) {
							throw new java.lang.Exception("Operator error.");
						}
						operator = target;
						break;
					default:
						BigDecimal v = BigDecimal.valueOf(Double.valueOf(target));
						switch (operator) {
						case "+":
							runningTotal = runningTotal.add(v);
							break;
						case "-":
							runningTotal = runningTotal.subtract(v);
							break;
						case "/":
							if (v.doubleValue() == 0) {
								throw new ArithmeticException("Divide by 0 ");
							}
							try {
								runningTotal = runningTotal.divide(v);
							} catch (ArithmeticException e) {
								runningTotal = runningTotal.divide(v, new MathContext(10, RoundingMode.HALF_DOWN));
							}
							break;
						case "*":
							runningTotal = runningTotal.multiply(v);
							break;
						default:
							runningTotal = v;
						}
						operator = "";
					}
				}
				
				//a very dirty way of handling this representation which is outside of our 'allowed characters' range - it has the side effect of not allowing the user to click the input field to add to the sum.
				//we 'could' handle this in a more functional manner, but it is way too outside of a project which has already spiraled out of scope-creep control.
				String set = runningTotal.toPlainString();
				if (set.contains("E")) {
					this.systemSetText(set);
					return;
				}
				this.setText(set);
			} catch (ArithmeticException e) {
				String m = "Arithmetic Error: " + e.getMessage();
				dbg(m);
				this.systemSetText(m);
			} catch (Exception e) {
				String m = "Exception: " + e.getMessage();
				dbg(m);
				this.systemSetText(m);
			}

		}

		// return a value indicating whether we can use this character on our
		// calculator's input field.
		public static boolean charAllowed(char c) {
			char[] allowedCharacters = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '/', '*', '+', '-', ' ',
					'.' };
			java.util.Arrays.sort(allowedCharacters);
			if (java.util.Arrays.binarySearch(allowedCharacters, c) > -1) {
				return true;
			}
			return false;
		}
	}

	private class calculatorButton extends Button {

		// Define class properties, including styles.
		private final String styleNormal = "-fx-border-width:1px; -fx-border-color:  #777777;"
				+ "-fx-border-width: 1px;"
				+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #eeeeee, #dddddd);"
				+ "-fx-effect: dropshadow( one-pass-box , #EEEEEE , 2 , 0.0 , 2 , 0 );";
		private final String styleHover = "-fx-border-width:1px; -fx-border-color:  #999;" + "-fx-border-width: 1px;"
				+ "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ddd, #eee);"
				+ "-fx-effect: dropshadow( one-pass-box , #EEEEEE , 2 , 0.0 , 2 , 0 );";

		// Constructors
		public calculatorButton(String function) {
			super();
			this.setText(function);
			this.setTextAlignment(TextAlignment.CENTER);
			this.setStyle(styleNormal);
			this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

			this.onMouseEnteredProperty().set(e -> this.setStyle(styleHover));
			this.onMouseExitedProperty().set(e -> this.setStyle(styleNormal));

		}
	} // End calculatorButton class;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		// Get a reasonable value for our window size based on the main display.
		Dimension windowSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		windowSize.setSize((windowSize.getWidth() * .25) * 1.25, windowSize.getWidth() * .25);
		primaryStage.setResizable(false);

		// Create layouts;
		VBox root = new VBox();
		root.setPadding(new Insets(5, 5, 5, 5));

		GridPane gridPane = new GridPane();
		gridPane.setHgap(5);
		gridPane.setVgap(5);

		// Create controls;

		String[] buttonLabels = { "1", "2", "3", "+", "4", "5", "6", "-", "7", "8", "9", "*", "0", "=", "C", "/" };
		calculatorButton[] calculatorButtons = new calculatorButton[buttonLabels.length];
		for (int i = 0; i < calculatorButtons.length; i++) {
			calculatorButtons[i] = new calculatorButton(buttonLabels[i]);
		}

		// Add controls to layout;
		gridPane.setAlignment(Pos.CENTER);
		gridPane.addRow(0, Arrays.copyOfRange(calculatorButtons, 0, 4));
		gridPane.addRow(1, Arrays.copyOfRange(calculatorButtons, 4, 8));
		gridPane.addRow(2, Arrays.copyOfRange(calculatorButtons, 8, 12));
		gridPane.addRow(3, Arrays.copyOfRange(calculatorButtons, 12, 16));
		gridPane.setPrefHeight(windowSize.height);

		// Configure our grid with constraints;

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

		root.getChildren().addAll(outputField, gridPane);
		VBox.setMargin(outputField, new Insets(5, 0, 5, 0));

		// Create event handlers for all buttons on the grid;
		gridPane.getChildren().stream().forEach(e -> e.onMouseClickedProperty()
				.set(f -> this.outputField.buttonInput(((calculatorButton) e).getText())));

		// Create a scene and place it in the stage
		Scene scene = new Scene(root, windowSize.getWidth(), windowSize.getHeight());

		// Create event handlers for the scene itself;
		scene.setOnKeyPressed((event) -> {
			dbg("Button event: " + event.getText());
			switch (event.getCode()) {
			case ENTER:
				this.outputField.calculateOutput();
				break;
			case ESCAPE:
				this.outputField.buttonInput("C");
				return;
			case EQUALS:
				if (event.isShiftDown()) {
					this.outputField.buttonInput("+");
					break;
				}
				this.outputField.buttonInput("=");
				break;
			default:
				this.outputField.buttonInput(event.getText());
			}
		});
		primaryStage.setTitle("Calculator"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
