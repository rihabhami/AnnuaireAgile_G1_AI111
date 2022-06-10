package fr.eql.ai111.groupe1.annuaire.agile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.Scene;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import static fr.eql.ai111.groupe1.annuaire.agile.ArbreDansLeRAF.LONGUEUSTAGIARE;

public class Scene_tableauDeBordAdmin extends Application {


    ArbreDansLeRAF arbreDansLeRAF = new ArbreDansLeRAF();

    public static final int NOMSTAGIARE = 20;
    public static final int PRENOMSTAGIARE = 20;
    public static final int DEPARTEMENT = 15;
    public static final int PROMO = 10;


    public static Stagiaires fabriqueStagiaires(String chaine) {
        Stagiaires stagiaire = null;
        StringTokenizer st = new StringTokenizer(chaine);
        if (st.countTokens() == 5) {
            String nom = st.nextToken();
            String prenom = st.nextToken();
            String departement = st.nextToken();
            String promo = st.nextToken();
            String année = String.valueOf(Integer.valueOf(st.nextToken()));

            stagiaire = new Stagiaires(nom, prenom, departement, promo, année);
        }
        return stagiaire;
    }

    public static List<Stagiaires> lectureStag(int taillechaine, RandomAccessFile raf){
        boolean fileNotFinished = true;
        List<Stagiaires> listStagiaires = new Vector<>();
        String chaine = "";
        while (fileNotFinished){
            chaine ="";
            for (int i = 0; i < taillechaine; i++) {
                try {
                    chaine += raf.readChar();
                }catch (EOFException e){
                    fileNotFinished=false;
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                chaine = chaine  + raf.readInt() ;
            }catch (EOFException e){
                fileNotFinished = false;
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(chaine);
            listStagiaires.add(fabriqueStagiaires(chaine));

        }
        return listStagiaires;
    }
    static TableView<Stagiaires> table;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ArbreDansLeRAF arbreDansLeRAF = new ArbreDansLeRAF();

        //Création de la table
        table = new TableView<>();
        table.setPrefSize(700, 490);
        table.setEditable(true);




        //Création de 5 colonnes
        TableColumn<Stagiaires, String> nomCol =
                new TableColumn<Stagiaires, String>("Nom");
        nomCol.setMinWidth(100);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        nomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("nom"));

        TableColumn<Stagiaires, String> prenomCol = new TableColumn<Stagiaires, String>("Prénom");
        nomCol.setMinWidth(100);
        //spécifier une préférence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        prenomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("prenom"));

        TableColumn<Stagiaires, String> departementCol = new TableColumn<Stagiaires, String>("Département");
        departementCol.setMinWidth(100);
        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        departementCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("departement"));


        TableColumn<Stagiaires, String> promoCol = new TableColumn<Stagiaires, String>("Promotion");
        promoCol.setMinWidth(100);
        //spécifier une préférence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        promoCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("promo"));

        TableColumn<Stagiaires, Integer> annéeCol = new TableColumn<>("Année");
        annéeCol.setMinWidth(100);
        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        annéeCol.setCellValueFactory(
                new PropertyValueFactory<>("Année"));



        //Creation des zones de textes et boutons

        TextField prenomTextField = new TextField();
        prenomTextField.setPromptText("Prénom");

        TextField nomTextField = new TextField();
        nomTextField.setPromptText("Nom");

        TextField departementTextField = new TextField();
        departementTextField.setPromptText("Département");

        TextField promoTextField = new TextField();
        promoTextField.setPromptText("Promotion");

        TextField annéeTextField = new TextField();
        annéeTextField.setPromptText("Année");

        TextField resultatRechercheTextField = new TextField();
        annéeTextField.setPromptText("Année ");



        Button modifierButton = new Button("Modifier");
        Button ajouterButton = new Button("Ajouter");
        Button rechercherButton = new Button("Rechercher");
        Button supprimerButton = new Button("Supprimer");
        Button exporterButton = new Button("Exporter");

        nomTextField.setMaxWidth(170);
        prenomTextField.setMaxWidth(170);
        departementTextField.setMaxWidth(170);
        promoTextField.setMaxWidth(170);
        annéeTextField.setMaxWidth(170);

        rechercherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nomAchercher = nomTextField.getText();
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                List<Stagiaires> StagiaireTrouve = rechercheStag(0, 1317, raf, nomAchercher);
                table.getItems().clear();
                table.setItems((FXCollections.observableArrayList(StagiaireTrouve)));
            }


        });

        // Ajout des Events sur les buttons

        ajouterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stagiaires contact = new Stagiaires(prenomTextField.getText(),nomTextField.getText(),
                        departementTextField.getText(),
                        promoTextField.getText()
                        ,annéeTextField.getText());
                table.getItems().add(contact);
                prenomTextField.clear();
                nomTextField.clear();
                departementTextField.clear();
                promoTextField.clear();
                annéeTextField.clear();
            }
        });

        // In case of empty fields , gives alert for respective empty field and requests focus on that field










        // Text fields : Nom , prénom , departement , promo , année
        //  Boutons : trier  / ajouter/ rechercher / exporter



        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nomTextField,prenomTextField,departementTextField,promoTextField,annéeTextField);

        // Ajouter les items ci dessus dans une Hbox

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10,10,10,10));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(modifierButton,ajouterButton,rechercherButton,supprimerButton,exporterButton);

        //On ajoute les cinq colonnes à la table
        table.getColumns().addAll(nomCol, prenomCol,departementCol,promoCol,annéeCol);

        //Instanciation de la classe LesStagiaires

        LesStagiaires lesStagiaires = new LesStagiaires("C:\\Projet1\\listeStagiaire.txt");

        // Creer une liste observable des Stagiaires
        RandomAccessFile raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
        ObservableList<Stagiaires>list = FXCollections.observableArrayList(lectureStag(65,raf));

        // Ajout de la liste au tableau "table"

        table.setItems(list);

        // on crée une deuxième Hbox et un label pour placer le table et un label pour le rendre plus esthétique
        // Label lbl2 = new Label("Résultat de la recherche :");



        supprimerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list.remove(table.getSelectionModel().getSelectedItem());
            }
        });

        // MenuBar
        MenuBar menuBar = new MenuBar();

        // menus
        Menu connexionMenu = new Menu("Connexion");
        Menu compteMenu = new Menu("Compte");
        Menu aideMenu = new Menu("Aide");

        // MenuItems pour les menus ci dessus

        MenuItem superAdminMenuItem = new MenuItem("super-administrateur");
        MenuItem administrateurMenuItem = new MenuItem("administrateur");
        MenuItem modifierIdMenuItem = new MenuItem("modifier mes identifiants");
        MenuItem documentationUtilisateurMenuItem = new MenuItem("documentation utilisateur");
        MenuItem retourMenuItem = new MenuItem("retour");
        MenuItem quitterItem = new MenuItem("Quitter");

        retourMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene_Acceuil scene_acceuil = new Scene_Acceuil();
                try {
                    scene_acceuil.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        // Spécifier un raccourci clavier au menuItem Quitter.

        quitterItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        // Gestion du click sur le menuItem Quitter.

        quitterItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        //SeparatorMenuItem.

        SeparatorMenuItem separator= new SeparatorMenuItem();

        // Ajouter les menuItems aux Menus
        connexionMenu.getItems().addAll(retourMenuItem,quitterItem);
        compteMenu.getItems().add(modifierIdMenuItem);
        aideMenu.getItems().addAll(documentationUtilisateurMenuItem);


        // Ajouter les Menus au MenuBar
        menuBar.getMenus().addAll(connexionMenu,compteMenu,aideMenu);

        Button menuPrincipaleButton = new Button();


        VBox vbox2 = new VBox(table);

        BorderPane root = new BorderPane(null,menuBar,vBox1,hBox,vbox2);

        root.setTop(menuBar);
        root.setLeft(vbox2);
        root.setBottom(hBox);
        root.setAlignment(hBox,Pos.BOTTOM_CENTER);
        root.setRight(vBox1);
        vBox1.setPadding(new Insets(15));
        vBox1.setSpacing(10);
        hBox.setPadding(new Insets(40));
        root.setMargin(vbox2,new Insets(18));




        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheettbdsuperadmin.css").toExternalForm());
        primaryStage.setTitle("Annuaire des Stagiaires");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static List<Stagiaires> rechercheStag(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche) {

        int pivot = 0;
        List<Stagiaires> nomStagActuel;

        try {

            if (borneInf <= borneSup) {
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot * LONGUEUSTAGIARE);
                nomStagActuel = lectureStag(65, raf);
                if (nomStagRecherche.compareToIgnoreCase(nomStagActuel.get(0).getNom()) == 0) {
                    raf.seek(pivot * LONGUEUSTAGIARE);
                    System.out.println("Utilisateur trouvé");
                    System.out.println(nomStagActuel.get(0).getNom());
                    System.out.println(nomStagActuel.get(0).getPrenom());
                    System.out.println(nomStagActuel.get(0).getDepartement());
                    System.out.println(nomStagActuel.get(0).getAnnée());
                    System.out.println(nomStagActuel.get(0).getPromo());



                    return nomStagActuel;


                } else {

                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel.get(0).getNom()) < 0) {
                        return rechercheStag(borneInf, pivot - 1, raf, nomStagRecherche);
                    } else {
                        return rechercheStag(pivot + 1, borneSup, raf, nomStagRecherche);
                    }
                }

            } else {

                System.out.println("\r\n***** Stagiaire introuvable *****");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



}



