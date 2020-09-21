package models;

public class Song {
	private String name;
	private String artist;
	private String album;
	private int year;
	
	public Song(String name, String artist) {
		this.name = name;
		this.artist = artist;
		this.album = "";
		this.year = 0;
	}
	
	public Song(String name, String artist, String album, int year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public Song(String name, String artist, String album) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = 0;
	}
	
	public Song(String name, String artist, int year) {
		this.name = name;
		this.artist = artist;
		this.album = "";
		this.year = year;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public int getYear() {
		return this.year;
	}
}
