package br.com.vaniltoncoroa.greetings.service;

import br.com.vaniltoncoroa.greetings.strategy.AnagramStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnagramService {
    
    private static final Logger logger = LoggerFactory.getLogger(AnagramService.class);
    
    private final Map<String, AnagramStrategy> strategies;
    
    @Autowired
    public AnagramService(@Qualifier("recursiveStrategy") AnagramStrategy recursiveStrategy,
                         @Qualifier("iterativeStrategy") AnagramStrategy iterativeStrategy,
                         @Qualifier("heapStrategy") AnagramStrategy heapStrategy,
                         @Qualifier("backtrackingStrategy") AnagramStrategy backtrackingStrategy) {
        this.strategies = Map.of(
            "recursive", recursiveStrategy,
            "iterative", iterativeStrategy,
            "heap", heapStrategy,
            "backtracking", backtrackingStrategy
        );
    }
    
    public List<String> generateAnagrams(String input, String algorithm) {
        AnagramStrategy strategy = strategies.get(algorithm.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid algorithm: " + algorithm + ". Valid options: recursive, iterative, heap, backtracking");
        }
        
        logger.info("Using {} for input: '{}'", strategy.getAlgorithmName(), input);
        return strategy.generateAnagrams(input);
    }
    
    public Map<String, List<String>> testAllAlgorithms(String input) {
        logger.info("Testing all algorithms for input: '{}'", input);
        return Map.of(
            "recursive", strategies.get("recursive").generateAnagrams(input),
            "iterative", strategies.get("iterative").generateAnagrams(input),
            "heap", strategies.get("heap").generateAnagrams(input),
            "backtracking", strategies.get("backtracking").generateAnagrams(input)
        );
    }
    
    public List<String> getAvailableAlgorithms() {
        return List.of("recursive", "iterative", "heap", "backtracking");
    }
}