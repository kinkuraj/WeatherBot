package com.weatherbot.api.bot;

import org.telegram.telegrambots.logging.BotLogger;

import com.google.gson.Gson;
import com.weatherbot.api.pojo.Weather;


public class WeatherService {

	public static final String DEFAULT_REPLY="Whoops this is embarrassing, Enter city name or pin code. e.g. Chennai or 600119";
	private static final String URL ="http://api.openweathermap.org/data/2.5/weather?q=##,IN&&units=metric&appid=<API-Key>";
	private static final String URL_ZIP ="http://api.openweathermap.org/data/2.5/weather?zip=##,IN&&units=metric&appid=<API-Key>";
	public static String getTemperature(String zipCode) {
		BotLogger.info("WeatherService", "Weather details requested for pin code: " + zipCode);
		String weatherData = Consumer.requestContent(Consumer.getUrl(URL_ZIP,zipCode));
		BotLogger.info("WeatherService", "Json received: " + weatherData);
		Weather weather = new Gson().fromJson(weatherData, Weather.class);
		if(weather == null || 404 == weather.getCod()){
			return "Pin code "+zipCode+" not found";
		}
		return createAText(weather).toString();
	}

	private static StringBuilder createAText(Weather weather) {
		StringBuilder textMsg = new StringBuilder(weather.getName());
		textMsg.append(":\n Temp: "+ weather.getMain().getTemp().intValue() +"°C");
		textMsg.append("\n Humidity: "+ weather.getMain().getHumidity() +"%");
		textMsg.append("\n Wind: "+ weather.getWind().getSpeed() +"m/s");
		return textMsg;
	}

	public static String getTemperatureByCityName(String city) {
		BotLogger.info("WeatherService", "Weather details requested for city: " + city);
		String weatherData = Consumer.requestContent(Consumer.getUrl(URL,city));
		BotLogger.info("WeatherService", "Json received: " + weatherData);
		Weather weather = new Gson().fromJson(weatherData, Weather.class);
		if(weather == null || 404 == weather.getCod()){
			return "City "+city+" not found";
		}
		return createAText(weather).toString();
	}

}
