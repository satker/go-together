package org.go.together.notification.transport.interfaces;

import org.go.together.dto.ComparingObject;

import java.util.Map;

public interface Transport {
    Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties);
}
