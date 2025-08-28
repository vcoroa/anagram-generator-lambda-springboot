package br.com.vaniltoncoroa.greetings.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("recursiveStrategy")
public class RecursiveAnagramStrategy implements AnagramStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(RecursiveAnagramStrategy.class);
    
    @Override
    public List<String> generateAnagrams(String input) {
        long startTime = System.nanoTime();
        logger.info("Starting recursive anagram generation for input: '{}'", input);
        
        List<String> result = new ArrayList<>();
        generateAnagramsRecursive("", input, result);
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        logger.info("Recursive algorithm completed in {} ms. Generated {} anagrams.", duration, result.size());
        
        return result;
    }
    
    private void generateAnagramsRecursive(String prefix, String remaining, List<String> result) {
        if (remaining.length() == 0) {
            result.add(prefix);
            return;
        }
        
        for (int i = 0; i < remaining.length(); i++) {
            char ch = remaining.charAt(i);
            String newPrefix = prefix + ch;
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generateAnagramsRecursive(newPrefix, newRemaining, result);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Recursive Algorithm";
    }
}