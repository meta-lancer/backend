package com.metalancer.backend.external.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SlackService {

    @Value("${slack.private.token}")
    private String privateToken;
    @Value("${slack.channel.error}")
    private String errorChannel;
    @Value("${slack.channel.info}")
    private String infoChannel;

    public void postSlackMessageToInfoChannel(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(privateToken);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(infoChannel)
                .text(message)
                .build();

            ChatPostMessageResponse slackMsgResponse = methods.chatPostMessage(request);
            log.info("보냄" + slackMsgResponse.getMessage().getText());
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }

    public void postSlackMessageToExceptionChannel(String message) {
        try {
            MethodsClient methods = Slack.getInstance().methods(privateToken);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(errorChannel)
                .text(message)
                .build();

            ChatPostMessageResponse slackMsgResponse = methods.chatPostMessage(request);
            log.info("보냄" + slackMsgResponse.getMessage().getText());
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
