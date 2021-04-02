package RestaurantWithMaven.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Plat {
    int plat_id;
    String nom;
    double prix_unitaire;

    //Constructeur
    public Plat(int plat_id, String nom, double prix_unitaire) throws SQLException {
        this.plat_id = plat_id;
        this.nom = nom;
        this.prix_unitaire = prix_unitaire;
    }

    //Formatage de sortie de l'extraction

    @Override
    public String toString() {
        return plat_id + "- " + nom + " " + prix_unitaire;
    }

    // Affichage de la liste des plats
    public static List<Plat> gestListePlat(Connection connection) throws SQLException {
        Statement ordreSQL = connection.createStatement();
        ResultSet resultatSQL = ordreSQL.executeQuery("SELECT * from plat");

        List<Plat> platList = new ArrayList<>();
        while (resultatSQL.next()) {
            Plat dbplat = new Plat(
                    resultatSQL.getInt("plat_id"),
                    resultatSQL.getString("nom"),
                    resultatSQL.getDouble("prix_unitaire"));

            platList.add(dbplat);
            System.out.println(dbplat);
        }
        resultatSQL.close();
        ordreSQL.close();

        return platList;
    }
}

