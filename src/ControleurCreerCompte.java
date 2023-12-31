import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurCreerCompte implements EventHandler<ActionEvent> {

    /**
     * La vue FenetreCoInsc
     */
    private FenetreCreationCompte vue;

    /**
     * La vue de l'application
     */
    private AppliVae appli;
    private ConnexionMySQL connexionMySQL;

    public ControleurCreerCompte(FenetreCreationCompte vue, AppliVae appli, ConnexionMySQL connexionMySQL) {
        this.vue = vue;
        this.appli = appli;
        this.connexionMySQL = connexionMySQL;
    }

    /**
     * L'action consiste à changer de fenêtre pour aller à la page de connexion
     * 
     * @param actionEvent l'événement action
     */
	@Override
	public void handle(ActionEvent actionEvent) {
        this.vue.setEmailErreur("");
        this.vue.setEmailMessageErreur(false);
        if (VerificateurMDP.mdpConfirmationValide(this.vue.getMdp(), this.vue.getMdpConfirmation())) {
            this.vue.setMessageMdpConfirmationErreur("");
            this.vue.setMdpConfimationErreur(false);
        } else {
            this.vue.setMessageMdpConfirmationErreur("  * Les mots de passe saisis ne correspondent pas");
            this.vue.setMdpConfimationErreur(true);
        }
        try {
            VerificateurEmail.estValide(this.vue.getMail());
            try {
                VerificateurMDP.estValide(this.vue.getMdp());
                try{
                    UtilisateurBD userBd = new UtilisateurBD(this.connexionMySQL);
                    Utilisateur user = new Utilisateur(userBd.idLibre(), vue.getPseudo(), vue.getMail(), vue.getMdp(), 2);
                    userBd.insererUtilisateur(user);
                    vue.popUpCompteValide(user.getPseudo());
                    appli.modeAccueil();
                }
                catch(SQLException e){
                    vue.popUpErreurSQL(e);
                }
            } catch (FormatMotDePasseException e) {
                this.vue.setMdpErreur();
                this.vue.setMessageErreur(e.getMessage());
            }
        } catch (EmailInvalideException e) {
            this.vue.setEmailErreur("   * Email invalide, veuillez renter un email valide.");
            this.vue.setEmailMessageErreur(true);
        }
}
}