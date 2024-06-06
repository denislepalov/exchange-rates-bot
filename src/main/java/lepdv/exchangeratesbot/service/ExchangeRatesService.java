package lepdv.exchangeratesbot.service;

import lepdv.exchangeratesbot.client.CbrClient;
import lepdv.exchangeratesbot.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeRatesService {

    private final CbrClient cbrClient;




    public String getGBPExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("GBP").getDouble("Value"));
    }

    public String getUSDExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("USD").getDouble("Value"));
    }

    public String getEURExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("EUR").getDouble("Value"));
    }

    public String getCNYExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("CNY").getDouble("Value"));
    }

    public String getCHFExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("CHF").getDouble("Value"));
    }

    public String getJPYExchangeRate() throws ServiceException {
        return String.valueOf(getRates().getJSONObject("JPY").getDouble("Value"));
    }


    private JSONObject getRates() throws ServiceException {
        String currencyRatesJson = cbrClient.getCurrencyRatesJson();
        try {
            JSONObject rates = new JSONObject(currencyRatesJson);
            return rates.getJSONObject("Valute");
        } catch (JSONException e) {
            throw new ServiceException("Failed to convert from json to object", e);
        }
    }

}
