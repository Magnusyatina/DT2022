package org.magnusario

import java.util.stream.Collectors
import java.util.stream.Stream

class Application {

    static void main(String[] args) {
        String message = """
                            Арсен Люпен Третий — внук 
                        """
        println message
        compute(message)

    }

    static def compute(String message) {
        message = message.replaceAll("[^а-яА-Яa-zA-Z\\ ]", "")
        String[] words = message.split(" ")
        Map<Integer, Integer> messageInfo = Stream.of(words)
                .map(word -> word.trim())
                .filter(word -> !word.isEmpty())
                .sorted((word1, word2) -> word1.length() <=> word2.length())
                .collect(Collectors.toMap(String::length, e -> 1, (e1, e2) -> e1 + e2))
        int wordsCount = messageInfo.values().stream().reduce(0, (e1, e2) -> e1 + e2)
        println "-------------------"
        println "Количество слов $wordsCount"
        List<Double> possibilities = messageInfo.values()
                .stream()
                .map(repeatCount -> repeatCount / wordsCount)
                .collect(Collectors.toList())
        println "-------------------"
        print "Xi: "
        Set<Integer> wordLengths = messageInfo.keySet()
        wordLengths.each {
            print String.format("%-10s", it)
        }
        println()
        print "Pi: "
        possibilities.each {
            print String.format("%-10s", it)
        }
        println "\n-------------------"
        double expectedValue = getExpectedValue(wordLengths, possibilities)
        println "Математическое ожидание равняется $expectedValue"
        double dispersion = getDispersion(wordLengths, possibilities, expectedValue)
        println "Дисперсия равняется $dispersion"
    }

    static double getExpectedValue(Collection values, Collection possibilities) {
        if (values.size() != possibilities.size())
            throw new IllegalArgumentException("Длина списка значений не соответствует длине списка вероятностей вероятностей")
        Double expectedValue = 0.0
        (0..values.size() - 1).each {
            expectedValue += values[it] * possibilities[it]
        }
        expectedValue
    }

    static double getDispersion(Collection<Double> values, Collection<Double> possibilities, double expectedValue) {
        if (values.size() != possibilities.size())
            throw new IllegalArgumentException("Длина списка значений не соответствует длине списка вероятностей вероятностей")
        double dispersion = 0.0
        (0..values.size() - 1).each {
            dispersion += ((values[it] - expectedValue) ** 2) / possibilities[it]
        }
        dispersion
    }
}
