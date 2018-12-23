package kosmoglou.antogkou.learninganalytics.Classes;

public class Classroom {
    private String classname;
    private String description;
    private String classCode;
    private String syllabus;
    private String createdby;

    public Classroom(){
        //Empty constructor needed for firestore
    }

    public Classroom(String classname, String description){
        this.classname = classname;
        this.description = description;
    }

    public Classroom(String classname, String description, String createdby){
        this.classname = classname;
        this.description = description;
        this.createdby = createdby;
    }

    public Classroom(String classname, String description, String classCode, String syllabus){
        this.classname = classname;
        this.description = description;
        this.classCode = classCode;
        this.syllabus = syllabus;
    }

    public Classroom(String classname, String description, String classCode, String syllabus, String createdby){
        this.classname = classname;
        this.description = description;
        this.classCode = classCode;
        this.syllabus = syllabus;
        this.createdby = createdby;
    }

    public String getClassname() {
        return classname;
    }

    public String getDescription() {
        return description;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getCreatedby() {
        return createdby;
    }
}
