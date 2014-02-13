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
package ${game-package}.game;

import im.bci.jnuit.lwjgl.LwjglNuitPreferences;
import com.artemis.Entity;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import im.bci.jnuit.NuitToolkit;

import java.io.File;
import java.util.Random;

import com.google.inject.Singleton;
import im.bci.jnuit.NuitControls;
import im.bci.jnuit.NuitDisplay;
import im.bci.jnuit.NuitPreferences;
import im.bci.jnuit.NuitRenderer;
import im.bci.jnuit.NuitTranslator;
import im.bci.jnuit.lwjgl.LwjglNuitControls;
import im.bci.jnuit.lwjgl.LwjglNuitDisplay;
import im.bci.jnuit.lwjgl.LwjglNuitFont;
import im.bci.jnuit.lwjgl.assets.AssetsLoader;

import im.bci.jnuit.lwjgl.assets.GarbageCollectedAssets;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.lwjgl.assets.VirtualFileSystem;
import ${game-package}.game.components.IngameControls;
import ${game-package}.game.components.ZOrder;
import ${game-package}.game.components.ui.CutScenes;
import ${game-package}.game.components.ui.DialogComponent;
import ${game-package}.game.components.ui.LevelSelector;
import ${game-package}.game.components.ui.MainMenu;
import ${game-package}.game.constants.ZOrders;
import ${game-package}.game.events.HideMenuTrigger;
import ${game-package}.game.events.ShowLevelMenuTrigger;
import ${game-package}.game.events.ShowMenuTrigger;
import ${game-package}.game.systems.IngameInputSystem;
import ${game-package}.game.systems.DrawSystem;

/**
 *
 * @author devnewton
 */
public class GameModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NuitRenderer.class).to(GameNuitRenderer.class);
        bind(NuitTranslator.class).to(GameNuitTranslator.class);
        bind(NuitControls.class).toInstance(new LwjglNuitControls());
        bind(NuitDisplay.class).toInstance(new LwjglNuitDisplay());
        bind(NuitToolkit.class).to(GameNuitToolkit.class);
        bind(LevelSelector.class);
        bind(IngameInputSystem.class);
        bind(MainLoop.class);
        bind(DialogComponent.class);
        bind(DrawSystem.class);
        bind(ShowMenuTrigger.class);
        bind(HideMenuTrigger.class);
        bind(ShowLevelMenuTrigger.class);
        bind(CutScenes.class);
        bind(Random.class).toInstance(new Random(42));
    }
    
    @Provides
    @Singleton
    public NuitPreferences createPreferences(NuitControls controls) {
        return new LwjglNuitPreferences(controls, "${game-name}");
    }
    
    @Provides
    @Singleton
    public IAssets createAssets() {
        File applicationDir = NormalLauncher.getApplicationDir();
        VirtualFileSystem vfs = new VirtualFileSystem(new File(applicationDir, "data"), new File(applicationDir.getParentFile(), "data"));
        return new GarbageCollectedAssets(new AssetsLoader(vfs));
    }

    @Provides
    @NamedEntities.MainMenu
    @Singleton
    public Entity createMainMenu(Game game, MainMenu mainMenuComponent) {
        Entity mainMenu = game.createEntity();
        mainMenu.addComponent(mainMenuComponent);
        mainMenu.addComponent(new ZOrder(ZOrders.MENU));
        game.addEntity(mainMenu);
        return mainMenu;
    }

    @Provides
    @NamedEntities.DefaultFont
    public LwjglNuitFont createDefaultFont(IAssets assets) {
        return assets.getFont("");
    }

    @Provides
    @NamedEntities.IngameControls
    @Singleton
    public Entity createIngameControls(Game game, IngameControls ingameControlsComponent) {
        Entity ingameControls = game.createEntity();
        ingameControls.addComponent(ingameControlsComponent);
        ingameControls.disable();
        game.addEntity(ingameControls);
        return ingameControls;
    }
}