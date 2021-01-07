package me.cepera.regioncalculator;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalculateWindowController {

	public static final int REGION_BLOCK_WIDTH = 512;
	
	@FXML
	private ResourceBundle resources;
	
	@FXML
	private TextField labelPos1X;
	
	@FXML
	private TextField labelPos1Z;
	
	@FXML
	private TextField labelPos2X;
	
	@FXML
	private TextField labelPos2Z;
	
	private Stage stage;
	
	public void initialize() {
		labelPos1X.setText("0");
		labelPos1Z.setText("0");
		labelPos2X.setText("0");
		labelPos2Z.setText("0");
		labelPos1X.textProperty().addListener(new IntFilter(labelPos1X));
		labelPos1Z.textProperty().addListener(new IntFilter(labelPos1Z));
		labelPos2X.textProperty().addListener(new IntFilter(labelPos2X));
		labelPos2Z.textProperty().addListener(new IntFilter(labelPos2Z));
	}
	
	public void postInitialize(Stage stage) {
		this.stage = stage;
	}
	
	public void onCalculateButtonClick(ActionEvent e) {
		int x1 = Math.floorDiv(parse(labelPos1X), REGION_BLOCK_WIDTH);
		int x2 = Math.floorDiv(parse(labelPos2X), REGION_BLOCK_WIDTH);
		int z1 = Math.floorDiv(parse(labelPos1Z), REGION_BLOCK_WIDTH);
		int z2 = Math.floorDiv(parse(labelPos2Z), REGION_BLOCK_WIDTH);
		if(x1 > x2) {
			x1 ^= x2;
			x2 ^= x1;
			x1 ^= x2;
		}
		if(z1 > z2) {
			z1 ^= z2;
			z2 ^= z1;
			z1 ^= z2;
		}
		StringBuilder result = new StringBuilder();
		for(int x = x1; x <= x2; x++)
			for(int z = z1; z <= z2; z++) {
				result.append("r.");
				result.append(x);
				result.append('.');
				result.append(z);
				result.append(".mca ");
			}
		showResultWindow(result.toString());
	}
	
	private int parse(TextField field) {
		try {
			return Integer.parseInt(field.getText().trim());
		}catch(Exception ex) {}
		return 0;
	}
	
	public void showResultWindow(String result) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(ResourceHelper.getResourceURL("fxml/result_window.fxml"), resources);
			loader.setCharset(ResourceHelper.CHARSET);
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(resources.getString("window.result.title"));
			stage.getIcons().add(ResourceHelper.ICON);
			stage.setResizable(false);
			((ResultWindowController)loader.getController()).setResult(result);;
			stage.sizeToScene();
			stage.initOwner(this.stage);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.toFront();
			stage.show();
		} catch(Exception ex) {
			RegionCalculator.logException("Result window initialization failed", ex);
		}
	}
	
	private class IntFilter implements ChangeListener<String>{

		TextField field;
		
		IntFilter(TextField field) {
			this.field = field;
		}
		
		@Override
		public void changed(ObservableValue<? extends String> obs, String oldVal, String newVal) {
			newVal = newVal.trim();
			if(!newVal.isEmpty() && !newVal.equals("-")) {
				String onv = newVal;
				try {
					Integer.parseInt(newVal);
				}catch(Exception ex) {
					newVal = oldVal;
				}
				if(!newVal.equals(onv)) {
					final String nv = newVal;
					Platform.runLater(()->field.setText(nv));
				}
			}
		}
		
	}
	
}
