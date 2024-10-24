package fiap.wtu_ancora.model;

import java.util.HashMap;
import java.util.Map;

public class EventEmailJobResponse {
    Map<Long, EventDetailToEmailJob> eventDetailToEmailJobMap = new HashMap<>();

    public Map<Long, EventDetailToEmailJob> getEventDetailToEmailJobMap() {
        return eventDetailToEmailJobMap;
    }

    public void setEventDetailToEmailJobMap(Long eventId, EventDetailToEmailJob eventDetailToEmailJob) {
        eventDetailToEmailJobMap.put(eventId, eventDetailToEmailJob);
    }
}
