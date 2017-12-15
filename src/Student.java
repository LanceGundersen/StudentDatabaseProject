/**
 * Student.java
 * Lance Gundersen
 * 15DEC17
 * @version 1.0
 *
 * This program is part of the student database management project.
 */

public class Student {
  private double gradePoints = 0;
  private int creditHours = 0;
  private String name;
  private String major;

  Student(String name, String major) {
    this.name = name;
    this.major = major;
  }

  void courseCompleted(int credits, double gradeAsNumber) {
    this.gradePoints += (credits * gradeAsNumber);
    this.creditHours += credits;
  }

  String getName() {
    return this.name;
  }

  String getMajor() {
    return this.major;
  }

   public String toString() {
    return "Name: " + name + "\nMajor: " + major + "\nGPA: "
        + normalizedGPA(this.gradePoints,
        this.creditHours);
  }

  private double calculateGPA(double gradePoints, double creditHours) {
    return Math.round((((gradePoints / creditHours) * 100.0))) /
        100.0;
  }

  private double normalizedGPA(double gradePoints, double creditHours) {
    double gpa = calculateGPA(gradePoints, creditHours);
    return (this.creditHours == 0) ? 4 : gpa;
  }
}
