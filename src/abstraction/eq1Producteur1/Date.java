package abstraction.eq1Producteur1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Date {

    private int jour;
    private int mois;
    private int annee;

    public Date(int jour, int mois, int annee) {
        if (mois < 1 || mois > 12) throw new IllegalArgumentException("Mois invalide : " + mois);
        if (jour < 1 || jour > nbJoursDansMois()) throw new IllegalArgumentException("Jour invalide : " + jour);
        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    public String getDate() {
        return String.format("%02d/%02d/%04d", jour, mois, annee);
    }

    public boolean estPerime() {
        return toLocalDate().isBefore(LocalDate.now());
    }

    public boolean expireAujourdhui() {
        return toLocalDate().equals(LocalDate.now());
    }

    public long joursAvantPeremption() {
        return ChronoUnit.DAYS.between(LocalDate.now(), toLocalDate());
    }

    public boolean expireDansLes(int jours) {
        long reste = joursAvantPeremption();
        return reste >= 0 && reste <= jours;
    }

    // --- Utilitaires privés ---

    private boolean estBissextile() {
        return (annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0);
    }

    private int nbJoursDansMois() {
        return switch (mois) {
            case 2 -> estBissextile() ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    private LocalDate toLocalDate() {
        return LocalDate.of(annee, mois, jour);
    }

    @Override
    public String toString() {
        return getDate();
    }
}