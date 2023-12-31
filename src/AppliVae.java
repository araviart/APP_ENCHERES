import java.sql.Statement;
import java.sql.PreparedStatement;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AppliVae extends Application{
    
    /**
     * Le panel central de la vue
     */
    private Pane panelCentral;

    /**
     * La scene
     */
    private Scene scene;

    /**
     * L'utilisateur qui utilise l'appli
     */
    private Utilisateur user;

    /** 
     * La connexion à la BD pour Utilisateur
     */
    private UtilisateurBD laConnexionUtilisateur;

    /**
     * La connexion à la BD pour Vente
     */
    private VenteBD laConnexionVente;

    /**
     * La connexion à la BD pour Objet
     */
    private ObjetBD laConnexionObjet;

    /**
     * La connexion à la BD pour Encherir
     */
    private EncherirBD laConnexionEncherir;

    /**
     * Page de connexion/inscription
     */
    private FenetreCoInsc pageCoInsc;

    /**
     * Page de mise en vente
     */
    private VueVente pageVente;

    /**
     * Page d'accueil
     */
    private PageAccueil pageAccueil;

    /**
     * Page du profil utilisateur
     */
    private PageProfilUtilisateur pageProfilUtilisateur;

    /**
     * La connexion à la BD
     */
    private ConnexionMySQL connexionMySQL;

    /**
     * La barre de navigation
     */
    private NavBar navBar;

    private BorderPane root;

    private Utilisateur utilisateurActuel;

    private PageProfilObjet pageProfilObjet;

    private VueAdminGestionUtilisateurs vueAdminGestionUtilisateurs;

    private FenetrePageMessage fenetrePageMessage;

    private VueEncheresUtilisateur vueEncheresUtilisateur;

    //private Objet objetActuel;



    /**
     * Initialise tous les attributs
     * @throws ClassNotFoundException
     */
    @Override
    public void init() throws ClassNotFoundException{
        this.laScene();
        try{
            this.connexionMySQL = new ConnexionMySQL();
            this.connexionMySQL.connecter();
        }
        catch(Exception e){System.out.println(e);}
        this.laConnexionUtilisateur = new UtilisateurBD(this.connexionMySQL);
        this.laConnexionVente = new VenteBD(this.connexionMySQL);
        this.laConnexionObjet = new ObjetBD(this.connexionMySQL);
        this.laConnexionEncherir = new EncherirBD(this.connexionMySQL);

        this.pageCoInsc = new FenetreCoInsc(this, this.connexionMySQL);
        this.pageAccueil = new PageAccueil(this, this.connexionMySQL);
        this.pageProfilUtilisateur = new PageProfilUtilisateur(this, this.connexionMySQL, this.utilisateurActuel);
        this.navBar = new NavBar(this, this.connexionMySQL);
        this.fenetrePageMessage = new FenetrePageMessage(this, this.connexionMySQL);
        this.root = (BorderPane) this.scene.getRoot();
    }

    /**
     * Crée la scène
     */
    private void laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setCenter(this.panelCentral);
        this.scene = new Scene(fenetre, 1080, 1920);
    }

    /**
     * Permet de passer à l'affichage de la page d'inscription/connexion
     */
    public void modeCoInsc(){
        scene.getStylesheets().setAll("file:src/styleCoInsc.css");
        this.root.setTop(null);
        this.pageCoInsc.resetTF();
        this.root.setCenter(this.pageCoInsc);
    }

    /**
     * Permet de passer à l'affichage de la page de mise en vente
     */
    public void modeMiseEnVente(){
        scene.getStylesheets().setAll("file:src/styleNavBar.css");
        this.root.setTop(this.navBar);
        this.pageVente = new VueVente(this, this.connexionMySQL, this.utilisateurActuel);
        this.root.setCenter(pageVente);
    }

    /**
     * Permet de passer à l'affichage de la page d'accueil
     */
    public void modeAccueil(){
        this.pageVente = new VueVente(this, this.connexionMySQL, utilisateurActuel);
        this.vueEncheresUtilisateur = new VueEncheresUtilisateur(this, this.connexionMySQL, this.utilisateurActuel.getId(), this.utilisateurActuel);
        
        scene.getStylesheets().setAll("styleNavBar.css", "styleCoInsc.css");
        this.root.setTop(this.navBar);
        this.root.setCenter(this.pageAccueil);
    }

    /**
     * Permet de passer à l'affichage de la page profil de l'utilisateur
     */
    public void modeProfilUtilisateur(){
        scene.getStylesheets().setAll("styleNavBar.css", "stylePageUtilisateur.css");
        this.root.setTop(this.navBar);
        this.root.setCenter(this.pageProfilUtilisateur);
    }

    public void modeProfilObjet(Vente vente){
        this.pageProfilObjet = new PageProfilObjet(this, this.connexionMySQL, vente, this.utilisateurActuel);
        scene.getStylesheets().setAll("styleNavBar.css", "stylePageProfilObjet.css");
        this.root.setTop(this.navBar);
        this.root.setCenter(this.pageProfilObjet);
    }

    public void modeAdministrateur(){
        this.vueAdminGestionUtilisateurs = new VueAdminGestionUtilisateurs(this.connexionMySQL, utilisateurActuel);
        this.root.setCenter(this.vueAdminGestionUtilisateurs);
    }

    public void modeMessage(){
        scene.getStylesheets().setAll("styleNavBar.css", "stylePageMessagerie.css");
        this.root.setTop(this.navBar);
        this.root.setCenter(this.fenetrePageMessage);
    }

    public void modeEnchereUtilisateur(){
        this.root.setTop(this.navBar);
        this.root.setCenter(this.vueEncheresUtilisateur);
    }


    public void setUtilisateurActuel(Utilisateur utilisateur){
        this.utilisateurActuel = utilisateur;
    }

    public boolean isConnect(){
        return false;
    }
    
    public PreparedStatement preparedStatement(String requete){
        return null;
    }

    public PageAccueil getPageAccueil() { return this.pageAccueil; }

    /*
    public void setObjet(Objet objet){
        this.objetActuel = objet;
    }
    */

    @Override
    public void start(Stage stage){
        stage.setTitle("Appli VAE");
        stage.setHeight(1080);
        stage.setWidth(1920);
        stage.setFullScreen(false);
        stage.setFullScreenExitHint("");
        stage.setScene(this.scene);
        this.modeCoInsc();
        stage.show();
    }

    /**
     * Programme principal
     * @param args
     */
    public static void main(String[] args){
        launch(args);
    }
}
