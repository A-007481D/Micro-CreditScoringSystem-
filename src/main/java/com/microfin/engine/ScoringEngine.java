package main.java.com.microfin.engine;

import main.java.com.microfin.model.Person;

import java.sql.PreparedStatement;

public interface ScoringEngine {

    double calculateScore(Person person);
    double calculateProfessionalStability(Person person);
    double calculateFinancialCapacity(Person person);
    double calculateHistory(Person person);
    double calculateRelationClient(Person person);
    double calculateCriteresComplementaires(Person person);



}
