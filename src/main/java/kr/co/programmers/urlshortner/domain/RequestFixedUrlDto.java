package kr.co.programmers.urlshortner.domain;

import lombok.Getter;

public class RequestFixedUrlDto {
    String shortURL;
    String fixURL;

    public String getShortURL() {
        return shortURL;
    }

    public String getFixURL() {
        return fixURL;
    }
}
