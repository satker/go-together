package org.go.together.notification.comparators.impl.collection.transformers.interfaces;

import org.go.together.dto.ComparingObject;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;

public interface FinderTransformer {
    Finder<?> get(ComparingObject fieldProperties);
}
