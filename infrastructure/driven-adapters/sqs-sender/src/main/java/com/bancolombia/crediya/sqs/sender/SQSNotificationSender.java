package com.bancolombia.crediya.sqs.sender;

import com.bancolombia.crediya.model.notificacionrevisiondeestado.NotificacionRevisionDeEstado;
import com.bancolombia.crediya.sqs.sender.config.SQSSenderProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import com.bancolombia.crediya.model.notificacionrevisiondeestado.gateways.NotificacionRevisionDeEstadoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Log4j2
@RequiredArgsConstructor
public class SQSNotificationSender implements NotificacionRevisionDeEstadoRepository {
    private final SQSSenderProperties properties;
    private final SqsAsyncClient client;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public Mono<String> enviarNotificacion(NotificacionRevisionDeEstado notificacion) {
        log.info("==> INICIO: Proceso de envio de notificacion SQS. Datos de entrada: {}", notificacion);
        return Mono.fromCallable(() -> convertToJson(notificacion))
                .map(this::buildRequest)
                .flatMap(request -> {
                    log.info("Enviando mensaje a SQS. Peticion: {}", request);
                    return Mono.fromFuture(client.sendMessage(request));
                })
                .doOnNext(response -> log.info("Respuesta recibida de SQS. Response: {}", response))
                .map(response -> {
                    log.info("<== FIN: Notificacion enviada con exito. Message ID: {}", response.messageId());
                    return response.messageId();
                })
                .doOnError(error -> log.error("<== FIN CON ERROR: Falla en el envio a SQS. Causa: ", error));
    }

    private String convertToJson(NotificacionRevisionDeEstado notificacion) throws JsonProcessingException {
        log.debug("Convirtiendo notificacion a JSON...");
        String jsonMessage = objectMapper.writeValueAsString(notificacion);
        log.debug("Notificacion convertida a JSON: {}", jsonMessage);
        return jsonMessage;
    }

    private SendMessageRequest buildRequest(String message) {
        log.debug("Construyendo peticion SQS con el mensaje JSON...");
        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(properties.queueUrl())
                .messageBody(message)
                .build();
        log.debug("Peticion SQS construida: {}", request);
        return request;
    }

}
