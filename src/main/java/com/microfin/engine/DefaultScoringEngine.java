package main.java.com.microfin.engine;

import main.java.com.microfin.model.Person;

public class DefaultScoringEngine implements ScoringEngine{
    private static final double  STABILITY_WEIGHT = 0.30;
    private static final double  CAPACITY_WEIGHT = 0.30;
    private static final double  HISTORIQUE  = 0.15;
    private static final double  RELATION_WEIGHT = 0.10;
    private static final double  CRITERS_WEIGHT = 0.15;




    @Override
    public double calculateScore(Person person) {
        return 0;
    }

    @Override
    public double calculateProfessionalStability(Person person) {
        return 0;
    }

    @Override
    public double calculateFinancialCapacity(Person person) {
        return 0;
    }

    @Override
    public double calculateHistory(Person person) {
        return 0;
    }

    @Override
    public double calculateRelationClient(Person person) {
        return 0;
    }

    @Override
    public double calculateCriteresComplementaires(Person person) {
        return 0;
    }
}
