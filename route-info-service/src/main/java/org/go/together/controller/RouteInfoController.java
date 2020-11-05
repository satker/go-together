package org.go.together.controller;

import org.go.together.base.FindController;
import org.go.together.client.RouteInfoClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteInfoController extends FindController implements RouteInfoClient {
}
