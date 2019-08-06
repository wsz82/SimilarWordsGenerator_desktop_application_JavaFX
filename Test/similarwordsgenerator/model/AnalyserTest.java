package similarwordsgenerator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalyserTest {
    @Nested
    class EmptyListInAnalyser {
        private List<String> input = new ArrayList<>(Arrays.asList(""));
        private Analyser analyser = new Analyser();

        @BeforeEach
        void fulfillAnalyser() {
            analyser.analyze(input);
        }

        @Test
        void wordLengthShouldBeEmpty() {
            assertTrue(analyser.getWordsLengths().isEmpty());
        }

        @Test
        void firstCharShouldBeEmpty() {
            assertTrue(analyser.getFirstChars().isEmpty());
        }

        @Test
        void lastCharShouldBeEmpty() {
            assertTrue(analyser.getLastChars().isEmpty());
        }

        @Test
        void charsCountShouldBeEmpty() {
            assertTrue(analyser.getCharsCount().isEmpty());
        }
    }
    @Nested
    class AnalyserWithOneChar {
        private List<String> input = new ArrayList<>(Arrays.asList("A"));
        private Analyser analyser = new Analyser();

        @BeforeEach
        void fulfillAnalyser() {
            analyser.analyze(input);
        }

        @Test
        void shouldContainOneWordLength() {
            assertEquals(1, analyser.getWordsLengths().size());
        }

        @Test
        void wordLengthShouldBe_1() {
            assertTrue(analyser.getWordsLengths().contains(1));
        }

        @Test
        void shouldContainOneFirstChar() {
            assertEquals(1, analyser.getFirstChars().size());
        }

        @Test
        void firstCharShouldBe_A() {
            assertTrue(analyser.getLastChars().contains('A'));
        }

        @Test
        void shouldContainOneLastChar() {
            assertEquals(1, analyser.getLastChars().size());
        }

        @Test
        void lastCharShouldBe_A() {
            assertTrue(analyser.getLastChars().contains('A'));
        }

        @Test
        void charsCountMapShouldBeEmpty() {
            assertTrue(analyser.getCharsCount().isEmpty());
        }

        @Test
        void charInMapShouldBe_A() {
            assertTrue(analyser.getLastChars().contains('A'));
        }

        @Test
        void shouldBeEmptyInListInCharsCount() {
            assertTrue(analyser.getCharsCount().values().isEmpty());
        }
    }

    @Nested
    class AnalyserWithOneWord {

        private List<String> input = new ArrayList<>(Arrays.asList("Analyser"));
        private Analyser analyser = new Analyser();

        @BeforeEach
        void fulfillAnalyser() {
            analyser.analyze(input);
        }

        @Test
        void wordLengthShouldBe_8() {
            assertTrue(analyser.getWordsLengths().contains(8));
        }

        @Test
        void firstCharShouldBe_A() {
            assertTrue(analyser.getFirstChars().contains('A'));
        }

        @Test
        void lastCharShouldBe_r() {
            assertTrue(analyser.getLastChars().contains('r'));
        }

        @Test
        void shouldContain7charsInMap() {
            assertEquals(7, analyser.getCharsCount().size());
        }

        @Test
        void mapShouldContain7Lists() {
            assertEquals(7, analyser.getCharsCount().values().size());
        }
    }

    @Nested
    class AnalyserWithMultipleWords {
        private List<String> input = Arrays.asList("John", "Nancy", "Stacy");
        private Analyser analyser = new Analyser();

        @BeforeEach
        void fulfillAnalyser() {
            analyser.analyze(input);
        }

        @Test
        void wordLengthShouldContain4() {
            assertTrue(analyser.getWordsLengths().contains(4));
        }

        @Test
        void wordLengthShouldContain5() {
            assertTrue(analyser.getWordsLengths().contains(5));
        }

        @Test
        void firstCharShouldContain_J() {
            assertTrue(analyser.getFirstChars().contains('J'));
        }

        @Test
        void firstCharShouldContain_N() {
            assertTrue(analyser.getFirstChars().contains('N'));
        }

        @Test
        void firstCharShouldContain_S() {
            assertTrue(analyser.getFirstChars().contains('S'));
        }

        @Test
        void lastCharShouldContain_n() {
            assertTrue(analyser.getLastChars().contains('n'));
        }

        @Test
        void lastCharShouldContain_y() {
            assertTrue(analyser.getLastChars().contains('y'));
        }

        @Test
        void shouldContain9charsInMap() {
            assertEquals(9, analyser.getCharsCount().size());
        }

        @Test
        void mapShouldContain9Lists() {
            assertEquals(9, analyser.getCharsCount().values().size());
        }
    }

    @Nested
    class CompressTests {
        private List<String> input = new ArrayList<>(Arrays.asList("John", "Nancy", "Stacy"));
        private Analyser analyser = new Analyser();

        @BeforeEach
        void fulfillAnalyser() {
            analyser.analyze(input);
        }

        @Test
        void wordsLengthsSizeShouldBe_2() {
            analyser.compress(2);
            assertEquals(2, analyser.getWordsLengths().size());
        }

        @Test
        void wordsLengthsSizeShouldBe_3() {
            analyser.compress(4);
            assertEquals(3, analyser.getWordsLengths().size());
        }

        @Test
        void firstCharsSizeShouldBe_2() {
            analyser.compress(2);
            assertEquals(2, analyser.getFirstChars().size());
        }

        @Test
        void firstCharsSizeShouldBe_3() {
            analyser.compress(4);
            assertEquals(3, analyser.getFirstChars().size());
        }

        @Test
        void lastCharsSizeShouldBe_2() {
            analyser.compress(2);
            assertEquals(2, analyser.getLastChars().size());
        }

        @Test
        void lastCharsSizeShouldBe_3() {
            analyser.compress(4);
            assertEquals(3, analyser.getLastChars().size());
        }

        @Test
        void charsCountListsSizeShouldBe_1() {
            analyser.compress(1);
            assertEquals(1, analyser.getCharsCount().get('c').size());
        }

        @Test
        void charsCountListsSizeShouldBe_2() {
            analyser.compress(3);
            assertEquals(2, analyser.getCharsCount().get('c').size());
        }
    }
}
