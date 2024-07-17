package com.sunrise;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Results {

        String sunrise;
        String sunset;
        String solarNoon;
        String dayLength;
        String civilTwilightBegin;
        String civil_twilight_begin;
        String civil_twilight_end;
        String nautical_twilight_begin;
        String nautical_twilight_end;
        String astronomical_twilight_begin;
        String astronomical_twilight_end;
    }


