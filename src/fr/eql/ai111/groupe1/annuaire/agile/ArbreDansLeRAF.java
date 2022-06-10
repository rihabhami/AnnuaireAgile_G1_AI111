package fr.eql.ai111.groupe1.annuaire.agile;



import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author Rihab
 */

public class ArbreDansLeRAF {
    /**
     *
     * Cette classe permet de faire les traitements au niveau du RAF:
     *  1. Charger le RAF à partir s'un fichier.txt d'une manière structurée
     *  2. Trie des stagiaires par ordre alphabétique
     *
     *
     */
    public static final int NOMSTAGIARE = 20;
    public static final int PRENOMSTAGIARE = 20;
    public static final int DEPARTEMENT = 15;
    public static final int PROMO = 10;

    public static final int LONGUEUSTAGIARE = ((NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO ) * 2) + 4;
    //le 4 c'est pour l'année puisque c'est un int donc on ne le mélange pas avec les string

    public static void main(String[] args) {

        RandomAccessFile raf;
        String ligne = "";
        String infoStag = "";
        int compteurChoix = 0;
        int compteurStag = 0; //donne le nombre des stagiaires



        //Création du RAF
        try {
            raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
            //Lecture de fichier.txt
            FileReader fichierOriginal = new FileReader("c:/Projet1/stagiaires.txt");
            BufferedReader bf = new BufferedReader(fichierOriginal);

            while ((ligne = bf.readLine()) != null) {// Tant qu'il y a des lignes dans mon fichier.txt
                if (!ligne.equals("*")) {// si ma ligne != "*" je vais ajouter le stagiaire dans le RAF
                    // sinon j'augmente le nombre des stagiaires

                    for (int i = 0; i < ligne.length(); i++) {

                        if (ligne.charAt(i) != ' ') {
                            infoStag += ligne.charAt(i);

                        }
                    }
                    switch (compteurChoix) {
                        case 0:
                            infoStag = completer(infoStag, NOMSTAGIARE);
                            raf.writeChars(infoStag);
                            break;
                        case 1:
                            infoStag = completer(infoStag, PRENOMSTAGIARE);
                            raf.writeChars(infoStag);

                            break;
                        case 2:
                            infoStag = completer(infoStag, DEPARTEMENT);
                            raf.writeChars(infoStag);

                            break;
                        case 3:
                            infoStag = completer(infoStag, PROMO);
                            raf.writeChars(infoStag);
                            break;
                        case 4:
                            int nbannee = Integer.parseInt(infoStag);
                            raf.writeInt(nbannee);
                            break;
                        default:
                            break;

                    }

                    compteurChoix +=1;


                    infoStag = "";


                } else {

                    compteurStag += 1;
                    compteurChoix = 0;
                }

                infoStag ="";

            }
            triParOrdreAlpha(LONGUEUSTAGIARE,compteurStag,raf);
            listeStag(compteurStag, raf);


            String rechercheStagiaire = JOptionPane.showInputDialog("Veuillez entrer le nom d'un stagiaire");
            //System.out.println(compteurStag);
            rechercheStagiaire = completer(rechercheStagiaire, NOMSTAGIARE);
            rechercheStag(0, compteurStag, raf, rechercheStagiaire);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //Méthodes

    /**
     * La méthode completer permet de structurer les données qui vont être saisi dans le RAF
     * @param mot : c'est une chaine de caractère : prend nom, prénom, department, promo
     * @param taille : taille de chaque paramètres (nom, prénom, department, promo)
     * @return paramètres (nom, prénom, department, promo) structuré
     */
    public static String completer(String mot, int taille) {

        int nbEspace = taille - mot.length();
        for (int i = 0; i < nbEspace; i++) {
            mot += " ";
        }
        return mot;


    }


    /**
     *
     * Cette méthode permet de lire un stagiaire (que les paramètres String) à partir de RAF
     */
    public static String lectureStag (int taillechaine, RandomAccessFile raf){

        String chaine = "";

        for (int i = 0; i < taillechaine; i++){

            try {
                chaine += raf.readChar();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return chaine;
    }

    /**
     * Cette méthode permet de lister les stagiaires dans la console
     * Elle va être modifiée pour lister dans une interface
     * @param compteurStag : il s'agit de nombre des stagiares dans le RAF
     * @param raf
     */

    //Cette méthode permet de lister les stagiaires en console sans ordre
    public static void listeStag (int compteurStag, RandomAccessFile raf){

        try {
            raf.seek(0);
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;
            for (int k = 0; k < compteurStag; k++){
                String chaine = "";
                String chaine1= lectureStag(taille,raf);
                chaine = chaine + chaine1 + raf.readInt() ;// on ajoute l'année aux données de type String
                System.out.println(chaine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     *
     * Cette fonction permet d'afficher en console les informations d'un stagiaire dont le nom a été saisi par
     * l'utilisateur
     * C'est le principe des arbres binaires
     * On compare le nom saisi avec le premier puis on compare : si c'est le même bingo
     * sinon s'il est plus petit on cherche dans les noms <
     * sinon on cherche dans les noms >
     * c'est pour ça on doit trier le RAF avant de faire la recherche
     * @param borneInf
     * @param borneSup
     * @param raf
     * @param nomStagRecherche
     */
    public static void rechercheStag(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche){

        int pivot =0;
        String nomStagActuel ="";

        try {

            if (borneInf <= borneSup){
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot* LONGUEUSTAGIARE);
                nomStagActuel =  lectureStag(NOMSTAGIARE,raf);
                if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) == 0 ){
                    raf.seek(pivot*LONGUEUSTAGIARE);
                    System.out.println("**************\r\n" + " Les infos de stagiaire demandé :\r\n" +
                            "Nom  : "  + lectureStag(NOMSTAGIARE, raf) + "\r\n" +
                            "Prénom  : " + lectureStag(PRENOMSTAGIARE, raf) + "\r\n" +
                            "Département : " + lectureStag(DEPARTEMENT, raf) + "\r\n" +
                            "Promo : " + lectureStag(PROMO, raf) + "\r\n" +
                            "Année : " + raf.readInt());

                }else {

                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) < 0) {
                        rechercheStag(borneInf, pivot - 1, raf, nomStagRecherche);
                    } else {
                        rechercheStag(pivot + 1, borneSup, raf, nomStagRecherche);
                    }
                }

            } else {

                System.out.println("\r\n***** Stagiaire introuvable *****");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }





    }

    /**
     * Cette fonction permet de trier les stagiaires par ordre alphabétique qui sont dans le RAF
     * Elle prend comme pivot le premier nom de le RAF qu'elle va comparer avec le suivant
     * Si le deuxième plus petit : on permute (on fait appel à la fonction permuter)
     * sinon on compare avec le troisième ainsi de suite
     *
     * @param longueurStag
     * @param compteurStag
     * @param raf
     */
    public static void triParOrdreAlpha(int longueurStag, int compteurStag, RandomAccessFile raf){


        try {

            for (int i = 0; i < compteurStag - 1; i++) {
                int indexmin = i;
                raf.seek(i * longueurStag);
                String nomStagMini = "";
                nomStagMini = lectureStag(NOMSTAGIARE, raf);

                for (int j = i + 1; j < compteurStag; j ++) {
                    raf.seek(j * longueurStag);
                    String nomStagCourant = "";
                    nomStagCourant = lectureStag(NOMSTAGIARE, raf);


                    if (nomStagCourant.compareTo(nomStagMini) < 0) {
                        indexmin = j;
                        nomStagMini = nomStagCourant;
                    }
                }
                permuter(indexmin * longueurStag, i * longueurStag, raf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }


    /**
     * Cette méthode permet de permuter deux stagiaires
     * @param indexmin
     * @param frontiere
     * @param raf
     */
    private static void permuter(int indexmin, int frontiere, RandomAccessFile raf){


        try {

            int stagChaineTaille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;

            String stagChaine = "";
            int stagAnnee = 0;

            String stag2Chaine = "";
            int stag2Annee = 0;

            raf.seek(frontiere);
            stagChaine= lectureStag(stagChaineTaille,raf);
            stagAnnee = raf.readInt();

            raf.seek(indexmin);
            stag2Chaine = lectureStag(stagChaineTaille,raf);
            stag2Annee= raf.readInt();

            raf.seek(frontiere);
            raf.writeChars(stag2Chaine);
            raf.writeInt(stag2Annee);

            raf.seek(indexmin);
            raf.writeChars(stagChaine);
            raf.writeInt(stagAnnee);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
