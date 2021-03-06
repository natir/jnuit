#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${rootArtifactId}.game.components.ui;

import im.bci.jnuit.NuitToolkit;
import im.bci.jnuit.lwjgl.assets.IAssets;
import im.bci.jnuit.widgets.Button;
import im.bci.jnuit.widgets.Container;
import im.bci.jnuit.widgets.Stack;
import im.bci.jnuit.widgets.Dialogue;

/**
 *
 * @author devnewton
 */
public class CutScenesMenu extends Stack {

    private final Container menu;

    CutScenesMenu(final NuitToolkit toolkit, final IAssets assets, final CutScenes cutScenes) {
        menu = new Container();

        final Button creditsButton = new Button(toolkit, "cutscenes.menu.button.credits") {
            @Override
            public void onOK() {
                Dialogue credits = new Dialogue(toolkit);
                cutScenes.createCredits(credits);
                credits.setWidth(CutScenesMenu.this.getWidth());
                credits.setHeight(CutScenesMenu.this.getHeight());
                CutScenesMenu.this.show(credits);
            }
        };
        creditsButton.setX(110);
        creditsButton.setY(500);
        creditsButton.setWidth(400);
        creditsButton.setHeight(80);
        menu.add(creditsButton);

        final Button backButton = new Button(toolkit, "cutscenes.menu.button.back") {
            @Override
            public void onOK() {
                CutScenesMenu.this.close();
            }
        };
        backButton.setX(110);
        backButton.setY(700);
        backButton.setWidth(400);
        backButton.setHeight(80);
        menu.add(backButton);
        show(menu);
    }
}
