package com.example.cropdiseasesdetection;

public class userhelper {


    String idnumber;
    String fullname;
    String Email;
    String password;
    String location;
    int usertype;
    String phone;
    String uid;
    String dategraduated;
    String jobcategory;
    String education;
    String gender;
    String skills;

    public userhelper()
    {

    }

    public userhelper(String idnumber, String fullname, String email, String password, String location, int usertype, String phone, String uid, String dategraduated, String jobcategory, String education, String gender, String skills) {
        this.idnumber = idnumber;
        this.fullname = fullname;
        Email = email;
        this.password = password;
        this.location = location;
        this.usertype = usertype;
        this.phone = phone;
        this.uid = uid;
        this.dategraduated=dategraduated;
        this.jobcategory=jobcategory;
        this.education=education;
        this.gender=gender;
        this.skills=skills;
    }


    public String getDategraduated() {
        return dategraduated;
    }

    public void setDategraduated(String dategraduated) {
        this.dategraduated = dategraduated;
    }

    public String getJobcategory() {
        return jobcategory;
    }

    public void setJobcategory(String jobcategory) {
        this.jobcategory = jobcategory;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
