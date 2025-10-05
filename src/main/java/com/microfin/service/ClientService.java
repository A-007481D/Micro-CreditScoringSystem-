package com.microfin.service;

import com.microfin.engine.ScoringEngine;
import com.microfin.model.Employe;
import com.microfin.model.Personne;
import com.microfin.model.Professionnel;
import com.microfin.repository.EmployeRepository;
import com.microfin.repository.ProfessionnelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientService {
    
    private final EmployeRepository employeRepository;
    private final ProfessionnelRepository professionnelRepository;
    private final ScoringEngine scoringEngine;

    public ClientService() {
        this.employeRepository = new EmployeRepository();
        this.professionnelRepository = new ProfessionnelRepository();
        this.scoringEngine = new ScoringEngine();
    }

    public Employe creerEmploye(Employe employe) {
        int score = scoringEngine.calculerScore(employe);
        employe.setScore(score);
        return employeRepository.save(employe);
    }

    public Professionnel creerProfessionnel(Professionnel professionnel) {
        int score = scoringEngine.calculerScore(professionnel);
        professionnel.setScore(score);
        return professionnelRepository.save(professionnel);
    }

    public boolean modifierEmploye(Employe employe) {
        int score = scoringEngine.calculerScore(employe);
        employe.setScore(score);
        return employeRepository.update(employe);
    }

    public boolean modifierProfessionnel(Professionnel professionnel) {
        int score = scoringEngine.calculerScore(professionnel);
        professionnel.setScore(score);
        return professionnelRepository.update(professionnel);
    }

    public Optional<Personne> consulterClient(String type, Long id) {
        if ("EMPLOYE".equalsIgnoreCase(type)) {
            return employeRepository.findById(id).map(e -> (Personne) e);
        } else {
            return professionnelRepository.findById(id).map(p -> (Personne) p);
        }
    }

    public boolean supprimerClient(String type, Long id) {
        if ("EMPLOYE".equalsIgnoreCase(type)) {
            return employeRepository.delete(id);
        } else {
            return professionnelRepository.delete(id);
        }
    }

    public List<Personne> listerTousLesClients() {
        List<Personne> clients = new ArrayList<>();
        clients.addAll(employeRepository.findAll());
        clients.addAll(professionnelRepository.findAll());
        return clients;
    }

    public void recalculerScore(Personne personne) {
        int nouveauScore = scoringEngine.calculerScore(personne);
        personne.setScore(nouveauScore);
        
        if (personne instanceof Employe) {
            employeRepository.update((Employe) personne);
        } else if (personne instanceof Professionnel) {
            professionnelRepository.update((Professionnel) personne);
        }
    }

    public List<Employe> listerEmployes() {
        return employeRepository.findAll();
    }

    public List<Professionnel> listerProfessionnels() {
        return professionnelRepository.findAll();
    }
}
