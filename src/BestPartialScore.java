import java.util.ArrayList;

public class BestPartialScore implements CourseStrategy{
    @Override
    public Student getBestStudent(ArrayList<Grade> grades) {
        double max_partialScore = (double)0;
        Grade gr = new Grade();

        for(Grade grade : grades) {
            if(grade.getPartialScore().compareTo(max_partialScore) > 0) {
                max_partialScore = grade.getPartialScore();
                gr = grade;
            }
        }
        return gr.getStudent();
    }
}
