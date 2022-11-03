package org.example;

import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.st0601.UasDatalinkMessage;
import org.jmisb.api.klv.st0601.UasDatalinkTag;
import org.jmisb.api.video.IMetadataListener;
import org.jmisb.api.video.IVideoListener;
import org.jmisb.api.video.MetadataFrame;
import org.jmisb.api.video.VideoFrame;

import java.awt.image.BufferedImage;
import java.io.IOException;

class ExampleProcessor implements IVideoListener, IMetadataListener
    {

        OutputStreamer output;
        boolean inputHasStarted = false;

        public void setupOutputStream() throws IOException {
            output = new OutputStreamer();
            output.createSampleMetadata();
            output.setupStream();
        }
        int count = 0;
        @Override
        public void onVideoReceived(VideoFrame frame)
        {

            BufferedImage image = frame.getImage();

            try {
                if (count % 2 == 0) {
                    output.start(image, true);

                } else {
                    output.start(image, false);
                }

                count += 1;
            } catch (IOException e) {
                System.out.println("Exception received on output.start");
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onMetadataReceived(MetadataFrame frame)
        {
            IMisbMessage metadata = frame.getMisbMessage();
            if (metadata instanceof UasDatalinkMessage)
            {
                UasDatalinkMessage msg = (UasDatalinkMessage)metadata;
                System.out.println("Sensor position: " +
                        msg.getField(UasDatalinkTag.SensorLatitude).getDisplayableValue() +
                        ", " +
                        msg.getField(UasDatalinkTag.SensorLongitude).getDisplayableValue());
            }

        }
}
