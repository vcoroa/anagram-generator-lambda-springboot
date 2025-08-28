package br.com.vaniltoncoroa.greetings.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("iterativeStrategy")
public class IterativeAnagramStrategy implements AnagramStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(IterativeAnagramStrategy.class);
    
    @Override
    public List<String> generateAnagrams(String input) {
        long startTime = System.nanoTime();
        logger.info("Starting iterative anagram generation for input: '{}'", input);
        
        List<String> result = new ArrayList<>();
        
        if (input.isEmpty()) {
            result.add("");
            return result;
        }
        
        result.add(String.valueOf(input.charAt(0)));
        
        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            List<String> newResult = new ArrayList<>();
            
            for (String anagram : result) {
                for (int j = 0; j <= anagram.length(); j++) {
                    String newAnagram = anagram.substring(0, j) + currentChar + anagram.substring(j);
                    newResult.add(newAnagram);
                }
            }
            result = newResult;
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        logger.info("Iterative algorithm completed in {} ms. Generated {} anagrams.", duration, result.size());
        
        return result;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Iterative Algorithm";
    }
}