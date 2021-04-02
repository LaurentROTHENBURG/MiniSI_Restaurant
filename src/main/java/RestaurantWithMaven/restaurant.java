package RestaurantWithMaven;

import RestaurantWithMaven.model.Facture;
import RestaurantWithMaven.model.Plat;
import RestaurantWithMaven.model.Serveur;
import RestaurantWithMaven.model.Tables;

import java.lang.invoke.SwitchPoint;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class restaurant {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";

    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);

        //Paramétres d'accès à la base de données postgres
        String url = "jdbc:postgresql://localhost:5432/restaurant";
        String user = "postgres";
        String password = "postgres";

        //Construction du Menu du programme
        //Affiche les choix de saisie possibles
        System.out.println("_____________________________");
        System.out.println("  - SAISISSEZ VOTRE CHOIX -   ");
        System.out.println("   1- Saisie de facture : ");
        System.out.println("   2- CA par Plats      : ");
        System.out.println("   3- CA par Tables     : ");
        System.out.println("_____________________________");

        //Gestion des choix - le choix renvoie à une fonction
        int choice = 0;
        choice = scan.nextInt();

        switch (choice) {
            case 1: {
                enterInvoice(scan, url, user, password);
                break;
            }
            case 2: {
                showBestDishes(url, user, password);
                break;
            }
            case 3: {
                showBestTables(url, user, password);
                break;
            }
            default: {
                System.out.println("Erreur de saisie");
            }
        }
    }

    private static void enterInvoice(Scanner scan, String url, String user, String password) throws SQLException {
        //Affichage de la liste des serveurs
        //On commence par afficher un message
        System.out.println("____________________________");
        System.out.println("Voici la liste des serveurs : ");
        System.out.println("____________________________");

        //Ensuite on appelle la fonction de la classe serveur
        Connection connection = DriverManager.getConnection(url, user, password);
        Serveur.getListeServeur(connection);

        System.out.println(RED + "________________________________________________" + RESET);
        System.out.println(RED + "Renseigner le numéro correspondant au serveur : " + RESET);
        System.out.println(RED + "________________________________________________" + RESET);
        int serveur_idx = scan.nextInt();

        //Affichage de la liste des tables
        //On commence par afficher un message
        System.out.println("____________________________");
        System.out.println("Voici la liste des tables : ");
        System.out.println("____________________________");

        Tables.gestListeTables(connection);

        System.out.println(RED + "________________________________________________" + RESET);
        System.out.println(RED + "Renseigner le numéro correspondant à la table : " + RESET);
        System.out.println(RED + "________________________________________________" + RESET);
        int tables_idx = scan.nextInt();


        //Affichage de la liste des plats
        //On commence par afficher un message
        System.out.println("____________________________");
        System.out.println("Voici la liste des plats : ");
        System.out.println("____________________________");
        //puis la liste des plats
        Plat.gestListePlat(connection);

        //on pose la question pour saisir les plats

        int plat_idx = 0;
        char reponse = 'Y';

        while (reponse == 'Y') {
            System.out.println(RED + "________________________________________________" + RESET);
            System.out.println(RED + "Renseigner le numéro correspondant au plat : " + RESET);
            System.out.println(RED + "________________________________________________" + RESET);

            plat_idx = scan.nextInt();

             //Enregistrement dans la table facture_plat du plat_idx pour gérer le multi plats par commandes
            Facture newFacturePlat;
            newFacturePlat = new Facture(plat_idx);
            newFacturePlat.insertionFacturePlat(connection);

            //Autre saisie ?
            System.out.println("Voulez-vous saisir autres plats ? (Y/N)");
            scan.nextLine();
            reponse = scan.nextLine().charAt(0);
        }

        //Enregistrement dans la table facture
        Facture newFacture;
        newFacture = new Facture(serveur_idx, tables_idx);
        newFacture.insertionFacture(connection);
    }

    private static void showBestDishes(String url, String user, String password) {
        //Extraction des données de la base restaurant via SQL
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement requete = connection.createStatement();

            ResultSet resultSet = requete.executeQuery("select p.nom NOM_PLAT,sum(p.prix_unitaire) CHIFFRE_AFFAIRE from facture\n" +
                    "join facture_plat fp on facture.facture_id = fp.facture_idx\n" +
                    "join plat p on fp.plat_idx = p.plat_id\n" +
                    "group by  p.nom\n" +
                    "ORDER BY CHIFFRE_AFFAIRE DESC;");

            System.out.println("______________________________________________________");
            System.out.println("Le classement des meilleurs plats est le suivant : ");
            System.out.println("______________________________________________________");

            // Affichage du résultat de la requete
            while (resultSet.next()) {
                System.out.println("Plat " + resultSet.getString("NOM_PLAT") + ", " + resultSet.getString("CHIFFRE_AFFAIRE") + " Eur");
                //System.out.println(resultSet.getString("NOM_TABLE") + " " + resultSet.getString("CHIFFRE_AFFAIRE"));
            }

            resultSet.close();
            requete.close();

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void showBestTables(String url, String user, String password) {
        //Extraction des données de la base restaurant via SQL
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement requete = connection.createStatement();
            //ResultSet resultSet = requete.executeQuery("SELECT * from plat where nom='Burger'");
            ResultSet resultSet = requete.executeQuery("select t.nom NOM_TABLE, sum(p.prix_unitaire) CHIFFRE_AFFAIRE from facture\n" +
                    "join tables t on facture.tables_idx = t.tables_id\n" +
                    "join facture_plat fp on facture.facture_id = fp.facture_idx\n" +
                    "join plat p on fp.plat_idx = p.plat_id\n" +
                    "group by tables_id\n" +
                    "order by CHIFFRE_AFFAIRE DESC");

            System.out.println("______________________________________________________");
            System.out.println("Le classement des meilleurs tables est le suivant : ");
            System.out.println("______________________________________________________");

            //Affichage deu résultat de la requete
            while (resultSet.next()) {
                System.out.println("Table " + resultSet.getString("NOM_TABLE") + ", " + resultSet.getString("CHIFFRE_AFFAIRE") + " Eur");
                //System.out.println(resultSet.getString("NOM_TABLE") + " " + resultSet.getString("CHIFFRE_AFFAIRE"));
            }

            resultSet.close();
            requete.close();

            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
