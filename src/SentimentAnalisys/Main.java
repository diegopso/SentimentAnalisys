/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package SentimentAnalisys;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(classify("Alessandra, gostaria de lhe parabenizar pela coragem e sinceridade que usou para desnudar esse turbilhao de afetos..."));
    }
    
    public static void train(){
        try {
            Trainer t = new Trainer();
            t.Train();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String classify(String str){
        SentimentClassifier sc = new SentimentClassifier();
        return sc.classify(str);
    }
    
}
