package study;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Printer printer = new Printer();

        Class<? extends Printer> cls = printer.getClass();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyAnnotation.class)) {
                MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
                int count = annotation.count();
                for (int i = 0; i < count; i++) {
                    method.invoke(printer);
                }
                System.out.println();
            }
        }
    }
}
