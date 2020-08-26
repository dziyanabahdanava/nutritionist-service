package com.epam.ms.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * The class represents data to be sent to the notification-service
 */
@Data
@AllArgsConstructor
public class Event implements Serializable {

    private String group;
    private String event;
    private Map<String, Object> parameters;
}
