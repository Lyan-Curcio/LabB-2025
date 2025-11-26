package bookrecommender.controls;

import java.io.IOException;

import bookrecommender.App;
import bookrecommender.csv.CsvUsers;
import bookrecommender.csv.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Regcontrols{

    @FXML
    private Button ButtonReturn;

    @FXML
    private TextField Tfnome;

    @FXML
    private TextField Tfcognome;

    @FXML
    private TextField TfUserid;

    @FXML
    private TextField TfCodiceFiscale;

    @FXML
    private TextField TfEmail;

    @FXML
    private TextField TfCreaPassword;

    @FXML
    private TextField TfConfermaPassword;

    @FXML
    void BtnClick(ActionEvent event) throws Exception 
    {
        String nome, cognome, CodiceFiscale, UserId, email, Password, CPassword;
        
        nome= Tfnome.getText();
        cognome= Tfcognome.getText();
        email = TfEmail.getText();

        if(IsADigit(nome) && IsADigit(cognome))
        {
            System.out.println("Dati errati");
            return;
        }

        CodiceFiscale= TfCodiceFiscale.getText();
        UserId= TfUserid.getText();

        Password= TfCreaPassword.getText();
        CPassword= TfConfermaPassword.getText();

        if(!Password.equals(CPassword))
        {
            System.out.println("le password non sono uguali");
            return;
        }
        else
        {
            if(Password.isEmpty())
            {
                System.out.println("inserire una password");
                return;
            }
        }

        User utente = new User(nome, cognome, CodiceFiscale, email, UserId, Password);
        System.out.println("dati inseriti");
        CsvUsers c = CsvUsers.getInstance();
        c.createUser(utente);
        return;
    }

    @FXML
    void BtnReturn(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("Benvenuto.fxml");
    }

    private boolean IsADigit(String a)
    {
        char c;
        for(int i = 0; i < a.length(); i++)
        {
            c=a.charAt(i);
            if(Character.isDigit(c))
                return true;
        }
        return false;
    }
}
