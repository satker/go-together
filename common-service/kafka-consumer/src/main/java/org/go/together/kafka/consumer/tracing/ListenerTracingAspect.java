package org.go.together.kafka.consumer.tracing;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
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
import java.util.Random;

import static org.go.together.kafka.producers.ReplyKafkaProducer.PARENT_SPAN_ID;
import static org.go.together.utils.ByteUtils.bytesToLong;

@Aspect
@Component
public class ListenerTracingAspect {
    private Tracer tracer;
    private static final Random random = new Random();

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Pointcut("execution(public * org.go.together.kafka.consumers.KafkaConsumer.*(..))")
    private void anyKafkaConsumerMethod() {
        // NOSONAR
    }

    @Pointcut("execution(public * org.go.together.base.CrudService.*(..)) || " +
            "execution(public * org.go.together.base.FindService.*(..))")
    private void anyCrudMethod() {
        // NOSONAR
    }

    @Around("anyCrudMethod()")
    public Object wrapCrudMethod(ProceedingJoinPoint pjp) throws Throwable {
        Span span = tracer.newChild(tracer.nextSpan().context()).name(pjp.getSignature().getName()).start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return pjp.proceed();
        } catch (RuntimeException | Error e) {
            span.error(e);
            throw e;
        } finally { // note the scope is independent of the span
            span.finish();
        }
    }

    @Around("anyKafkaConsumerMethod()")
    public Object wrapProducerFactory(ProceedingJoinPoint pjp) throws Throwable {
        Span span = getKafkaSpan(pjp);
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

    private Span getKafkaSpan(ProceedingJoinPoint pjp) {
        ConsumerRecord<Long, ?> message = getMessage(pjp);
        KafkaListener kafkaListenerAnnotation = getKafkaListenerAnnotation(pjp);
        long parentId = bytesToLong(getHeaderValue(message, PARENT_SPAN_ID));
        TraceContext context = TraceContext.newBuilder().parentId(parentId).traceId(message.key()).spanId(random.nextLong()).build();
        return tracer.newChild(context)
                .kind(Span.Kind.CONSUMER)
                .tag("listener.class", pjp.getTarget().getClass().getSimpleName())
                .tag("listener.method", pjp.getSignature().getName())
                .tag("listener." + KafkaHeaders.REPLY_TOPIC, wrapBytesToString(getHeaderValue(message, KafkaHeaders.REPLY_TOPIC)))
                .tag("listener." + KafkaHeaders.CORRELATION_ID, wrapBytesToString(getHeaderValue(message, KafkaHeaders.CORRELATION_ID)))
                .tag("topics", Arrays.toString(kafkaListenerAnnotation.topics()))
                .name(kafkaListenerAnnotation.topics()[0])
                .start();
    }

    private byte[] getHeaderValue(ConsumerRecord<Long, ?> message, String header) {
        Iterator<Header> headerIterator = message.headers().headers(header).iterator();
        if (!headerIterator.hasNext()) {
            return new byte[]{};
        }
        return headerIterator.next().value();
    }

    private String wrapBytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
