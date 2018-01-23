package com.danish_suri_bolbox.bolbox;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Danish on 4/24/15.
 */
public class CameraP extends SurfaceView implements SurfaceHolder.Callback
{
    SurfaceHolder mholder;
    Camera mcamera;
    public CameraP(Context context, Camera camera)
    {
        super(context);
        mcamera=camera;
        mholder=getHolder();
        mholder.addCallback(this);
        mholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        Log.d("MyMessage","surface created");
        try
        {
            if(mcamera==null)
            {
                mcamera.setPreviewDisplay(holder);
                Camera.Parameters p = mcamera.getParameters();
                List<String> focusMode=p.getSupportedFocusModes();
                if(focusMode.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                {
                    p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mcamera.setParameters(p);
                mcamera.startPreview();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void refreshCam(Camera camera)
    {
        if(mholder.getSurface()==null)
        {
            Log.d("MyMessage","refreshcam");
            return;
        }
        try
        {
            mcamera.stopPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        setCamera(camera);
        try
        {
            mcamera.setPreviewDisplay(mholder);
            mcamera.startPreview();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        refreshCam(mcamera);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mcamera.release();
    }
    public void setCamera(Camera camera)
    {
        mcamera=camera;
    }
}
