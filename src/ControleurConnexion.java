import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ControleurConnexion implements EventHandler<ActionEvent>{
    
        /**
     * La vue FenetreCoInsc
     */ 
    private FenetreDeLogin vue;

    /**
     * La vue de l'application
     */
    private AppliVae appli;
    private ConnexionMySQL connexionMySQL;
    private Map<String, Object> laMap;

    public ControleurConnexion(FenetreDeLogin vue, AppliVae appli, ConnexionMySQL connexionMySQL){
        this.vue = vue;
        this.appli = appli;
        this.connexionMySQL = connexionMySQL;
        laMap = new HashMap<>();
    }

    /**
     * L'action consiste à changer de fenêtre pour aller à la page de connexion
     * @param actionEvent l'événement action
     */
	@Override
	public void handle(ActionEvent actionEvent) {
        System.out.println("avant");
        try{
            UtilisateurBD userBd = new UtilisateurBD(connexionMySQL);
            String mail = vue.getEmail();
            this.laMap = userBd.rechercherJoueurParMail(mail);
            if (this.laMap == null){
                String pseudo = vue.getEmail();
                this.laMap = userBd.rechercherJoueurParPseudo(pseudo);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        try {
            if (this.vue.getEmail().equals("erreur")) throw new Exception();
            this.vue.setEmailErreur(false);
            this.vue.setMessageEmailErreur("");
            if(this.laMap == null){
                throw new Exception();
            }
            try {
                if (this.vue.getMdp().equals("erreur")) throw new Exception();
                if (!vue.getMdp().equals(laMap.get("mdput"))) {
                    throw new Exception();
                }
                Utilisateur userCo = new Utilisateur((Integer) laMap.get("idut"),(String)laMap.get("pseudout"), (String) laMap.get("emailut"), (String) laMap.get("mdput"),true, (Integer) laMap.get("idrole"));
                appli.setUtilisateurActuel(userCo);
                vue.popUpCompteConnecte((String) laMap.get("pseudout"));
<<<<<<< HEAD
                this.appli.modeMiseEnVente();
=======

                if (userCo.getRole() == (Roles.ADMINISTRATEUR)){
                    this.appli.modeAdministrateur();
                }
                else{
                    this.appli.modeAccueil();
                }
                
>>>>>>> main
            } catch (Exception e) {
                System.out.println("b");
                this.vue.setMdpErreur();
                this.vue.setMessageErreur("   * Mot de passe incorrect");
            }
        } catch (Exception e) {
            System.out.println("a");
            this.vue.setEmailErreur(true);
            this.vue.setMessageEmailErreur("   * Cet Email n'existe pas");
        }
	}

}
