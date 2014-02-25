package sut.game01.core.spite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.TestScreen;

/**
 * Created by Boom on 29/1/2557.
 */
public class Sp {
    private Sprite sprite;
    private int spriteIndex =0;
    private Body other;
    private boolean contacted;
    private int contactCheck;
    private boolean hasLoaded=false;
    private float x;
    private float y;
    private int hp=100;
    private int t=0;
    public enum State{
        WALK,ATK,SKILL,DIE
    };
    private State state = State.WALK;
    private int e =0;
    private int offset = 0;
    private Body body;
    private Body initPhysicsBody(World world,float x, float y){
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyType.DYNAMIC;
        bodydef.position = new Vec2(0,0);
        Body body = world.createBody(bodydef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * TestScreen.m_per_pixel/2,
                sprite.layer().height()*TestScreen.m_per_pixel/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape =  shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);
        return body;
    }
    public Sp(final World world,final float x,final float y){
        this.x = x;
        this.y = y;
        this.sprite = SpriteLoader.getSprite("images/sp.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y+13f);
                body = initPhysicsBody(world, x*TestScreen.m_per_pixel, y*TestScreen.m_per_pixel);
                hasLoaded = true;
            }
            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
            }
        });
        sprite.layer().addListener(new Pointer.Adapter(){
            @Override
            public void onPointerEnd(Pointer.Event event){
                state = State.ATK;
                spriteIndex = -1;
                e=0;
            }

        });
        PlayN.keyboard().setListener(new Keyboard.Listener() {
            @Override
            public void onKeyDown(Keyboard.Event event) {

            }

            @Override
            public void onKeyTyped(Keyboard.TypedEvent event) {

            }

            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key()== Key.A){
                    state = State.SKILL;
                    spriteIndex =-1;
                    e=0;
                }
            }
        });
    }
    public void update(int delta){
        if(!hasLoaded)return;
        t+=delta;
        e+=delta;

        if(e>150){
            switch (state){
                case WALK: offset = 0;
                    x +=0.5;
                    break;
                case ATK: offset = 8;
                    if(spriteIndex==15){
                        state = State.WALK;
                    }
                    break;
                case SKILL:offset=16;
                    if(spriteIndex==23){
                        state = State.WALK;
                    }
                    break;
                case DIE:offset=24;
                    if (spriteIndex==31){
                        state = State.WALK;
                    }
            }
        }
    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                (body.getPosition().x/TestScreen.m_per_pixel),
                body.getPosition().y/TestScreen.m_per_pixel);
    }
    public Layer layer(){
        return sprite.layer();
    }
    public Body getBody(){
        return this.body;
    }
    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;

        if(state==State.WALK){
            state = State.DIE;
        }

        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        } else {
            other = contact.getFixtureA().getBody();
        }
    }
}
