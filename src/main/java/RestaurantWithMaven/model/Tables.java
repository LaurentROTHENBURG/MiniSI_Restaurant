package RestaurantWithMaven.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Tables {
    private int tables_id;
    private String nom;
    private int nbconvive;

    //Constructeur
    public Tables(int tables_id, String nom, int nbconvive) throws SQLException {
        this.tables_id = tables_id;
        this.nom = nom;
        this.nbconvive = nbconvive;
    }

    //Formatage de sortie de l'extraction
    @Override
    public String toString() {
        return tables_id + "- " + nom + " - Capacité de " + nbconvive + " convives";
    }

    // Affichage de la liste des tables
    public static List<Tables> gestListeTables(Connection connection) throws SQLException {

        Statement ordreSQL = connection.createStatement();
        ResultSet resultatSQL = ordreSQL.executeQuery("SELECT * from tables");

        List<Tables> tablesList = new ArrayList<>();

        while (resultatSQL.next()) {
            Tables dbTables = new Tables(
                    resultatSQL.getInt("tables_id"),
                    resultatSQL.getString("nom"),
                    resultatSQL.getInt("nbconvive"));

            tablesList.add(dbTables);
            System.out.println(dbTables);
        }

        resultatSQL.close();
        ordreSQL.close();

        return tablesList;
    }
}
