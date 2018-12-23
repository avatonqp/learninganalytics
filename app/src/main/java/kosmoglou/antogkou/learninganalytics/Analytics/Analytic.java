package kosmoglou.antogkou.learninganalytics.Analytics;

public class Analytic {
    public int year;
    public int month;
    public int day;
    public String typeOf;
    public String value1;
    public String value2;
    public String classRoom;

    public Analytic(){
        // empty constructor for firestore
    }

    public Analytic(int year, int month, int day, String typeOf, String value1, String value2, String classRoom) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.typeOf = typeOf;
        this.value1 = value1;
        this.value2 = value2;
        this.classRoom = classRoom;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }
    public String getTypeOf() {
        return typeOf;
    }
    public void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }
    public String getValue1() {
        return value1;
    }
    public void setValue1(String value1) {
        this.value1 = value1;
    }
    public String getValue2() {
        return value2;
    }
    public void setValue2(String value2) {
        this.value2 = value2;
    }
    public String getClassRoom() {
        return classRoom;
    }
    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }
}
