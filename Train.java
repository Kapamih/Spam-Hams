import java.io.*;
import java.util.HashMap;
import java.math.BigDecimal;

import static java.lang.StrictMath.log;

public class Train {

    public static HashMap<String,Integer> ham = new HashMap<String,Integer>();//plithos ham txts pou periexoun thn
    public static HashMap<String,Integer> spam = new HashMap<String,Integer>();

    public static HashMap<String,Integer> multOccuranceH = new HashMap<String,Integer>();
    public static HashMap<String,Integer> multOccuranceS = new HashMap<String,Integer>();
    public int numOfSpams=0,numOfHams=0,countWords=0,countTrainTxts=0;

    public void ReadFile(String s,int training) throws IOException {
        BufferedReader reader = null;
        String line;
        File f=null;

        boolean bool;

        try {
             f = new File(s); } //fakelos me upofakelous
        catch (NullPointerException e){
            System.err.println ("File not found.");
        }


        for(int i = 1 ; i <training+1 ; i++){//for parts

            File f1=new File(s + "\\" + "part" + i); //fakelos me texts

            for(File texts: f1.listFiles()){//mesa se ena txt
                countTrainTxts++;
                if (texts.getName().contains("spmsg")){
                    bool=true;
                    numOfSpams+=1;
                }
                else {
                    bool = false;
                    numOfHams+=1;
                }
                try{
                    reader = new BufferedReader(new FileReader(texts));
                }
                catch (FileNotFoundException e ){
                    System.err.println("Error opening file!");
                }

                line = reader.readLine();
                while(line!=null) {
                    String[] words = line.split(" ");

                    for (int j = 0; j < words.length; j++) {//gia kathe leksi mias garmmis
                        if(!spam.containsKey(words[j])&&!ham.containsKey(words[j])) countWords++;

                        if(!(words[j].equals(" ")||words[j].equals("Subject:")||words[j].equals(""))) {
                            if (bool) { //spamtxtx
                                if (spam.containsKey(words[j])){
                                    if(!multOccuranceS.containsKey(words[j])) {
                                        spam.put(words[j], spam.get(words[j]) + 1);
                                        multOccuranceS.put(words[j], 1);
                                    }

                                }
                                else spam.put(words[j], 1);
                            } else {//hamtxt
                                if (ham.containsKey(words[j])) {
                                    if(!multOccuranceH.containsKey(words[j])) {
                                        ham.put(words[j], ham.get(words[j]) + 1);
                                        multOccuranceH.put(words[j], 1);
                                    }
                                }
                                else ham.put(words[j], 1);
                            }
                        }
                    }

                    line = reader.readLine();
                }//while
                multOccuranceS.clear();
                multOccuranceH.clear();
            }//for inside txts


        }//for
    }

    static HashMap<String,Double> WordHamProps =new HashMap<String, Double>();
    static HashMap<String, Double> WordSpamProps=new HashMap<String, Double>();

    public void Props(String s,int training) throws IOException {

        ReadFile(s,training);

        double WordHam;
        double WordSpam;

        for (String i : spam.keySet()) {// i = word,get(i) = number

            WordSpam= log((double)(spam.get(i)+1)/(spam.size()+countWords));//P(xi|spam)
            WordSpamProps.put(i,WordSpam);
            if (!ham.containsKey(i)){
                WordHam = log((double)(0+1)/(ham.size()+countWords));//P(xi|ham)
                WordHamProps.put(i,WordHam);
            }
        }
        for (String i : ham.keySet()) {// i = word,get(i) = number

            WordHam = log((double)(ham.get(i)+1)/(ham.size()+countWords));//P(xi|ham)
            WordHamProps.put(i,WordHam);
            if(!spam.containsKey(i)){
                WordSpam= log((double)(0+1)/(spam.size()+countWords));//P(xi|spam)
                WordSpamProps.put(i,WordSpam);
            }
        }
    }

}
