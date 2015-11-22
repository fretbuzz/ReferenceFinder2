package wildhacks2015v2.referencefinder;

import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import java.io.*;
        import java.net.*;
        import java.util.ArrayList;
        import java.util.regex.*;
        import org.apache.commons.io.IOUtils;
        import android.app.Application;
        import android.test.ApplicationTestCase;


        import org.apache.commons.io.IOUtils;

import java.util.ArrayList;

/**
 * Created by Joe on 11/21/2015.
 */
public class WikiQuoteAccess {



    //private ScriptEngine engine;
    //private Invocable inv;
    //private Object wqa;
    private static Pattern quoteResultStartPat = Pattern.compile("<li><div class='mw-search-result-heading'>");;
    private static Pattern quoteResultEndPat = Pattern.compile("</li>");

	/*
	private void collect(){
		System.out.println("collect");
		String txt = textField.getText();
		try {
			String out = querySite(txt);
			outputField.setText(out);
			System.out.println(findResults(out, quoteResultStartPat, quoteResultEndPat));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    private static String querySite(String inputString) throws IOException{
        final URL url = new URL("http://en.wikiquote.org/w/index.php?format=json&action=search"
                + "&search="+URLEncoder.encode(inputString,"UTF-8")
                + "&namespace=0"
                + "&suggest=");
        final URLConnection urlConnection = url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.connect();
        final OutputStream outputStream = urlConnection.getOutputStream();
		/*outputStream.write(("{\"format\": \"json\","
							+ "\"action\": \"opensearch\","
							+ "\"namespace\": 0,"
							+ "\"suggest\": \"\","
							+ "\"search\": \"" + inputString + "\"}").getBytes("UTF-8"));*/
//	{\"fNamn\": \"" + inputString + "\"}").getBytes("UTF-8"));
        outputStream.flush();
        final InputStream inputStream = urlConnection.getInputStream();
		/*byte[] b = new byte[10000];
		inputStream.read(b);*/
        String theString = IOUtils.toString(inputStream);
        IOUtils.closeQuietly(inputStream);
        return theString;
    }

    public static ArrayList<String> findResults(String inputString, Pattern quoteResultStartPat, Pattern quoteResultEndPat){
        String string = new String(inputString);
        ArrayList<String> results = new ArrayList<String>();
        Matcher matcherStart = quoteResultStartPat.matcher(string);
        Matcher matcherEnd = quoteResultEndPat.matcher(string);
        while (matcherStart.find()) {
            int startIndex = matcherStart.start();
            int endIndex = startIndex;
            if (matcherEnd.find(startIndex))
                endIndex = matcherEnd.end();
            results.add(string.substring(startIndex, endIndex));
        }
        return results;
    }
}

