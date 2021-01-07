package me.cepera.regioncalculator;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultWindowController {
	
	@FXML
	private TextArea result; 

	public void setResult(String resultText) {
		Platform.runLater(()->result.setText(resultText));
	}
	
}
