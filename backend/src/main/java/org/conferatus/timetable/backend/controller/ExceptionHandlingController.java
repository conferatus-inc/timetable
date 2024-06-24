package org.conferatus.timetable.backend.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.conferatus.timetable.backend.exception.ServerException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(ServerException.class)
    private void handle(HttpServletResponse response, ServerException serverException) {
        response.setStatus(serverException.getStatus().value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(
                    serverException.getAnswer() + " " +
                            serverException.getMessage() + " " +
                            serverException.getCause() + " " +
                            Arrays.toString(serverException.getStackTrace())
            ));
        } catch (IOException e) {
//            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @ExceptionHandler(NullPointerException.class)
    private void handle(HttpServletResponse response, NullPointerException nullPointerException) {
        response.setStatus(500);
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(
                    nullPointerException.getMessage() + " " +
                            nullPointerException.getCause() + " " +
                            Arrays.toString(nullPointerException.getStackTrace())
            ));
        } catch (IOException e) {
//            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}