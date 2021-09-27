package com.sumit1334.textdrawable;

import android.content.Context;
import android.graphics.*;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;

import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;

import java.io.*;
import java.io.File;

/**
 * @author Sumit Kumar
 * @created at 26/09/2021 Sunday
 * <p>
 * Extension is created by Sumit kumar for MIT AI2 and its distros like kodular
 */

public class TextDrawable extends AndroidNonvisibleComponent implements Component {
    private final Context context;
    private final String TAG = "Text Drawable";
    private String saveAs;
    private String typeface;
    private int fontSize;
    private int textColor;
    private int backgroundColor;
    private boolean fontBold = false;
    private int radius;
    private int strokeColor;
    private int strokeWidth;

    public TextDrawable(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.SaveAs("Image.png");
        this.Typeface("None");
        this.FontSize(20);
        this.TextColor(-1);
        this.BackgroundColor(COLOR_CYAN);
        this.CornerRadius(0);
        this.StrokeColor(-1);
        this.StrokeWidth(0);
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = DEFAULT_VALUE_COLOR_CYAN)
    public void BackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @SimpleProperty
    public int BackgroundColor() {
        return backgroundColor;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "0")
    public void CornerRadius(int radius) {
        this.radius = radius;
    }

    @SimpleProperty
    public int CornerRadius() {
        return radius;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, defaultValue = "false")
    public void FontBold(boolean bold) {
        this.fontBold = bold;
    }

    @SimpleProperty
    public boolean FontBold() {
        return fontBold;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "20")
    public void FontSize(int size) {
        this.fontSize = size;
    }

    @SimpleProperty
    public int FontSize() {
        return fontSize;
    }

    @SimpleProperty
    @DesignerProperty(defaultValue = "Image.png")
    public void SaveAs(String path) {
        this.saveAs = path;
    }

    @SimpleProperty
    public String SaveAs() {
        return saveAs;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = DEFAULT_VALUE_COLOR_WHITE)
    public void StrokeColor(int color) {
        this.strokeColor = color;
    }

    @SimpleProperty
    public int StrokeColor() {
        return strokeColor;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_NON_NEGATIVE_INTEGER, defaultValue = "2")
    public void StrokeWidth(int width) {
        this.strokeWidth = width;
    }

    @SimpleProperty
    public int StrokeWidth() {
        return strokeWidth;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_COLOR, defaultValue = DEFAULT_VALUE_COLOR_WHITE)
    public void TextColor(int color) {
        this.textColor = color;
    }

    @SimpleProperty
    public int TextColor() {
        return textColor;
    }

    @SimpleProperty
    @DesignerProperty(editorType = PropertyTypeConstants.PROPERTY_TYPE_ASSET)
    public void Typeface(String typeface) {
        this.typeface = typeface;
    }

    @SimpleProperty
    public String Typeface() {
        return typeface;
    }

    @SimpleEvent
    public void Created(String path) {
        EventDispatcher.dispatchEvent(this, "Created", path);
    }

    @SimpleFunction
    public void Create(final String text, final int height, final int width) {
        if (text.isEmpty())
            throw new IllegalArgumentException("Text can not be empty");
        else {
            try {
                this.createBackground(text, height, width);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createBackground(String text, int height, int width) {
        final Paint paint = new Paint() {
            {
                setColor(textColor);
                setTextAlign(Align.CENTER);
                setTextSize(px2dp(fontSize));
                setAntiAlias(true);
                setTypeface(getCustomFont(typeface));
                setFakeBoldText(fontBold);
                setStrokeWidth(px2dp(strokeWidth));
            }
        };
        final Bitmap bitmap = Bitmap.createBitmap(px2dp(width), px2dp(height), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        final Paint background = new Paint() {
            {
                setColor(backgroundColor);
                setStyle(Style.FILL);
            }
        };
        final Paint borderPaint = new Paint() {
            {
                setColor(getDarkerShade(strokeColor));
                setStyle(Style.STROKE);
                setStrokeWidth(px2dp(strokeWidth));
            }
        };
        canvas.drawARGB(0, 0, 0, 0);
        final RectF rectF = new RectF(canvas.getClipBounds());
        int radius = px2dp(this.radius);
        canvas.drawRoundRect(rectF, radius, radius, background);
        canvas.drawRoundRect(rectF, radius, radius, borderPaint);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
        canvas.drawText(text, xPos, yPos, paint);
        this.saveImage(this.roundBitmap(bitmap));
    }

    private Bitmap roundBitmap(Bitmap bitmap) {
        int radius = px2dp(this.radius);
        int stroke = px2dp(this.strokeWidth);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth() + stroke, bitmap.getHeight() + stroke, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private void saveImage(Bitmap bitmap) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final File image = new File(Environment.getExternalStorageDirectory() + "/" + saveAs);
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(image);
            fileOutputStream.write(stream.toByteArray());
            Log.d(TAG, "makeImage: Image PNG has saved to " + Environment.getExternalStorageDirectory() + "/" + saveAs + " path");
            this.Created(Environment.getExternalStorageDirectory() + "/" + saveAs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed: " + e.toString());
        } catch (IOException e2) {
            e2.printStackTrace();
            Log.d(TAG, "Failed: " + e2.toString());
        }
    }

    public int px2dp(int px) {
        return (int) (context.getResources().getDisplayMetrics().density * px);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int) (0.9f * Color.red(color)),
                (int) (0.9f * Color.green(color)),
                (int) (0.9f * Color.blue(color)));
    }

    private Typeface getCustomFont(String font) {
        try {
            if (font.equalsIgnoreCase("none"))
                return null;
            else {
                if (Form.getActiveForm() instanceof ReplForm) {
                    String path = Environment.getExternalStorageDirectory().getPath();
                    if (context.getPackageName().contains("makeroid"))
                        path = path + "/Makeroid/assets/";
                    else
                        path = path + "/AppInventor/assets/";
                    return Typeface.createFromFile(path + font);

                } else
                    return Typeface.createFromAsset(context.getAssets(), font);
            }
        } catch (Exception e) {
            Log.d(TAG, "getCustomFont: " + e.getMessage());
            return null;
        }
    }
}
