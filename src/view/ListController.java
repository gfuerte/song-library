package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Song;

public class ListController {
	@FXML
	ListView<String> listView;

	private ObservableList<String> obsList;

	public void start(Stage mainStage) {

		obsList = FXCollections.observableArrayList(readFile());

		listView.setItems(obsList);


		// select the first item
		listView.getSelectionModel().select(0);

		// set listener for the items
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> showItem(mainStage));
		
		// after user adds/deletes/edits songs writeFile updates song.txt file
		mainStage.setOnCloseRequest(event -> {
			//test add
			obsList.add("test");
			
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

	private void showItem(Stage mainStage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("List Item");
		alert.setHeaderText("Selected list item properties");

		String content = "Index: " + listView.getSelectionModel().getSelectedIndex() + "\nValue: "
				+ listView.getSelectionModel().getSelectedItem();

		alert.setContentText(content);
		alert.showAndWait();
	}

	private void showItemInputDialog(Stage mainStage) {
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();

		TextInputDialog dialog = new TextInputDialog(item);
		dialog.initOwner(mainStage);
		dialog.setTitle("List Item");
		dialog.setHeaderText("Selected Item (Index: " + index + ")");
		dialog.setContentText("Enter name: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			obsList.set(index, result.get());
		}
	}
}
