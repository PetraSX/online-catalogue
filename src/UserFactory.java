public class UserFactory {
    private static UserFactory factoryInstance;

    private UserFactory() {

    }

    //Made the UserType for easy swithching
    public enum UserType {
        Student,
        Parent,
        Assistant,
        Teacher
    }


    //Made the UserFactory a singleton (one instance across the app)
    public static UserFactory getFactory() {
        if(factoryInstance == null) {
            synchronized (UserFactory.class) {
                if(factoryInstance == null) {
                    factoryInstance = new UserFactory();
                }
            }
        }
        return factoryInstance;
    }

    //Returning the new user type according to the Factory Desing Pattern
    public User getUser(UserType userType, String firstName, String lastName) {
        switch (userType) {
            case Student:
                return new Student(firstName, lastName);
            case Parent:
                return new Parent(firstName, lastName);
            case Assistant:
                return new Assistant(firstName, lastName);
            case Teacher:
                return new Teacher(firstName, lastName);
            default:
                return null;
        }
    }
}