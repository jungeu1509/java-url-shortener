package kr.co.programmers.urlshortner;

import java.util.Optional;
import kr.co.programmers.urlshortner.domain.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortenerRepository extends JpaRepository<Url, Integer> {
    
    Optional<Url> findByBeforeUrl(String Url);

    Optional<Url> findByChangedUrl(String Url);
}
