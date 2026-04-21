package abstraction.eq3Producteur3;

/** @author Vassili Spiridonov*/
public class Agriculteurs3 {
    private int nbCDI;  
    private int nbInterim;
    private int nbEnfant;    
    private double salaireCDI; 
    private double salaireInterim;     
    private double salaireEnfant;   


    public void Agriculteur3(Plantation3 plantation) {
        this.nbCDI = 8 * plantation.getNbHectareTotal(); // 8 employés adultes par hectare
    }
    
    public Agriculteurs3(Plantation3 plantation) {
        this.nbCDI = 8 * plantation.getNbHectareTotal();
        this.nbInterim = 0;
        this.nbEnfant = 0; // Entrerpise éthique : aucun enfants exploités 
        this.salaireCDI = 12.0; // On les rémunères au max décidé dans les règles de fonctionnement (0.8€/jour)
        this.salaireInterim = 18.0; // On paye plus chère les intérimaires
        this.salaireEnfant = 3.0;  // D'après les règles de fonctionnemments : 0.2€/jour 
    }

    public double getForceDeTravailTotale() {
        return this.nbCDI + (this.nbEnfant * 0.5) + this.nbInterim; /* Les enfants comptent pour 0.5 d'un adulte.
     */
    }

    public double getCoutMainOeuvreTotal() {
        return (this.nbCDI * this.salaireCDI) + (this.nbInterim * this.salaireInterim) + (this.nbInterim ) +
               (this.nbEnfant * this.salaireEnfant);
    }

    //On Embauche des CDI 
    public void EmbaucheCDI(int nb) { 
        this.nbCDI += Math.max(0, nb);
    }

    //On licencie des CDI
    public void licencieCDI(int nb) {
        this.nbCDI = Math.max(0, this.nbCDI - nb);
    }

    //Recrutement ponctuel des intérimaires 
    public void setNbInterim(int nb) {
        this.nbInterim = Math.max(0, nb);
    }


    


    //Cette fonction décrit notre engagement éthique 
    
    public boolean estEthique() {
        boolean PasExploitationEnfant = this.nbEnfant == 0;
        boolean SalaireMinimum = this.salaireCDI >= 7.5; //Salaire minimum de 0.5€/jour 
        double totalAdultes = this.nbCDI + this.nbInterim;
        boolean ContratLongTerme = false;
        if (totalAdultes > 0) {
            ContratLongTerme = (this.nbCDI / totalAdultes) >= 0.8;
        }

        return PasExploitationEnfant && SalaireMinimum && ContratLongTerme;
    }

    //Verification de notre éligibilité 
    public String getStatutHappyWorker() {
        if (this.estEthique()) {
            return "Eligible au label Happy Worker";
        }
        return "Non éligible au label Happy Worker.";
    }
 

   
}