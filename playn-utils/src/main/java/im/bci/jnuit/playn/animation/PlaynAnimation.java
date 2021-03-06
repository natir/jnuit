/*
 The MIT License (MIT)

 Copyright (c) 2013 devnewton <devnewton@bci.im>

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
package im.bci.jnuit.playn.animation;

import im.bci.jnuit.animation.IPlay;
import im.bci.jnuit.animation.PlayMode;
import im.bci.jnuit.animation.IAnimation;

import java.util.ArrayList;

/**
 *
 * @author devnewton
 */
public class PlaynAnimation implements IAnimation {

    public class Play implements IPlay {

        public int currentFrameIndex;
        public long currentTime;
        public PlayMode mode = PlayMode.LOOP;
        private PlaynAnimationPlayState state = PlaynAnimationPlayState.STOPPED;

        @Override
        public String getName() {
            return PlaynAnimation.this.name;
        }

        @Override
        public PlaynAnimationFrame getCurrentFrame() {
            return frames.get(currentFrameIndex);
        }

        @Override
        public void start(PlayMode mode) {
            this.state = PlaynAnimationPlayState.STARTED;
            this.mode = mode;
            this.currentTime = 0;
            this.currentFrameIndex = 0;
        }

        @Override
        public void restart() {
            stop();
            start(mode);
        }

        @Override
        public void stop() {
            state = PlaynAnimationPlayState.STOPPED;
            currentTime = 0;
            currentFrameIndex = 0;
        }

        @Override
        public boolean isStopped() {
            return state == PlaynAnimationPlayState.STOPPED;
        }

        @Override
        public void update(long elapsedTime) {

            if (state == PlaynAnimationPlayState.STOPPED) {
                return;
            }

            this.currentTime += elapsedTime;
            if (currentTime >= totalDuration) {

                switch (mode) {
                    case ONCE:
                        currentFrameIndex = frames.size() - 1;
                        state = PlaynAnimationPlayState.STOPPED;
                        return;
                    case LOOP:
                        currentTime %= totalDuration;
                        currentFrameIndex = 0;
                        break;
                }
            }

            while (currentTime > frames.get(currentFrameIndex).endTime) {
                ++this.currentFrameIndex;
            }
        }

        @Override
        public PlayMode getMode() {
            return mode;
        }
    }
    private final ArrayList<PlaynAnimationFrame> frames = new ArrayList<>();
    private long totalDuration;// milliseconds
    private final String name;

    public PlaynAnimation(String name) {
        this.name = name;
    }

    public PlaynAnimationFrame addFrame(PlaynAnimationImage image, long duration) {
        final PlaynAnimationFrame frame = new PlaynAnimationFrame(image, duration);
        frames.add(frame);
        totalDuration += duration;
        frame.endTime = totalDuration;
        return frame;
    }

    public PlaynAnimationFrame addFrame(PlaynAnimationImage image, long duration, float u1, float v1, float u2, float v2) {
        final PlaynAnimationFrame frame = new PlaynAnimationFrame(image, duration, u1, v1, u2, v2);
        frames.add(frame);
        totalDuration += duration;
        frame.endTime = totalDuration;
        return frame;
    }

    @Override
    public Play start(PlayMode mode) {
        if (!frames.isEmpty()) {
            Play play = new Play();
            play.start(mode);
            return play;
        } else {
            return null;
        }
    }

    /**
     * Call play.stop
     *
     * @param play
     */
    @Override
    public void stop(IPlay play) {
        if (null != play) {
            play.stop();
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
