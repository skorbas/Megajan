package util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ExceptionUtil
{
    public static String getStackTraceAsString( Exception ex )
    {
	    final Writer writer = new StringWriter();
		final PrintWriter printWriter = new PrintWriter( writer );
		ex.printStackTrace( printWriter );
		return writer.toString();
    }
}
