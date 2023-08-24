package com.cstudy.moduleapi.application.geoIpAddress;

import com.cstudy.moduleapi.dto.ipAddress.GeoLocationDto;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Subdivision;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final GeoReader geoReader;

    public GeoLocationDto findLocation(InetAddress ipAddress) {
        CityResponse response = geoReader.city(ipAddress);

        Subdivision subdivision = response.getMostSpecificSubdivision();
        City city = response.getCity();
        Continent continent = response.getContinent();
        Country country = response.getCountry();

        return new GeoLocationDto(
                subdivision.getName(),
                city.getName(),
                continent.getName(),
                country.getName(),
                country.getIsoCode(),
                ipAddress.getHostAddress()
        );

    }
}