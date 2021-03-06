#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${rootArtifactId}.game;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.artemis.sprite.SpriteAnimateSystem;

import ${package}.${rootArtifactId}.game.systems.DebugSpriteSystem;
import ${package}.${rootArtifactId}.game.systems.DialogSystem;
import ${package}.${rootArtifactId}.game.systems.DrawSystem;
import ${package}.${rootArtifactId}.game.systems.IngameInputSystem;
import ${package}.${rootArtifactId}.game.systems.TriggerSystem;
import ${package}.${rootArtifactId}.game.systems.MainMenuSystem;
import ${package}.${rootArtifactId}.game.systems.SpritePuppetControlSystem;
import ${package}.${rootArtifactId}.game.systems.TriggerWhenRemovedSystem;
import ${package}.${rootArtifactId}.game.systems.TintMouseSelectionSystem;
import org.lwjgl.LWJGLException;

/**
 *
 * @author devnewton
 */
@Singleton
public class Game extends World {

    private Entity hero;

    @Inject
    public Game(NuitToolkit toolkit, DrawSystem drawSystem, IngameInputSystem ingameInputSystem) throws LWJGLException {
        setSystem(ingameInputSystem);
        setSystem(new SpriteAnimateSystem());
        setSystem(new SpritePuppetControlSystem());
        setSystem(new TintMouseSelectionSystem());
        setSystem(drawSystem);
        setSystem(new TriggerSystem());
        setSystem(new TriggerWhenRemovedSystem());
        setSystem(new MainMenuSystem());
        setSystem(new DialogSystem());
        setSystem(new DebugSpriteSystem());
        setManager(new GroupManager());

        initialize();
    }

    public void setHero(Entity hero) {
        this.hero = hero;
    }

    public Entity getHero() {
        return hero;
    }
}
