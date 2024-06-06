package lepdv.exchangeratesbot.config;
import lepdv.exchangeratesbot.bot.ExchangeRatesBot;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ExchangeRatesBotConfig {

    @Bean
    TelegramBotsApi telegramBotsApi(ExchangeRatesBot exchangeRatesBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(exchangeRatesBot);
        return telegramBotsApi;
    }

    @Bean
    OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }


}
