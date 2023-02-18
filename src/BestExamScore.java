import java.util.ArrayList;

public class BestExamScore implements CourseStrategy{
    @Override
    public Student getBestStudent(ArrayList<Grade> grades) {
        double max_examScore = (double)0;
        Grade gr = new Grade();

        for(Grade grade : grades) {
            if(grade.getExamScore().compareTo(max_examScore) > 0) {
                max_examScore = grade.getExamScore();
                gr = grade;
            }
        }
        return gr.getStudent();
    }
}
