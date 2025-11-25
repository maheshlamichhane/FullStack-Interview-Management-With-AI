package com.itsutra.project.controller;

import com.itsutra.project.enums.OTPType;
import com.itsutra.project.service.OTPService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/send")
    public ResponseEntity<?> sendOTP(@RequestBody Map<String, String> request,
                                     HttpServletRequest httpRequest) {
        String email = request.get("email");
        OTPType type = OTPType.valueOf(request.get("type"));

        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Map<String, Object> result = otpService.generateAndSendOTP(email, type, ipAddress, userAgent);

        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@RequestBody Map<String, String> request,
                                       HttpServletRequest httpRequest) {
        String email = request.get("email");
        String otp = request.get("otp");
        OTPType type = OTPType.valueOf(request.get("type"));

        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Map<String, Object> result = otpService.verifyOTP(email, otp, type, ipAddress, userAgent);

        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendOTP(@RequestBody Map<String, String> request,
                                       HttpServletRequest httpRequest) {
        String email = request.get("email");
        OTPType type = OTPType.valueOf(request.get("type"));

        String ipAddress = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");

        Map<String, Object> result = otpService.resendOTP(email, type, ipAddress, userAgent);

        if ((Boolean) result.get("success")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
