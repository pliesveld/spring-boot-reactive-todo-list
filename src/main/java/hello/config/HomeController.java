package hello.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/")
    public ResponseEntity index() {
        return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/swagger-ui.html").build();
    }
}
