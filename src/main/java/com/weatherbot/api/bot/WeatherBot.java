package com.weatherbot.api.bot;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class WeatherBot extends TelegramLongPollingBot {

	public WeatherBot() {
		super();
	}

	public WeatherBot(DefaultBotOptions botOptions) {
		super(botOptions);
	}

	@Override
	public String getBotUsername() {
		return System.getenv("BOT_USERNAME");
	}

	@Override
	public void onUpdateReceived(Update update) {
		BotLogger.info("WeatherBot","A command received with chat id: " + update.getMessage().getChatId());
		if (update.hasMessage() && update.getMessage().hasText()) {
			String text = update.getMessage().getText();
			SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
			if ("/start".equalsIgnoreCase(text)) {
				message.setText("Hello there! can you enter city name or pin code.");
			} else if (isValidPinCode(update.getMessage().getText())) {
				message.setText(WeatherService.getTemperature(update.getMessage().getText()));
			}else if(isValidCity(update.getMessage().getText())) {
				message.setText(WeatherService.getTemperatureByCityName(update.getMessage().getText()));
			}else {
				message.setText(WeatherService.DEFAULT_REPLY);
			}
			
			try {
				execute(message);
			} catch (TelegramApiException teleApiExce) {
				BotLogger.error("WeatherBot : There is an exception while responding to the chat id: "
						+ update.getMessage().getChatId() , teleApiExce);
			}
		}
		BotLogger.info("WeatherBot","A command processed with chat id: " + update.getMessage().getChatId());
		

	}

	private boolean isValidCity(String text) {
		return StringUtils.isNoneEmpty(text) && StringUtils.isAlpha(text);
	}

	private boolean isValidPinCode(String text) {
		return StringUtils.isNoneEmpty(text) && StringUtils.isNumeric(text) && StringUtils.length(text) == 6;
	}

	@Override
	public String getBotToken() {
		return System.getenv("BOT_TOKEN");
	}

}