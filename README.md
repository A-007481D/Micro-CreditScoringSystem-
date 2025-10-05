# Système de Scoring Crédit - Microfinance Maroc

## Description
Système de scoring automatisé pour le secteur micro-finance marocain permettant l'évaluation du risque crédit, la décision automatique et l'historisation complète des paiements.

## Fonctionnalités

### Module 1: Gestion des Clients
- Créer client (Employé/Professionnel)
- Modifier informations client
- Consulter profil client
- Supprimer client
- Lister tous les clients

### Module 2: Calcul de Score
- Scoring automatique basé sur 5 composants
- Validation des seuils d'éligibilité
- Calcul de la capacité d'emprunt

### Module 3: Gestion des Crédits
- Demande de crédit
- Décision automatique (Accord/Étude/Refus)
- Génération automatique des échéances
- Suivi des crédits

### Module 4: Historique de Paiement
- Enregistrement des paiements
- Classification automatique (À temps/Retard/Impayé)
- Gestion des incidents
- Mise à jour dynamique du score

### Module 5: Analytics
- Recherche clients éligibles crédit immobilier
- Identification clients à risque
- Tri par score, revenus, ancienneté
- Répartition par type d'emploi
- Sélection pour campagnes marketing

## Prérequis
- Java 17+
- PostgreSQL
- Maven

## Installation

1. Créer la base de données PostgreSQL:
```sql
CREATE DATABASE microfin;
```

2. Configurer la connexion dans `DBConnection.java`

3. Compiler le projet:
```bash
mvn clean compile
```

4. Exécuter:
```bash
mvn exec:java -Dexec.mainClass="com.microfin.Main"
```

## Structure du Projet
```
src/main/java/com/microfin/
├── config/          # Configuration (DBConnection)
├── enums/           # Énumérations
├── model/           # Modèles de données
├── repository/      # Couche d'accès aux données
├── engine/          # Moteurs de calcul
├── service/         # Logique métier
├── view/            # Interface console
├── util/            # Utilitaires
└── Main.java        # Point d'entrée
```

## Auteur
Système développé pour le secteur micro-finance marocain
