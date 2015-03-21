/**
 * Insight Data Engineering Challenge Code
 * @author: Nehal Doshi
 * @date: Mar 21st 2015
 */
package com.data.insight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;


/**
 * Counter -  Value class used by the treemap so that the map doesn't have to be updated with every value change
 *  
 *  */
class Counter {
	private int i = 1;
	
	public int read() {
	 return i;
	}
	
	public void increment() {
	 i++;
	}
}
/**
 * WordCount - word counter class with the two methods:
 * readFile - to read all the files from a dir and store the word count in a map
 * writeFile - outputs the word count, tab limited to the result file
 * @author Nehal Doshi
 *
 */
public class WordCount {
	
	// A TreeMap keeps keys in sorted order
	private TreeMap<String,Counter> counts = new TreeMap<String,Counter>();
	ArrayList<Double> medianList = new ArrayList<Double>();
	 RunningMedian rm = new RunningMedian();
	 
	 /** 
	  * readFile : - This method is twofold
	  * Counts the words in a line and invokes running median functionality
	  * Tokenizes the line to update the treemap with the word and its count
	  */
	private  void readFile(File fin) {
		BufferedReader br = null;
		try{
			FileInputStream fis = new FileInputStream(fin); 
			//Construct BufferedReader from InputStreamReader
			 br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null; 
			while ((line = br.readLine()) != null){
				StringTokenizer tokenizer = new StringTokenizer(line);
				
				//count the words and invoke running median
		        Double wordCount = (double) tokenizer.countTokens();
		       // System.out.println(wordCount);
		       
				Double rmedian = rm.runMedian(wordCount);
				medianList.add(rmedian);
				
	          //iterating through all the words available in that line and forming the key value pair
	            while (tokenizer.hasMoreTokens())
	            {
	               String word = (tokenizer.nextToken());
	               //remove punctuation
	               word = word.replaceAll("[^\\w\\s]","");
	               word = word.toLowerCase();
	               //add word to the treemap
	               if (counts.containsKey(word)){
	            	   ((Counter) counts.get(word)).increment();
	               }
	                else{
	                   counts.put(word, new Counter());
	                }
	            }			
			}
			br.close();
			fis.close();
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		catch(Exception e){
			System.err.println("Oops...An error occured processing the files");
			e.printStackTrace();
		}		
	}
		
	public Collection<Counter> values() {
		 return counts.values();
	}

	public Set<String> keySet() {
	 return counts.keySet();
	}

	public Counter getCounter(String s) {
	 return (Counter) counts.get(s);
	}
	
	/**
	 * 
	 * Writes the word counter map into the wc_result.txt file in the wc_output folder	  	 	   	
	 */
	private void writeFile(String path){
		
		BufferedWriter brWriter = null;
		try{
			File fop = new File(path);		
			FileOutputStream fos = new FileOutputStream(fop); 			
			brWriter = new BufferedWriter(new OutputStreamWriter(fos));	
			
			Iterator<String> keys = counts.keySet().iterator();
			 while (keys.hasNext()) {
			   String key = (String) keys.next();
			   brWriter.write(key + "\t" + getCounter(key).read());
			   brWriter.append("\n");
			 }
			brWriter.close();
			fos.close();
			System.out.println("Word Count updated successfully to wc_result.txt");
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		catch(Exception e){
			System.err.println("Unable to write word count successfully");
			e.printStackTrace();
		}		
	}
	
	/**
	 * 
	 * Output the running median into the med_result.txt file in the wc_output folder	  	 	   	
	 */
	private void writeMedianFile(String path){
		
		BufferedWriter brWriter = null;
		try{
			File fop = new File(path);		
			FileOutputStream fos = new FileOutputStream(fop); 			
			brWriter = new BufferedWriter(new OutputStreamWriter(fos));	
				
			 for (Double num:medianList) {
			   brWriter.write(Double.toString(num));
			   brWriter.append("\n");
			 }
			brWriter.close();
			fos.close();
			System.out.println("Running Median values successfully written to med_result.txt");
		}
		catch(IOException e){
			throw new RuntimeException(e);
		}
		catch(Exception e){
			System.err.println("Oops! An error occured outputing to med_result.txt file ");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Main class to invoke the word count
	 * @param args
	 */
	public static void main(String[] args){
	
			String sourceDir = "";
			String targetFilePath = "";
			String targetMedianFilePath = "";
		
		if (args != null && args.length == 3){	
			 sourceDir = args[0];
			 targetFilePath = args[1];
			 targetMedianFilePath = args[2];
		}
		else{
			System.out.println("Please pass in the source dir, path to wc_result.txt and path to med_result,txt");
		}
			System.out.println("Running the program...");
			WordCount wordCount = new WordCount();
			
	        File dir = new File(sourceDir);
	        if(dir.exists()){
		        File[] files = dir.listFiles();
		        
		        if (files.length > 0){
			        for (File f : files) {
			        	wordCount.readFile(f);
			        }
			        wordCount.writeFile(targetFilePath);
			        wordCount.writeMedianFile(targetMedianFilePath);
		        }
		        else{
		        	System.out.println("Please place files to parse in the input directory - wc_input");
		        }
	        }
	        else{
	        	System.out.println("Please ensure the input directory - wc_input exists" );
	        }
		
	}

}
