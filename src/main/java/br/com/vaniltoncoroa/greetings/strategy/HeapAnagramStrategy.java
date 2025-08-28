package br.com.vaniltoncoroa.greetings.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("heapStrategy")
public class HeapAnagramStrategy implements AnagramStrategy {
    
    private static final Logger logger = LoggerFactory.getLogger(HeapAnagramStrategy.class);
    
    @Override
    public List<String> generateAnagrams(String input) {
        long startTime = System.nanoTime();
        logger.info("Starting Heap's algorithm anagram generation for input: '{}'", input);
        
        List<String> result = new ArrayList<>();
        char[] array = input.toCharArray();
        heapPermutation(array, array.length, array.length, result);
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        logger.info("Heap's algorithm completed in {} ms. Generated {} anagrams.", duration, result.size());
        
        return result;
    }
    
    private void heapPermutation(char[] array, int size, int n, List<String> result) {
        if (size == 1) {
            result.add(new String(array));
            return;
        }
        
        for (int i = 0; i < size; i++) {
            heapPermutation(array, size - 1, n, result);
            
            if (size % 2 == 1) {
                swap(array, 0, size - 1);
            } else {
                swap(array, i, size - 1);
            }
        }
    }
    
    private void swap(char[] array, int i, int j) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Heap's Algorithm";
    }
}