package com.microfin.repository;

import com.microfin.config.DBConnection;
import com.microfin.enums.Secteur;
import com.microfin.enums.SituationFamiliale;
import com.microfin.enums.TypeContrat;
import com.microfin.model.Employe;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeRepository {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    public Employe save(Employe employe) {
        String sql = "INSERT INTO employes (nom, prenom, date_naissance, ville, nombre_enfants, " +
                "investissement, placement, situation_familiale, score, client_existant, " +
                "salaire, anciennete, poste, type_contrat, secteur) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setDate(3, Date.valueOf(employe.getDateDeNaissance()));
            stmt.setString(4, employe.getVille());
            stmt.setInt(5, employe.getNombreEnfants());
            stmt.setDouble(6, employe.getInvestissement());
            stmt.setDouble(7, employe.getPlacement());
            stmt.setString(8, employe.getSituationFamiliale().name());
            stmt.setInt(9, employe.getScore());
            stmt.setBoolean(10, employe.isClientExistant());
            stmt.setDouble(11, employe.getSalaire());
            stmt.setInt(12, employe.getAnciennete());
            stmt.setString(13, employe.getPoste());
            stmt.setString(14, employe.getTypeContrat().name());
            stmt.setString(15, employe.getSecteur().name());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                employe.setId(rs.getLong(1));
            }
            
            return employe;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
            return null;
        }
    }

    public Optional<Employe> findById(Long id) {
        String sql = "SELECT * FROM employes WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToEmploye(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    public List<Employe> findAll() {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes ORDER BY id";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employes.add(mapResultSetToEmploye(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }
        
        return employes;
    }

    public boolean update(Employe employe) {
        String sql = "UPDATE employes SET nom = ?, prenom = ?, date_naissance = ?, ville = ?, " +
                "nombre_enfants = ?, investissement = ?, placement = ?, situation_familiale = ?, " +
                "score = ?, client_existant = ?, salaire = ?, anciennete = ?, poste = ?, " +
                "type_contrat = ?, secteur = ? WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setDate(3, Date.valueOf(employe.getDateDeNaissance()));
            stmt.setString(4, employe.getVille());
            stmt.setInt(5, employe.getNombreEnfants());
            stmt.setDouble(6, employe.getInvestissement());
            stmt.setDouble(7, employe.getPlacement());
            stmt.setString(8, employe.getSituationFamiliale().name());
            stmt.setInt(9, employe.getScore());
            stmt.setBoolean(10, employe.isClientExistant());
            stmt.setDouble(11, employe.getSalaire());
            stmt.setInt(12, employe.getAnciennete());
            stmt.setString(13, employe.getPoste());
            stmt.setString(14, employe.getTypeContrat().name());
            stmt.setString(15, employe.getSecteur().name());
            stmt.setLong(16, employe.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM employes WHERE id = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    private Employe mapResultSetToEmploye(ResultSet rs) throws SQLException {
        Employe employe = new Employe();
        employe.setId(rs.getLong("id"));
        employe.setNom(rs.getString("nom"));
        employe.setPrenom(rs.getString("prenom"));
        employe.setDateDeNaissance(rs.getDate("date_naissance").toLocalDate());
        employe.setVille(rs.getString("ville"));
        employe.setNombreEnfants(rs.getInt("nombre_enfants"));
        employe.setInvestissement(rs.getDouble("investissement"));
        employe.setPlacement(rs.getDouble("placement"));
        employe.setSituationFamiliale(SituationFamiliale.valueOf(rs.getString("situation_familiale")));
        employe.setCreatedAt(rs.getDate("created_at").toLocalDate());
        employe.setScore(rs.getInt("score"));
        employe.setClientExistant(rs.getBoolean("client_existant"));
        employe.setSalaire(rs.getDouble("salaire"));
        employe.setAnciennete(rs.getInt("anciennete"));
        employe.setPoste(rs.getString("poste"));
        employe.setTypeContrat(TypeContrat.valueOf(rs.getString("type_contrat")));
        employe.setSecteur(Secteur.valueOf(rs.getString("secteur")));
        return employe;
    }
}
