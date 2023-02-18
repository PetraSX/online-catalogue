//import org.jetbrains.annotations.NotNull;
import java.lang.Double;
public class Grade implements Comparable<Grade>, Cloneable {
    private Double partialScore;
    private Double examScore;
    private Student student;
    private String course;

    public Double getPartialScore() {
        return partialScore;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Double getTotal() {
        if(partialScore != null && examScore == null) {
            return partialScore;
        } else if(partialScore == null && examScore != null) {
            return examScore;
        } else if(partialScore != null && examScore != null) {
            return partialScore + examScore;
        } else {
            return 0.0;
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Grade)super.clone();
    }

    @Override
    public int compareTo(Grade gr) {
        return Double.compare(gr.getTotal(), getTotal());
        }
}

