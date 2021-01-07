package me.cepera.regioncalculator;
	
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class CalculateWindow extends Application {
	
	private static ResourceBundle bundle;
	private static Alert currentErrorDialog;
	
	@Override
	public void start(Stage primaryStage) {
		Thread.setDefaultUncaughtExceptionHandler(CalculateWindow::handleError);
		try {
			bundle = ResourceBundle.getBundle("assets/regioncalculator/lang/lang");
			FXMLLoader loader = new FXMLLoader(ResourceHelper.getResourceURL("fxml/calculation_window.fxml"), bundle);
			loader.setCharset(ResourceHelper.CHARSET);
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(ResourceHelper.getResourceURL("css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.show();
			primaryStage.setMinWidth(primaryStage.getWidth());
			primaryStage.setMinHeight(primaryStage.getHeight());
			primaryStage.setTitle(bundle.getString("window.calculate.title"));
			primaryStage.getIcons().add(ResourceHelper.ICON);
			primaryStage.setResizable(false);
			((CalculateWindowController)loader.getController()).postInitialize(primaryStage);
		} catch(Exception e) {
			RegionCalculator.logException("Window initialization failed", e);
			Platform.exit();
		}
	}
	
	@Override
	public void stop() throws Exception {
		RegionCalculator.LOGGER.info("Shutdown...");
	}
	
	public static void handleError(Thread t, Throwable e) {
		RegionCalculator.logException("Unexpected error", e);
        if (currentErrorDialog == null && Platform.isFxApplicationThread()) {
        	Throwable root = e;
        	while(root.getCause() != null) root = root.getCause();
        	showError(root);
        }
    }
	
	public static void showError(Throwable e) {
        StringWriter stack = new StringWriter();
        e.printStackTrace(new PrintWriter(stack));
        currentErrorDialog = new Alert(AlertType.ERROR);
        currentErrorDialog.setTitle(bundle.getString("window.error.title"));
        Stage stage = (Stage) currentErrorDialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ResourceHelper.ICON);
        String message = e.getMessage();
        if(message == null || message.isEmpty()) {
        	message = "Unexpected error";
        }
        currentErrorDialog.setHeaderText(message);
        VBox content = new VBox();
        Label label = new Label("Stack Trace:");
        String stackTrace = stack.toString();
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);
        content.getChildren().addAll(label, textArea);
        currentErrorDialog.getDialogPane().setContent(content);
        currentErrorDialog.showAndWait();
        currentErrorDialog = null;
    }
	
}
