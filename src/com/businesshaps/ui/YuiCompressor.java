package com.businesshaps.ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
 

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class YuiCompressor {
	public static void main(String[] args){
		//YuiCompressor("C:\\Java\\apache-tomcat-7.0.25\\am\\ROOT\\WEB-INF\\gudeam\\combineFile\\_js_combine_jsfile.js", "C:\\Java\\apache-tomcat-7.0.25\\am\\ROOT\\WEB-INF\\gudeam\\combineFile\\_js_combine_jsfile.out.js");
		
	}
	public static void YuiCompressor(String fileIn, String fileOut) {
	    Reader in = null;
	    Writer out = null;
	    try {
	        Options o = new Options();
	        in = new InputStreamReader(new FileInputStream(fileIn), o.charset);
	 
	        JavaScriptCompressor compressor = new JavaScriptCompressor(in, new YuiCompressorErrorReporter());
	        in.close(); in = null;
	 
	        out = new OutputStreamWriter(new FileOutputStream(fileOut), o.charset);
	        compressor.compress(out, o.lineBreakPos, o.munge, o.verbose, o.preserveAllSemiColons, o.disableOptimizations);
	        out.flush();
	        out.close();
	    } catch(Exception e) {
	    	
	    } finally {

	    }
	}
}
class Options {
    public String charset = "UTF-8";
    public int lineBreakPos = -1;
    public boolean munge = true;
    public boolean verbose = false;
    public boolean preserveAllSemiColons = false;
    public boolean disableOptimizations = false;
}
class YuiCompressorErrorReporter implements ErrorReporter {
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (line < 0) {
        } else {
        }
    }
 
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (line < 0) {
        } else {
        }
    }
 
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        error(message, sourceName, line, lineSource, lineOffset);
        return new EvaluatorException(message);
    }
}