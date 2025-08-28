package br.com.vaniltoncoroa.greetings.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("backtrackingStrategy")
public class BacktrackingAnagramStrategy implements AnagramStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(BacktrackingAnagramStrategy.class);
    
    @Override
    public List<String> generateAnagrams(String input) {
        long startTime = System.nanoTime();
        logger.info("Starting backtracking anagram generation for input: '{}'", input);
        
        List<String> result = new ArrayList<>();
        char[] chars = input.toCharArray();
        boolean[] used = new boolean[chars.length];
        char[] current = new char[chars.length];
        
        backtrack(chars, used, current, 0, result);
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        logger.info("Backtracking algorithm completed in {} ms. Generated {} anagrams.", duration, result.size());
        
        return result;
    }
    
    private void backtrack(char[] chars, boolean[] used, char[] current, int position, List<String> result) {
        if (position == chars.length) {
            result.add(new String(current));
            return;
        }
        
        for (int i = 0; i < chars.length; i++) {
            if (!used[i]) {
                used[i] = true;
                current[position] = chars[i];
                
                backtrack(chars, used, current, position + 1, result);
                
                used[i] = false;
            }
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Backtracking Algorithm";
    }
}