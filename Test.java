import java.io.*;
import java.util.HashMap;

import static java.lang.StrictMath.log;
import static java.lang.StrictMath.signum;

public class Test {


    public static void main(String[] args) throws IOException {

        String s=args[0];
        boolean Spam=false;
        boolean bool;
        double totalH=0,totalS=0;
        double probH,probS;
        int tp=0;
        int tn=0;
        int spamsFound=0,hamsFound=0;
        int TestSpams=0,TestHams=0;
        int count=0;
        int training = Integer.parseInt(args[1]); //#files to be trained from
        if(training>9||training<1){
            System.out.println("Error!Max num of training files :9 ,min: 1");
            return;
        }
        Train tr= new Train();
        tr.Props(s,training);

        BufferedReader reader = null;
        String line;
        File f=null;
        try {
            f = new File(s); } //the main folder that contains other folders
        catch (NullPointerException e){
            System.err.println ("File not found.");
        }

        for(File f1: f.listFiles()){//for every testing subfolder

            count++;
            if (count>training){
                for(File texts: f1.listFiles()){//gia kathe txt
                    if (texts.getName().contains("spmsg")){
                        bool=true;
                        TestSpams+=1;
                    }
                    else {
                        bool = false;
                        TestHams+=1;
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

                        for (int j = 0; j < words.length; j++) {//mesa se mia grammh

                            if(tr.WordSpamProps.containsKey(words[j])){
                                totalS = totalS + tr.WordSpamProps.get(words[j]); //pol/se ta P(xi|spam)
                            }
                            if(tr.WordHamProps.containsKey(words[j])) {
                                totalH = totalH + tr.WordHamProps.get(words[j]);//pol/se ta P(xi|ham)
                            }
                        }
                        line = reader.readLine();
                    }//end of text

                    probS=log((double)tr.numOfSpams/tr.countTrainTxts)+totalS;//P(spam|xi)=P(spam)*P(xi|spam)/P(xi)
                    probH=log((double)tr.numOfHams/tr.countTrainTxts)+totalH;//P(ham|xi)=P(ham)*P(xi|ham)/P(xi)
                    if(probS>probH) {
                        Spam=true;//predicted as Spam
                        spamsFound++;
                    }
                    else{
                        Spam=false;//predicted as Ham
                        hamsFound++;
                    }

                    if(Spam&&bool) tp++;//predicted as Spam and it is Spam
                    if(!Spam&&!bool) tn++;//predicted as Ham and it is Ham


                    totalH=0;
                    totalS=0;

                }//for texts

            }//if

        }//for test files

        double Recall=(double)tp/TestSpams;//actual spams found/actual spams exists
        double Percision=(double)tp/spamsFound;//actual spams found/spams found
        double f1=2*Recall*Percision/(Recall+Percision);
        double accuracy=(double)(tp+tn)/(spamsFound+hamsFound);//correct prediction/total predictions

        System.out.println("Number of training messages: "+tr.countTrainTxts);
        System.out.println("Percision: "+Percision+" ,Recall: "+Recall+", F1: "+f1+" , Accuracy:"+ accuracy );


    }
}
