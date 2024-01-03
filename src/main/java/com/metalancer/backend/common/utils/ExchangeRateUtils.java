package com.metalancer.backend.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExchangeRateUtils {

    @Value("${exchange.api.key}")
    private String exchangeApiKey;

    private final BigDecimal defaultExchangeRate = BigDecimal.valueOf(1300);

    public BigDecimal getExchangeRate() {
        String response = fetchExchangeRateData();
        log.info("response: {}", response);
        BigDecimal exchangeRate = parseExchangeRate(response);

        if (exchangeRate == null) {
            exchangeRate = defaultExchangeRate;
        }

        return exchangeRate.setScale(2, RoundingMode.HALF_UP);
    }

    // api request
    private String fetchExchangeRateData() {
        String searchDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dataType = "AP01";

        try {
            URL url = new URL(
                "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey="
                    + exchangeApiKey
                    + "&searchdate=" + searchDate + "&data=" + dataType);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            log.info("exchange api status: {}", status);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                status > 299 ? connection.getErrorStream() : connection.getInputStream()))) {
                log.info("reader : {}", reader);
                StringBuilder responseContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                return responseContent.toString();
            } finally {
                connection.disconnect();
            }
        } catch (MalformedURLException e) {
            throw new ExchangeRateException("Invalid URL", e);
        } catch (IOException e) {
            throw new ExchangeRateException("Error fetching exchange rate data", e);
        }
    }

    // 얻어온 데이터 parse
    private BigDecimal parseExchangeRate(String response) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray exchangeRateInfoList = (JSONArray) parser.parse(response);

            for (Object o : exchangeRateInfoList) {
                JSONObject exchangeRateInfo = (JSONObject) o;
                if ("USD".equals(exchangeRateInfo.get("cur_unit"))) {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    BigDecimal result = BigDecimal.valueOf(
                        format.parse(exchangeRateInfo.get("deal_bas_r").toString()).doubleValue());
                    log.info("exchangeRate: {}", result);
                    return result;
                }
            }
        } catch (ParseException e) {
            throw new ExchangeRateException("Error parsing exchange rate data", e);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Define a custom exception for better error handling
    private static class ExchangeRateException extends RuntimeException {

        public ExchangeRateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
