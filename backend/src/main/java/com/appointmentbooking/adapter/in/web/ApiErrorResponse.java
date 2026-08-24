package com.appointmentbooking.adapter.in.web;

import java.util.Map;

public record ApiErrorResponse(String code, String message, Map<String, String> fieldErrors, Map<String, Object> details) { }