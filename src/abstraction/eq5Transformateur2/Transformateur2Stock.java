package abstraction.eq5Transformateur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Integer;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur2Stock extends Transformateur2Acteur{

    // Attributs
    private HashMap<Feve, Double> stock_feve;
    private HashMap<Feve, Double> stock_chocolat;
    
    // Constructeur
    public Transformateur2Stock(){
        this.stock_feve = new HashMap<Feve, Double>();
        this.stock_feve.put(Feve.F_BQ, 0.0);
        this.stock_feve.put(Feve.F_BQ_E, 0.0);
        this.stock_feve.put(Feve.F_MQ, 0.0);
        this.stock_feve.put(Feve.F_MQ_E, 0.0);
        this.stock_feve.put(Feve.F_HQ, 0.0);
        this.stock_feve.put(Feve.F_HQ_E, 0.0);
        this.stock_chocolat = new HashMap<Feve, Double>();
    }

    // Méthodes

    public void add_feve(double n, Feve Q){
        if (Q == Feve.F_BQ){
            this.stock_feve.put(Feve.F_BQ, this.stock_feve.get(Feve.F_BQ) + n);
        } else if (Q == Feve.F_BQ_E){
            this.stock_feve.add(1, n);
        } else if (Q == Feve.F_MQ){
            this.stock_feve.add(2, n);
        } else if (Q == Feve.F_MQ_E){
            this.stock_feve.add(3, n);
        } else if (Q == Feve.F_HQ){
            this.stock_feve.add(4, n);
        } else {
            this.stock_feve.add(5, n);
        }
    }

    public void remove_feve(int n, Feve Q){
        if (Q == Feve.F_BQ & n <= this.stock_feve.get(0)){
            this.stock_feve.add(0, -n);
        } else if (Q == Feve.F_BQ_E & n <= this.stock_feve.get(1)){
            this.stock_feve.add(1, -n);
        } else if (Q == Feve.F_MQ & n <= this.stock_feve.get(2)){
            this.stock_feve.add(2, -n);
        } else if (Q == Feve.F_MQ_E & n <= this.stock_feve.get(3)){
            this.stock_feve.add(3, -n);
        } else if (Q == Feve.F_HQ & n <= this.stock_feve.get(4)){
            this.stock_feve.add(4, -n);
        } else if (n <= this.stock_feve.get(5)){
            this.stock_feve.add(5, -n);
        }
    }
}
