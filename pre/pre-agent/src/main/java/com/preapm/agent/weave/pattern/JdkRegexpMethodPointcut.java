package com.preapm.agent.weave.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class JdkRegexpMethodPointcut {

    /**
     * Regular expressions to match.
     */
    private String[] patterns = new String[0];

    /**
     * Regular expressions <strong>not</strong> to match.
     */
    private String[] excludedPatterns = new String[0];
    /**
     * Compiled form of the patterns.
     */
    private Pattern[] compiledPatterns = new Pattern[0];

    /**
     * Compiled form of the exclusion patterns.
     */
    private Pattern[] compiledExclusionPatterns = new Pattern[0];

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public String[] getExcludedPatterns() {
        return excludedPatterns;
    }

    public void setExcludedPatterns(String[] excludedPatterns) {
        this.excludedPatterns = excludedPatterns;
    }

    public Pattern[] getCompiledPatterns() {
        return compiledPatterns;
    }

    public void setCompiledPatterns(Pattern[] compiledPatterns) {
        this.compiledPatterns = compiledPatterns;
    }

    public Pattern[] getCompiledExclusionPatterns() {
        return compiledExclusionPatterns;
    }

    public void setCompiledExclusionPatterns(Pattern[] compiledExclusionPatterns) {
        this.compiledExclusionPatterns = compiledExclusionPatterns;
    }

    /**
     * Initialize {@link Pattern Patterns} from the supplied {@code String[]}.
     */
    protected void initPatternRepresentation(String[] patterns) throws PatternSyntaxException {
        this.compiledPatterns = compilePatterns(patterns);
    }

    /**
     * Initialize exclusion {@link Pattern Patterns} from the supplied {@code String[]}.
     */
    protected void initExcludedPatternRepresentation(String[] excludedPatterns) throws PatternSyntaxException {
        this.compiledExclusionPatterns = compilePatterns(excludedPatterns);
    }

    /**
     * Returns {@code true} if the {@link Pattern} at index {@code patternIndex}
     * matches the supplied candidate {@code String}.
     */
    protected boolean matches(String pattern, int patternIndex) {
        Matcher matcher = this.compiledPatterns[patternIndex].matcher(pattern);
        return matcher.matches();
    }

    /**
     * Returns {@code true} if the exclusion {@link Pattern} at index {@code patternIndex}
     * matches the supplied candidate {@code String}.
     */
    protected boolean matchesExclusion(String candidate, int patternIndex) {
        Matcher matcher = this.compiledExclusionPatterns[patternIndex].matcher(candidate);
        return matcher.matches();
    }


    /**
     * Compiles the supplied {@code String[]} into an array of
     * {@link Pattern} objects and returns that array.
     */
    public Pattern[] compilePatterns(String[] source) throws PatternSyntaxException {
        Pattern[] destination = new Pattern[source.length];
        for (int i = 0; i < source.length; i++) {
            destination[i] = Pattern.compile(source[i]);
        }
        return destination;
    }

    /**
     * Match the specified candidate against the configured patterns.
     * @param signatureString "java.lang.Object.hashCode" style signature
     * @return whether the candidate matches at least one of the specified patterns
     */
    public boolean matchesPattern(String signatureString) {
        for (int i = 0; i < this.patterns.length; i++) {
            boolean matched = matches(signatureString, i);
            if (matched) {
                for (int j = 0; j < this.excludedPatterns.length; j++) {
                    boolean excluded = matchesExclusion(signatureString, j);
                    if (excluded) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
