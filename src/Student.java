import java.util.ArrayList;
import java.util.Comparator;

public class Student extends User{

    private Parent mother;
    private Parent father;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public void setMother(Parent mother) {
        this.mother = mother;
    }

    public void setFather(Parent father) {
        this.father = father;
    }

    public Parent getFather() {
        return father;
    }

    public Parent getMother() {
        return mother;
    }

    static class SortbyFullName implements Comparator<Student> {
        public int compare(Student a, Student b) {
            return a.toString().compareTo(b.toString());
        }
    }
}