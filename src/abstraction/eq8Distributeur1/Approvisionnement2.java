package abstraction.eq8Distributeur1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

/** @author Ewen Landron */
public class Approvisionnement2 {

    private Map<ChocolatDeMarque, Double> prixDAchat;
    private Map<ChocolatDeMarque, Double> stockPredit;
    // Structure de données : 6 listes accessibles par une clé (ex: "BQ_EQUITABLE")
    private Map<String, List<ChocolatDeMarque>> classements;

    public Approvisionnement2() {
        this.prixDAchat = new HashMap<>();
        this.classements = new HashMap<>();
        // On initialise les 6 structures demandées
        this.classements.put("BQ", new ArrayList<>());
        this.classements.put("BQ_EQUITABLE", new ArrayList<>());
        this.classements.put("MQ", new ArrayList<>());
        this.classements.put("MQ_EQUITABLE", new ArrayList<>());
        this.classements.put("HQ", new ArrayList<>());
        this.classements.put("HQ_EQUITABLE", new ArrayList<>());
    }

    /**
     * Initialise les prix avec les données historiques (1 an en arrière)
     */
    public void initialiserPrixReference() {
        List<ChocolatDeMarque> tousLesChocolats = Filiere.LA_FILIERE.getChocolatsProduits();
        
        // On remonte 24 étapes en arrière (1 an)
        int etapeReference = Filiere.LA_FILIERE.getEtape() - 24;

        for (ChocolatDeMarque cdm : tousLesChocolats) {
            double prixHisto = Filiere.LA_FILIERE.prixMoyen(cdm, etapeReference);
            this.prixDAchat.put(cdm, prixHisto);
        }
    }

    /**
     * Répartit les chocolats de la filière dans les 6 listes et les trie par prix de vente croissant
     */
    public void trierChocolatsParPrix() {
        // 1. On vide les listes pour la mise à jour
        for (List<ChocolatDeMarque> liste : classements.values()) {
            liste.clear();
        }

        // 2. Récupération de tous les chocolats produits
        List<ChocolatDeMarque> tousLesChocolats = Filiere.LA_FILIERE.getChocolatsProduits();

        // 3. Distribution dans les 6 listes selon la gamme et le label
        for (ChocolatDeMarque cdm : tousLesChocolats) {
            String cle = genererCle(cdm);
            if (classements.containsKey(cle)) {
                classements.get(cle).add(cdm);
            }
        }

        // 4. Tri de chaque liste par prix de vente croissant (basé sur notre dictionnaire prixDAchat)
        for (List<ChocolatDeMarque> liste : classements.values()) {
            Collections.sort(liste, new Comparator<ChocolatDeMarque>() {
                public int compare(ChocolatDeMarque c1, ChocolatDeMarque c2) {
                    // On récupère les prix depuis notre HashMap (dictionnaire)
                    // On utilise 1000.0 par défaut si le prix n'est pas encore répertorié
                    double prix1 = prixDAchat.getOrDefault(c1, 1000.0);
                    double prix2 = prixDAchat.getOrDefault(c2, 1000.0);
                    
                    return Double.compare(prix1, prix2);
                }
            });
        }
    }

    /**
     * Méthode utilitaire pour obtenir la clé correspondant aux 6 types
     */
    private String genererCle(ChocolatDeMarque cdm) {
        String gamme = cdm.getGamme().toString(); // "BQ", "MQ" ou "HQ"
        return cdm.isEquitable() ? gamme + "_EQUITABLE" : gamme;
    }

    /**
     * Permet au reste du code d'accéder à l'une des 6 listes triées
     */
    public List<ChocolatDeMarque> getListeTriee(Gamme gamme, boolean equitable) {
        String cle = gamme.toString() + (equitable ? "_EQUITABLE" : "");
        return this.classements.getOrDefault(cle, new ArrayList<>());
    }
    /**
     * Méthode principale d'achat
     * @param volumeCibleTotal Quantité totale de chocolat que l'on souhaite avoir en stock (tous types confondus)
     */
    public void lancerApprovisionnementGeneral(double volumeCibleTotal) {
        // 1. Initialiser le stock prédit avec le stock actuel réel (méthode à implémenter selon votre acteur)
        this.stockPredit = initialiserStockPredit();

        // 2. Définir les volumes cibles par gamme selon tes pourcentages
        double cibleBasse = volumeCibleTotal * 0.20;
        double cibleMoyenne = volumeCibleTotal * 0.45;
        double cibleHaute = volumeCibleTotal * 0.35;

        // 3. Lancer les achats pour chaque gamme
        // Ordre de priorité interne : Equitable d'abord, puis moins cher (déjà géré par le tri)
        acheterParGamme(Gamme.BQ, cibleBasse);
        acheterParGamme(Gamme.MQ, cibleMoyenne);
        acheterParGamme(Gamme.HQ, cibleHaute);
    }

    /**
     * Tente de remplir le stock d'une gamme spécifique jusqu'à atteindre son volume cible
     */
    private void acheterParGamme(Gamme gamme, double volumeCibleGamme) {
        List<ChocolatDeMarque> equitables = getListeTriee(gamme, true);
        List<ChocolatDeMarque> standards = getListeTriee(gamme, false);

        // 1. On traite les équitables
        parcourirEtAcheter(equitables, volumeCibleGamme);

        // 2. On traite les standards
        parcourirEtAcheter(standards, volumeCibleGamme);
    }

    private void parcourirEtAcheter(List<ChocolatDeMarque> liste, double volumeCibleGamme) {
        for (int i = 0; i < liste.size(); i++) {
            ChocolatDeMarque actuel = liste.get(i);
            
            // Prix Cible = Prix stocké dans notre dictionnaire (historique ou dernier achat)
            double prixCible = this.prixDAchat.getOrDefault(actuel, 1000.0);
            
            // Prix Max = Prix du produit suivant dans la liste triée
            // Si c'est le dernier de la liste, on fixe un prix plafond (ex: prixCible + 20%)
            double prixMax;
            if (i < liste.size() - 1) {
                ChocolatDeMarque suivant = liste.get(i + 1);
                prixMax = this.prixDAchat.getOrDefault(suivant, prixCible * 1.2);
            } else {
                prixMax = prixCible * 1.2; 
            }

            remplirProduit(actuel, volumeCibleGamme, prixCible, prixMax);
        }
    }

    
    /**
     * Détermine combien acheter pour un produit spécifique et appelle la méthode d'achat
     */
    private void remplirProduit(ChocolatDeMarque cdm, double volumeCibleGamme, double prixCible, double prixMax) {
        double stockActuelGamme = calculerStockGamme(cdm.getGamme());
        
        if (stockActuelGamme < volumeCibleGamme) {
            // Calcul du besoin pour atteindre la cible de la gamme
            double besoin = volumeCibleGamme - stockActuelGamme;
            
            // Appelle la méthode intermédiaire (Contrat Cadre, Enchères, etc.)
            double quantiteAchetee = methodeIntermediaireAchat(cdm, besoin, prixCible, prixMax);
            
            // Mise à jour du stock prédit
            double nouveauStock = stockPredit.getOrDefault(cdm, 0.0) + quantiteAchetee;
            stockPredit.put(cdm, nouveauStock);
        }
    }

    // --- Méthodes utilitaires à définir ---

    private double calculerStockGamme(Gamme gamme) {
        double total = 0;
        for (Map.Entry<ChocolatDeMarque, Double> entry : stockPredit.entrySet()) {
            if (entry.getKey().getGamme() == gamme) {
                total += entry.getValue();
            }
        }
        return total;
    }

    public double calculerStockTotalPredit() {
        double total = 0;
        for (double qte : stockPredit.values()) {
            total += qte;
        }
        return total;
    }

    // Méthode à coder plus tard (Contrats cadres / Enchères)
    private double methodeIntermediaireAchat(ChocolatDeMarque cdm, double besoin, double prixCible, double prixMax) {
        // TODO: Implémenter la logique d'achat réelle
        return 0.0; 
    }

    private Map<IProduit, Double> initialiserStockPredit() {
        // TODO: Récupérer les stocks réels de l'acteur
        return Distributeur1Acteur.Stock;
        
    }
}