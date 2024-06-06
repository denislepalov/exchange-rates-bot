package lepdv.exchangeratesbot.bot;

import lepdv.exchangeratesbot.exception.ServiceException;
import lepdv.exchangeratesbot.service.ExchangeRatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;

@Component
@Slf4j
public class ExchangeRatesBot extends TelegramLongPollingBot {

    private final ExchangeRatesService service;

    public ExchangeRatesBot(@Value("${bot.token}") String botToken,
                            ExchangeRatesService service) {
        super(botToken);
        this.service = service;
    }
    private final static String START = "/start";
    private final static String GBP = "/gbp";
    private final static String USD = "/usd";
    private final static String EUR = "/eur";
    private final static String CNY = "/cny";
    private final static String CHF = "/chf";
    private final static String JPY = "/jpy";
    private final static String HELP = "/help";



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            String username = update.getMessage().getChat().getFirstName();
            switch (text) {
                case START -> startCommand(chatId, username);
                case GBP -> coinCommand(chatId, "gbp");
                case USD -> coinCommand(chatId, "usd");
                case EUR -> coinCommand(chatId, "eur");
                case CNY -> coinCommand(chatId, "cny");
                case CHF -> coinCommand(chatId, "chf");
                case JPY -> coinCommand(chatId, "jpy");
                case HELP -> helpCommand(chatId);
                default -> unknownCommand(chatId);
            }
        }
    }



    @Override
    public String getBotUsername() {
        return "exchange_rates174_bot";
    }



    private void sendMessage(String chatId, String text) {

        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send message");
        }
    }



    private void startCommand(String chatId, String username) {
        String text = """
                Добро пожаловать, %s!
                
                Здесь вы можете узнать актуальные курсы валют.
                
                Для этого используйте команды:
                /gbp - курс фунта стерлингов
                /usd - курс доллара
                /eur - курс евро
                /cny - курс китайского юаня
                /chf - курс швейцарского франка
                /jpy - курс японской иены
                
                Дополнительные команды:
                /help - получение справки
                """;
        String formattedText = String.format(text, username);
        sendMessage(chatId, formattedText);
    }



    private void coinCommand(String chatId, String coin) {
        String formattedText;
        String coinRate;
        try {
            switch (coin) {
                case ("gbp") -> coinRate = service.getGBPExchangeRate();
                case ("usd") -> coinRate = service.getUSDExchangeRate();
                case ("eur") -> coinRate = service.getEURExchangeRate();
                case ("cny") -> coinRate = service.getCNYExchangeRate();
                case ("chf") -> coinRate = service.getCHFExchangeRate();
                case ("jpy") -> coinRate = service.getJPYExchangeRate();
                default -> coinRate = "неизвестно";
            }
            String text = "Курс выбранной валюты (%s) на %s составляет %s рублей.";
            formattedText = String.format(text, coin, LocalDate.now(), coinRate);
        } catch (ServiceException e) {
            String errorText = String.format("Failed to get exchange rate (%s). Cause: %s", coin, e.getMessage());
            log.error(errorText);
            formattedText = String.format("Не удалось получить текущий курс выбранной валюты (%s). " +
                    "Попробуйте позже.", coin);
        }
        sendMessage(chatId, formattedText);
    }



    private void helpCommand(String chatId) {
        String text = """
                Справочная информация.
                
                Для получения текущих курсов валют используйте команды:
                /gbp - курс фунта стерлингов
                /usd - курс доллара
                /eur - курс евро
                /cny - курс китайского юаня
                /chf - курс швейцарского франка
                /jpy - курс японской иены
                """;
        sendMessage(chatId, text);
    }



    private void unknownCommand(String chatId) {
        sendMessage(chatId, "Не удалось распознать команду.");
    }


}
