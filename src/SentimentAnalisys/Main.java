package SentimentAnalisys;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diegopso <https://github.com/diegopso>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //train(); //use this to train
        System.out.println(classify("muito interessante me ajudou muito no meu trabalho"));
    }
    
    /**
     * Train example function
     */
    public static void train(){
        try {
            Trainer t = new Trainer();
            t.Train();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Clissify example function
     * @param str   Text to classify
     * @return Category name
     */
    public static String classify(String str){
        SentimentClassifier sc = new SentimentClassifier();
        return sc.classify(str);
    }
    
}
