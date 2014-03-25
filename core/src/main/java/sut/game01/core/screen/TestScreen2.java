package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import react.UnitSlot;
import sut.game01.core.spite.Bas;
import sut.game01.core.spite.Player;
import sut.game01.core.spite.Sp;
import sut.game01.core.spite.Zealot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class TestScreen2 extends UIScreen{

    private final ScreenStack ss;
    private boolean finished = true;
    private Zealot z;
    private Zealot z2;
    private Player player;
    private int thiscore=-1;
    private int thistime=-1;
    private Sp sp;
    private int time =0;
    private int time2 = 0;
    private Bas bas;
    boolean bashasloaded = false;
    private int times =0;
    private Root root,root2,root3,root4,root5,root6;
    private DebugDrawBox2D debugDraw;
    public static float m_per_pixel = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private int score = 0;
    private boolean showDebugDraw=true;
    private Body ground;
    private Body ground2;
    private Body ground3;
    public static final Font TITLE_FONT = graphics().createFont(
            "Helvetica",
            Font.Style.PLAIN,
            24);
    @Override
    public void wasAdded(){
        if(PlayN.platformType()==Platform.Type.ANDROID){
            layer.setScaleX(800f/640f);
        }
        Image bgImage = assets().getImage("images/stage.png");
        Image button = assets().getImage("images/stop-button.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        ImageLayer buttonLayer = graphics().createImageLayer(button);
        buttonLayer.addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                ss.remove(TestScreen2.this);
            }
        });
        layer.add(bgLayer);
        layer.add(buttonLayer);
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        //z = new Zealot(world,150f,300f);
        player = new Player(world,30f,400f);
        //bas = new Bas (world,100f,100f);
        //layer.add(z.layer());
        player.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event) {
                super.onPointerEnd(event);
                player.state = Player.State.JUMP;
                player.setspriteindex(-1);
                player.sete(0);
            }
        });
        layer.add(player.layer());
        //layer.add(bas.layer());
        //sp = new Sp(world,450f,300f);
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/TestScreen.m_per_pixel),
                    (int)(height/TestScreen.m_per_pixel));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 26.666667f);
            world.setDebugDraw(debugDraw);
            ground = world.createBody(new BodyDef());
            ground2 = world.createBody(new BodyDef());
            ground3 = world.createBody(new BodyDef());
            PolygonShape groundShape = new PolygonShape();
            PolygonShape groundShape2 = new PolygonShape();
            PolygonShape groundShape3 = new PolygonShape();
            PolygonShape groundShape4 = new PolygonShape();
            PolygonShape groundShape5 = new PolygonShape();
            PolygonShape groundCheck = new PolygonShape();
            PolygonShape groundCheck2 = new PolygonShape();
            groundShape2.setAsEdge(new Vec2(21.1f, height-6),new Vec2(22f, height-8));
            groundShape3.setAsEdge(new Vec2(19f, height-8),new Vec2(19.9f, height-6));
            groundShape4.setAsEdge(new Vec2(22f, height-8),new Vec2(22f, height-12));
            groundShape5.setAsEdge(new Vec2(0f,height),new Vec2(0f,height-26));
            groundCheck.setAsEdge(new Vec2(2.2f,height-1),new Vec2(23f,height-0));
            groundCheck2.setAsEdge(new Vec2(19f,height-6f),new Vec2(21f,height-3));
            groundShape.setAsEdge(new Vec2(0f, height), new Vec2(width , height));
            ground.createFixture(groundShape5, 0f);
            ground.createFixture(groundShape, 0.0f);
            ground.createFixture(groundShape2,0.0f);
            ground.createFixture(groundShape3,0.0f);
            ground.createFixture(groundShape4,0.0f);
            ground3.createFixture(groundCheck,0.0f);
            ground2.createFixture(groundCheck2,0.0f);
            root5 = iface.createRoot(
                    AxisLayout.vertical().gap(15),
                    SimpleStyles.newSheet(),layer);
            root5.setSize(250f, 30f);
            root5.add(new Label("Score must : 10 ").addStyles(Style.FONT.is(TestScreen.TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFFFFFF)));
            root6 = iface.createRoot(
                    AxisLayout.vertical().gap(15),
                    SimpleStyles.newSheet(),layer);
            root6.setSize(400f,400f);
            root6.add(new Label("Click Character to Shoot").addStyles(Style.FONT.is(TestScreen.TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFFFFFF)));
            world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {

                }

                @Override
                public void endContact(Contact contact) {
                    try{
                        if(contact.getFixtureA().getBody()==ground2&&contact.getFixtureB().getBody()==bas.getBody()){
                            bas.layer().destroy();
                            //bas.getBody().setTransform(new Vec2(-1f,0f),0f);
                            bashasloaded = false;
                            score += 1;
                            System.out.println("Score is :"+score);
                        }else if(contact.getFixtureB().getBody()==ground2&&contact.getFixtureA().getBody()==bas.getBody()){
                            bas.layer().destroy();
                            //bas.getBody().setTransform(new Vec2(-1f,0f),0f);
                            bashasloaded = false;
                            score += 1;
                            System.out.println("Score is :"+score);
                        }
                        if(contact.getFixtureA().getBody()==ground3&&contact.getFixtureB().getBody()==bas.getBody()){
                            bas.layer().destroy();
                            //bas.getBody().setTransform(new Vec2(-1f,0f),0f);
                            bashasloaded = false;
                        }else if(contact.getFixtureB().getBody()==ground3&&contact.getFixtureA().getBody()==bas.getBody()){
                            bas.layer().destroy();
                            //bas.getBody().setTransform(new Vec2(-1f,0f),0f);
                            bashasloaded = false;
                        }
                    }catch(Exception e){

                    }
                }

                @Override
                public void preSolve(Contact contact, Manifold manifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse contactImpulse) {

                }
            });
            PlayN.pointer().setListener(new Pointer.Adapter(){
                @Override
                public void onPointerEnd(Pointer.Event event) {
                    super.onPointerEnd(event);
                    finished = false;
                    root6.removeAll();
                }
            });
        }
    }

    public TestScreen2(ScreenStack ss){
        this.ss = ss;
    }

    @Override
    public void update(int delta) {
        if(!finished){
            thistime = -1;
            time = time+delta;
            if(time>=1000){
                times +=1;
                time = 0;
            }
            if(times==60){
                time2 +=1;
                times =0;
            }
            if(thistime<times){
                try{
                    root2.removeAll();
                }catch (Exception e){

                }
                thistime = times;
                root2 = iface.createRoot(
                        AxisLayout.vertical().gap(15),
                        SimpleStyles.newSheet(),layer);
                root2.setSize(600f, 30f);
                root2.add(new Label("Times is " +time2+":"+ thistime).addStyles(Style.FONT.is(TestScreen.TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFFFFFF)));
            }
            thiscore = -1;
            if(thiscore<score){
                root.removeAll();
                thiscore = score;
                root = iface.createRoot(
                        AxisLayout.vertical().gap(15),
                        SimpleStyles.newSheet(),layer);
                root.setSize(1000f, 30f);
                root.add(new Label("Score is "+thiscore).addStyles(Style.FONT.is(TestScreen.TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFFFFFF)));
            }
            if(thiscore==10){
                root3 = iface.createRoot(
                        AxisLayout.vertical().gap(15),
                        SimpleStyles.newSheet(),layer);
                root3.setSize(width()/2, height()/2);
                root3.add(new Label("Yourtime is "+time2 + ":"+thistime).addStyles(Style.FONT.is(TestScreen.TITLE_FONT)).addStyles(Style.COLOR.is(0xFFFFFFFF)));
                root4 = iface.createRoot(
                        AxisLayout.vertical().gap(15),
                        SimpleStyles.newSheet(),layer);
                root4.setSize(width()/2, (height()/2)+100);
                root4.add(new Button("Back").onClick(new UnitSlot() {
                    @Override
                    public void onEmit() {
                        ss.remove(TestScreen2.this);
                    }
                }));
                finished = true;
            }
            //z.update(delta);
            player.update(delta);
            if(bashasloaded){
                bas.update(delta);
            }
            if (!bashasloaded){
                if(player.spriteIndex()==10){
                    bas = new Bas(world,player.getBody().getPosition().x/TestScreen.m_per_pixel,(player.getBody().getPosition().y/TestScreen.m_per_pixel)-20);
                    layer.add(bas.layer());
                    bashasloaded = true;
                }}
        }
        super.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void wasShown(){
        super.wasShown();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(),layer);
        root.setSize(width(),height());
    }
    @Override
    public void paint(Clock clock){
        super.paint(clock);
        //z.paint(clock);
        if(bashasloaded){
            bas.paint(clock);
        }
        player.paint(clock);
        if(false){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
