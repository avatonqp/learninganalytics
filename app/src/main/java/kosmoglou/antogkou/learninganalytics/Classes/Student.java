package kosmoglou.antogkou.learninganalytics.Classes;

public class Student {
    private String firstname;
    private String lastname;
    private String username;
    private String usertype;
    private String AM;
    private String borndate;
    private String email;
    private String phone;
    private String fathersname;

    private Student(){
        //Empty constructor needed for firestore
    }

    private Student(String firstname, String lastname, String username, String usertype){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.usertype = usertype;
    }

    private Student(String firstname, String lastname, String username, String usertype, String AM, String borndate, String email, String phone, String fathersname){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.usertype = usertype;
        this.AM = AM;
        this.borndate = borndate;
        this.email = email;
        this.phone = phone;
        this.fathersname = fathersname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFullname() {
        return lastname + " " + firstname;
    }

    public String getUsername() {
        return username;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getAM() { return AM; }

    public String getBorndate() { return borndate; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public String getFathersname() { return fathersname; }
}
