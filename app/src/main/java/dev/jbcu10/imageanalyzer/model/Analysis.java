package dev.jbcu10.imageanalyzer.model;

/**
 * Created by dev on 1/13/18.
 */

public class Analysis {

    private String score;
    private String description;

    public Analysis(String score, String description) {
        this.score = score;
        this.description = description;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
