package similarwordsgenerator;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public final class Parameters implements Serializable {

    transient private final List<String> input;
    private final boolean sorted;
    private final boolean firstCharAsInInput;
    private final boolean lastCharAsInInput;
    private final int numberOfWords;
    private final int minWordLength ;
    private final int maxWordLength;
    transient private final Path path;
    private final int compressionNumber;

    private Parameters(Builder builder) {
        this.input = builder.input;
        this.sorted = builder.sorted;
        this.firstCharAsInInput = builder.firstCharAsInInput;
        this.lastCharAsInInput = builder.lastCharAsInInput;
        this.numberOfWords = builder.numberOfWords;
        this.minWordLength = builder.minWordLength;
        this.maxWordLength = builder.maxWordLength;
        this.path = builder.path;
        this.compressionNumber = builder.compressionNumber;
    }

    public List<String> getInput() {
        return input;
    }

    public boolean isSorted() {
        return sorted;
    }

    public boolean isFirstCharAsInInput() {
        return firstCharAsInInput;
    }

    public boolean isLastCharAsInInput() {
        return lastCharAsInInput;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    public Path getPath() {
        return path;
    }

    public int getCompressionNumber() {
        return compressionNumber;
    }

    public final static class Builder {

        private List<String> input = Collections.emptyList();
        private boolean sorted = true;
        private boolean firstCharAsInInput = true;
        private boolean lastCharAsInInput = true;
        private int numberOfWords = 1;
        private int minWordLength = 0;  //number 0 is a flag for default word length
        private int maxWordLength = 0;  //number 0 is a flag for default word length
        private Path path;
        private int compressionNumber;

        public Builder setInput(List<String> input) {
            this.input = input;
            return this;
        }

        public Builder setSorted(boolean sorted) {
            this.sorted = sorted;
            return this;
        }

        public Builder setFirstCharAsInInput(boolean firstCharAsInInput) {
            this.firstCharAsInInput = firstCharAsInInput;
            return this;
        }

        public Builder setLastCharAsInInput(boolean lastCharAsInInput) {
            this.lastCharAsInInput = lastCharAsInInput;
            return this;
        }

        public Builder setNumberOfWords(int numberOfWords) {
            this.numberOfWords = numberOfWords;
            return this;
        }

        public Builder setMinWordLength(int minWordLength) {
            this.minWordLength = minWordLength;
            return this;
        }

        public Builder setMaxWordLength(int maxWordLength) {
            this.maxWordLength = maxWordLength;
            return this;
        }

        public Builder setPath(Path path) {
            this.path = path;
            return this;
        }

        public Builder setCompressionNumber(int compressionNumber) {
            this.compressionNumber = compressionNumber;
            return this;
        }

        public Parameters build() {
            return new Parameters(this);
        }
    }
}
