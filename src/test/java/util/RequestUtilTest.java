package util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RequestUtilTest {

    @Test
    @DisplayName("request line에서 url을 분리해서 반환한다.")
    public void separateUrlTest() {
        //given
        String requestLine = "GET /index.html HTTP/1.1";
        //when
        String url = RequestUtil.separateUrl(requestLine);
        //then
        Assertions.assertThat(url).isEqualTo("/index.html");
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
        Assertions.assertThat(resource).isEqualTo(Files.readAllBytes(Paths.get("./src/main/resources/templates/index.html")));
        Assertions.assertThat(resource2).isEqualTo(Files.readAllBytes(Paths.get("./src/main/resources/static/css/bootstrap.min.css")));
    }
}
