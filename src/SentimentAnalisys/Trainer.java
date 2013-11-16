package SentimentAnalisys;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import com.aliasi.util.Files;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author johnbyron <https://github.com/johnbyron>
 * @author diegopso <https://github.com/diegopso>
 */
public class Trainer {
    private String charset;
    private String trainDirectory;
    private String outFile;

    public Trainer() {
        charset = Config.charset;
        trainDirectory = Config.trainDirectory;
        outFile = Config.classifierPath;
    }

    public Trainer(String charset, String trainDirectory, String outFile) {
        this.charset = charset;
        this.trainDirectory = trainDirectory;
        this.outFile = outFile;
    }

    public void Train() throws IOException, ClassNotFoundException {
        File trainDir;
        String[] categories;
        LMClassifier clazz;
        trainDir = new File(this.trainDirectory);
        categories = trainDir.list();
        int nGram = 7; //the nGram level, any value between 7 and 12 works
        clazz = DynamicLMClassifier.createNGramProcess(categories, nGram);

        for (int i = 0; i < categories.length; ++i) {
            String category = categories[i];
            Classification classification = new Classification(category);
            File file = new File(trainDir, categories[i]);
            File[] trainFiles = file.listFiles();
            for (int j = 0; j < trainFiles.length; ++j) {
                File trainFile = trainFiles[j];
                String review = Files.readFromFile(trainFile, this.charset);
                Classified classified = new Classified(review, classification);
                ((ObjectHandler) clazz).handle(classified);
            }
        }
        AbstractExternalizable.compileTo((Compilable) clazz, new File(this.outFile));
    }
}
