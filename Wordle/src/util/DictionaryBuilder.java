package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dictionary.DictionaryInitializer;
import dictionary.WordLoader;
import word.Word;

public class DictionaryBuilder {
	private static final String GOALFILE = "goals.txt";
	private static final String ALLOWEDFILE = "allowed_words.txt";
	private static final String SOLUTIONFILE = "solutions.txt";
	private static final String OLDSOLUTIONFILE = "oldsolutions.txt";

	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		

//		DictionaryInitializer dict = new DictionaryInitializer();
		WordLoader loader = new WordLoader();
		List<Word> glist = loader.loadWords(GOALFILE);
		List<Word> alist = loader.loadWords(ALLOWEDFILE);
		List<Word> slist = loader.loadWords(SOLUTIONFILE);
		System.out.println(GOALFILE + " has "+ glist.size() + " words.");
		System.out.println(ALLOWEDFILE + " has "+ alist.size() + " words.");
		System.out.println(SOLUTIONFILE + " has "+ slist.size() + " words.");
		
//		alist.addAll(glist);
//		Set<Word> aset = new HashSet<Word>(alist); 
//		alist = new ArrayList<Word>(aset);
//		loader.writeWords(alist, "allowed_words.txt");
//		
//		aset = new HashSet<Word>(glist);
//		alist = new ArrayList<Word>(aset);
//		loader.writeWords(alist, "goals.txt");
//		
		Set<Word> aset = new HashSet<Word>(slist);
		alist = new ArrayList<Word>(aset);
		loader.writeWords(alist, "past_solutions.txt");
		
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

	}

}
