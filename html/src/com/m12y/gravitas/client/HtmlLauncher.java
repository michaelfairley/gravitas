package com.m12y.gravitas.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.m12y.gravitas.Gravitas;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(800, 600);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new Gravitas();
        }
}
