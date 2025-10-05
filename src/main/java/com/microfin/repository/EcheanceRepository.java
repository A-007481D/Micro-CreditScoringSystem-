package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.StatutPaiement;
import com.microfin.model.Echeance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EcheanceRepository {
    
    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    public Echeance save(Echeance echeance) {
        String sql = """
            INSERT INTO echeances (credit_id, date_echeance, mensualite, date_paiement,
                statut_paiement, numero_echeance)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
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
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return echeances;
    }

    public boolean update(Echeance echeance) {
        String sql = """
            UPDATE echeances SET date_paiement = ?, statut_paiement = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            if (echeance.getDateDePaiement() != null) {
                stmt.setDate(1, Date.valueOf(echeance.getDateDePaiement()));
            } else {
                stmt.setNull(1, Types.DATE);
            }
            stmt.setString(2, echeance.getStatutPaiement().name());
            stmt.setLong(3, echeance.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return false;
        }
    }

    private Echeance mapResultSetToEcheance(ResultSet rs) throws SQLException {
        Echeance echeance = new Echeance();
        echeance.setId(rs.getLong("id"));
        echeance.setCreditId(rs.getLong("credit_id"));
        echeance.setDateEcheance(rs.getDate("date_echeance").toLocalDate());
        echeance.setMensualite(rs.getDouble("mensualite"));
        
        Date datePaiement = rs.getDate("date_paiement");
        if (datePaiement != null) {
            echeance.setDateDePaiement(datePaiement.toLocalDate());
        }
        
        echeance.setStatutPaiement(StatutPaiement.valueOf(rs.getString("statut_paiement")));
        echeance.setNumeroEcheance(rs.getInt("numero_echeance"));
        return echeance;
    }
}
