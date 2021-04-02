package RestaurantWithMaven.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Création de la class Serveur
public class Serveur {
    private int serveur_id;
    private String nom;
    private String prenom;

    //constructeur pour affichage de toString en ajoutant id
    public Serveur(int newServeur_id, String newNom, String newPrenom) {
        serveur_id = newServeur_id;
        nom = newNom;
        prenom = newPrenom;
    }

    //constructeur utilisé pour la construction de la liste
    public Serveur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    //Formatage de sortie de l'extraction
    @Override
    public String toString() {
        return serveur_id + "- " + nom + " " + prenom;
    }

    //Affichage de la liste des serveurs
    public static List<Serveur> getListeServeur(Connection connection) throws SQLException {

        Statement ordreSQL = connection.createStatement();
        ResultSet resultatSQL = ordreSQL.executeQuery("SELECT * from serveur");

        List<Serveur> serveurList = new ArrayList<>();

        while (resultatSQL.next()) {
            Serveur dbServeur = new Serveur(
                    resultatSQL.getInt("serveur_id"),
                    resultatSQL.getString("nom"),
                    resultatSQL.getString("prenom"));

            serveurList.add(dbServeur);
            System.out.println(dbServeur);
        }
        resultatSQL.close();
        ordreSQL.close();

        return serveurList;
    }
}