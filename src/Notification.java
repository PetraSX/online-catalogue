public class Notification {
    private Grade grade;

    public Notification(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        String str = "";
        str += "Student ";
        str += grade.getStudent().toString();
        str += " recieved a new grade. Check Catalog for details.";

        return str;
    }
}
