package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.StatutPaiement;
import com.microfin.model.Echeance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EcheanceRepository {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    public Echeance save(Echeance echeance) {
        String sql = "INSERT INTO echeances (credit_id, date_echeance, montant, date_paiement, " +
                "statut, numero_echeance) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, echeance.getCreditId());
            stmt.setDate(2, Date.valueOf(echeance.getDateEcheance()));
            stmt.setDouble(3, echeance.getMensualite());
            
            if (echeance.getDateDePaiement() != null) {
                stmt.setDate(4, Date.valueOf(echeance.getDateDePaiement()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            
            stmt.setString(5, echeance.getStatutPaiement().name());
            stmt.setInt(6, echeance.getNumeroEcheance());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                echeance.setId(rs.getLong(1));
            }
            
            return echeance;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return null;
        }
    }

    public Optional<Echeance> findById(Long id) {
        String sql = "SELECT * FROM echeances WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToEcheance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    /**
     * Trouve toutes les échéances d'un crédit
     * @param creditId ID du crédit
     * @return Liste des échéances triées par date d'échéance
     */
    public List<Echeance> findByCreditId(Long creditId) {
        List<Echeance> echeances = new ArrayList<>();
        String sql = "SELECT * FROM echeances WHERE credit_id = ? ORDER BY date_echeance";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, creditId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                echeances.add(mapResultSetToEcheance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des échéances: " + e.getMessage());
        }
        
        return echeances;
    }
    
    /**
     * Trouve les échéances en retard d'un crédit
     * @param creditId ID du crédit
     * @return Liste des échéances en retard
     */
    public List<Echeance> findEnRetardByCreditId(Long creditId) {
        List<Echeance> echeances = new ArrayList<>();
        // Récupère toutes les échéances non payées et en retard
        String sql = "SELECT * FROM echeances WHERE credit_id = ? AND date_paiement IS NULL AND date_echeance < CURRENT_DATE - INTERVAL '4 days' ORDER BY date_echeance";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, creditId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                echeances.add(mapResultSetToEcheance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des échéances en retard: " + e.getMessage());
        }
        
        return echeances;
    }
    
    /**
     * Trouve les échéances impayées d'un crédit
     * @param creditId ID du crédit
     * @return Liste des échéances impayées (en retard de plus de 30 jours)
     */
    public List<Echeance> findImpayeesByCreditId(Long creditId) {
        List<Echeance> echeances = new ArrayList<>();
        // Récupère toutes les échéances non payées et en retard de plus de 30 jours
        String sql = "SELECT * FROM echeances WHERE credit_id = ? AND date_paiement IS NULL AND date_echeance < CURRENT_DATE - INTERVAL '30 days' ORDER BY date_echeance";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, creditId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                echeances.add(mapResultSetToEcheance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des échéances impayées: " + e.getMessage());
        }
        
        return echeances;
    }
    
    /**
     * Trouve les échéances à payer (non échues ou moins de 5 jours de retard)
     * @param creditId ID du crédit
     * @return Liste des échéances à payer
     */
    public List<Echeance> findAPayerByCreditId(Long creditId) {
        List<Echeance> echeances = new ArrayList<>();
        // Récupère toutes les échéances non payées et pas encore en retard de plus de 5 jours
        String sql = "SELECT * FROM echeances WHERE credit_id = ? AND date_paiement IS NULL AND (date_echeance >= CURRENT_DATE - INTERVAL '4 days' OR date_echeance > CURRENT_DATE) ORDER BY date_echeance";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, creditId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                echeances.add(mapResultSetToEcheance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des échéances à payer: " + e.getMessage());
        }
        
        return echeances;
    }

    /**
     * Met à jour une échéance dans la base de données
     * @param echeance L'échéance à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean update(Echeance echeance) {
        // Mettre à jour le statut avant de sauvegarder
        echeance.setStatutPaiement(echeance.determinerStatut());
        
        String sql = "UPDATE echeances SET date_paiement = ?, statut = ?, date_echeance = ?, montant = ?, numero_echeance = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            // Gestion de la date de paiement
            if (echeance.getDateDePaiement() != null) {
                stmt.setDate(1, Date.valueOf(echeance.getDateDePaiement()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            
            // Mise à jour du statut
            stmt.setString(2, echeance.getStatutPaiement().name());
            
            // Mise à jour des autres champs
            stmt.setDate(3, Date.valueOf(echeance.getDateEcheance()));
            stmt.setDouble(4, echeance.getMensualite());
            stmt.setInt(5, echeance.getNumeroEcheance());
            stmt.setLong(6, echeance.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✓ Échéance " + echeance.getId() + " mise à jour avec succès");
                return true;
            } else {
                System.err.println("✗ Aucune échéance trouvée avec l'ID: " + echeance.getId());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'échéance: " + e.getMessage());
            return false;
        }
    }

    /**
     * Convertit un ResultSet en objet Echeance
     * @param rs Le ResultSet à convertir
     * @return L'objet Echeance créé
     * @throws SQLException En cas d'erreur lors de la lecture du ResultSet
     */
    private Echeance mapResultSetToEcheance(ResultSet rs) throws SQLException {
        Echeance echeance = new Echeance();
        echeance.setId(rs.getLong("id"));
        echeance.setCreditId(rs.getLong("credit_id"));
        echeance.setNumeroEcheance(rs.getInt("numero_echeance"));
        
        // Gestion de la date d'échéance
        echeance.setDateEcheance(rs.getDate("date_echeance").toLocalDate());
        
        // Montant de la mensualité
        echeance.setMensualite(rs.getDouble("montant"));
        
        // Gestion de la date de paiement
        Date datePaiement = rs.getDate("date_paiement");
        if (datePaiement != null) {
            echeance.setDateDePaiement(datePaiement.toLocalDate());
        }
        
        // Gestion du statut
        try {
            String statutStr = rs.getString("statut");
            if (statutStr != null && !statutStr.isEmpty()) {
                echeance.setStatutPaiement(StatutPaiement.valueOf(statutStr));
            } else {
                // Si le statut n'est pas défini, on le calcule
                echeance.setStatutPaiement(echeance.determinerStatut());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Statut inconnu dans la base de données, utilisation du statut par défaut");
            echeance.setStatutPaiement(echeance.determinerStatut());
        }
        
        return echeance;
    }
}
