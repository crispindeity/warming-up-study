package study;

public class Printer {

    @MyAnnotation()
    public void printName() {
        System.out.println("name");
    }

    @MyAnnotation(count = 3)
    public void printAge() {
        System.out.println("age");
    }
}
