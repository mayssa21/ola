
package gui;

import entities.Contrat;
import entities.Offre;
import entities.User;
import entities.Vehicule;
import service.ContratService;
import service.IContratService;
import service.IOffreService;
import service.IUserService;
import service.IVehiculeService;
import service.OffreService;
import service.UserService;
import service.VehiculeService;
import test.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author zaghd
 */
public class AddContratCardControler implements Initializable {

    @FXML
    private Button add_new_ContratBtn;

    @FXML
    private HBox choose_photoBtn;

    @FXML
    private ComboBox<String> cinInput;

    @FXML
    private Text cinInputError;

    @FXML
    private HBox cinInputErrorHbox;

    @FXML
    private HBox dateauErrorInputHbox;

    @FXML
    private DatePicker dateauInput;

    @FXML
    private Text dateauInputError;

    @FXML
    private Text dateduErrorInput;

    @FXML
    private HBox dateduErrorInputHbox;

    @FXML
    private DatePicker dateduInput;

    @FXML
    private ImageView imageInput;

    @FXML
    private ComboBox<String> matInput;

    @FXML
    private Text matInputError;

    @FXML
    private HBox matInputErrorHbox;

    @FXML
    private Text photoInputError;

    @FXML
    private HBox photoInputErrorHbox;

    @FXML
    private Text productName;

    @FXML
    private Button update_contratBtn;

    
    
    private File selectedImageFile;
    private String imageName = null;
    private int userId = -1;
    private int matId = -1;
    
 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cinInputErrorHbox.setVisible(false);
        dateduErrorInputHbox.setVisible(false);
        dateauErrorInputHbox.setVisible(false);
        matInputErrorHbox.setVisible(false);
        photoInputErrorHbox.setVisible(false);

        if (Offre.actionTest == 0) { // add offre
            update_contratBtn.setVisible(false);

        } else { // update offre
            add_new_ContratBtn.setVisible(false);

            // Instancier le service de produit
            IContratService contratService = new ContratService();
            Contrat contrat = new Contrat();
           try {
             contrat = contratService.getOneContrat(Contrat.getIdContrat());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        cinInput.setValue(""+ contrat.getId_client());
        //dateduInput.setValue(contrat.getValiditedu());
        String dateString = contrat.getValiditedu();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        dateduInput.setValue(localDate);

        String dateString2 = contrat.getValiditeau();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate2 = LocalDate.parse(dateString2, formatter2);
        dateauInput.setValue(localDate2);

        matInput.setValue(""+ contrat.getId_client());
        Image image = new Image(getClass().getResource("/assets/OffresUploads/" + contrat.getPhoto_cin()).toExternalForm());
        imageInput.setImage(image);
        imageName = contrat.getPhoto_cin();


        }
        //---------------------------------ajouter à combobox client
        // Instancier le service de categorie
        IUserService userService = new UserService();

        // Récupérer toutes les categories
        List<User> users = userService.getAll();
        
        System.out.println("user ");
        Map<String, Integer> valuesMap = new HashMap<>();
        for (User user : users) {
            System.out.println("user"+user);
            cinInput.getItems().add("" + user.getCin());
            valuesMap.put(""+ user.getCin(), user.getId());
        }

        cinInput.setOnAction(event -> {
            String selectedOption = cinInput.getValue();
            int selectedValue = valuesMap.get(selectedOption);
            userId = selectedValue;
            
             System.out.println("Selected option: " + selectedOption);
             System.out.println("Selected value: " + userId);
        });

        //---------------------------------ajouter à combobox mat
        // Instancier le service de categorie
        IVehiculeService vehiculeService = new VehiculeService();

        // Récupérer toutes les categories
        List<Vehicule> vehicules = vehiculeService.getAll();
        
        System.out.println("user ");
        Map<String, Integer> valuesMap2 = new HashMap<>();
        for (Vehicule vehicule : vehicules) {
            System.out.println("Vehicule"+vehicule);
            matInput.getItems().add("" + vehicule.getMatricule());
            valuesMap2.put(""+ vehicule.getMatricule(), vehicule.getId());
        }

        matInput.setOnAction(event -> {
            String selectedOption = matInput.getValue();
            int selectedValue = valuesMap2.get(selectedOption);
            matId = selectedValue;
            
             System.out.println("Selected option mat: " + selectedOption);
             System.out.println("Selected value mat: " + matId);
        });

}
@FXML
    void addNewContrat(MouseEvent event) throws SQLException{

        Contrat contrat = new Contrat();
       
                contrat.setId_client(userId);

          //      LocalDate localDate = dateduInput.getValue();
              //  Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            //    Date date = Date.from(instant);
                contrat.setValiditedu(""+dateduInput.getValue());


              //  LocalDate localDate2 = dateauInput.getValue();
              //  Instant instant2 = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
              //  Date date2 = Date.from(instant2);
                contrat.setValiditeau(""+dateauInput.getValue());
                

                contrat.setId_vehicule(matId);

            contrat.setPhoto_cin(imageName);

            System.out.println("ID: " + userId);
           
            // Instancier le service de produit
            IContratService contratService = new ContratService();
            try {
                contratService.ajouter(contrat);
                

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ListeContrat.fxml"));

                Parent root = loader.load();
                // Accéder à la pane content_area depuis le controller de
                // OneProductListCard.fxml
              //  Parent root = FXMLLoader.load(getClass().getResource("ListeOffre.fxml"));
              //  Main.stage.getScene().setRoot(root);
                
              Pane contentArea = (Pane) ((Node) event.getSource()).getScene().lookup("#content_area");

                // Vider la pane et afficher le contenu de ProductsList.fxml
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        

    }

    @FXML
    void ajouter_image(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        selectedImageFile = fileChooser.showOpenDialog(imageInput.getScene().getWindow());
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageInput.setImage(image);

            // Générer un nom de fichier unique pour l'image
            String uniqueID = UUID.randomUUID().toString();
            String extension = selectedImageFile.getName().substring(selectedImageFile.getName().lastIndexOf("."));
            imageName = uniqueID + extension;

            Path destination = Paths.get(System.getProperty("user.dir"), "src", "assets", "OffresUploads", imageName);
            Files.copy(selectedImageFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            // pour le controle de saisie
            
        }
    }

    

   

   




    @FXML
    void updateContrat(MouseEvent event) throws SQLException {
        Contrat contrat = new Contrat();
        contrat.setId(Contrat.getIdContrat());
        
       // contrat.setId_client(userId);

  //      LocalDate localDate = dateduInput.getValue();
      //  Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    //    Date date = Date.from(instant);
        contrat.setValiditedu(""+dateduInput.getValue());


      //  LocalDate localDate2 = dateauInput.getValue();
      //  Instant instant2 = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
      //  Date date2 = Date.from(instant2);
        contrat.setValiditeau(""+dateauInput.getValue());
        

        contrat.setId_vehicule(matId);

    contrat.setPhoto_cin(imageName);

    System.out.println("ID: " + userId);
   
    // Instancier le service de produit
    IContratService contratService = new ContratService();
    try {
        contratService.ajouter(contrat);
        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ListeContrat.fxml"));

        Parent root = loader.load();
        // Accéder à la pane content_area depuis le controller de
        // OneProductListCard.fxml
      //  Parent root = FXMLLoader.load(getClass().getResource("ListeOffre.fxml"));
      //  Main.stage.getScene().setRoot(root);
        
      Pane contentArea = (Pane) ((Node) event.getSource()).getScene().lookup("#content_area");

        // Vider la pane et afficher le contenu de ProductsList.fxml
        contentArea.getChildren().clear();
        contentArea.getChildren().add(root);
    } catch (IOException e) {
        e.printStackTrace();
    }


    /*     Offre offre = new Offre();

        offre.setId(Offre.getIdOffre());

        if (nameInput.getText().isEmpty()) {
            nomTest = 0;
            nameInputErrorHbox.setVisible(true);
        } else {
            if (nomTest == 1) {
                offre.setTitre(nameInput.getText());
            }
        }

        if (descriptionInput.getText().isEmpty()) {
            descriptionTest = 0;
            descriptionInputErrorHbox.setVisible(true);
        } else {
            if (descriptionTest == 1) {
                offre.setDescription(descriptionInput.getText());
            }
        }

       

        if (validiteInput.getText().isEmpty()) {
            validiteTest = 0;
            validiteInputErrorHbox.setVisible(true);
        } else {
            if (validiteTest == 1) {
                offre.setValidite_offre(validiteInput.getText());
            }
        }

        if (priceInput.getText().isEmpty()) {
            priceTest = 0;
            priceInputErrorHbox.setVisible(true);
        } else {
            if (priceTest == 1) {
                offre.setPrix(Integer.parseInt(priceInput.getText()));
            }
        }

        

        if (imageName == null) {
            photoTest = 0;
            photoInputErrorHbox.setVisible(true);
        } else {
            photoTest = 1;
            offre.setImage_offre(imageName);
        }

        System.out.println("nom: " +nomTest+"des: " + descriptionTest+ "va: " + validiteTest+"prix: " + priceTest+"photo: " + photoTest);
        if (nomTest == 1 && descriptionTest == 1 && validiteTest == 1 && priceTest == 1
                && photoTest == 1) {
            // Instancier le service de produit
            IOffreService offreService = new OffreService();
            try {
                offreService.modifier(offre);
                

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ListeOffre.fxml"));

                Parent root = loader.load();
                // Accéder à la pane content_area depuis le controller de
                // OneProductListCard.fxml
              //  Parent root = FXMLLoader.load(getClass().getResource("ListeOffre.fxml"));
              //  Main.stage.getScene().setRoot(root);
                
              Pane contentArea = (Pane) ((Node) event.getSource()).getScene().lookup("#content_area");

                // Vider la pane et afficher le contenu de ProductsList.fxml
                contentArea.getChildren().clear();
                contentArea.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        */

    }

    
}
    
