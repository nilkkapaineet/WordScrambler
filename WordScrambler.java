import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

class WordScrambler {
  
  public static void main(String args[]) throws IOException {  
    
    long tStart = System.currentTimeMillis();
    Scanner s = new Scanner ( System.in );
    System.out.print("Kirjoita nimesi: ");
    String first_name = s.next( );
    String[] letters5 = new String[5];
    ArrayList<String> wordList = new ArrayList<String>();
    StringBuffer stringBuffer = new StringBuffer();
    
    // opens a file of words
    String fileName = "5kirjainta.txt";  // file for words to be read
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
  
}
