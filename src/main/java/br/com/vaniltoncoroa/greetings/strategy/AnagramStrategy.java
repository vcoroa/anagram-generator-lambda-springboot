package br.com.vaniltoncoroa.greetings.strategy;

import java.util.List;

public interface AnagramStrategy {
    List<String> generateAnagrams(String input);
    String getAlgorithmName();
}