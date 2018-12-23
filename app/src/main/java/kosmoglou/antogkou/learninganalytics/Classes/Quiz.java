package kosmoglou.antogkou.learninganalytics.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz{
    public String name; // the name of the quiz
    public String description;
    public int duration; // the integer of duration. in minutes
    public int totalQuestions; // the integer of total questions
    public List<Question> questions = new ArrayList<Question>(); // the list of questions


    public void Quiz(String name, String description, int duration, int totalQuestions, List<Question> questions){
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.totalQuestions = totalQuestions;
        this.questions = questions;
    }

    public void Quiz(){
        // no-arg empty constructor
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
