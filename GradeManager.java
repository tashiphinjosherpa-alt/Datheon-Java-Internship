import java.util.*;

public class GradeManager {

    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {

            System.out.println("\n===== STUDENT GRADE MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. Record Marks");
            System.out.println("3. Class Average & Median");
            System.out.println("4. Generate Report Cards");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addStudent();
                    break;

                case 2:
                    recordMarks();
                    break;

                case 3:
                    classStatistics();
                    break;

                case 4:
                    generateReportCards();
                    break;

                case 5:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 5);

        sc.close();
    }

    private static void addStudent() {

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        students.add(new Student(id, name));

        System.out.println("Student Added Successfully!");
    }

    private static void recordMarks() {

        if (students.isEmpty()) {
            System.out.println("No students available.");
            return;
        }

        System.out.print("Enter Student ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student Not Found!");
            return;
        }

        System.out.print("Enter Subject: ");
        String subject = sc.nextLine();

        System.out.print("Enter Score: ");
        int score = sc.nextInt();

        student.addGrade(subject, score);

        System.out.println("Marks Recorded Successfully!");
    }

    private static Student findStudent(int id) {

        for (Student s : students) {
            if (s.getId() == id)
                return s;
        }

        return null;
    }

    private static void classStatistics() {

        ArrayList<Double> averages = new ArrayList<>();

        for (Student s : students) {
            averages.add(s.calculateAverage());
        }

        if (averages.isEmpty()) {
            System.out.println("No data available.");
            return;
        }

        double total = 0;

        for (double avg : averages) {
            total += avg;
        }

        double classAverage = total / averages.size();

        Collections.sort(averages);

        double median;

        int size = averages.size();

        if (size % 2 == 0) {
            median = (averages.get(size / 2 - 1)
                    + averages.get(size / 2)) / 2;
        } else {
            median = averages.get(size / 2);
        }

        System.out.printf("\nClass Average : %.2f%n", classAverage);
        System.out.printf("Median Score  : %.2f%n", median);
    }

    private static void generateReportCards() {

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("\n==============================================================");
        System.out.printf("%-5s %-20s %-10s %-10s\n",
                "ID", "Name", "Average", "GPA");
        System.out.println("==============================================================");

        for (Student s : students) {

            System.out.printf("%-5d %-20s %-10.2f %-10.2f\n",
                    s.getId(),
                    s.getName(),
                    s.calculateAverage(),
                    s.calculateGPA());

            for (Map.Entry<String, Integer> entry :
                    s.getGrades().entrySet()) {

                System.out.printf("      %-15s : %d\n",
                        entry.getKey(),
                        entry.getValue());
            }

            System.out.println("----------------------------------------------------------");
        }
    }
}