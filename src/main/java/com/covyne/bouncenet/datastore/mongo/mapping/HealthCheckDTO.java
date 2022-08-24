package com.covyne.bouncenet.datastore.mongo.mapping;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class HealthCheckDTO {

    @Id
    private String id;

    private String serviceName;

    private String serviceVersion;

    private String lastStatusCheckTime;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLastStatusCheckTime() {
        return lastStatusCheckTime;
    }

    public void setLastStatusCheckTime(String lastStatusCheckTime) {
        this.lastStatusCheckTime = lastStatusCheckTime;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
}
