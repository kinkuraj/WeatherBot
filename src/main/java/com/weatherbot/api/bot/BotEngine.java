package com.weatherbot.api.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * @author Kinkuraj Sahoo
 * @version 1.0
 *          <p>
 *          BotEngine starts the bot and register the bot on telegram server.
 *          </p>
 *
 */
public class BotEngine {


	public static void main(String [] args) {
		BotLogger.info("BotEngine", "Starting the bot engine.");
		ApiContextInitializer.init();

		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			WeatherBot myBot = new WeatherBot();
			botsApi.registerBot(myBot);
		} catch (TelegramApiException exception) {
			BotLogger.error("BotEngine : There is an exception occurred during Bot initialization.",exception);
		}
	}

}
