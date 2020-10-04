# song-library
Java application with a graphical user interface to manage a library of songs using JavaFX. 
Song libarary displays a single with three functions: song list display, details of songs (song name, artist, ablum, year), and buttons for adding new songs, deleting a selected song, and editing a selected song.

Add: When a new song is added, the song name and artist should be entered at the very least. Year and album are optional. If the name and artist are the same as an existing song, the add is not be allowed - a message is shown in a pop-up dialog within the main application window. The newly added song is automatically placed in the correct position in the alphabetical order in the list. Also, it is automatically selected, replacing the previously selected song, and its details should be shown.

Edit: Any of the fields can be changed. If name and artist conflict with those of an existing song, the edit is not be allowed and a message is be shown in a pop-up dialog within the main application window.

Delete: The selected song in the list is deleted. After deletion, the next song in the list is selected, and the details displayed. If there is no next song, the previous song is selected, and if there is no previous song either, then the list is empty and the detail info is all blanks. 
