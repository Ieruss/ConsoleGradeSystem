import java.util.*;
import java.util.Map;

public class GradeBook{

    public static Scanner sc = new Scanner(System.in);
    public static Map<String, String> DBTeachers = new HashMap<String, String>();

    public static String get_S(String text) {
        System.out.print(text);
        return sc.nextLine();
    }

    public static void Login() {
        DBTeachers.put("Windows", "window");
        // DBStudents.add(new Quartet<>("Aomine", "Daiki", "230107101", 21));
        // DBStudents.add(new Quartet<>("Kise", "Ryota", "230107102", 24));
        // DBStudents.add(new Quartet<>("Akashi", "Seijuro", "230107103", 25));

        while(true) {
            String username = get_S("Enter username: ");
            String password = get_S("Enter password: ");
            if (DBTeachers.containsKey(username) && DBTeachers.get(username).equals(password)) {
                System.out.println("You have successfully logged in to your account");
                return;
            }
            System.out.println("The name or password is not correct");
        }
    }

    public static boolean Check_Student_Name(String name) {

        return name.matches("[A-Z][a-z]+");
    }
    public static boolean Contain_Student_ID(String ID) {
        for(Quartet<String, String, String, Integer> Q : DBStudents) {
            if (Q.ID().equals(ID)) {
                return true;
            }
        }
        return false;
    }
    public static boolean Check_Student_ID(String ID) {
        return ID.matches("[0-9]{9}");
    }

    public static boolean Check_Number(String number, Integer L, Integer R) {
        if(number.isEmpty() || !number.matches("[0-9]+")) return false;
        int x = Integer.parseInt(number);
        return (L <= x && x <= R);
    }

    public static class Quartet<A, B, C, D> {
        private final A Name;
        private final B Surname;
        private final C ID;
        public  D Score;
        public Quartet(A Name, B Surname, C ID, D Score) {
            this.Name = Name;
            this.Surname = Surname;
            this.ID = ID;
            this.Score = Score;
        }
        public C ID() {return ID;}
        public D Score() {return Score;}
    }
    public static int[] grades = new int[]{90, 80, 70, 60, 0};
    public static int[] sum = new int[5];
    public static char[] alp = new char[]{'A', 'B', 'C', 'D', 'F'};
    public static List<Quartet<String, String, String, Integer>> DBStudents = new ArrayList<>();

    public static int Highest_score, Lowest_score;
    public static double Average_score;
    public static void Update_results() {
        if (DBStudents.isEmpty()) return;
        sum = new int[5];
        Highest_score = -1; Lowest_score = 1000; Average_score = -1;
        for(Quartet<String,String,String,Integer> Q : DBStudents){
           Highest_score = Math.max(Highest_score, Q.Score());
           Lowest_score = Math.min(Lowest_score, Q.Score());
           Average_score += Q.Score();
           for(int j = 0; j < 5; ++j) {
               if (grades[j] <= Q.Score()) {
                   sum[j] += 1;
                   break;
               }
           }
        }
        Average_score /= (double)DBStudents.size();
    }
    public static void AddStudent(boolean type){
        String student_name = get_S("\nEnter name: ");
        while(!Check_Student_Name(student_name)) {
            System.out.println("Student's name is incorrect, please write correctly");
            student_name = get_S("Enter name: ");
        }

        String student_surname = get_S("Enter surname: ");
        while(!Check_Student_Name(student_surname)) {
            System.out.println("Student's surname is incorrect, please write correctly");
            student_surname = get_S("Enter surname: ");
        }

        String student_ID = get_S("Enter ID: ");
        while(!Check_Student_ID(student_ID) || Contain_Student_ID(student_ID)) {
            System.out.println("Student's ID is incorrect, please write correctly");
            student_ID = get_S("Enter ID: ");
        }

        String student_score = get_S("Enter Score: ");
        while(!Check_Number(student_score, 0, 100)) {
            System.out.println("Student's Score is incorrect, please write correctly");
            student_score = get_S("Enter Score: ");
        }

        DBStudents.add(new Quartet<>(student_name, student_surname, student_ID, Integer.parseInt(student_score)));
        Update_results();
        System.out.println("Student " + (type ? "added" : "modified")  + " successfully");
    }
    public static void Remove_Student(String ID) {
        for(int i = 0; i < DBStudents.size(); ++i) {
            if (DBStudents.get(i).ID().equals(ID)) {
                DBStudents.remove(i);
                return;
            }
        }
    }

    public static void MangeRecords(){
        if (DBStudents.isEmpty()) {
            System.out.println("You can't manage change the data because there are no students");
            return;
        }
        GenerateReports();
        DeleteStudent(false);
        AddStudent(false);
    }

    public static void CalulateGrades() {
        Update_results();
        System.out.println("\nGrades calculated" + "\n" + "Grade Details added");
    }
    public static void ViewStatistics() {
        System.out.println("\nTotal Students: " + DBStudents.size());
        System.out.println("Highest Students: " + Highest_score);
        System.out.println("Average Score: " + String.format("%(.2f", Average_score));
        System.out.println("Lowest Students: " + Lowest_score);
        for(int i = 0; i < 5; ++i) {
            System.out.println(alp[i] + ": " + sum[i]);
        }
    }

    public static void DeleteStudent(boolean need) {
        if (DBStudents.isEmpty()) {
            System.out.println("You can't manage change the data because there are no students");
            return;
        }
        if (need) {
            GenerateReports();
        }
        String choosed = get_S("Enter number or ID: ");
        while(!Check_Number(choosed, 1, DBStudents.size()) && !Contain_Student_ID(choosed)) {
            System.out.println("You are entered incorrect number");
            choosed = get_S("Enter number or ID: ");
        }

        if (Check_Number(choosed, 1, DBStudents.size())) {
            choosed = DBStudents.get(Integer.parseInt(choosed)-1).ID();
        }

        Remove_Student(choosed);
        if (need) {
            System.out.println("Student deleted successfully.");
        }
    }

    public static void output(String s, Integer n) {
        System.out.print(s);
        for(int i = 1; i <= n - (int)s.length(); ++i) {
            System.out.print(" ");
        }
    }

    public static void GenerateReports() {
        if (DBStudents.isEmpty()) {
            System.out.println("There are no students on the list");
            return;
        }

        int i = 1;
        System.out.println("The are list of students");
        output("№", 3); output("Name", 10); output("Surname", 10); output("ID", 15); output("Score\n",0);
        for(Quartet<String, String, String, Integer> Q : DBStudents){
            System.out.print(i++ + ": ");
            output(Q.Name, 10); output(Q.Surname, 10); output(Q.ID, 15); output(Q.Score.toString()+"\n", 0);
        }
    }

    public static void Enter() {
        System.out.print("\nEnter anything to continue: ");
        sc.nextLine();
    }

    public static void Menu() {
        while(true) {
            System.out.println("\nMain Menu");
            System.out.println("1. Add student");
            System.out.println("2. Manage Records");
            System.out.println("3. Calculate Grades");
            System.out.println("4. View Statistics");
            System.out.println("5. Generate Reports");
            System.out.println("6. Delete Student");
            System.out.println("7. Logout and exit\n");
            System.out.print("Enter your choice: ");

            String s = sc.nextLine();
            if (!Check_Number(s, 1, 7)) {
                System.out.println("You entered the number incorrectly");
            } else {
                int c = Integer.parseInt(s);
                switch(c){
                    case 1:
                        AddStudent(true);
                        Enter();
                        break;
                    case 2:
                        MangeRecords();
                        Enter();
                        break;
                    case 3:
                        CalulateGrades();
                        Enter();
                        break;
                    case 4:
                        ViewStatistics();
                        Enter();
                        break;
                    case 5:
                        GenerateReports();
                        Enter();
                        break;
                    case 6:
                        DeleteStudent(true);
                        Enter();
                        break;
                    case 7:
                        System.out.println("You are successfully log outed");
                        System.exit(0);
                }
            }
        }
    }

    public static void main(String[] args) {
        Login();
        Menu();
    }
}