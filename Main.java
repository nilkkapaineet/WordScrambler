package com.company;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.io.FileWriter;

public class Main {

    public static void main(String args[]) throws IOException {

        long tStart = System.currentTimeMillis();
        Scanner s = new Scanner ( System.in );
        System.out.println("Tervetuloa pelaamaan sanapeliä! Järjestä kirjaimet uudelleen muodostaaksesi sanan. Mahdollisuuksia sanaan voi olla useita, mutta vain yksi niistä on oikea.");
        System.out.print("Kirjoita nimesi: ");
        String first_name = s.next( );
        String[] letters5 = new String[5];
        ArrayList<String> wordList = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();

        System.out.print("Haluatko pelata neljän (4), viiden (5) vai (6) kuuden sanan peliä? ");
        int noOfLetters = Integer.parseInt(s.next() );
        // opens a file of words
        String fileName = "";
        if (noOfLetters == 4) {
            fileName = "4kirjainta.txt";
        } else if (noOfLetters == 5) {
            fileName = "5kirjainta.txt";
        } else if (noOfLetters == 6) {
            fileName = "6kirjainta.txt";
        }
        // file for words to be read

        try { // read a file line by line
            File file = new File(fileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                wordList.add(line);
                stringBuffer.append("\n");
            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        for(int i=0;i<5;i++) {
            letters5[i] = wordList.get(random.nextInt(wordList.size() ) );
        }

        String[] drow = new String[5];
        // preparing words to be scrambled
        for(int i=0;i<5;i++) {
            String word = letters5[i];
            drow[i] = scramble(word);
        }

        for(int i=0;i<5;i++) {
            System.out.println("Mikä sana: " + drow[i] + " ?");
            // game round and timer starts

            String userInput = "";
            while(!userInput.equals(letters5[i]) ) {
                userInput = s.next( );
                userInput = new String(userInput.getBytes(), "utf-8" );
                if (!userInput.equals(letters5[i]) ) {
                    System.out.print("Ei käy, koita uudelleen: ");
                } else {
                    System.out.print("Oikein! ");
                }
            }
        }

        // game round ends, count time elapsed
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("Hienoa, " + first_name + "! Oikea sana löytyi. Aikaa kului: " + elapsedSeconds);
        highscore(noOfLetters, elapsedSeconds, first_name);

    }

    // scrambling method
    public static String scramble(String woord) {
        String droow = "";
        Random rnd = new Random();
        String[] tempString = new String[woord.length()];

        // generate different random numbers
        ArrayList<Integer> letters = new ArrayList<Integer>();
        int counter = 0;
        while (letters.size() < woord.length() ) {
            int random = rnd.nextInt(woord.length() );
            if (!letters.contains(random)) {
                // generate also a scrambled word
                tempString[counter] = woord.substring(random, random+1);
                letters.add(random);
                counter++;
            }
        }

        for(int i=0;i<woord.length();i++) {
            droow = droow.concat(tempString[i]);
        }
        return droow;
    }

    public static void highscore(int nol, double secs, String hn) {
        // high score

        Date date = new Date();
        String strDate = date.toString();

        double score=0;
        String name="";
        String sdate = "";

        int hs_position = 0;

        highScoreObject[] hso = new highScoreObject[100];

        StringBuffer stringBuffer = new StringBuffer();
        String hfileName = "";
        if (nol == 4) {
            hfileName = "h4kirjainta.txt";
        } else if (nol == 5) {
            hfileName = "h5kirjainta.txt";
        } else if (nol == 6) {
            hfileName = "h6kirjainta.txt";
        }

        int i=0;
        int countoor = 0;

        try { // read a file line by line
            File file = new File(hfileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            fileReader.close();

            Pattern p = Pattern.compile("\\#.*?\\#");
            Matcher m = p.matcher(stringBuffer.toString());

            while(m.find())
            {
                CharSequence line2 = m.group().subSequence(1, m.group().length()-1);
                String highscoreLine = line2.toString();

                if (i%3 == 0) {
                    score = Double.parseDouble(highscoreLine);
                }
                if (i%3 == 1) {
                    name = highscoreLine;
                }
                if (i%3 == 2) {
                    sdate = highscoreLine;
                    hso[countoor] = new highScoreObject(score, name, sdate);
                    //hso[countoor].printHSO();
                    countoor++;
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //------------------------------------------------------

        // open file for writing
        FileWriter fw;
        String hf = "";
        if (nol == 4) {
            hf = "h4kirjainta.txt";
        } else if (nol == 5) {
            hf = "h5kirjainta.txt";
        } else if (nol == 6) {
            hf = "h6kirjainta.txt";
        }

        // find out your position at highscore list
        int position = 0;
        boolean found_position = true;
        do {
            if (hso[position].getTime() > secs) {
                found_position = false;
            }
            position++;
        } while(found_position);
        position--; // move one step back

        try {
            fw = new FileWriter(new File(hf));
            for(int j=0;j<countoor;j++) {
                if (position == j) {
                    fw.write(String.format("#" + secs + "# #" + hn + "# #" + strDate + "#"));
                    fw.write(System.lineSeparator()); //new line
                    position += 1;
                    System.out.println("Onnittelut! Olit sijalla " + position);
                    hs_position = position;
                    position -= 1;
                    fw.write(String.format("#" + hso[j].getTime() + "# #" + hso[j].getName() + "# #" + hso[j].getDate() + "#"));
                    fw.write(System.lineSeparator()); //new line
                } else {
                    fw.write(String.format("#" + hso[j].getTime() + "# #" + hso[j].getName() + "# #" + hso[j].getDate() + "#"));
                    fw.write(System.lineSeparator()); //new line
                }
            }
            fw.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int ii=0;
        do {
            int iii = ii+1;
            if (hs_position == iii) {
                System.out.println("Sija " + hs_position + ": " + secs + " " + hn + " " + strDate);
                iii++;
            }
            System.out.print("Sija " + iii + ": ");
            hso[ii].printHSO();
            ii++;
        } while(ii<countoor);

    }
}

class highScoreObject {

    double time;
    String name_hs;
    String date_hs;

    public highScoreObject(double t, String nhs, String dhs) {
        time = t;
        name_hs = nhs;
        date_hs = dhs;
    }

    public void printHSO() {
        System.out.println(time + " " + name_hs + " " + date_hs);
    }

    public void setTime(double t) {
        time = t;
    }

    public String getName() {
        return name_hs;
    }

    public String getDate() {
        return date_hs;
    }

    public double getTime() {
        return time;
    }

}

