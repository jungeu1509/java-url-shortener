package kr.co.programmers.urlshortner.domain;

import org.springframework.stereotype.Component;

@Component
public class UrlConverter {
    public UrlDto convertUrlDto(Url url) {
        return new UrlDto(url.getId(), url.getBeforeUrl(), url.getChangedUrl());
    }


}
