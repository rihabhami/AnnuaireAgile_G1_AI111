package fr.eql.ai111.groupe1.annuaire.agile;

import org.omg.PortableInterceptor.LOCATION_FORWARD;

import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ArbreDansLeRAF {
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

            while ((ligne = bf.readLine()) != null) {
                if (!ligne.equals("*")) {
                    //compteurChoix = 0;
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
    public static String completer(String mot, int taille) {

        int nbEspace = taille - mot.length();
        for (int i = 0; i < nbEspace; i++) {
            mot += " ";
        }
        return mot;


    }


    //Cette méthode permet de lire un stagiaire de RAF
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


    //Cette méthode permet de lister les stagiaires en console sans ordre
    public static void listeStag (int compteurStag, RandomAccessFile raf){

        try {
            raf.seek(0);
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;
            for (int k = 0; k < compteurStag; k++){
                String chaine = "";
                String chaine1= lectureStag(taille,raf);
                chaine = chaine + chaine1 + raf.readInt() ;
                System.out.println(chaine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
//Avant de faire rechercheStag : il faut trier le RAF par ordre alphabétique
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

}
