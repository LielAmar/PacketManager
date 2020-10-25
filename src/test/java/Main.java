import java.util.AbstractCollection;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println(isInheriting(ArrayList.class, AbstractCollection.class));
    }

    private static boolean isInheriting(Class<?> clazz, Class<?> superClass) {
        Class<?> current = clazz;

        while(current.getSuperclass().getSuperclass() != null) {
            if(current.getSuperclass().getName().equalsIgnoreCase(superClass.getName())) return true;

            current = current.getSuperclass();
        }
        return false;
    }
}
