package kr.co.programmers.urlshortner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import javassist.NotFoundException;
import kr.co.programmers.urlshortner.domain.Url;
import kr.co.programmers.urlshortner.domain.UrlConverter;
import kr.co.programmers.urlshortner.domain.UrlDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortenerService {

    private final ShortenerRepository shortenerRepository;
    private final UrlConverter urlConverter;
    private final Base62 base62;

    public ShortenerService(ShortenerRepository shortenerRepository,
        UrlConverter urlConverter, Base62 base62) {
        this.shortenerRepository = shortenerRepository;
        this.urlConverter = urlConverter;
        this.base62 = base62;
    }

    public UrlDto checkAndRun(String inputUrl) throws MalformedURLException, NotFoundException {
        URL url = new URL(inputUrl);
        UrlDto ret;
        if(Objects.equals(url.getHost(), "local:8080")) {
            ret = findBeforeUrl(inputUrl);
        } else {
            ret = makeUrl(inputUrl);
        }
        return ret;
    }

    // 생성된 URL 요청시 원본 URL로 리다이렉트 시키는 기능
    @Transactional
    public UrlDto findBeforeUrl(String changedURL) throws NotFoundException {
        Url url = shortenerRepository.findByChangedUrl(changedURL)
            .orElseThrow(() -> new NotFoundException("지정된 URL이 아닙니다. URL 리다이렉트에 실패했습니다."));
        return urlConverter.convertUrlDto(url);
    }

    // 단축 URL을 생성하는 기능
    @Transactional
    public UrlDto makeUrl(String beforeURL) {
        Optional<Url> byBeforeUrl = shortenerRepository.findByBeforeUrl(beforeURL);
        if(byBeforeUrl.isPresent()){
            return urlConverter.convertUrlDto(byBeforeUrl.get());
        }

        shortenerRepository.save(new Url(beforeURL));
        Url url = shortenerRepository.findByBeforeUrl(beforeURL).get();
        Integer urlId = url.getId();
        String shortUrl = base62.toBase62(urlId);
        Optional<Url> byChangedUrl = shortenerRepository.findByChangedUrl(shortUrl);
        if(byChangedUrl.isPresent()) {
            try {
                fixUrl(shortUrl, beforeURL);
                url = byChangedUrl.get();
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        url.setChangedUrl(shortUrl);

        return urlConverter.convertUrlDto(url);
    }

    // 이미 단축되어있는 URL의 원본 URL을 수정하는 기능
    @Transactional
    public UrlDto fixUrl(String shortURL, String fixURL) throws NotFoundException {
        Url url = shortenerRepository.findByChangedUrl(shortURL)
            .orElseThrow(() -> new NotFoundException("지정된 URL이 아닙니다. changedURL 수정에 실패했습니다."));
        url.setBeforeUrl(fixURL);
        return urlConverter.convertUrlDto(url);
    }

    // 저장되어있는 URL 수
    public Long findSize() {
        return shortenerRepository.count();
    }

}
