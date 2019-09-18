package galdr;

import java.util.ArrayList;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class GaldrLoggerTest
  extends AbstractTest
{
  @Test
  public void log()
  {
    final String message1 = randomString();
    final String message2 = randomString();
    GaldrLogger.log( message1, null );
    final Throwable throwable = new Throwable();
    GaldrLogger.log( message2, throwable );

    final ArrayList<TestLogger.LogEntry> entries = getTestLogger().getEntries();
    assertEquals( entries.size(), 2 );
    final TestLogger.LogEntry entry1 = entries.get( 0 );
    assertEquals( entry1.getMessage(), message1 );
    assertNull( entry1.getThrowable() );
    final TestLogger.LogEntry entry2 = entries.get( 1 );
    assertEquals( entry2.getMessage(), message2 );
    assertEquals( entry2.getThrowable(), throwable );
  }
}
