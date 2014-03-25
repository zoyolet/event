package sut.game01.core;

import playn.core.Game;
import playn.core.Keyboard;
import playn.core.PlayN;
import playn.core.util.Clock;
import sut.game01.core.screen.HomeScreen;
import sut.game01.core.screen.TestScreen;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class MyGame extends Game.Default {
    private ScreenStack ss = new ScreenStack();
    private Clock.Source clock = new Clock.Source(33);
  public MyGame() {
    super(33); // call update every 33ms (30 times per second)
  }

  @Override
  public void init() {
    final Screen home = new HomeScreen(ss);
    final Screen test = new TestScreen(ss);
    ss.push(home);
      PlayN.keyboard().setListener(new Keyboard.Adapter(){
          @Override
          public void onKeyUp(Keyboard.Event event) {
              ss.popTo(home);
          }
      });
  }

  @Override
  public void update(int delta) {
      ss.update(delta);
  }

  @Override
  public void paint(float alpha) {
      clock.paint(alpha);
      ss.paint(clock);

    // the background automatically paints itself, so no need to do anything here!
  }
}
