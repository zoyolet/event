package sut.game01.core.screen;

import playn.core.Font;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends UIScreen {

    public static final Font TITLE_FONT = graphics().createFont(
            "Helvetica",
            Font.Style.PLAIN,
            24);
    private final ScreenStack ss;
    public HomeScreen(ScreenStack ss) {
        this.ss = ss;
    }

    private Root root;
    @Override
    public void wasAdded(){
        Image bgImage = assets().getImage("images/bg.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);
    }
    @Override
    public void wasShown(){
        super.wasShown();
        root = iface.createRoot(
            AxisLayout.vertical().gap(15),
            SimpleStyles.newSheet(),layer);
        root.addStyles(Style.BACKGROUND
        .is(Background.bordered(0xFFCCCCCC,0xFF99CCFF,5)
        .inset(5,10)));
        root.setSize(width(),height());

        root.add(new Label("Dokapon Pocket")
        .addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));

        root.add(new Button("Start").onClick(new UnitSlot() {
            @Override
            public void onEmit() {
                ss.push(new TestScreen(ss));
            }
        }));
    }
}
