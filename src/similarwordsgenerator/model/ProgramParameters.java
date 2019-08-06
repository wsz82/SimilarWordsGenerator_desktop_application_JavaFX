package similarwordsgenerator.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public final class ProgramParameters implements Serializable {
    private final Analyser analyser;
    private final List<String> input;
    private final String path;
    private final boolean sorted;
    private final boolean firstCharAsInInput;
    private final boolean lastCharAsInInput;
    private final boolean compressed;
    private final int numberOfWords;
    private final int minWordLength ;
    private final int maxWordLength;
    private final int levelOfCompression;

    private ProgramParameters(Builder builder) {
        this.analyser = builder.analyser;
        this.input = builder.input;
        this.path = builder.path;
        this.sorted = builder.sorted;
        this.firstCharAsInInput = builder.firstCharAsInInput;
        this.lastCharAsInInput = builder.lastCharAsInInput;
        this.compressed = builder.compressed;
        this.numberOfWords = builder.numberOfWords;
        this.minWordLength = builder.minWordLength;
        this.maxWordLength = builder.maxWordLength;
        this.levelOfCompression = builder.levelOfCompression;
    }

    public Analyser getAnalyser() {
        return analyser;
    }

    public List<String> getInput() {
        return input;
    }

    public String getPath() {
        return path;
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

    public boolean isCompressed() {
        return compressed;
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

    public int getLevelOfCompression() {
        return levelOfCompression;
    }

    public final static class Builder {
        private Analyser analyser = null;
        private List<String> input = Collections.emptyList();
        private String path = null;
        private boolean sorted = true;
        private boolean firstCharAsInInput = true;
        private boolean lastCharAsInInput = true;
        private boolean compressed = false;
        private int numberOfWords = 10;
        private int minWordLength = 0;  //number 0 is a flag for default word length
        private int maxWordLength = 0;  //number 0 is a flag for default word length
        private int levelOfCompression = 0; //number 0 is a flag for non-compression

        public Builder setAnalyser(Analyser analyser) {
            this.analyser = analyser;
            return this;
        }

        public Builder setInput(List<String> input) {
            this.input = input;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
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

        public Builder setCompressed(boolean compressed) {
            this.compressed = compressed;
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

        public Builder setLevelOfCompression(int levelOfCompression) {
            this.levelOfCompression = levelOfCompression;
            return this;
        }

        public ProgramParameters build() {
            return new ProgramParameters(this);
        }
    }
}
