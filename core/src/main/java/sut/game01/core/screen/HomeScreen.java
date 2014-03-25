package sut.game01.core.screen;

import playn.core.Font;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.util.Callback;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
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
        Image bgImage = assets().getImage("images/basketballfabric.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        Image bgImage2 = assets().getImage("images/time.png");
        ImageLayer bgLayer2 = graphics().createImageLayer(bgImage2);
        Image bgImage3 = assets().getImage("images/speed.png");
        ImageLayer bgLayer3 = graphics().createImageLayer(bgImage3);
        bgLayer2.setTranslation(150f,270f);
        bgLayer3.setTranslation(350f,270f);
        bgLayer2.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new TestScreen(ss));
            }
        });
        bgLayer3.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.push(new TestScreen2(ss));
            }
        });
        root.layer.add(bgLayer);
        root.setSize(width(),height());
        root.add(new Label("Basketball Crazy")
        .addStyles(Style.FONT.is(HomeScreen.TITLE_FONT)));
        layer.add(bgLayer2);
        layer.add(bgLayer3);
    }
}
