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
package im.bci.jnuit.timed;

/**
 * A timed action that will linearly go from 0f to 1f then from 1f to 0f and so on.
 * @author devnewton
 */
public strictfp class PingPongTimedAction extends TimedAction{

    private float progress;
    private final long duration;
    private long time;
    private boolean goForward = true;

    public PingPongTimedAction(long duration) {
        this.duration = duration;
    }

    @Override
    public void update(float elapsedTime) {
        if (goForward) {
            time += elapsedTime;
            if (time > duration) {
                time = duration;
                goForward = false;
            }
        } else {
            time -= elapsedTime;
            if (time < 0) {
                time = 0;
                goForward = true;
            }

        }
        progress = (float) time / (float) duration;
    }

    @Override
    public float getProgress() {
        return progress;
    }
}
