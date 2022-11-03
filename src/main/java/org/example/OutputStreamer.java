package org.example;

import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.st0601.*;
import org.jmisb.api.video.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

public class OutputStreamer {

    private final int width = 640;
    private final int height = 480;
    private final int bitRate = 1_500_000;
    private final double frameRate = 60.0;
    private final int gopSize = 30;
    IMisbMessage message;
    IVideoStreamOutput output;

    IMisbMessage toyenMessage;

    void createSampleMetadata() {
        // Sample metadata

        SortedMap<UasDatalinkTag, IUasDatalinkValue> values = new TreeMap<>();
        values.put(UasDatalinkTag.PrecisionTimeStamp, new PrecisionTimeStamp(LocalDateTime.now()));
        values.put(
                UasDatalinkTag.PlatformHeadingAngle, new PlatformHeadingAngle(7.04));
        values.put(
                UasDatalinkTag.PlatformPitchAngle, new PlatformPitchAngle(7.04));
        values.put(
                UasDatalinkTag.PlatformRollAngle, new PlatformRollAngle(7.04));
        values.put(
                UasDatalinkTag.SensorRelativeRollAngle, new SensorRelativeRoll(7.04)
        );
        values.put(
                UasDatalinkTag.SensorRelativeElevationAngle, new SensorElevationRate(7.04)
        );
        values.put(
                UasDatalinkTag.SensorRelativeAzimuthAngle, new SensorRelativeAzimuth(7.04)
        );
        values.put(
                UasDatalinkTag.SensorHorizontalFov, new HorizontalFov(7.04)
        );
        values.put(
                UasDatalinkTag.SensorVerticalFov, new VerticalFov(7.04)
        );
        values.put(
                UasDatalinkTag.ImageCoordinateSystem,
                new UasDatalinkString(UasDatalinkString.IMAGE_COORDINATE_SYSTEM, "Geodetic WGS84"));
        values.put(UasDatalinkTag.SensorLatitude, new SensorLatitude(59.9103413));
        values.put(UasDatalinkTag.SensorLongitude, new SensorLongitude(10.7630849));
        values.put(UasDatalinkTag.SensorTrueAltitude, new SensorTrueAltitude(128.3));
        values.put(UasDatalinkTag.UasLdsVersionNumber, new ST0601Version((byte) 11));

        message = new UasDatalinkMessage(values);

        SortedMap<UasDatalinkTag, IUasDatalinkValue> secondValues = new TreeMap<>();
        secondValues.put(UasDatalinkTag.PrecisionTimeStamp, new PrecisionTimeStamp(LocalDateTime.now()));
        secondValues.put(
                UasDatalinkTag.PlatformHeadingAngle, new PlatformHeadingAngle(7.04));
        secondValues.put(
                UasDatalinkTag.PlatformPitchAngle, new PlatformPitchAngle(7.04));
        secondValues.put(
                UasDatalinkTag.PlatformRollAngle, new PlatformRollAngle(7.04));
        secondValues.put(
                UasDatalinkTag.SensorRelativeRollAngle, new SensorRelativeRoll(7.04)
        );
        secondValues.put(
                UasDatalinkTag.SensorRelativeElevationAngle, new SensorElevationRate(7.04)
        );
        secondValues.put(
                UasDatalinkTag.SensorRelativeAzimuthAngle, new SensorRelativeAzimuth(7.04)
        );
        secondValues.put(
                UasDatalinkTag.SensorHorizontalFov, new HorizontalFov(7.04)
        );
        secondValues.put(
                UasDatalinkTag.SensorVerticalFov, new VerticalFov(7.04)
        );
        secondValues.put(
                 UasDatalinkTag.ImageCoordinateSystem,
                 new UasDatalinkString(UasDatalinkString.IMAGE_COORDINATE_SYSTEM, "Geodetic WGS84"));
        secondValues.put(UasDatalinkTag.SensorLatitude, new SensorLatitude(59.917547));
        secondValues.put(UasDatalinkTag.SensorLongitude, new SensorLongitude(10.7776398));
        secondValues.put(UasDatalinkTag.SensorTrueAltitude, new SensorTrueAltitude(128.3));
        secondValues.put(UasDatalinkTag.UasLdsVersionNumber, new ST0601Version((byte) 11));

        toyenMessage = new UasDatalinkMessage(secondValues);
    }

    void setupStream() throws IOException {
        final String url = "udp://127.0.0.1:9999";

        output = new VideoStreamOutput(
                    new VideoOutputOptions(
                                     width,
                                     height,
                                     bitRate,
                                     frameRate,
                                     gopSize,
                                     KlvFormat.Asynchronous,
                                     CodecIdentifier.H264));
            output.open(url);
        }

    void start(BufferedImage image, boolean isGeodata) throws IOException {
        final double frameDuration = 1.0 / frameRate;
            // Write some video and metadata frames
            double pts = 0.0;
            output.queueVideoFrame(new VideoFrame(image, pts));
            if (isGeodata) {
                output.queueMetadataFrame(new MetadataFrame(message, pts));

            } else {
                output.queueMetadataFrame(new MetadataFrame(toyenMessage, pts));
            }
            pts += frameDuration;
    }
}
