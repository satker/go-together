package org.go.together.notification.transport.interfaces;

import org.go.together.compare.ComparingObject;

import java.util.Map;

public interface Transport {
    Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties);
}
