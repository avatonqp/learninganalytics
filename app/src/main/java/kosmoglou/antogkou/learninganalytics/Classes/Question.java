package kosmoglou.antogkou.learninganalytics.Classes;

public class Question {
    public String theQuestion;
    public String answerCorrect;
    public String answerTwo;
    public String answerThree;
    public String answerFour;

    public Question(String theQuestion, String answerCorrect, String answerTwo, String answerThree, String answerFour){
        this.theQuestion = theQuestion;
        this.answerCorrect = answerCorrect;
        this.answerTwo = answerTwo;
        this.answerThree = answerThree;
        this.answerFour = answerFour;
    }

    public Question(){
        // no-arg empty constructor
    }

    public String getTheQuestion() {
        return theQuestion;
    }

    public void setTheQuestion(String theQuestion) {
        this.theQuestion = theQuestion;
    }

    public String getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(String answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    public String getAnswerTwo() {
        return answerTwo;
    }

    public void setAnswerTwo(String answerTwo) {
        this.answerTwo = answerTwo;
    }

    public String getAnswerThree() {
        return answerThree;
    }

    public void setAnswerThree(String answerThree) {
        this.answerThree = answerThree;
    }

    public String getAnswerFour() {
        return answerFour;
    }

    public void setAnswerFour(String answerFour) {
        this.answerFour = answerFour;
    }
}
