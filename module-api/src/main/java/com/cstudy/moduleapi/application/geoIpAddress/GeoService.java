package com.cstudy.moduleapi.application.geoIpAddress;

import com.cstudy.moduleapi.dto.ipAddress.GeoLocationDto;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class GeoService {
    public GeoLocationDto findLocation(InetAddress ipAddress) {
        return new GeoLocationDto(ipAddress.getHostAddress());
    }
}