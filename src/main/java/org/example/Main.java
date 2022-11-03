package org.example;

import org.jmisb.api.video.IVideoStreamInput;
import org.jmisb.api.video.VideoStreamInput;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (IVideoStreamInput stream = new VideoStreamInput())
        {
            stream.open("http://samples.ffmpeg.org/MPEG2/mpegts-klv/Day%20Flight.mpg");
            ExampleProcessor processor = new ExampleProcessor();
            processor.setupOutputStream();
            stream.addFrameListener(processor);
            while (stream.isOpen()) {
                Thread.sleep(10);
            }
        } catch (IOException e) {
            System.out.println("Could not open the stream");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello world!");
    }

}