package org.lightnsalt.hikingdom.service.hiking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/sub");
		config.setApplicationDestinationPrefixes("/pub");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/api/v1/hiking/ws")
			.setAllowedOriginPatterns("http://localhost:3000", "http://localhost:8080", "https://hikingdom.kr",
				"http://hikingdom.kr:3001", "http://hikingdom.kr:8081", "http://hikingdom.kr:8080",
				"https://k8a102.p.ssafy.io", "http://70.12.246.181:3000")
			.withSockJS();
	}
}

