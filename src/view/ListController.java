package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Song;

public class ListController {
	@FXML
	ListView<String> listView;
	@FXML
	Text songName;
	@FXML // fx:id="add"
    private Button add; // Value injected by FXMLLoader


	private ObservableList<String> obsList;

	public void start(Stage mainStage) {

		obsList = FXCollections.observableArrayList(readFile());

		listView.setItems(obsList);
				
		// select the first item
		listView.getSelectionModel().select(0);
		songName.setText(listView.getSelectionModel().getSelectedItem());

		
		// set listener for the items
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> selectItem(mainStage));
		
		// after user adds/deletes/edits songs writeFile updates song.txt file
		mainStage.setOnCloseRequest(event -> {
			//obsList.add("test");
			
			//writeFile writes to song.txt file
			writeFile(obsList);
	     
	    });
	}

	private List<String> readFile() {
		List<String> list = new ArrayList<>();

		File file = new File("song.txt");
		if (!file.exists() || file.isDirectory()) {
			return list;
		} else {
			try {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					String data = scanner.nextLine();
					list.add(data);
					//System.out.println(data);
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	private void writeFile(ObservableList<String> obsList) {
		File file = new File("song.txt");
		
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter("song.txt");
			for(int i = 0; i < obsList.size(); i++) {
				writer.write(obsList.get(i) + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@FXML
    private void addSong(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		String content = "we gon add";
		alert.setContentText(content);
		alert.showAndWait();
    }


	private void selectItem(Stage mainStage) {
		songName.setText(listView.getSelectionModel().getSelectedItem());
	}
}
