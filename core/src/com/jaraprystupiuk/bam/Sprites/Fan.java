package com.jaraprystupiuk.bam.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.jaraprystupiuk.bam.Bam;
import com.jaraprystupiuk.bam.Screens.PlayScreen;

public class Fan extends Sprite {
    public enum State {SUBIENDO, BAJANDO, DERECHA, PARADO, DEESPALDAS}

    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion fanParado;
    private Animation<TextureRegion> fanDerecha;
    private Animation<TextureRegion> fanSubiendo;
    private Animation<TextureRegion> fanBajando;
    private TextureRegion fanDeEspaldas;
    private TextureRegion fanParadoDerecha;

    private float stateTimer;

    private boolean runningRight;
    private boolean subio;
    private boolean fueDerecha;

    public Fan(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("fan"));
        this.world = world;

        currentState = State.PARADO;
        previousState = State.PARADO;
        stateTimer = 0;
        runningRight = true;
        subio = false;


        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 20, 20, 16, 20));
        }
        fanDerecha = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 20, 0, 16, 20));
        }
        fanSubiendo = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 20, 40, 16, 20));
        }
        fanBajando = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();


        fanDeEspaldas = new TextureRegion(getTexture(), 20, 0, 16, 20);
        fanParadoDerecha = new TextureRegion(getTexture(), 20, 20, 16, 20);


        defineCharacter();
        fanParado = new TextureRegion(getTexture(), 20, 40, 16, 20);

        setBounds(20, 40, 16 / Bam.PPM, 20 / Bam.PPM);
        setRegion(fanParado);

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;

        switch (currentState) {
            case DERECHA:
                region = fanDerecha.getKeyFrame(stateTimer, true);
                fueDerecha = true;
                subio = false;
                break;
            case SUBIENDO:
                region = fanSubiendo.getKeyFrame(stateTimer, true);
                subio = true;
                fueDerecha = false;
                break;


            case BAJANDO:
                region = fanBajando.getKeyFrame(stateTimer, true);
                subio = false;
                fueDerecha = false;
                break;

            default:
                if (subio)
                    region = fanDeEspaldas;
                else if (fueDerecha)
                    region = fanParadoDerecha;
                else
                    region = fanParado;
                break;


        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;

        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0)
            return State.SUBIENDO;
        else if (b2body.getLinearVelocity().y < 0)
            return State.BAJANDO;
        else if (b2body.getLinearVelocity().x != 0)
            return State.DERECHA;
        else return State.PARADO;

    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Bam.PPM, 32 / Bam.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Bam.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
