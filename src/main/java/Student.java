package com.school.model;

public class Student {
    private int studentId;
    private String rollNumber;
    private String name;
    private String className;
    private String section;
    private String qrCodePath;

    public Student() {}

    public Student(String rollNumber, String name, String className, String section) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.className = className;
        this.section = section;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getQrCodePath() { return qrCodePath; }
    public void setQrCodePath(String qrCodePath) { this.qrCodePath = qrCodePath; }
}
