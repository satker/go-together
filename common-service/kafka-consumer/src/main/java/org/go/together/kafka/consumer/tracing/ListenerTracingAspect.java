package org.go.together.kafka.consumer.tracing;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import brave.propagation.TraceIdContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import static org.go.together.enums.ExtraKafkaHeaders.SPAN_ID;
import static org.go.together.enums.ExtraKafkaHeaders.TRACE_ID;

@Aspect
@Component
public class ListenerTracingAspect {
    private Tracer tracer;

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Pointcut("execution(public * org.go.together.kafka.consumers.KafkaConsumer.*(..))")
    private void anyKafkaConsumerMethod() {
        // NOSONAR
    }

    @Around("anyKafkaConsumerMethod()")
    public Object wrapProducerFactory(ProceedingJoinPoint pjp) throws Throwable {
        ConsumerRecord<UUID, ?> message = (ConsumerRecord<UUID, ?>) Arrays.stream(pjp.getArgs()).iterator().next();
        Span span = getSpan(message);
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return pjp.proceed();
        } catch (RuntimeException | Error e) {
            span.error(e);
            throw e;
        } finally { // note the scope is independent of the span
            span.finish();
        }
    }

    private Span getSpan(ConsumerRecord<UUID, ?> message) {
        String traceId = new String(message.headers().headers(TRACE_ID.getDescription()).iterator().next().value(), StandardCharsets.UTF_8);
        String spanId = new String(message.headers().headers(SPAN_ID.getDescription()).iterator().next().value(), StandardCharsets.UTF_8);
        return tracer.nextSpan(TraceContextOrSamplingFlags.newBuilder(TraceIdContext.newBuilder()
                .traceId(Long.parseLong(traceId))
                .build()).build()).name(spanId).start();
    }
}
