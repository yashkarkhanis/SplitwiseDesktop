package com.splitwise.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class SplitwiseLogger {
	static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;

    static {
    	try {
    		setup();
    	} catch(Exception e) {
    		
    	}
    }
    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler console = rootLogger.getHandlers()[0];
        rootLogger.getHandlers()[0].setLevel(Level.FINEST);
        /*Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }*/
        
        console.setFormatter(new MyConsoleFormatter());

        
        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("Logging.log");
        fileHTML = new FileHandler("Logging.log.html");
        
        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(new MyConsoleFormatter());
        logger.addHandler(fileTxt);
        
        // create an HTML formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
    }
    
    static class MyHtmlFormatter extends Formatter {
        // this method is called for every log records
        public String format(LogRecord rec) {
            StringBuffer buf = new StringBuffer(1000);
            buf.append("<tr>\n");

            // colorize any levels >= WARNING in red
            if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
                buf.append("\t<td style=\"color:red\">");
                buf.append("<b>");
                buf.append(rec.getLevel());
                buf.append("</b>");
            } else {
                buf.append("\t<td>");
                buf.append(rec.getLevel());
            }

            buf.append("</td>\n");
            buf.append("\t<td>");
            buf.append(calcDate(rec.getMillis()));
            buf.append("</td>\n");
            buf.append("<td>" + rec.getSourceClassName() + "</td>\n");
            buf.append("<td>" + rec.getSourceMethodName() + "</td>\n");
            buf.append("\t<td>");
            buf.append(formatMessage(rec));
            buf.append("</td>\n");
            buf.append("</tr>\n");

            return buf.toString();
        }

        private String calcDate(long millisecs) {
            SimpleDateFormat date_format = new SimpleDateFormat("MMddyyHHmm");
            Date resultdate = new Date(millisecs);
            return date_format.format(resultdate);
        }

        // this method is called just after the handler using this
        // formatter is created
        public String getHead(Handler h) {
            return "<!DOCTYPE html>\n<head>\n<style>\n"
                + "table { width: 100% }\n"
                + "th { font:bold 10pt Tahoma; }\n"
                + "td { font:normal 10pt Tahoma; }\n"
                + "h1 {font:normal 11pt Tahoma;}\n"
                + "</style>\n"
                + "<meta http-equiv=\"refresh\" content=\"2\"/>"
                + "</head>\n"
                + "<body>\n"
                + "<h1>" + (new Date()) + "</h1>\n"
                + "<table border=\"0\" cellpadding=\"5\" cellspacing=\"3\">\n"
                + "<tr align=\"left\">\n"
                + "\t<th style=\"width:10%\">Loglevel</th>\n"
                + "\t<th style=\"width:15%\">Time</th>\n"
                + "\t<th style=\"width:15%\">Class</th>\n"
                + "\t<th style=\"width:15%\">Method</th>\n"
                + "\t<th style=\"width:75%\">Log Message</th>\n"
                + "</tr>\n";
          }

        // this method is called just after the handler using this
        // formatter is closed
        public String getTail(Handler h) {
            return "</table>\n</body>\n</html>";
        }
    }
    
    static class MyConsoleFormatter extends Formatter {
        // this method is called for every log records
        public String format(LogRecord rec) {
            StringBuffer buf = new StringBuffer(1000);
            
            buf.append(calcDate(rec.getMillis()));
            buf.append(" ");
            buf.append("[" + rec.getSourceClassName() +" " + rec.getSourceMethodName() + " " + Thread.currentThread().getName() + "] ");
            for(int i=buf.toString().length();i<100;i++) {
            	buf.append(" ");
            }
            if(rec.getMessage().length() > 100) {
            	buf.append("\n");
            	buf.append(rec.getMessage());
            	buf.append("\n");
            } else {
            	buf.append(rec.getMessage());
            }
            
            buf.append("\n");

            return buf.toString();
        }
        
        public String getHead(Handler h) {
        	StringBuffer buf = new StringBuffer(1000);
        	buf.append("\n\n");
        	for(int i=buf.toString().length();i<200;i++) {
            	buf.append("=");
            }
        	buf.append("\n");
        	return buf.toString();
        }

        private String calcDate(long millisecs) {
            SimpleDateFormat date_format = new SimpleDateFormat("MMddyyHHmm");
            Date resultdate = new Date(millisecs);
            return date_format.format(resultdate);
        }
    }

}
