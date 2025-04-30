package ro.unibuc.hello.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.AuthenticationResponse;
import ro.unibuc.hello.dto.RegisterRequest;
import ro.unibuc.hello.exception.InvalidInputException;
import ro.unibuc.hello.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final MeterRegistry meterRegistry;

    public AuthenticationController(AuthenticationService authenticationService, MeterRegistry meterRegistry) {
        this.authenticationService = authenticationService;
        this.meterRegistry = meterRegistry;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest user) throws Exception {
        meterRegistry.counter("auth.register.attempts").increment();

        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            var jwt = authenticationService.register(user);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } finally {
            sample.stop(meterRegistry.timer("auth.register.duration"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest user, BindingResult result){
        meterRegistry.counter("auth.login.attempts").increment();

        if (result.hasErrors()) {
            meterRegistry.counter("auth.login.failed.validation").increment();

            String errorMessages = result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .reduce((message1, message2) -> message1 + ", " + message2)
                    .orElse("Invalid data");
            throw new InvalidInputException(errorMessages);
        }

        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            return new ResponseEntity<>(authenticationService.login(user), HttpStatus.OK);
        } finally {
            sample.stop(meterRegistry.timer("auth.login.duration"));
        }
    }

}
