  import java.util.Scanner;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.util.Arrays;

/**
 * Class that contains helper methods for the Review Lab
 **/
public class Review {
  
  private static HashMap<String, Double> sentiment = new HashMap<String, Double>();
  private static ArrayList<String> posAdjectives = new ArrayList<String>();
  private static ArrayList<String> negAdjectives = new ArrayList<String>();

  private static final String SPACE = " ";
  
  static{
    try {
      Scanner input = new Scanner(new File("cleanSentiment.csv"));
      while(input.hasNextLine()){
        String[] temp = input.nextLine().split(",");
        sentiment.put(temp[0],Double.parseDouble(temp[1]));
        //System.out.println("added "+ temp[0]+", "+temp[1]);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing cleanSentiment.csv");
    }
  
  
  //read in the positive adjectives in postiveAdjectives.txt
     try {
      Scanner input = new Scanner(new File("positiveAdjectives.txt"));
      while(input.hasNextLine()){
        String temp = input.nextLine().trim();
        //System.out.println(temp);
        posAdjectives.add(temp);
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing postitiveAdjectives.txt\n" + e);
    }   
 
  //read in the negative adjectives in negativeAdjectives.txt
     try {
      Scanner input = new Scanner(new File("negativeAdjectives.txt"));
      while(input.hasNextLine()){
        negAdjectives.add(input.nextLine().trim());
      }
      input.close();
    }
    catch(Exception e){
      System.out.println("Error reading or parsing negativeAdjectives.txt");
    }   
  }
  
  /** 
   * returns a string containing all of the text in fileName (including punctuation), 
   * with words separated by a single space 
   */
  public static String textToString( String fileName )
  {  
    String temp = "";
    try {
      Scanner input = new Scanner(new File(fileName));
      
      //add 'words' in the file to the string, separated by a single space
      while(input.hasNext()){
        temp = temp + input.next() + " ";
      }
      input.close();
      
    }
    catch(Exception e){
      System.out.println("Unable to locate " + fileName);
    }
    //make sure to remove any additional space that may have been added at the end of the string.
    return temp.trim();
  }
  
  /**
   * @returns the sentiment value of word as a number between -1 (very negative) to 1 (very positive sentiment) 
   */
  public static double sentimentVal( String word )
  {
    try
    {
      return sentiment.get(word.toLowerCase());
    }
    catch(Exception e)
    {
      return 0;
    }
  }
  
  /**
   * Returns the ending punctuation of a string, or the empty string if there is none 
   */
  public static String getPunctuation( String word )
  { 
    String punc = "";
    for(int i=word.length()-1; i >= 0; i--){
      if(!Character.isLetterOrDigit(word.charAt(i))){
        punc = punc + word.charAt(i);
      } else {
        return punc;
      }
    }
    return punc;
  }

      /**
   * Returns the word after removing any beginning or ending punctuation
   */
  public static String removePunctuation( String word )
  {
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(0)))
    {
      word = word.substring(1);
    }
    while(word.length() > 0 && !Character.isAlphabetic(word.charAt(word.length()-1)))
    {
      word = word.substring(0, word.length()-1);
    }
    
    return word;
  }
 
  /** 
   * Randomly picks a positive adjective from the positiveAdjectives.txt file and returns it.
   */
  public static String randomPositiveAdj()
  {
    int index = (int)(Math.random() * posAdjectives.size());
    return posAdjectives.get(index);
  }
  
  /** 
   * Randomly picks a negative adjective from the negativeAdjectives.txt file and returns it.
   */
  public static String randomNegativeAdj()
  {
    int index = (int)(Math.random() * negAdjectives.size());
    return negAdjectives.get(index);
    
  }
  
  /** 
   * Randomly picks a positive or negative adjective and returns it.
   */
  public static String randomAdjective()
  {
    boolean positive = Math.random() < .5;
    if(positive){
      return randomPositiveAdj();
    } else {
      return randomNegativeAdj();
    }
  }

  /**
   * Returns the total sentiment of a review
   * @param fileName
   * @return double
   */
  public static double totalSentiment(String fileName){
    double total = 0;
    String matching = textToString(fileName);
    String[] split = matching.split(" ");

    for(int i = 0; i < split.length - 1; i ++){
      split[i] = removePunctuation(split[i]);
      split[i] = split[i].toLowerCase(Locale.ENGLISH);
      System.out.println(split[i]);
      total = total + sentimentVal(split[i]);
    }
 
    return total;
      
  }

  public static int starRating(String fileName){
    double total =  totalSentiment(fileName);
    if (total > 40.0){
      return 5;
    }
    else if(total > 30.0){
      return 4;
    }
    else if(total > 20.0){
      return 3;
    }
    else if(total > 15.0){
      return 2;
    }
    else{
      return 1;
    }
  }
  public static double enhancedTotalSentiment(String fileName){
    double total = 0;
    String matching = textToString(fileName);
    String[] split = matching.split("\\.");
    double temp = 0;
    for(int i = 0; i < split.length; i ++){
      temp = 0;
      ArrayList<Double> values = new ArrayList<Double>();
      String[] split2 = split[i].split(" ");
      for(int a = 0; a < split2.length; a ++){
        String word = split2[a];
        
        temp = temp + sentimentVal(word);
        values.add(Review.sentimentVal(word));
      }
      if(temp > 0){
        double newTotal = 0;
        for(int j = 0; j < values.size(); j ++){
          newTotal = newTotal + Math.abs(values.get(j));
        }
        total = total + newTotal;
      }
      if(temp < 0){
        double newTotal = 0;
        for(int j = 0; j < values.size(); j ++){
          newTotal = newTotal + Math.abs(values.get(j));
        }
        total = total - newTotal;
      }
    }
 
    return total;
      
  }
}

