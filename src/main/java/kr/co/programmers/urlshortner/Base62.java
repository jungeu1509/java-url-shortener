package kr.co.programmers.urlshortner;

import org.springframework.stereotype.Component;

@Component
public class Base62 {
    static final char[] codec
        = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        .toCharArray();

    public String toBase62(Integer index) {
        String ret = null;
        Integer remainder = index;

        do {
            ret += (codec[(int) (remainder % 62L)]);
            remainder /= 62;
        } while (remainder > 0);
        return ret;
    }

}
