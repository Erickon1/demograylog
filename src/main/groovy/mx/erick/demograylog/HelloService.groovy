package mx.erick.demograylog;

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class HelloService {
    
    String grettings(){
        return "Hello";
    }
}
