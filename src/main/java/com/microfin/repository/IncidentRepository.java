package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.TypeIncident;
import com.microfin.model.Incident;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IncidentRepository {
    
    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    public Incident save(Incident incident) {
        String sql = """
            INSERT INTO incidents (date_incident, echeance_id, score, type_incident, client_id)
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(incident.getDateIncident()));
            stmt.setLong(2, incident.getEcheanceId());
            stmt.setInt(3, incident.getScore());
            stmt.setString(4, incident.getTypeIncident().name());
            stmt.setLong(5, incident.getClientId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                incident.setId(rs.getLong(1));
            }
            
            return incident;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return null;
        }
    }

    public List<Incident> findByClientId(Long clientId) {
        List<Incident> incidents = new ArrayList<>();
        String sql = "SELECT * FROM incidents WHERE client_id = ? ORDER BY date_incident DESC";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return incidents;
    }

    public List<Incident> findRecentByClientId(Long clientId, int mois) {
        List<Incident> incidents = new ArrayList<>();
        LocalDate dateDebut = LocalDate.now().minusMonths(mois);
        String sql = "SELECT * FROM incidents WHERE client_id = ? AND date_incident >= ? ORDER BY date_incident DESC";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, clientId);
            stmt.setDate(2, Date.valueOf(dateDebut));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return incidents;
    }

    private Incident mapResultSetToIncident(ResultSet rs) throws SQLException {
        Incident incident = new Incident();
        incident.setId(rs.getLong("id"));
        incident.setDateIncident(rs.getDate("date_incident").toLocalDate());
        incident.setEcheanceId(rs.getLong("echeance_id"));
        incident.setScore(rs.getInt("score"));
        incident.setTypeIncident(TypeIncident.valueOf(rs.getString("type_incident")));
        incident.setClientId(rs.getLong("client_id"));
        return incident;
    }
}
