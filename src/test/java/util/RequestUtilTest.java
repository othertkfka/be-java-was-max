package util;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RequestUtilTest {

    @Test
    @DisplayName("request line에서 url을 분리해서 반환한다.")
    public void separateUrlTest() {
        //given
        String requestLine = "GET /index.html HTTP/1.1";
        //when
        String url = RequestUtil.separateUrl(requestLine);
        //then
        assertThat(url).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("url에 해당하는 파일을 읽어서 반환한다.")
    public void findResourcesTest() throws IOException {
        //given
        String html = "/index.html";
        String css = "/css/bootstrap.min.css";
        //when
        byte[] resource = RequestUtil.findResources(html);
        byte[] resource2 = RequestUtil.findResources(css);
        //then
        assertThat(resource).isEqualTo(Files.readAllBytes(Paths.get("./src/main/resources/templates/index.html")));
        assertThat(resource2).isEqualTo(Files.readAllBytes(Paths.get("./src/main/resources/static/css/bootstrap.min.css")));
    }

    @Test
    @DisplayName("쿼리스트링의 파라미터를 각각 분리하여 Map으로 반환한다.")
    public void parseQueryStringTest() {
        //given
        String queryString = "userId=sjseop2&password=5678&name=sjs&email=sjs%40naver.com";
        //when
        Map<String, String> result = RequestUtil.parseQueryString(queryString);
        //then
        assertThat(result.get("userId")).isEqualTo("sjseop2");
        assertThat(result.get("password")).isEqualTo("5678");
        assertThat(result.get("name")).isEqualTo("sjs");
        assertThat(result.get("email")).isEqualTo("sjs%40naver.com");
    }
}
