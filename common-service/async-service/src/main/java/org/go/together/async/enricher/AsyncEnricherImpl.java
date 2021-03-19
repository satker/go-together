package org.go.together.async.enricher;

import brave.Tracer;
import org.go.together.async.common.CommonAsyncExecutorImpl;
import org.go.together.base.async.AsyncEnricher;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.stereotype.Component;

@Component
public class AsyncEnricherImpl extends CommonAsyncExecutorImpl implements AsyncEnricher {
    private final TraceableExecutorService traceableExecutorService;
    private final Tracer tracer;

    public AsyncEnricherImpl(TraceableExecutorService traceableExecutorService,
                             Tracer tracer) {
        this.traceableExecutorService = traceableExecutorService;
        this.tracer = tracer;
    }


    @Override
    protected Tracer getTracer() {
        return tracer;
    }

    @Override
    protected TraceableExecutorService getTraceableExecutorService() {
        return traceableExecutorService;
    }
}
