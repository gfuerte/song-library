//Project by Greg Fuerte & Aries Regalado
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Song;

public class ListController {
	@FXML
	ListView<String> listView;
	@FXML
	Button add, edit, delete;
	@FXML
	TextField songName, songArtist, songAlbum, songYear;
	@FXML
	TextField addSongName, addSongArtist, addSongAlbum, addSongYear;

	private ObservableList<String> songList;
	private HashMap<String, Song> songMap = new HashMap<>();
	private final String separator = " \u200e- ";

	public void start(Stage mainStage) {
		songList = FXCollections.observableArrayList(readFile());

		listView.setItems(songList);
		listView.getSelectionModel().select(0);
		if (songMap.size() == 0) {
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

		listView.getSelectionModel().selectedIndexProperty()
				.addListener((songList, oldVal, newVal) -> selectItem(mainStage));

		mainStage.setOnCloseRequest(event -> {
			writeFile(songList);
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
				String name, artist, album, year;
				name = artist = album = year = "";
				int count = 1;
				while (scanner.hasNextLine()) {
					String data = scanner.nextLine();
					// System.out.println(data);
					switch (count) {
					case 1:
						name = data;
						break;
					case 2:
						artist = data;
						list.add(name + separator + artist);
						break;
					case 3:
						album = data;
						break;
					case 4:
						year = data;
						songMap.put(name + separator + artist, new Song(name, artist, album, year));
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
		Collections.sort(list);
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
			for (int i = 0; i < songList.size(); i++) {
				Song song = songMap.get(songList.get(i));
				writer.write(song.getName() + "\n" + song.getArtist() + "\n" + song.getAlbum() + "\n" + song.getYear()
						+ "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void addSong(ActionEvent event) {
		String songName = addSongName.getText().trim();
		String artistName = addSongArtist.getText().trim();
		String album = addSongAlbum.getText().trim();
		String year = addSongYear.getText().trim();

		if (songName.isEmpty() || artistName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "Please make sure song name and artist name is not empty";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		for (String s : songList) {
			Song song = songMap.get(s);
			if (songName.compareToIgnoreCase(song.getName()) == 0
					&& artistName.compareToIgnoreCase(song.getArtist()) == 0) {
				Alert alert = new Alert(AlertType.ERROR);
				String content = "This song already exists";
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}

		if (!year.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			String content = "Year is not valid";
			try {
				if (Integer.parseInt(year) <= 0) {
					alert.setContentText(content);
					alert.showAndWait();
					return;
				}
			} catch (NumberFormatException e) {
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to perform this action?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Song song = new Song(songName, artistName, album, year);
			songMap.put(songName + separator + artistName, song);
			insertSong(song);

			addSongName.setText("");
			addSongArtist.setText("");
			addSongAlbum.setText("");
			addSongYear.setText("");
		}
	}

	@FXML
	private void deleteSong(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		if (songList.size() == 0) {
			String content = "No songs to be deleted";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to perform this action?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Song song = songMap.get(listView.getSelectionModel().getSelectedItem());
			int index = listView.getSelectionModel().getSelectedIndex();
			songList.remove(song.getName() + separator + song.getArtist());
			songMap.remove(song.getName() + separator + song.getArtist());

			if (songList.size() == 0) {
				songName.setText("");
				songArtist.setText("");
				songAlbum.setText("");
				songYear.setText("");
				return;
			}

			if (index == songList.size()) {
				listView.getSelectionModel().select(index - 1);
			} else {
				listView.getSelectionModel().select(index);
			}

			song = songMap.get(listView.getSelectionModel().getSelectedItem());
			songName.setText(song.getName());
			songArtist.setText(song.getArtist());
			songAlbum.setText(song.getAlbum());
			songYear.setText(song.getYear());
		}
	}

	@FXML
	private void editSong(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		String content = "";
		if (songList.size() == 0) {
			content = "No song to be edited";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}

		String name = songName.getText().trim();
		String artist = songArtist.getText().trim();
		String album = songAlbum.getText().trim();
		String year = songYear.getText().trim();

		Song original = songMap.get(listView.getSelectionModel().getSelectedItem());

		if (name.isEmpty() || artist.isEmpty()) {
			content = "Please enter song name and artist";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		if (!year.isEmpty()) {
			content = "Year is not valid";
			try {
				if (Integer.parseInt(year) <= 0) {
					alert.setContentText(content);
					alert.showAndWait();
					return;
				}
			} catch (NumberFormatException e) {
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}

		if (original.getName().compareToIgnoreCase(name) == 0 && original.getArtist().compareToIgnoreCase(artist) == 0
				&& original.getAlbum().compareToIgnoreCase(album) == 0
				&& original.getYear().compareToIgnoreCase(year) == 0) {
			content = "No changes to be made";
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}

		for (String s : songList) {
			Song song = songMap.get(s);
			if (song.getName().compareToIgnoreCase(original.getName()) == 0
					&& song.getArtist().compareToIgnoreCase(original.getArtist()) == 0)
				continue;
			if (name.compareToIgnoreCase(song.getName()) == 0 && artist.compareToIgnoreCase(song.getArtist()) == 0) {
				content = "This song already exists";
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}

		alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Are you sure you want to perform this action?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			songList.remove(original.getName() + separator + original.getArtist());
			songMap.remove(original.getName() + separator + original.getArtist());

			Song modified = new Song(name, artist, album, year);
			songMap.put(modified.getName() + separator + modified.getArtist(), modified);
			insertSong(modified);
		} else {
			songName.setText(original.getName());
			songArtist.setText(original.getArtist());
			songAlbum.setText(original.getAlbum());
			songYear.setText(original.getYear());
		}
	}

	private void selectItem(Stage mainStage) {
		Song song = songMap.get(listView.getSelectionModel().getSelectedItem());
		if (song == null)
			song = new Song("", "", "", "");
		songName.setText(song.getName());
		songArtist.setText(song.getArtist());
		songAlbum.setText(song.getAlbum());
		songYear.setText(song.getYear());
	}

	private void insertSong(Song song) {
		if (songList.size() == 0) {
			songList.add(song.getName() + separator + song.getArtist());
			listView.getSelectionModel().selectFirst();
			return;
		}

		String info = song.getName() + separator + song.getArtist();
		for (int i = 0; i < songList.size(); i++) {
			if (info.compareToIgnoreCase(songList.get(i)) < 0) {
				songList.add(i, info);
				listView.getSelectionModel().select(i);
				return;
			}
		}
		songList.add(info);
		listView.getSelectionModel().selectLast();
	}
}
