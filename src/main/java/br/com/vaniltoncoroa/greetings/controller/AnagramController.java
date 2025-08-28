package br.com.vaniltoncoroa.greetings.controller;

import br.com.vaniltoncoroa.greetings.service.AnagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/anagram")
public class AnagramController {
    
    private final AnagramService anagramService;
    
    @Autowired
    public AnagramController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }
    
    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateAnagrams(
            @RequestParam String input,
            @RequestParam String algorithm) {
        
        try {
            List<String> anagrams = anagramService.generateAnagrams(input, algorithm);
            
            return ResponseEntity.ok(Map.of(
                "input", input,
                "algorithm", algorithm,
                "anagrams", anagrams,
                "count", anagrams.size()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "availableAlgorithms", anagramService.getAvailableAlgorithms()
            ));
        }
    }
    
    @GetMapping("/algorithms")
    public ResponseEntity<List<String>> getAvailableAlgorithms() {
        return ResponseEntity.ok(anagramService.getAvailableAlgorithms());
    }
}