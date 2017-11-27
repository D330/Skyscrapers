package ru.flippy.skyscrapers.sdk.api.model.profile;

import java.util.List;

public class Cup {
    private String name;
    private List<CupStep> steps;
    private boolean complete;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CupStep> getSteps() {
        return steps;
    }

    public void setSteps(List<CupStep> steps) {
        this.steps = steps;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
