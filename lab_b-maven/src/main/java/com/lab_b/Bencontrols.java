package bookrecommender.controls;

import java.io.IOException;

import bookrecommender.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Bencontrols {

    
    @FXML
    private TextField TfRicerca;

    @FXML
    private Button ButtonResearch;

    @FXML
    private ListView<?> myListView;

    @FXML
    private Label InfoBook;

    @FXML
    void Accedi(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("Login.fxml");
    }
    
    @FXML
    void Registrati(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("Registrazione.fxml");
    }

    @FXML
    void BtnReaserch(ActionEvent event) 
    {

    }
}
