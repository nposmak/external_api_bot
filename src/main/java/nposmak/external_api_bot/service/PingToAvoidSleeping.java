package nposmak.external_api_bot.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


@Service
@Getter
@Setter
public class PingToAvoidSleeping{
    @Value("${ping.pingURL}")
    private String pingUrl;

    @Scheduled(fixedRateString = "${ping.period}")
    public void ping(){
        try{
        URL url = new URL(getPingUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.connect();
            System.out.println(url.getHost() +"\n"+ connection.getResponseCode());
        connection.disconnect();

    }catch (IOException e){
        e.printStackTrace();
        }

    }
}
