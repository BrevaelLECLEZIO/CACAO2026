package abstraction.eq2Producteur2;

import java.awt.Color;
import java.net.http.HttpResponse.BodySubscribers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

/**@author Simon */

public class Producteur2Bourse extends Producteur2VenteCC implements IVendeurBourse{

	private Journal journalBourse;
	
	public Producteur2Bourse() {

		super();
		this.journalBourse = new Journal(" journal Bourse Eq2", this);
		

	}

	public double offre(Feve f, double cours) {
		SetStockMin(0.1);

		double offre = 0;

		if (stockvar.containsKey(f) && cout_unit_t.containsKey(f) && seuil_stock.containsKey(f)) {
			
			double stock_a = stockvar.get(f).getValeur();
			double a_garder = restantDu(f);
			
			// Calcul du prix minimal voulu : marge de 20% (x1.2) par défaut
			double marge = 1.2;
			// Si la fève est équitable/bio, on applique une marge de 30% (x1.3) selon votre stratégie
			if (f.isEquitable() || f.isBio()) {
				marge = 1.3;
			}
			
			double prixMinimal = cout_unit_t.get(f) * marge;

			journalBourse.ajouter("Valeur du cours de la feve " + f + " : " + cours + "\nValeur du prix minimal voulu : " + prixMinimal);

			if ((stock_a - a_garder > seuil_stock.get(f)) && (prixMinimal < cours)) {
				offre = stock_a - a_garder - seuil_stock.get(f);
				journalBourse.ajouter(Filiere.LA_FILIERE.getEtape() + " Je mets en vente " + offre + " T de " + f);
			}
		}

		return offre;
	}

}