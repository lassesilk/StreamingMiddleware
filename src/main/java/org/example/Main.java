package org.example;

import org.jmisb.api.video.IVideoStreamInput;
import org.jmisb.api.video.VideoStreamInput;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try (IVideoStreamInput stream = new VideoStreamInput())
        {
            //stream.open("C:/Users/lasses/Downloads/youtube.mp4");
            stream.open("udp://172.16.4.46:9988");
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