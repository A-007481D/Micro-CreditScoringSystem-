package com.microfin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    
    private static final String URL = "jdbc:postgresql://localhost:5432/microfin";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Connexion à la base de données établie");
            initializeTables();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL non trouvé: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la reconnexion: " + e.getMessage());
        }
        return connection;
    }

    private void initializeTables() {
        try (Statement stmt = connection.createStatement()) {
            
            // Table Employes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employes (
                    id SERIAL PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    date_naissance DATE NOT NULL,
                    ville VARCHAR(100),
                    nombre_enfants INTEGER DEFAULT 0,
                    investissement DECIMAL(15,2) DEFAULT 0,
                    placement DECIMAL(15,2) DEFAULT 0,
                    situation_familiale VARCHAR(50),
                    created_at DATE DEFAULT CURRENT_DATE,
                    score INTEGER DEFAULT 0,
                    client_existant BOOLEAN DEFAULT FALSE,
                    salaire DECIMAL(15,2) NOT NULL,
                    anciennete INTEGER DEFAULT 0,
                    poste VARCHAR(100),
                    type_contrat VARCHAR(50),
                    secteur VARCHAR(50)
                )
            """);

            // Table Professionnels
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS professionnels (
                    id SERIAL PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    date_naissance DATE NOT NULL,
                    ville VARCHAR(100),
                    nombre_enfants INTEGER DEFAULT 0,
                    investissement DECIMAL(15,2) DEFAULT 0,
                    placement DECIMAL(15,2) DEFAULT 0,
                    situation_familiale VARCHAR(50),
                    created_at DATE DEFAULT CURRENT_DATE,
                    score INTEGER DEFAULT 0,
                    client_existant BOOLEAN DEFAULT FALSE,
                    revenu DECIMAL(15,2) NOT NULL,
                    immatriculation_fiscale VARCHAR(100),
                    secteur_activite VARCHAR(100),
                    activite VARCHAR(100)
                )
            """);

            // Table Credits
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS credits (
                    id SERIAL PRIMARY KEY,
                    client_id BIGINT NOT NULL,
                    client_type VARCHAR(20) NOT NULL,
                    date_credit DATE DEFAULT CURRENT_DATE,
                    montant_demande DECIMAL(15,2) NOT NULL,
                    montant_octroye DECIMAL(15,2) NOT NULL,
                    taux_interet DECIMAL(5,2) NOT NULL,
                    duree_mois INTEGER NOT NULL,
                    type_credit VARCHAR(50),
                    decision VARCHAR(50),
                    score_au_moment INTEGER,
                    actif BOOLEAN DEFAULT TRUE
                )
            """);

            // Table Echeances
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS echeances (
                    id SERIAL PRIMARY KEY,
                    credit_id BIGINT NOT NULL,
                    date_echeance DATE NOT NULL,
                    mensualite DECIMAL(15,2) NOT NULL,
                    date_paiement DATE,
                    statut_paiement VARCHAR(50),
                    numero_echeance INTEGER,
                    FOREIGN KEY (credit_id) REFERENCES credits(id) ON DELETE CASCADE
                )
            """);

            // Table Incidents
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS incidents (
                    id SERIAL PRIMARY KEY,
                    date_incident DATE DEFAULT CURRENT_DATE,
                    echeance_id BIGINT NOT NULL,
                    score INTEGER,
                    type_incident VARCHAR(50),
                    client_id BIGINT NOT NULL,
                    FOREIGN KEY (echeance_id) REFERENCES echeances(id) ON DELETE CASCADE
                )
            """);

            System.out.println("✓ Tables initialisées avec succès");
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation des tables: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture: " + e.getMessage());
        }
    }
}
