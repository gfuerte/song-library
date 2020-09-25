package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	Text songName, songArtist, songAlbum, songYear;
	@FXML
    Button add, edit, delete;


	private ObservableList<String> songList;
	private HashMap<String, Song> songMap = new HashMap<>();
	
	public void start(Stage mainStage) {

		songList = FXCollections.observableArrayList(readFile());

		listView.setItems(songList);
				
		// select the first item
		listView.getSelectionModel().select(0);
		if(songMap.size() == 0) {
			songName.setText("");
			songArtist.setText("");
			songAlbum.setText("");
			songYear.setText("");
		} else {
			Song song = songMap.get(listView.getSelectionModel().getSelectedItem());
			songName.setText(song.getName());
			songArtist.setText(song.getArtist());
			songAlbum.setText(song.getAlbum());
			songYear.setText(song.getYear());
		}

		
		// set listener for the items
		listView.getSelectionModel().selectedIndexProperty().addListener((songList, oldVal, newVal) -> selectItem(mainStage));
		
		// after user adds/deletes/edits songs writeFile updates song.txt file
		mainStage.setOnCloseRequest(event -> {
			//obsList.add("test");
			
			//writeFile writes to song.txt file
			writeFile(songList);
	     
	    });
	}

	//locates song.txt and reads 
	private List<String> readFile() {
		List<String> list = new ArrayList<>();

		File file = new File("song.txt");
		if (!file.exists() || file.isDirectory()) {
			return list;
		} else {
			try {
				Scanner scanner = new Scanner(file);
				String name, artist, album, year;
				name = artist = album = year = "";
				int count = 1;
				while (scanner.hasNextLine()) {
					String data = scanner.nextLine();
					//System.out.println(data);
					switch(count) {
					case 1:
						name = data;
						list.add(data);
						break;
					case 2:
						artist = data;
						break;
					case 3:
						album = data;
						break;
					case 4:
						year = data;
						songMap.put(name, new Song(name, artist, album, year));
						count = 0;
						break;
					}
					count++;
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private void writeFile(ObservableList<String> songList) {
		File file = new File("song.txt");
		
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
		
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter("song.txt");
			for(int i = 0; i < songList.size(); i++) {
				Song song = songMap.get(songList.get(i));
				writer.write(
						song.getName() + "\n" + 
						song.getArtist() + "\n" +
						song.getAlbum() + "\n" +
						song.getYear() + "\n");
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
	
	@FXML
    private void deleteSong(ActionEvent event) {
		/* Not working currently
		Song song = songMap.get(listView.getSelectionModel().getSelectedItem());
		int index = listView.getSelectionModel().getSelectedIndex();
		songList.remove(song.getName());
		songMap.remove(song.getName());	
		listView.setItems(songList);
		
		if(songList.size() == 0) return;
		
		if(index == songList.size()) {
			listView.getSelectionModel().select(index-1);
		} else {
			listView.getSelectionModel().select(index);
		}
		*/
    }
	
	@FXML
    private void editSong(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		String content = "we gon edit";
		alert.setContentText(content);
		alert.showAndWait();
    }

	//gets String value from selected item --> accesses hashmap for song --> sets all fields to song details
	private void selectItem(Stage mainStage) {
		Song song = songMap.get(listView.getSelectionModel().getSelectedItem());
		//if(song == null) return;
		songName.setText(song.getName());
		songArtist.setText(song.getArtist());
		songAlbum.setText(song.getAlbum());
		songYear.setText(song.getYear());
	}
}
