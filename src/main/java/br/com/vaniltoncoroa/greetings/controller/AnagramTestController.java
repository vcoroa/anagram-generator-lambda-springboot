package br.com.vaniltoncoroa.greetings.controller;

import br.com.vaniltoncoroa.greetings.service.AnagramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anagram/test")
public class AnagramTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(AnagramTestController.class);
    private final AnagramService anagramService;
    
    @Autowired
    public AnagramTestController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }
    
    @GetMapping("/compare")
    public ResponseEntity<Map<String, Object>> compareAllAlgorithms(@RequestParam String input) {
        logger.info("Starting performance comparison for input: '{}'", input);
        
        Map<String, Object> results = new HashMap<>();
        Map<String, Long> executionTimesNs = new HashMap<>();
        Map<String, Double> executionTimesMs = new HashMap<>();
        Map<String, Double> executionTimesUs = new HashMap<>();
        Map<String, List<String>> anagramResults = new HashMap<>();
        
        // Test each algorithm separately to measure individual performance
        List<String> algorithms = anagramService.getAvailableAlgorithms();
        
        for (String algorithm : algorithms) {
            long startTime = System.nanoTime();
            List<String> anagrams = anagramService.generateAnagrams(input, algorithm);
            long endTime = System.nanoTime();
            
            long executionTimeNs = endTime - startTime;
            double executionTimeMs = executionTimeNs / 1_000_000.0;
            double executionTimeUs = executionTimeNs / 1_000.0;
            
            executionTimesNs.put(algorithm, executionTimeNs);
            executionTimesMs.put(algorithm, executionTimeMs);
            executionTimesUs.put(algorithm, executionTimeUs);
            anagramResults.put(algorithm, anagrams);
            
            logger.info("Algorithm '{}' executed in {:.3f} ms ({:.1f} μs, {} ns)", 
                       algorithm, executionTimeMs, executionTimeUs, executionTimeNs);
        }
        
        // Find fastest and slowest algorithms based on nanoseconds
        String fastestAlgorithm = executionTimesNs.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
            
        String slowestAlgorithm = executionTimesNs.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
        
        results.put("input", input);
        results.put("executionTimesNs", executionTimesNs);
        results.put("executionTimesMs", executionTimesMs);
        results.put("executionTimesUs", executionTimesUs);
        results.put("fastestAlgorithm", fastestAlgorithm);
        results.put("slowestAlgorithm", slowestAlgorithm);
        results.put("anagramCount", anagramResults.get(algorithms.get(0)).size());
        results.put("results", anagramResults);
        
        logger.info("Performance comparison completed. Fastest: {} ({:.3f} ms), Slowest: {} ({:.3f} ms)", 
                   fastestAlgorithm, executionTimesMs.get(fastestAlgorithm),
                   slowestAlgorithm, executionTimesMs.get(slowestAlgorithm));
        
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/benchmark")
    public ResponseEntity<Map<String, Object>> benchmarkAlgorithm(
            @RequestParam String input,
            @RequestParam String algorithm,
            @RequestParam(defaultValue = "1") int iterations) {
        
        logger.info("Starting benchmark for algorithm '{}' with {} iterations for input: '{}'", 
                   algorithm, iterations, input);
        
        long totalTimeNs = 0;
        long minTimeNs = Long.MAX_VALUE;
        long maxTimeNs = Long.MIN_VALUE;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            anagramService.generateAnagrams(input, algorithm);
            long endTime = System.nanoTime();
            
            long executionTimeNs = endTime - startTime;
            totalTimeNs += executionTimeNs;
            minTimeNs = Math.min(minTimeNs, executionTimeNs);
            maxTimeNs = Math.max(maxTimeNs, executionTimeNs);
            
            logger.debug("Iteration {} completed in {:.3f} ms ({:.1f} μs)", 
                        i + 1, executionTimeNs / 1_000_000.0, executionTimeNs / 1_000.0);
        }
        
        long averageTimeNs = totalTimeNs / iterations;
        double totalTimeMs = totalTimeNs / 1_000_000.0;
        double averageTimeMs = averageTimeNs / 1_000_000.0;
        double minTimeMs = minTimeNs / 1_000_000.0;
        double maxTimeMs = maxTimeNs / 1_000_000.0;
        double totalTimeUs = totalTimeNs / 1_000.0;
        double averageTimeUs = averageTimeNs / 1_000.0;
        double minTimeUs = minTimeNs / 1_000.0;
        double maxTimeUs = maxTimeNs / 1_000.0;
        
        Map<String, Object> benchmark = new HashMap<>();
        benchmark.put("input", input);
        benchmark.put("algorithm", algorithm);
        benchmark.put("iterations", iterations);
        benchmark.put("totalTimeNs", totalTimeNs);
        benchmark.put("totalTimeMs", totalTimeMs);
        benchmark.put("totalTimeUs", totalTimeUs);
        benchmark.put("averageTimeNs", averageTimeNs);
        benchmark.put("averageTimeMs", averageTimeMs);
        benchmark.put("averageTimeUs", averageTimeUs);
        benchmark.put("minTimeNs", minTimeNs);
        benchmark.put("minTimeMs", minTimeMs);
        benchmark.put("minTimeUs", minTimeUs);
        benchmark.put("maxTimeNs", maxTimeNs);
        benchmark.put("maxTimeMs", maxTimeMs);
        benchmark.put("maxTimeUs", maxTimeUs);
        
        logger.info("Benchmark completed. Average: {:.3f} ms ({:.1f} μs), Min: {:.3f} ms, Max: {:.3f} ms", 
                   averageTimeMs, averageTimeUs, minTimeMs, maxTimeMs);
        
        return ResponseEntity.ok(benchmark);
    }
    
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, Object>> getTestSuggestions() {
        Map<String, Object> suggestions = Map.of(
            "message", "Para medir diferenças de performance significativas, use strings maiores:",
            "testInputs", Map.of(
                "small", List.of("abc", "test", "word"),
                "medium", List.of("abcdef", "string", "example"),
                "large", List.of("abcdefgh", "algorithm", "performance"),
                "warning", "Strings > 8 caracteres podem demorar muito (8! = 40.320 permutações)"
            ),
            "recommendedTests", List.of(
                "/anagram/test/compare?input=abcdef",
                "/anagram/test/benchmark?input=string&algorithm=backtracking&iterations=10",
                "/anagram/test/compare?input=test"
            )
        );
        
        return ResponseEntity.ok(suggestions);
    }
}