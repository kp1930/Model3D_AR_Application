package com.theblackdiamonds.model3d;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);
        assert arFragment != null;
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) ->
                placeModel(hitResult.createAnchor())
        ));
    }

    private void placeModel(Anchor anchor) {

        //Add path of your url via xampp or etc.
        String ASSET_3D = "";
        ModelRenderable.builder()
                .setSource(MainActivity.this,
                        RenderableSource.builder().setSource(MainActivity.this, Uri.parse(ASSET_3D)
                                , RenderableSource.SourceType.GLTF2).setScale(0.75f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT).build()
                ).setRegistryId(ASSET_3D).build().thenAccept(modelRenderable -> addNodeToScene(modelRenderable, anchor))
                .exceptionally(throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(throwable.getMessage()).show();
                    return null;
                });

    }

    private void addNodeToScene(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
    }
}