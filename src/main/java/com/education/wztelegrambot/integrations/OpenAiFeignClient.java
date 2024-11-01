package com.education.wztelegrambot.integrations;

import com.education.wztelegrambot.entities.openai.OpenAiChatCompletionRequest;
import com.education.wztelegrambot.entities.openai.OpenAiChatCompletionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        value = "open-ai-feign-client",
        url = "${integrations.openai-api.url}"
)
public interface OpenAiFeignClient {
    @PostMapping("/chat/completions")
    OpenAiChatCompletionResponse generate(@RequestHeader("Authorization") String key,
                                          @RequestBody OpenAiChatCompletionRequest request);
}
