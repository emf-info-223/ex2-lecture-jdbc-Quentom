package app.presentation;

import app.beans.Personne;
import app.exceptions.MyDBException;
import app.helpers.JfxPopup;
import app.workers.DbWorker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.io.File;
import app.workers.DbWorkerItf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author PA/STT
 */
public class MainCtrl implements Initializable {

    // DBs à tester
    private enum TypesDB {
        MYSQL, HSQLDB, ACCESS
    };

    // DB par défaut
    final static private TypesDB DB_TYPE = TypesDB.ACCESS;

    private DbWorkerItf dbWrk;

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;

    /*
   * METHODES NECESSAIRES A LA VUE
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbWrk = new DbWorker();
        ouvrirDB();
    }

    @FXML
    public void actionPrevious(ActionEvent event) throws MyDBException {
        Personne personnes = dbWrk.precedentPersonne();
        txtNom.setText(personnes.getNom());
        txtPrenom.setText(personnes.getPrenom());
    }

    @FXML
    public void actionNext(ActionEvent event) throws MyDBException {
        Personne personnes = dbWrk.suivantPersonne();
        txtNom.setText(personnes.getNom());
        txtPrenom.setText(personnes.getPrenom());
    }

    public void quitter() {
        try {
            dbWrk.deconnecter();
        } catch (MyDBException ex) {
            Logger.getLogger(MainCtrl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.exit();
    }

    /*
   * METHODES PRIVEES 
     */
    private void afficherPersonne(Personne p) {

    }

    private void ouvrirDB() {
        try {
            switch (DB_TYPE) {
                case MYSQL:
                    dbWrk.connecterBdMySQL("223_personne_1table");
                    break;
                case HSQLDB:
                    dbWrk.connecterBdHSQLDB("../data" + File.separator + "223_personne_1table");
                    break;
                case ACCESS:
                    dbWrk.connecterBdAccess("../data/access" + File.separator + "223_personne_1table.accdb");
                    break;
                default:
                    System.out.println("Base de données pas définie");
            }

            System.out.println("------- DB OK ----------");
            afficherPersonne(dbWrk.precedentPersonne());

        } catch (MyDBException ex) {
            JfxPopup.displayError("ERREUR", "Une erreur s'est produite", ex.getMessage());
            System.exit(1);
        }
    }

}
