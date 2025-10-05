package com.microfin.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    private DBConnection() {
        try {
            // Load database configuration
            Properties props = new Properties();
            InputStream input = null;
            
            try {
                input = new FileInputStream("config.properties");
                props.load(input);
            } catch (IOException e) {
                // If config file not found, use defaults
                props.setProperty("db.url", "jdbc:postgresql://localhost:5432/microfin");
                props.setProperty("db.username", "postgres");
                props.setProperty("db.password", "postgres");
                
                System.out.println("Using default database configuration. Create a config.properties file to customize.");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        System.err.println("Error closing input stream: " + e.getMessage());
                    }
                }
            }

            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");

            // Register PostgreSQL driver
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
            }
            
            // Initialize database and tables
            initializeDatabase();
            
            System.out.println("✓ Database connection initialized successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database connection", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    private void initializeDatabase() {
        String[] initScripts = {
            // Employes table - flat structure with all Personne fields
            "CREATE TABLE IF NOT EXISTS employes (" +
                "id SERIAL PRIMARY KEY, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "date_naissance DATE NOT NULL, " +
                "ville VARCHAR(100), " +
                "nombre_enfants INT DEFAULT 0, " +
                "investissement DOUBLE PRECISION DEFAULT 0, " +
                "placement DOUBLE PRECISION DEFAULT 0, " +
                "situation_familiale VARCHAR(50), " +
                "created_at DATE DEFAULT CURRENT_DATE, " +
                "score INT DEFAULT 0, " +
                "client_existant BOOLEAN DEFAULT false, " +
                "salaire DOUBLE PRECISION NOT NULL, " +
                "anciennete INT NOT NULL, " +
                "poste VARCHAR(100), " +
                "type_contrat VARCHAR(50), " +
                "secteur VARCHAR(50)" +
            ");",
            
            // Professionnels table - flat structure with all Personne fields
            "CREATE TABLE IF NOT EXISTS professionnels (" +
                "id SERIAL PRIMARY KEY, " +
                "nom VARCHAR(100) NOT NULL, " +
                "prenom VARCHAR(100) NOT NULL, " +
                "date_naissance DATE NOT NULL, " +
                "ville VARCHAR(100), " +
                "nombre_enfants INT DEFAULT 0, " +
                "investissement DOUBLE PRECISION DEFAULT 0, " +
                "placement DOUBLE PRECISION DEFAULT 0, " +
                "situation_familiale VARCHAR(50), " +
                "created_at DATE DEFAULT CURRENT_DATE, " +
                "score INT DEFAULT 0, " +
                "client_existant BOOLEAN DEFAULT false, " +
                "revenu DOUBLE PRECISION NOT NULL, " +
                "immatriculation_fiscale VARCHAR(100), " +
                "secteur_activite VARCHAR(50), " +
                "activite VARCHAR(200)" +
            ");",
            
            // Credits table
            "CREATE TABLE IF NOT EXISTS credits (" +
                "id SERIAL PRIMARY KEY, " +
                "client_id INT NOT NULL, " +
                "client_type VARCHAR(50) NOT NULL, " +
                "montant_demande DOUBLE PRECISION NOT NULL, " +
                "montant_octroye DOUBLE PRECISION, " +
                "taux_interet DOUBLE PRECISION, " +
                "duree_mois INT NOT NULL, " +
                "type_credit VARCHAR(50) NOT NULL, " +
                "decision VARCHAR(50), " +
                "score_au_moment INT, " +
                "actif BOOLEAN DEFAULT true, " +
                "date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "date_octroi TIMESTAMP" +
            ");",
            
            // Echeances table
            "CREATE TABLE IF NOT EXISTS echeances (" +
                "id SERIAL PRIMARY KEY, " +
                "credit_id INT REFERENCES credits(id), " +
                "numero_echeance INT NOT NULL, " +
                "date_echeance DATE NOT NULL, " +
                "date_paiement DATE, " +
                "montant DOUBLE PRECISION NOT NULL, " +
                "statut VARCHAR(50) DEFAULT 'A_PAYER', " +
                "jours_retard INT DEFAULT 0" +
            ");",
            
            // Incidents table
            "CREATE TABLE IF NOT EXISTS incidents (" +
                "id SERIAL PRIMARY KEY, " +
                "date_incident DATE NOT NULL, " +
                "echeance_id INT REFERENCES echeances(id), " +
                "client_id INT NOT NULL, " +
                "type_incident VARCHAR(50) NOT NULL, " +
                "score_impact INT NOT NULL, " +
                "commentaire TEXT" +
            ");"
        };

        Connection conn = null;
        Statement stmt = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            
            for (String script : initScripts) {
                try {
                    stmt.execute(script);
                } catch (SQLException e) {
                    System.err.println("Error executing SQL: " + script);
                    throw e;
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Error resetting auto-commit: " + e.getMessage());
                }
            }
        }
    }
}