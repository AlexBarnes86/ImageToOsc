package com.toastedbits.kadenze.mlartmusic;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.transport.udp.OSCPortOut;
import javafx.scene.paint.Color;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class OSCHelper {
    private String host;
    private int port;
    private String oscAddress;

    public OSCHelper(String host, int port, String oscAddress) {
        this.host = host;
        this.port = port;
        this.oscAddress = oscAddress;
    }

    public void sendOscMessage(final Color[][] colorMatrix) {
        if(colorMatrix != null) {
            List<Float> o = new ArrayList(colorMatrix.length * colorMatrix.length * 3);
            for (int i = 0; i < colorMatrix.length; ++i) {
                for (int j = 0; j < colorMatrix.length; ++j) {
                    o.add((float)colorMatrix[i][j].getRed());
                    o.add((float)colorMatrix[i][j].getGreen());
                    o.add((float)colorMatrix[i][j].getBlue());
                }
            }

            try {
                OSCPortOut sender = new OSCPortOut(InetAddress.getByName(host), port);
                OSCMessage msg = new OSCMessage(oscAddress, o);
                sender.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String host;
        private int port;
        private String oscAddress;

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withOscAddress(String oscAddress) {
            this.oscAddress = oscAddress;
            return this;
        }

        public OSCHelper build() {
            return new OSCHelper(host, port, oscAddress);
        }
    }
}
