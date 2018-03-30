import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Class Creates a stage and a scene for the menu and text area. Launches the
 * application.
 *
 * @author Randy Quimby
 * @version 1.0
 *
 *          COP4027 Project#: 3 File Name: MenuDemo.java
 */
public class MenuDemo extends Application {
	/**
	 * The Dictionary object to initialize the hashing algorithm for reading a dictionary
	 */
	private Dictionary dictionary;

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage stage) {
		dictionary = new Dictionary();
		dictionary.open();
		dictionary.generateHashTable();
		dictionary.close();

		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");

		MenuItem openFile = new MenuItem("Open");
		MenuItem save = new MenuItem("Save");
		MenuItem exit = new MenuItem("Exit");
		MenuItem spellCheck = new MenuItem("Spell Check");

		fileMenu.getItems().addAll(openFile, save, exit);
	    editMenu.getItems().add(spellCheck);
	    menuBar.getMenus().addAll(fileMenu, editMenu);

	    TextArea textArea = new TextArea();
	    textArea.setWrapText(true);
	    TextFieldHandler action = new TextFieldHandler(textArea, dictionary, stage);
	    ExitMenuHandler exitAction = new ExitMenuHandler();

	    openFile.setOnAction(action);
	    spellCheck.setOnAction(action);
	    exit.setOnAction(exitAction);
	    save.setOnAction(action);

	    BorderPane root = new BorderPane();
	    root.setTop(menuBar);
	    root.setCenter(textArea);
	    Scene scene = new Scene(root, 350, 400);

	    stage.setTitle("Spell Check");
	    stage.setScene(scene);
	    stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
