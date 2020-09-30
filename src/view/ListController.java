package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
		Dialog<Song> d = new Dialog<>();
		d.setTitle("Song");
		d.setHeaderText("Please Add The Respective Information For Song");
		
		//Will create a grid of rows and columns
		GridPane g = new GridPane();
		g.setPadding(new Insets(10, 10, 10, 10));
		g.setMinSize(300, 300);
		g.setVgap(8);
		g.setHgap(10);
				
		Label l1 = new Label("Song Name: ");
		GridPane.setConstraints(l1, 0, 0);
		Label l2 = new Label("Artist Name: ");
		GridPane.setConstraints(l2, 0, 1);
		Label l3 = new Label("Year Released: ");
		GridPane.setConstraints(l3, 0, 2);
		Label l4 = new Label("Album Name: ");
		GridPane.setConstraints(l4, 0, 3);
	
		TextField t1 = new TextField();
		GridPane.setConstraints(t1, 1, 0);
		TextField t2 = new TextField();
		GridPane.setConstraints(t2, 1, 1);
		TextField t3 = new TextField();
		GridPane.setConstraints(t3, 1, 2);
		TextField t4 = new TextField();
		GridPane.setConstraints(t4, 1, 3);
		
		ButtonType apply = new ButtonType("Apply", ButtonData.APPLY);
		g.getChildren().addAll(l1,t1,l2,t2,l3,t3,l4,t4);
		d.getDialogPane().getButtonTypes().addAll(apply);
		d.getDialogPane().setContent(g);

		System.out.println(t1);
		
		  Optional<Song> result = d.showAndWait();
		  if(result.isPresent()) {
			String sName = t1.getText();
			String aName = t2.getText();
			String year = t3.getText();
			String album = t4.getText();

			if(sName.isEmpty() || aName.isEmpty()) {
				
						Alert alert = new Alert(AlertType.ERROR);
						String content = "Please make sure song name and artist name is not empty";
						alert.setContentText(content);
						alert.showAndWait();
						return;
			}
			 
			for(String s : songList) {
				Song song = songMap.get(s);
				 
				if(song.getName().toLowerCase().equals(sName) && song.getArtist().toLowerCase().equals(aName)) {
					Alert alert = new Alert(AlertType.ERROR);
					String content = "This song already exists";
					alert.setContentText(content);
					alert.showAndWait();
					return;
	
				}				 
			}
			 		 
			try {
				Integer.parseInt(year);
			}
			catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
					String content = "Year is not valid";
					alert.setContentText(content);
					alert.showAndWait();
					return;
			}
			 	
			Song song = new Song(sName, aName, album, year);
			songMap.put(sName, song);
			songList.add(song.getName());
			
		  }
		  listView.getSelectionModel().selectLast();
    }
	
	@FXML
    private void deleteSong(ActionEvent event) {
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
		
		song = songMap.get(listView.getSelectionModel().getSelectedItem());
		songName.setText(song.getName());
		songArtist.setText(song.getArtist());
		songAlbum.setText(song.getAlbum());
		songYear.setText(song.getYear());
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
		if(song == null) song = new Song("", "", "", "");
		songName.setText(song.getName());
		songArtist.setText(song.getArtist());
		songAlbum.setText(song.getAlbum());
		songYear.setText(song.getYear());
	}
}
