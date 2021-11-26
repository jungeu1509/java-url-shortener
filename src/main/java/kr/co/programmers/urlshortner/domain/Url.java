package kr.co.programmers.urlshortner.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Url {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String beforeUrl;

    @Column
    String changedUrl;

    public Url(String beforeUrl) {
        this.beforeUrl = beforeUrl;
    }

    public void setBeforeUrl(String beforeUrl) {
        this.beforeUrl = beforeUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getBeforeUrl() {
        return beforeUrl;
    }

    public String getChangedUrl() {
        return changedUrl;
    }

    public void setChangedUrl(String changedUrl) {
        this.changedUrl = changedUrl;
    }
}

