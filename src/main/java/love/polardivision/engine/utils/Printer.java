package love.polardivision.engine.utils;

import java.util.Arrays;
import java.util.Optional;

public class Printer {

  public static void log(String title, Object... objects) {
    StringBuilder builder = new StringBuilder(title);
    Optional<Object> first = Arrays.stream(objects).findFirst();
    Arrays.stream(objects).forEach(object -> builder.append(object != first.get() ? ", " : ": ").append(object.toString()));
    System.out.println(builder);
  }

}
