public class Assistant extends User implements Element {
    private boolean partialScoresValidated;
    public Assistant(String firstName, String lastName) {
        super(firstName, lastName);
        partialScoresValidated = false;
    }

    public void setpartialScoresValidated() {
        partialScoresValidated = true;
    }

    public boolean getpartialScoresValidated() {
        return partialScoresValidated;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}