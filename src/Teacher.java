public class Teacher extends User implements Element {
    private boolean examScoresValidated;
    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
        examScoresValidated = false;
    }

    public void setExamScoresValidated() {
        examScoresValidated = true;
    }

    public boolean getExamScoresValidated() {
        return examScoresValidated;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}