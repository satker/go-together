package org.go.together.streams;

import org.go.together.streams.interfaces.NotificationMessageStream;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(NotificationMessageStream.class)
public class StreamsConfiguration {
}
