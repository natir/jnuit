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
package im.bci.jnuit.artemis.sprite;

import im.bci.jnuit.timed.OneShotTimedAction;
import pythagoras.f.Vector3;

/**
 *
 * @author devnewton
 *
 */
class SpriteMoveToRelative extends SpriteControl {

    private Vector3 diff;
    private Vector3 to;
    private final float duration;
    private final Sprite sprite;
    private Vector3 from;
    private OneShotTimedAction action;

    SpriteMoveToRelative(Sprite sprite, Vector3 diff, float duration) {
        this.sprite = sprite;
        this.diff = diff;
        this.duration = duration;
    }

    @Override
    public void update(float elapsedTime) {
        final OneShotTimedAction a = getAction();
        a.update(elapsedTime);
        Vector3 newPos;
        final float progress = a.getProgress();
        if (progress >= 1.0f) {
            newPos = to;
        } else {
            newPos = to.subtract(from);
            newPos.multLocal(progress);
            newPos.x += from.x;
            newPos.y += from.y;
            newPos.z += from.z;
        }
        sprite.getPosition().x = newPos.x;
        sprite.getPosition().y = newPos.y;
        sprite.getPosition().z = newPos.z;
    }

    private OneShotTimedAction getAction() {
        if (null == action) {
            this.from = new Vector3(sprite.getPosition());
            this.to = new Vector3(this.from.x + diff.x, this.from.y +diff.y, this.from.z + diff.z);
            action = new OneShotTimedAction(duration);
        }
        return action;
    }

    @Override
    public boolean isFinished() {
        return getAction().getProgress() >= 1.0f;
    }

}
