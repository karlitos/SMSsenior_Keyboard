package com.htl_donaustadt.andi.smssenior_keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.view.inputmethod.InputConnection;

/**
 * Created by Andi on 29.09.2015.
 * Project: SMSsenior_Keyboard
 * Package: com.htl_donaustadt.andi.smssenior_keyboard
 * <p/>
 * HARDWARE KEYBOARD IM EMULATOR DEAKTIVIEREN DAMIT DIE TASTATUR BEIM KLICK IN EIN TEXTFELD ERSCHEINT
 * <p/>
 * Quelle:  http://code.tutsplus.com/tutorials/create-a-custom-keyboard-on-android--cms-22615
 */

public class KeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener
{
    private KeyboardView keyboardView;
    private Keyboard defaultKeyboard;
    private Keyboard charKeyboard;
    private Keyboard recommendationKeyboard;
    private boolean isCaps = false;
    private boolean isCharKeyboard = false;

    @Override
    public View onCreateInputView()
    {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        defaultKeyboard = new Keyboard(this, R.xml.defaultkeyboard);
        charKeyboard = new Keyboard(this, R.xml.charkeyboard);
        recommendationKeyboard = new Keyboard(this, R.xml.recommendationkeyboard);
        keyboardView.setKeyboard(defaultKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setPreviewEnabled(false);
        return keyboardView;
    }

    @Override
    public void onKey(int i, int[] ints)
    {
        InputConnection inputConnection = getCurrentInputConnection();
        switch (i)
        {
            case Keyboard.KEYCODE_DELETE:
                inputConnection.deleteSurroundingText(1, 0);
                break;

            case Keyboard.KEYCODE_SHIFT:
                if (isCaps)
                {
                    defaultKeyboard.setShifted(false);
                    keyboardView.invalidateAllKeys();
                    isCaps = false;
                }
                else
                {
                    isCaps = true;
                    defaultKeyboard.setShifted(true);
                    keyboardView.invalidateAllKeys();
                }
                break;

            default:
                char code = (char) i;
                switch (i)
                {
                    case -99999:
                        if (!isCharKeyboard)
                        {
                            isCharKeyboard = true;
                            keyboardView.setKeyboard(charKeyboard);
                        }
                        else
                        {
                            isCharKeyboard = false;
                            keyboardView.setKeyboard(defaultKeyboard);
                        }
                        break;
                    case -88888:
                        keyboardView.setKeyboard(recommendationKeyboard);
                        break;
                    case -77777:
                        inputConnection.commitText("Das ist ein Vorschlag", 1);
                        keyboardView.setKeyboard(defaultKeyboard);
                        break;
                    case -66666:
                        keyboardView.setKeyboard(defaultKeyboard);
                        break;

                    default:
                        if (Character.isLetter(code) && isCaps)
                        {
                            code = Character.toUpperCase(code);
                        }
                        inputConnection.commitText(String.valueOf(code), 1);
                        if (isCaps)
                        {
                            defaultKeyboard.setShifted(false);
                            keyboardView.invalidateAllKeys();
                            isCaps = false;
                        }
                }
        }
    }

    @Override
    public void onPress(int i)
    {

    }

    @Override
    public void onRelease(int i)
    {

    }

    @Override
    public void onText(CharSequence charSequence)
    {

    }

    @Override
    public void swipeLeft()
    {

    }

    @Override
    public void swipeRight()
    {

    }

    @Override
    public void swipeDown()
    {

    }

    @Override
    public void swipeUp()
    {

    }
}
