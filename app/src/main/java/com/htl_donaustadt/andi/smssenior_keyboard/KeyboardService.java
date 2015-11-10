package com.htl_donaustadt.andi.smssenior_keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

/**
 * Created by Andi on 29.09.2015.
 * Package: com.htl_donaustadt.andi.smssenior_keyboard
 * <p/>
 * DISABLE HARDWARE KEYBOARD IN EMULATOR FOR PROPER WORK OF THE KEYBOARD
 * <p/>
 * Source:  http://code.tutsplus.com/tutorials/create-a-custom-keyboard-on-android--cms-22615
 */

public class KeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener
{
    private KeyboardView keyboardView;
    private Keyboard defaultKeyboard;
    private Keyboard charKeyboard;
    private boolean isCaps = true; //Start the keyboard in Caps-layout
    private boolean isCharKeyboard = false; //Start the keyboard with letter-layout

    @Override
    public View onCreateInputView()
    {
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        defaultKeyboard = new Keyboard(this, R.xml.letter_keyboard);
        charKeyboard = new Keyboard(this, R.xml.specialcharacter_keyboard);
        keyboardView.setKeyboard(defaultKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        keyboardView.setPreviewEnabled(false); //Disable the preview when key is long pressed

        defaultKeyboard.setShifted(true); // Start the keyboard in Caps-layout
        keyboardView.invalidateAllKeys(); // Requests a redraw of the entire keyboard
        return keyboardView;
    }

    @Override
    public void onKey(int primaryKeyCode, int[] ints)
    {
        InputConnection inputConnection = getCurrentInputConnection(); //Retrieve the currently active InputConnection that is bound to the input method
        switch (primaryKeyCode)
        {
            case Keyboard.KEYCODE_DONE: //Enter key pressed
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;

            case Keyboard.KEYCODE_DELETE: // BackSpace key pressed
                inputConnection.deleteSurroundingText(1, 0); //Delete 1 character of text before the current cursor position and 0 after the cursor position
                break;

            case Keyboard.KEYCODE_SHIFT: // Shit key pressed
                if (isCaps)
                {
                    defaultKeyboard.setShifted(false); // If keyboard is already in Caps-layout then return to lower key-layout
                    keyboardView.invalidateAllKeys(); // Requests a redraw of the entire keyboard
                    isCaps = false;
                }
                else
                {
                    isCaps = true;
                    defaultKeyboard.setShifted(true); // if keyboard is in lower key-layout then switch it to Caps-layout
                    keyboardView.invalidateAllKeys(); // Requests a redraw of the entire keyboard
                }
                break;

            default:
                char keyCode = (char) primaryKeyCode;

                if (primaryKeyCode == -99999) //keyCode for switching between letter keyboard layout and character-layout
                {
                    if (!isCharKeyboard) //if keyboard is in letter-layout then switch it to character-layout
                    {
                        isCharKeyboard = true;
                        keyboardView.setKeyboard(charKeyboard);
                    }
                    else //if keyboard is already in character-layout then switch back to letter-layout
                    {
                        isCharKeyboard = false;
                        keyboardView.setKeyboard(defaultKeyboard);
                    }
                }
                else
                {
                    if (Character.isLetter(keyCode) && isCaps) //check if the keyCode is a valid letter and check if the keyboard is in Caps-layout
                    {
                        keyCode = Character.toUpperCase(keyCode); // set keyValue to UpperCase
                    }
                    inputConnection.commitText(String.valueOf(keyCode), 1); //Commit value from keyCode to the text box and set the cursor 1 position to the right

                    if (isCaps) //switch back the keyboard to the lowercase letter-layout
                    {
                        defaultKeyboard.setShifted(false);
                        keyboardView.invalidateAllKeys();
                        isCaps = false;
                    }
                }

        }
    }

    //region  Not implemented abstract methods
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
    //endregion
}
