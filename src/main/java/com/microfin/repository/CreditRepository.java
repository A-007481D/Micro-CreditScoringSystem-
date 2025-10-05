package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.DecisionCredit;
import com.microfin.enums.TypeCredit;
import com.microfin.model.Credit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreditRepository {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    public Credit save(Credit credit, String clientType) {
        String sql = "INSERT INTO credits (client_id, client_type, montant_demande, montant_octroye, " +
                "taux_interet, duree_mois, type_credit, decision, score_au_moment, actif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, credit.getClientId());
            stmt.setString(2, clientType);
            stmt.setDouble(3, credit.getMontantDemande());
            stmt.setDouble(4, credit.getMontantOctroye());
            stmt.setDouble(5, credit.getTauxInteret());
            stmt.setInt(6, credit.getDureeEnMois());
            stmt.setString(7, credit.getTypeCredit().name());
            stmt.setString(8, credit.getDecision().name());
            stmt.setInt(9, credit.getScoreAuMomentDuCredit());
            stmt.setBoolean(10, credit.isActif());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                credit.setId(rs.getLong(1));
            }
            
            return credit;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return null;
        }
    }

    public Optional<Credit> findById(Long id) {
        String sql = "SELECT * FROM credits WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    public List<Credit> findByClientId(Long clientId) {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits WHERE client_id = ? ORDER BY date_credit DESC";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                credits.add(mapResultSetToCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return credits;
    }

    public List<Credit> findActiveByClientId(Long clientId) {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits WHERE client_id = ? AND actif = true ORDER BY date_credit DESC";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                credits.add(mapResultSetToCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return credits;
    }

    public List<Credit> findAll() {
        List<Credit> credits = new ArrayList<>();
        String sql = "SELECT * FROM credits ORDER BY date_credit DESC";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                credits.add(mapResultSetToCredit(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return credits;
    }

    public boolean update(Credit credit) {
        String sql = "UPDATE credits SET montant_demande = ?, montant_octroye = ?, taux_interet = ?, " +
                "duree_mois = ?, type_credit = ?, decision = ?, actif = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, credit.getMontantDemande());
            stmt.setDouble(2, credit.getMontantOctroye());
            stmt.setDouble(3, credit.getTauxInteret());
            stmt.setInt(4, credit.getDureeEnMois());
            stmt.setString(5, credit.getTypeCredit().name());
            stmt.setString(6, credit.getDecision().name());
            stmt.setBoolean(7, credit.isActif());
            stmt.setLong(8, credit.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return false;
        }
    }

    private Credit mapResultSetToCredit(ResultSet rs) throws SQLException {
        Credit credit = new Credit();
        credit.setId(rs.getLong("id"));
        credit.setClientId(rs.getLong("client_id"));
        credit.setDateDeCredit(rs.getDate("date_credit").toLocalDate());
        credit.setMontantDemande(rs.getDouble("montant_demande"));
        credit.setMontantOctroye(rs.getDouble("montant_octroye"));
        credit.setTauxInteret(rs.getDouble("taux_interet"));
        credit.setDureeEnMois(rs.getInt("duree_mois"));
        credit.setTypeCredit(TypeCredit.valueOf(rs.getString("type_credit")));
        credit.setDecision(DecisionCredit.valueOf(rs.getString("decision")));
        credit.setScoreAuMomentDuCredit(rs.getInt("score_au_moment"));
        credit.setActif(rs.getBoolean("actif"));
        return credit;
    }
}
