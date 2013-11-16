TwitterSentiment
================

Analyze the sentiment of tweets using Java

Required
=============
lingpipe < http://alias-i.com/lingpipe/web/download.html >
twitter4j < http://twitter4j.org/en/index.html >

How to: Sentiment Analysis of Tweets using Java
---------------------------------------------------

Since it is a quite interesting topic, I will describe a simple but accurate process for the sentiment analysis of tweets using Java, based on the use of two libraries: Twitter4J (to collect tweets) and LingPipe (to classify their sentiment).

If you want to easily download the code used in this tutorial, you can easily download it from GitHub here: https://github.com/johnbyron/TwitterSentiment

###Phase 1 - Train the classifier###

In this phase you will learn how to train a sentiment classifier and save it as a .txt file for later use. If you don't want to train your own classifier you can simply download one from here (it has been trained on random tweets written in English and can distinguish between positive, negative and neutral tweets with an accuracy of  approximately 75%) and skip to the next phase.

First of all you need to download LingPipe, a library for text processing, from here and import it into your project. Then you need to have a training dataset, that is a collection of tweets used to train the classifier. You can download one from here or create one by yourself.

Create a folder named "trainDirectory" in which you put three folders named "pos", "neg" and "neu". (you can also use only "pos" and "neg"). In the "pos" folder you have to put the positive tweets, in "neg" the negative tweets and in "neu" the neutral tweets. Each tweet has to be a different .txt file. Use the code below to train a classifier based on the training dataset and save it as a file named "classifier.txt".

~~~java
 void train() throws IOException, ClassNotFoundException {  
    File trainDir;  
    String[] categories;  
    LMClassifier class;  
    trainDir = new File("trainDirectory");  
    categories = trainDir.list();  
    int nGram = 7; //the nGram level, any value between 7 and 12 works  
    class= DynamicLMClassifier.createNGramProcess(mCategories, nGram);  
    for (int i = 0; i < categories.length; ++i) {  
       String category = categories[i];  
       Classification classification = new Classification(category);  
       File file = new File(trainDir, categories[i]);  
       File[] trainFiles = file.listFiles();  
       for (int j = 0; j < trainFiles.length; ++j) {  
          File trainFile = trainFiles[j];  
          String review = Files.readFromFile(trainFile, "ISO-8859-1");  
          Classified classified = new Classified(review, classification);  
          ((ObjectHandler>) class).handle(classified);   
       }  
     }  
    AbstractExternalizable.compileTo((Compilable) class, new File("classifier.txt"));  
 } 
~~~


###Phase 2 - Load the classifier###

In this phase you will load the previously created (or downloaded) classifier, saved as "classifier.txt".

The code below shows how to create a class called "SentimentClassifier", used to classify the tweets collected in the next phase.

~~~java
 public class SentimentClassifier {  
    String[] categories;  
    LMClassifier class;  
    public SentimentClassifier() {  
    try {  
       class= (LMClassifier) AbstractExternalizable.readObject(new File("abc.txt"));  
       categories = class.categories();  
    }  
    catch (ClassNotFoundException e) {  
       e.printStackTrace();  
    }  
    catch (IOException e) {  
       e.printStackTrace();  
    }  
    }  
    public String classify(String text) {  
    ConditionalClassification classification = class.classify(text);  
    return classification.bestCategory();  
    }  
 } 
~~~ 


###Phase 3 - Download and classify the tweets###

To collect the tweets from the Twitter API you will need to download the Twitter4J library from here and import it into your project.

The code below shows how to build a class named TwitterManager which will take care of collecting and classifying tweets. In the constructor of the class you have to enter your Twitter Developer credentials for the Search API which can be obtained here.

~~~java
 public class TwitterManager {  
    SentimentClassifier sentClassifier;  
    int LIMIT= 500; //the number of retrieved tweets  
    ConfigurationBuilder cb;  
    Twitter twitter;  
    public TwitterManager() {  
       cb = new ConfigurationBuilder();  
       cb.setOAuthConsumerKey("***");  
       cb.setOAuthConsumerSecret("***");  
       cb.setOAuthAccessToken("***");  
       cb.setOAuthAccessTokenSecret("***");  
       twitter = new TwitterFactory(cb.build()).getInstance();  
       sentClassifier = new SentimentClassifier();  
    }  
    public void performQuery(String inQuery) throws InterruptedException, IOException {  
       Query query = new Query(inQuery);  
       query.setCount(100);  
       try {  
          int count=0;  
          QueryResult r;  
          do {  
             r = twitter.search(query);  
             ArrayList ts= (ArrayList) r.getTweets();  
             for (int i = 0; i &lt; ts.size() &amp;&amp; count &lt; LIMIT; i++) {  
                count++;  
                Status t = ts.get(i);  
                String text = t.getText();  
                System.out.println("Text: " + text);  
                String name = t.getUser().getScreenName();  
                System.out.println("User: " + name);  
                String sent = sentClassifier.classify(t.getText());  
                System.out.println("Sentiment: " + sent);   
             }    
          } while ((query = r.nextQuery()) != null &amp;&amp; count &lt; LIMIT);  
       }  
       catch (TwitterException te) {  
          System.out.println("Couldn't connect: " + te);  
       }  
    }  
 }
 ~~~

Now you will be able to query the Twitter API and classify the retrieved tweets just calling the "performQuery" method in the main function. Below an example to retrieve and classify the sentiment of tweets talking about "Obama".

~~~java
 TwitterManager twitterManager = new TwitterManager();  
 twitterManager.performQuery("Obama"); 
- See more at: http://cavajohn.blogspot.com.br/2013/05/how-to-sentiment-analysis-of-tweets.html#sthash.7v90Ub8h.dpuf
~~~

More Information
======================
http://cavajohn.blogspot.com.br/2013/05/how-to-sentiment-analysis-of-tweets.html

Examples
======================
In the main file there is methods two methods to exemplification. The data used are comments from http://ulbra-to.br/encena/2013/09/23/Memorias-de-uma-mente-esquizofrenica.