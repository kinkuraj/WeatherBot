package com.weatherbot.api.bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

 /**
 * Consumer class consumes the Rest service which we passed and process to
 * output
 */
public class Consumer {

  private Consumer() {

  }

  /**
   * invokes the rest service and gets the output of Json objects
   * 
   * @param url
   * @return String
   */
  public static String requestContent(String url) {
    String result = null;
    HttpGet httpget = new HttpGet(url);
    httpget.setHeader(HttpHeaders.ACCEPT, "application/json");
    HttpResponse response = null;
    InputStream instream = null;
    //setProxy(httpget);
    try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
     
      // Execute HTTP request
      response = httpClient.execute(httpget);
      // Get hold of the response entity
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        instream = entity.getContent();
        result = convertStreamToString(instream);
      }
    } catch (IOException ioException) {

     System.out.println("Exception occurred in Consumer.requestContent"+ ioException.getMessage());

    }

    return result;
  }

/**
 * @param httpget
 */
protected static void setProxy(HttpGet httpget) {
	HttpHost httpHost = new HttpHost("192.168.147.7", 8080);
	RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).build();
    httpget.setConfig(requestConfig);
}

  /**
   * Reads all the Data from input stream and converts to String
   * 
   * @param InputStream
   * @return String
   * 
   */
  public static String convertStreamToString(InputStream is) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;

    try {
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException ioException) {
    	System.out.println("IOException occurred in Consumer.convertStreamToString"+ ioException.getMessage());
    } finally {
      try {
        is.close();
      } catch (IOException ioException) {
    	  System.out.println("IOException occurred in Consumer.convertStreamToString"+ ioException.getMessage());
      }
    }

    return sb.toString();
  }
  
  /**
   * @param url
   * @param componentKey
   * @return new URL
   */
  public static String getUrl(String url, String componentKey) {
    String newUrl = url;
    String[][] replacements = { { "##", componentKey } };
    for (String[] replacement : replacements) {
      newUrl = newUrl.replace(replacement[0], replacement[1]);
    }
    return newUrl;
  }
  
  /**
	 * @param url
	 * @param type
	 * @param componentKey
	 * @return new URL
	 */
	public static String getUrl(String url, String type, String componentKey) {
		String newUrl = url;
		String[][] replacements = { { "**", type }, { "##", componentKey } };
		for (String[] replacement : replacements) {
			newUrl = newUrl.replace(replacement[0], replacement[1]);
		}
		return newUrl;
	}
	
}
