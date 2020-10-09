import java.io.*;
import java.util.HashMap;
import java.util.*;
import java.math.BigDecimal;

import static java.lang.StrictMath.log;

public class Train {

    public static Map<String,Integer> ham = new HashMap<String,Integer>();//count ham messages containing the word
    public static Map<String,Integer> spam = new HashMap<String,Integer>();//count spam messages containing the word

    public static HashMap<String,Boolean> multOccurance = new HashMap<String,Boolean>();
    //public static HashMap<String,Boolean> multOccuranceS = new HashMap<String,Boolean>();//DELETE
    //public Map<String,Boolean> wordCount = new HashMap<String,Boolean>();
    static HashMap<String,Double> WordHamProps =new HashMap<String, Double>();
    static HashMap<String, Double> WordSpamProps=new HashMap<String, Double>();
    public int numOfSpams=0,numOfHams=0,countWords=0,count=0,countTrainTxts=0;

    /**
     * Reads the files' contents
     * @param s The name of the main folder e.g.C:\\pu1
     * @param training The number of subfolders to be used for training
     * @throws IOException
     */
    public void ReadFile(String s,int training) throws IOException {
        BufferedReader reader = null;
        String line;
        File f=null;

        boolean bool;

        try {
             f = new File(s); } //the main folder that contains other folders
        catch (NullPointerException e){
            System.err.println ("File not found.");
        }

        for(File f1: f.listFiles()){//for every subfolder
            count++;
            if (count==training+1) break;

            for(File texts: f1.listFiles()){//for each message
                countTrainTxts++;//counting messages//DELETE
                if (texts.getName().contains("spmsg")){//spam text
                    bool=true;
                    numOfSpams+=1;//counting spam messages
                }
                else {
                    bool = false;
                    numOfHams+=1;//counting ham messages
                }
                try{
                    reader = new BufferedReader(new FileReader(texts));
                }
                catch (FileNotFoundException e ){
                    System.err.println("Error opening file!");
                }

                line = reader.readLine();
                while(line!=null) {
                    String[] words = line.split(" ");//words of the line

                    if (bool) { //if spam
                        for (int j = 0; j < words.length; j++) {//for each word of the line

                            if (!(words[j].equals(" ") || words[j].equals("Subject:") || words[j].equals(""))) {
                                if (spam.containsKey(words[j])) {
                                    if (!multOccurance.containsKey(words[j])) {
                                        spam.put(words[j], spam.get(words[j]) + 1);
                                        multOccurance.put(words[j],true);
                                    }
                                } else spam.put(words[j],1);
                            }
                        }
                    } else {//hamtxt
                        for (int j = 0; j < words.length; j++) {//for each word of the line

                            if (!(words[j].equals(" ") || words[j].equals("Subject:") || words[j].equals(""))) {
                                if (ham.containsKey(words[j])) {
                                    if (!multOccurance.containsKey(words[j])) {
                                        ham.put(words[j], ham.get(words[j]) + 1);
                                        multOccurance.put(words[j],true);
                                    }
                                } else ham.put(words[j], 1);
                            }
                        }
                    }

                    line = reader.readLine();
                }//while
                multOccurance.clear();//DELETE
                //multOccuranceH.clear();//DELETE
            }//for inside txts
        }//for
    }


    public void Props(String s,int training) throws IOException {

        ReadFile(s,training);

        double WordHam;
        double WordSpam;

        for (String i : spam.keySet()) {// i = word,get(i) = number

            WordSpam= log((double)(spam.get(i)+1)/(numOfSpams+2));//P(xi|spam)

            WordSpamProps.put(i,WordSpam);
            if (!ham.containsKey(i)){
                WordHam = log((double)(0+1)/(numOfHams+2));//P(xi|ham)
                WordHamProps.put(i,WordHam);
            }
        }
        for (String i : ham.keySet()) {// i = word,get(i) = number

            WordHam = log((double)(ham.get(i)+1)/(numOfHams+2));//P(xi|ham) !use log for probabilities getting very small

            WordHamProps.put(i,WordHam);
            if(!spam.containsKey(i)){
                WordSpam= log((double)(0+1)/(numOfSpams+2));//P(xi|spam)
                WordSpamProps.put(i,WordSpam);
            }
        }
    }

}
