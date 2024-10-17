package fiap.wtu_ancora.model;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiReponse<T> {

    private String message;
    private int status;
    private Map<String, String> links;
    private LocalDateTime timestamp;
    private T data;

    public ApiReponse(String message, int status, Map<String, String> links, LocalDateTime timestamp, T data) {

        this.message = message;
        this.status = status;
        this.links = links;
        this.timestamp = timestamp;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
