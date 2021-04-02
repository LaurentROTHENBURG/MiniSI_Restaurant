package RestaurantWithMaven.model;

import javax.sound.midi.Soundbank;
import java.sql.*;

public class Facture {
    private int facture_id;
    private int serveur_idx;
    private int table_idx;
    private int plat_idx;

    public Facture(int serveur_idx, int table_idx) {
        this.serveur_idx = serveur_idx;
        this.table_idx = table_idx;
        this.plat_idx = plat_idx;
    }

    public Facture(int plat_idx) {
        this.plat_idx = plat_idx;
    }

    public void insertionFacture(Connection connection) throws SQLException {
        //Fonction insere dans facture et update facture_plat pour valoriser facture_idx
        Statement ordreSQL = connection.createStatement();
        ordreSQL.execute("INSERT INTO facture (serveur_idx, tables_idx)" +
                "VALUES ('" + serveur_idx + "','" + table_idx + "')");

        ordreSQL.execute("UPDATE facture_plat SET facture_idx = (SELECT max(facture_id) FROM facture)" +
                "WHERE facture_idx is null");

        ordreSQL.close();
    }

    public final java.sql.ResultSet getGeneratedKeys() {
        return null;
    }

    public void insertionFacturePlat(Connection connection) throws SQLException {
        //Fonction pour valoriser facure_plat
        Statement ordreSQL = connection.createStatement();
        ordreSQL.execute("INSERT INTO facture_plat (plat_idx)" +
                "VALUES ('" + plat_idx + "')");
        ordreSQL.close();
    }

}
