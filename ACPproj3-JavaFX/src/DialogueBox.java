import java.util.ArrayList;
import javafx.scene.control.ChoiceDialog;

public class DialogueBox {
	private ChoiceDialog<String> dialog;

	public DialogueBox(ArrayList<String> suggestions) {
        dialog = new ChoiceDialog<String>(null, suggestions);
      
        dialog.setTitle("Misspelling");
        dialog.setHeaderText("Select a suggestion:");
        dialog.setContentText("Suggestions:");
        dialog.showAndWait();   
	}
	
	public ChoiceDialog<String> getDialog() {
		return dialog;
	}

}



