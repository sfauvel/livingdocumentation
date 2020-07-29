package org.dojo.livingdoc.application;

public class SpecificRule {

    // tag::example[]
    /**
     *  stem:[(1.55^"level") + sqrt(12*"level") + 50]
     */
    public double xpNeedsToNextLevel(int level) {
        return Math.pow(1.55, level) + Math.sqrt(12*level) + 50;
    }
    // end::example[]
}
