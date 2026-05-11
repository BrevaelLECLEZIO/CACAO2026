package abstraction.eq5Transformateur2;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.bourseCacao.BourseCacao;

/**
 * @author Pierre GUTTIEREZ
 */
public class Transformateur2AchatAppelOffre extends Transformateur2AcheteurBourse implements IAcheteurAO{

    public Transformateur2AchatAppelOffre() {
        super();
    }

	public OffreVente choisirOV(List<OffreVente> propositions){
	// 1. Sécurité : si aucune proposition n'est faite, on arrête tout de suite
	if (propositions == null || propositions.isEmpty()) {
            return null;
        }

        OffreVente meilleureOffre = null;
        
        // 2. Optimisation : On récupère le cours de la bourse UNE SEULE FOIS avant la boucle
        double coursMQ = ((BourseCacao) (Filiere.LA_FILIERE.getActeur("BourseCacao"))).getCours(Feve.F_MQ).getValeur();
        double seuilPaiement = (this.getStock_feve(Feve.F_MQ) < 1000) ? 1.05 : 0.98;

        // 3. Boucle moderne (For-Each) plus lisible
        for (OffreVente ov : propositions) {
            
            // On vérifie que le prix total est inférieur ou égal à 97% du cours de la bourse pour cette quantité
            if (ov.getPrixT() <= ov.getQuantiteT() * coursMQ * seuilPaiement) {

                
                // Si c'est la première offre valide (meilleureOffre == null) 
                // OU si cette offre est MOINS CHÈRE que l'ancienne meilleure offre trouvée
                if (meilleureOffre == null || ov.getPrixT() < meilleureOffre.getPrixT()) {
                    meilleureOffre = ov; // On la retient
                }
            }
        }
        
        // 4. Sécurité pour le journal : On s'assure qu'on a bien trouvé une offre avant de logguer
        if (meilleureOffre != null) {
            this.getJournaux().get(7).ajouter("Achat fève en AO : " + meilleureOffre.toString() + "\n");
        }
        
        return meilleureOffre;
    }
}
    