import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Class to handle the Action Event of the "Exit" menu item
 *
 * @author Randy Quimby
 * @version 1.0
 *
 * COP4027 Project#: 3
 * File Name: ExitMenuHandler.java
 */
public class ExitMenuHandler implements EventHandler<ActionEvent> {

	/* (non-Javadoc)
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(ActionEvent event) {
			System.out.println("Goodbye...");
			Platform.exit();
	}

}
