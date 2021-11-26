package kr.co.programmers.urlshortner.domain;

import lombok.Getter;

public class UrlDto {
    Integer id;
    String BeforeUrl;
    String ChangedUrl;

    public UrlDto(Integer id, String beforeUrl, String changedUrl) {
        this.id = id;
        BeforeUrl = beforeUrl;
        ChangedUrl = changedUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getBeforeUrl() {
        return BeforeUrl;
    }

    public String getChangedUrl() {
        return ChangedUrl;
    }
}
