import java.util.ArrayList;

public class BestTotalScore implements CourseStrategy {
    @Override
    public Student getBestStudent(ArrayList<Grade> grades) {
        double max_totalScore = (double)0;
        Grade gr = new Grade();

        for(Grade grade : grades) {
            if(grade.getTotal().compareTo(max_totalScore) > 0) {
                max_totalScore = grade.getTotal();
                gr = grade;
            }
        }
        return gr.getStudent();
    }

}
