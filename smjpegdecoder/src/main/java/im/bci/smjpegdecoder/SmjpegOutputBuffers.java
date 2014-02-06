/*
 The MIT License (MIT)

 Copyright (c) 2014 devnewton <devnewton@bci.im>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
package im.bci.smjpegdecoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author devnewton
 */
public class SmjpegOutputBuffers {

    private final ByteBuffer videoFrame;
    private final int videoFrameStride;
    private int videoFrameTimestamp = -1;

    public SmjpegOutputBuffers(ByteBuffer videoFrame, int videoFrameStride) {
        this.videoFrame = videoFrame;
        this.videoFrameStride = videoFrameStride;
    }

    public SmjpegOutputBuffers(SmjpegDecoder parser) {
        this.videoFrameStride = parser.getVideoWidth() * 4;
        this.videoFrame = ByteBuffer.allocateDirect(this.videoFrameStride * parser.getVideoHeight()).order(ByteOrder.nativeOrder());
    }

    public ByteBuffer getVideoFrame() {
        return videoFrame;
    }

    public int getVideoFrameStride() {
        return videoFrameStride;
    }

    public int getVideoFrameTimestamp() {
        return videoFrameTimestamp;
    }

    public void setVideoFrameTimestamp(int videoFrameTimestamp) {
        this.videoFrameTimestamp = videoFrameTimestamp;
    }
}