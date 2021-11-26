package kr.co.programmers.urlshortner;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import javassist.NotFoundException;
import kr.co.programmers.urlshortner.domain.RequestFixedUrlDto;
import kr.co.programmers.urlshortner.domain.UrlDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortenerController {
    private final ShortenerService shortenerService;

    public ShortenerController(ShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @PostMapping("/api/v1/urls")
    public ResponseEntity<UrlDto> submitUrl(@RequestBody Optional<String> beforeUrl)
        throws MalformedURLException, NotFoundException, URISyntaxException {
        beforeUrl.orElseThrow(() -> new IllegalArgumentException("변환을 위해 URL을 입력해주세요"));
        UrlDto urlDto = shortenerService.checkAndRun(beforeUrl.get());

        if(!Objects.equals(urlDto.getChangedUrl(), beforeUrl.get()))
            return ResponseEntity.created(URI.create("/api/v1/urls")).body(urlDto);

        URI redirectUri = new URI(urlDto.getBeforeUrl());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);

    }

    @GetMapping("/api/v1/urls")
    public ResponseEntity<UrlDto> fix(@RequestBody RequestFixedUrlDto fixedDto)
        throws NotFoundException {
        UrlDto urlDto = shortenerService.fixUrl(fixedDto.getShortURL(), fixedDto.getFixURL());
        return ResponseEntity.ok(urlDto);
    }

    @PostMapping("/api/v1/numbers")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(shortenerService.findSize());
    }
}
