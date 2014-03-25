package sut.game01.core.screen;

import playn.core.util.Clock;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.Root;

/**
 * Created by Boom on 25/3/2557.
 */
public class Overscreen extends UIScreen {
    private ScreenStack ss;
    private Root root,root2,root3;
    public Overscreen (ScreenStack ss){
        this.ss =ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
    }

}
