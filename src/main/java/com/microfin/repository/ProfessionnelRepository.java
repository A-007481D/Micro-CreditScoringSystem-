package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.SecteurActivite;
import com.microfin.enums.SituationFamiliale;
import com.microfin.model.Professionnel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessionnelRepository {
    
    private Connection getConnection() {
        return DBConnection.getInstance().getConnection();
    }

    public Professionnel save(Professionnel professionnel) {
        String sql = """
            INSERT INTO professionnels (nom, prenom, date_naissance, ville, nombre_enfants,
                investissement, placement, situation_familiale, score, client_existant,
                revenu, immatriculation_fiscale, secteur_activite, activite)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, professionnel.getNom());
            stmt.setString(2, professionnel.getPrenom());
            stmt.setDate(3, Date.valueOf(professionnel.getDateDeNaissance()));
            stmt.setString(4, professionnel.getVille());
            stmt.setInt(5, professionnel.getNombreEnfants());
            stmt.setDouble(6, professionnel.getInvestissement());
            stmt.setDouble(7, professionnel.getPlacement());
            stmt.setString(8, professionnel.getSituationFamiliale().name());
            stmt.setInt(9, professionnel.getScore());
            stmt.setBoolean(10, professionnel.isClientExistant());
            stmt.setDouble(11, professionnel.getRevenu());
            stmt.setString(12, professionnel.getImmatriculationFiscale());
            stmt.setString(13, professionnel.getSecteurActivite().name());
            stmt.setString(14, professionnel.getActivite());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                professionnel.setId(rs.getLong(1));
            }
            
            return professionnel;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
            return null;
        }
    }

    public Optional<Professionnel> findById(Long id) {
        String sql = "SELECT * FROM professionnels WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToProfessionnel(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    public List<Professionnel> findAll() {
        List<Professionnel> professionnels = new ArrayList<>();
        String sql = "SELECT * FROM professionnels ORDER BY id";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                professionnels.add(mapResultSetToProfessionnel(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        
        return professionnels;
    }

    public boolean update(Professionnel professionnel) {
        String sql = """
            UPDATE professionnels SET nom = ?, prenom = ?, date_naissance = ?, ville = ?,
                nombre_enfants = ?, investissement = ?, placement = ?, situation_familiale = ?,
                score = ?, client_existant = ?, revenu = ?, immatriculation_fiscale = ?,
                secteur_activite = ?, activite = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, professionnel.getNom());
            stmt.setString(2, professionnel.getPrenom());
            stmt.setDate(3, Date.valueOf(professionnel.getDateDeNaissance()));
            stmt.setString(4, professionnel.getVille());
            stmt.setInt(5, professionnel.getNombreEnfants());
            stmt.setDouble(6, professionnel.getInvestissement());
            stmt.setDouble(7, professionnel.getPlacement());
            stmt.setString(8, professionnel.getSituationFamiliale().name());
            stmt.setInt(9, professionnel.getScore());
            stmt.setBoolean(10, professionnel.isClientExistant());
            stmt.setDouble(11, professionnel.getRevenu());
            stmt.setString(12, professionnel.getImmatriculationFiscale());
            stmt.setString(13, professionnel.getSecteurActivite().name());
            stmt.setString(14, professionnel.getActivite());
            stmt.setLong(15, professionnel.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM professionnels WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
            return false;
        }
    }

    private Professionnel mapResultSetToProfessionnel(ResultSet rs) throws SQLException {
        Professionnel prof = new Professionnel();
        prof.setId(rs.getLong("id"));
        prof.setNom(rs.getString("nom"));
        prof.setPrenom(rs.getString("prenom"));
        prof.setDateDeNaissance(rs.getDate("date_naissance").toLocalDate());
        prof.setVille(rs.getString("ville"));
        prof.setNombreEnfants(rs.getInt("nombre_enfants"));
        prof.setInvestissement(rs.getDouble("investissement"));
        prof.setPlacement(rs.getDouble("placement"));
        prof.setSituationFamiliale(SituationFamiliale.valueOf(rs.getString("situation_familiale")));
        prof.setCreatedAt(rs.getDate("created_at").toLocalDate());
        prof.setScore(rs.getInt("score"));
        prof.setClientExistant(rs.getBoolean("client_existant"));
        prof.setRevenu(rs.getDouble("revenu"));
        prof.setImmatriculationFiscale(rs.getString("immatriculation_fiscale"));
        prof.setSecteurActivite(SecteurActivite.valueOf(rs.getString("secteur_activite")));
        prof.setActivite(rs.getString("activite"));
        return prof;
    }
}
