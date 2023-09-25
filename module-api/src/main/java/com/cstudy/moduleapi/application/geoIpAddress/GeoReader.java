//package com.cstudy.moduleapi.application.geoIpAddress;
//
//import com.maxmind.geoip2.DatabaseReader;
//import com.maxmind.geoip2.exception.GeoIp2Exception;
//import com.maxmind.geoip2.model.CityResponse;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.InetAddress;
//
//@Component
//public class GeoReader {
//
//    private static final String DATABASE_CITY_PATH = "geo/GeoLite2-City.mmdb";
//    private final DatabaseReader reader;
//
//    public GeoReader() throws IOException {
//        ClassPathResource resource = new ClassPathResource(DATABASE_CITY_PATH);
//        File dbFile = resource.getFile();
//        reader = new DatabaseReader.Builder(dbFile).build();
//    }
//    public CityResponse city(InetAddress ipAddress) {
//        CityResponse response = null;
//
//        try {
//            response = reader.city(ipAddress);
//        } catch (IOException | GeoIp2Exception e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//}