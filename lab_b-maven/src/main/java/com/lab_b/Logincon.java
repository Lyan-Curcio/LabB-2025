package bookrecommender.controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import me.xdrop.fuzzywuzzy.Main;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import bookrecommender.App;
public class Logincon 
{
    @FXML
    private Button BLogin;

    @FXML
    private TextField UserID;

    @FXML
    private PasswordField Password;

    @FXML
    private Button ButtonReturn;

    @FXML
    void BtnclickLog(ActionEvent event) throws IOException
    {
        CheckLogin();   
    }

    @FXML
    void BtnReturn(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("Benvenuto.fxml");
    }
    
    private void CheckLogin() throws IOException
    {
        App m = new App();
        if(UserID.getText().equals("Sergio") && Password.getText().equals("123"))
        {
            System.out.println("successo");
            m.changeScene("Libri.fxml");
        }
        else if(StringUtils.isAnyBlank(UserID.getText(), Password.getText()))
        {
            System.out.println("Perfavore inserire i dati");
        }
        else
        {
            System.out.println("nome utente o password errati");
        }
    }
}

