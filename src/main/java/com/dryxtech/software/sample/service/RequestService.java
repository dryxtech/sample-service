package com.dryxtech.software.sample.service;

import com.dryxtech.software.sample.dao.DataRequestDAO;
import com.dryxtech.software.sample.model.DataRequest;
import com.dryxtech.software.sample.model.DataRequestEvent;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class RequestService {

    private final DataRequestDAO dataRequestDAO;
    private final ArrayBlockingQueue<DataRequest<?>> dataRequestQueue;
    private final ExecutorService dataRequestRecorder = Executors.newSingleThreadExecutor();
    private final AtomicBoolean recordDataRequest = new AtomicBoolean(false);

    public RequestService(DataRequestDAO dataRequestDAO, MeterRegistry meterRegistry,
                          @Value("${sample.request.record.queue.capacity}") int requestQueueCapacity) {

        this.dataRequestDAO = dataRequestDAO;
        this.dataRequestQueue = new ArrayBlockingQueue<>(requestQueueCapacity);

        Gauge.builder("recordDataRequest.queueSize", dataRequestQueue, Collection::size)
                .description("Queue size of completed data request waiting to be recorded")
                .register(meterRegistry);

        if (!startRequestQueueProcessor()) {
            throw new IllegalStateException("failed to start request recorder queue processor");
        }
    }

    @PreDestroy
    public void destroy() {
        if (!stopRequestQueueProcessor()) {
            log.warn("failed to stop request recorder queue processor");
        }
    }

    @EventListener
    public void processDataRequestEvent(DataRequestEvent event) {

        DataRequest<?> dataRequest = (DataRequest<?>) event.getSource();
        if (recordDataRequest.get() && dataRequestQueue.offer(dataRequest)) {
            log.debug("added to recorder queue data request {}", dataRequest);
        } else {
            log.info("received but not adding data request {} to recorder queue with size {}", dataRequest,
                    dataRequestQueue.size());
        }
    }

    public void recordDataRequest(@NonNull DataRequest<?> dataRequest) {
        try {
            dataRequestDAO.recordDataRequest(dataRequest);
        } catch (DataAccessException ex) {
            log.error("failed to record data request {}", dataRequest, ex);
        }
    }

    boolean startRequestQueueProcessor() {

        if (!recordDataRequest.get() || dataRequestRecorder.isShutdown()) {
            log.info("starting data request recorder queue processor");
            recordDataRequest.set(true);
            this.dataRequestRecorder.execute(() -> {
                try {
                    while (recordDataRequest.get() || !this.dataRequestQueue.isEmpty()) {
                        DataRequest<?> dataRequest = this.dataRequestQueue.take();
                        recordDataRequest(dataRequest);
                    }
                } catch (InterruptedException ex) {
                    if (dataRequestQueue.size() > 0) {
                        log.warn("data request recorder interrupted with queue size " + dataRequestQueue.size(), ex);
                    }
                    Thread.currentThread().interrupt();
                }
            });
        }

        return recordDataRequest.get() && !dataRequestRecorder.isShutdown();
    }

    boolean stopRequestQueueProcessor() {

        if (recordDataRequest.get() || !dataRequestRecorder.isShutdown()) {
            log.info("stopping data request recorder queue processor");
            recordDataRequest.set(false);
            try {
                if (dataRequestQueue.size() == 0 || !dataRequestRecorder.awaitTermination(2, TimeUnit.SECONDS)) {
                    dataRequestRecorder.shutdownNow();
                }
            } catch (InterruptedException ex) {
                dataRequestRecorder.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        return !recordDataRequest.get() && dataRequestRecorder.isShutdown();
    }
}
