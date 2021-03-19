package org.go.together.async.mapper;

import brave.Tracer;
import org.go.together.async.common.CommonAsyncExecutorImpl;
import org.go.together.base.async.AsyncMapper;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.stereotype.Component;

@Component
public class AsyncMapperImpl extends CommonAsyncExecutorImpl implements AsyncMapper {
    private final TraceableExecutorService traceableExecutorService;
    private final Tracer tracer;

    public AsyncMapperImpl(TraceableExecutorService traceableExecutorService,
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
