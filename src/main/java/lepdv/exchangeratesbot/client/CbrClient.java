package lepdv.exchangeratesbot.client;
import lepdv.exchangeratesbot.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CbrClient {

    private final OkHttpClient okHttpClient;

    @Value("${cbr.currency.rates.json.url}")
    private String url;
    private LocalDate localDate;
    private String currencyRatesJson;




    public String getCurrencyRatesJson() throws ServiceException {
        if (localDate == null || !localDate.equals(LocalDate.now())) {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                ResponseBody body = response.body();
                if (body != null) {
                    String ratesJson = body.string();
                    localDate = LocalDate.now();
                    currencyRatesJson = ratesJson;
                    return ratesJson;
                } else {
                    throw new ServiceException("Body of response (currency rates json) is null");
                }
            } catch (IOException e) {
                throw new ServiceException("Error getting exchange rates", e);
            }
        } else {
            return currencyRatesJson;
        }
    }



}
