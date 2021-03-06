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
package im.bci.jnuit.artemis.sprite;

import im.bci.jnuit.timed.OneShotTimedAction;


/**
 *
 * @author devnewton
 *
 */
class SpriteRotateTo extends SpriteControl {

    private final float to;
    private final float duration;
    private final Sprite sprite;
    private float from;
    private OneShotTimedAction action;

    SpriteRotateTo(Sprite sprite, float to, float duration) {
        this.sprite = sprite;
        this.to = to;
        this.duration = duration;
    }

    @Override
    public void update(float elapsedTime) {
        final OneShotTimedAction a = getAction();
        a.update(elapsedTime);
        float newRotate;
        final float progress = a.getProgress();
        if (progress >= 1.0f) {
            newRotate = to;
        } else {
            newRotate = from + (to - from) * progress;
        }
        sprite.setRotate(newRotate);
    }

    private OneShotTimedAction getAction() {
        if (null == action) {
            this.from = sprite.getRotate();
            action = new OneShotTimedAction(duration);
        }
        return action;
    }

    @Override
    public boolean isFinished() {
        return getAction().getProgress() >= 1.0f;
    }

}
