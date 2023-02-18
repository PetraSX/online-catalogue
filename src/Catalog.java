import java.util.ArrayList;

public class Catalog implements Subject {
    private ArrayList<Course> courses;
    private ArrayList<Observer> observers;
    private static Catalog catalogInstance;
    private Catalog() {
        //private constructor for Singleton
        courses = new ArrayList<Course>();
        observers = new ArrayList<Observer>();
    }

    //Singleton with double check locking
    public static Catalog getInstance() {
        if (catalogInstance == null) {
            synchronized (Catalog.class) {
                if (catalogInstance == null) {
                    catalogInstance = new Catalog();
                }
            }
        }
        return catalogInstance;
    }

    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(Grade grade) {
        Notification notification = new Notification(grade);
        for(Observer observer : observers) {
            if(grade.getStudent().getFather() != null) {
                if(observer.toString().equals(grade.getStudent().getFather().toString())) {
                    observer.Update(notification);
                }
            }
            if(grade.getStudent().getMother() != null) {
                if(observer.toString().equals(grade.getStudent().getMother().toString())) {
                    observer.Update(notification);
                }
            }
        }
    }
}
