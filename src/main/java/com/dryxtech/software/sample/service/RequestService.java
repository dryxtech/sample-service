package com.dryxtech.software.sample.service;

import com.dryxtech.software.sample.dao.DataRequestDAO;
import com.dryxtech.software.sample.model.DataRequest;
import com.dryxtech.software.sample.model.DataRequestEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
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
    private final ExecutorService dataRequestRecorder;
    private final AtomicBoolean recordDataRequest = new AtomicBoolean();

    public RequestService(DataRequestDAO dataRequestDAO) {

        this.dataRequestDAO = dataRequestDAO;
        this.dataRequestQueue = new ArrayBlockingQueue<>(1000);
        this.dataRequestRecorder = Executors.newSingleThreadExecutor();
        recordDataRequest.set(true);

        this.dataRequestRecorder.execute(() -> {
            try {
                while (recordDataRequest.get() || !this.dataRequestQueue.isEmpty()) {
                    DataRequest<?> dataRequest = this.dataRequestQueue.take();
                    recordDataRequest(dataRequest);
                }
            } catch (InterruptedException ex) {
                log.warn("data request recorder interrupted", ex);
                Thread.currentThread().interrupt();
            }
        });
    }

    public void recordDataRequest(DataRequest<?> dataRequest) {

        try {
            dataRequestDAO.recordDataRequest(dataRequest);
        } catch (DataAccessException ex) {
            log.error("failed to record data request: {}", dataRequest, ex);
        }
    }

    @PreDestroy
    public void destroy() {

        recordDataRequest.set(false);
        try {
            if (!dataRequestRecorder.awaitTermination(3, TimeUnit.SECONDS)) {
                dataRequestRecorder.shutdownNow();
            }
        } catch (InterruptedException ex) {
            dataRequestRecorder.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @EventListener
    public void processDataRequestEvent(DataRequestEvent event) {

        DataRequest<?> dataRequest = (DataRequest<?>) event.getSource();
        if (recordDataRequest.get()) {
            if (!dataRequestQueue.offer(dataRequest)) {
                log.warn("too many data request in queue to process. data request {}", dataRequest);
            }
        } else {
            log.info("data request event received {}", dataRequest);
        }
    }
}
