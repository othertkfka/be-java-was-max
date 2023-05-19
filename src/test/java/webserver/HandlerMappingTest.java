package webserver;

import static org.assertj.core.api.Assertions.*;
import http.HttpRequest;
import http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class HandlerMappingTest {

    @Test
    @DisplayName("Http 요청이 주어졌을 때 요청 메시지를 파싱하고 url에 해당하는 view 페이지를 찾아서 ModelAndView에 담아서 반환한다.")
    public void handleRequestTest() throws IOException {
        // given
        String httpMessage1 = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n\r\n";
        String httpMessage2 = "GET /user/login HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: max-age=0\r\n\r\n";
        BufferedReader br1 = new BufferedReader(new StringReader(httpMessage1));
        BufferedReader br2 = new BufferedReader(new StringReader(httpMessage2));

        HttpRequest httpRequest1 = new HttpRequest(br1);
        HttpResponse httpResponse1 = new HttpResponse();
        HttpRequest httpRequest2 = new HttpRequest(br2);
        HttpResponse httpResponse2 = new HttpResponse();

        // when
        ModelAndView modelAndView1 = HandlerMapping.handleRequest(httpRequest1, httpResponse1);
        ModelAndView modelAndView2 = HandlerMapping.handleRequest(httpRequest2, httpResponse2);

        // then
        assertThat(new ModelAndView("/index.html").getView()).isEqualTo(modelAndView1.getView());
        assertThat(new ModelAndView("/user/login_failed.html").getView()).isEqualTo(modelAndView2.getView());
    }
}
