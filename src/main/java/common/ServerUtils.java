package common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for server-related functions.
 */
public class ServerUtils {

  /**
   * Returns the current timestamp in the desired format.
   *
   * @return The formatted current timestamp.
   */
  public static String getTimestamp() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return sdf.format(new Date());
  }
}
