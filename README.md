# Système de Scoring Crédit - Microfinance Maroc

## Description
Système de scoring automatisé pour le secteur micro-finance marocain permettant l'évaluation du risque crédit, la décision automatique et l'historisation complète des paiements.

**Version Java 8 - Sans Maven - Pure Java**

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
- **Java 8** (JDK 1.8)
- **PostgreSQL** (9.5+)
- Driver JDBC PostgreSQL (téléchargé automatiquement par le script de compilation)

## Installation

### 1. Créer la base de données PostgreSQL:
```sql
CREATE DATABASE microfin;
```

### 2. Configurer la connexion à la base de données:
```bash
cp config.properties.template config.properties
# Éditez config.properties avec vos paramètres de connexion
```

### 3. Compiler le projet:
```bash
chmod +x compile.sh
./compile.sh
```

Le script de compilation:
- Télécharge automatiquement le driver PostgreSQL JDBC
- Compile tous les fichiers Java avec Java 8
- Crée un fichier JAR exécutable

### 4. Exécuter l'application:
```bash
chmod +x run.sh
./run.sh
```

Ou directement:
```bash
java -cp "out:lib/postgresql.jar" com.microfin.Main
```

### Compilation manuelle (sans scripts):
```bash
# Créer le répertoire de sortie
mkdir -p out lib

# Télécharger le driver PostgreSQL
curl -L https://jdbc.postgresql.org/download/postgresql-42.2.27.jar -o lib/postgresql.jar

# Compiler
find src/main/java -name "*.java" > sources.txt
javac -source 8 -target 8 -d out -cp "lib/postgresql.jar" @sources.txt

# Exécuter
java -cp "out:lib/postgresql.jar" com.microfin.Main
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
