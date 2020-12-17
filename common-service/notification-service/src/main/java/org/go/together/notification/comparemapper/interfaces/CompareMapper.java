package org.go.together.notification.comparemapper.interfaces;

import org.go.together.compare.ComparingObject;

import java.util.Map;

public interface CompareMapper {
    Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties);
}
