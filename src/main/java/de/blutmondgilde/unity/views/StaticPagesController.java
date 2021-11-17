package de.blutmondgilde.unity.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticPagesController {

    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "session-expired";
    }
}
