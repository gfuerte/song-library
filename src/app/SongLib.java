//Project by Greg Fuerte & Aries Regalado
package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.ListController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("/view/scene.fxml"));
		AnchorPane root = (AnchorPane)loader.load();

		ListController listController = 
				loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setTitle("Song Library - Greg Fuerte & Aries Regalado");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show(); 

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
