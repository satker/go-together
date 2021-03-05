package org.go.together.kafka.consumer.tracing;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContextOrSamplingFlags;
import brave.propagation.TraceIdContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

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
        Span span = getSpan(pjp);
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return pjp.proceed();
        } catch (RuntimeException | Error e) {
            span.error(e);
            throw e;
        } finally { // note the scope is independent of the span
            span.finish();
        }
    }

    private ConsumerRecord<Long, ?> getMessage(ProceedingJoinPoint pjp) {
        return (ConsumerRecord<Long, ?>) Arrays.stream(pjp.getArgs()).iterator().next();
    }

    private KafkaListener getKafkaListenerAnnotation(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(KafkaListener.class);
    }

    private Span getSpan(ProceedingJoinPoint pjp) {
        ConsumerRecord<Long, ?> message = getMessage(pjp);
        KafkaListener kafkaListenerAnnotation = getKafkaListenerAnnotation(pjp);
        return tracer.nextSpan(TraceContextOrSamplingFlags.newBuilder(TraceIdContext.newBuilder()
                .traceId(message.key())
                .build()).build())
                .kind(Span.Kind.CONSUMER)
                .tag("listener.class", pjp.getTarget().getClass().getSimpleName())
                .tag("listener.method", pjp.getSignature().getName())
                .tag("listener." + KafkaHeaders.REPLY_TOPIC, getHeaderValue(message, KafkaHeaders.REPLY_TOPIC))
                .tag("listener." + KafkaHeaders.CORRELATION_ID, getHeaderValue(message, KafkaHeaders.CORRELATION_ID))
                .tag("topics", Arrays.toString(kafkaListenerAnnotation.topics()))
                .name(kafkaListenerAnnotation.topics()[0])
                .start();
    }

    private String getHeaderValue(ConsumerRecord<Long, ?> message, String header) {
        Iterator<Header> headerIterator = message.headers().headers(header).iterator();
        if (!headerIterator.hasNext()) {
            return StringUtils.EMPTY;
        }
        Header next = headerIterator.next();
        return new String(next.value(), StandardCharsets.UTF_8);
    }
}
