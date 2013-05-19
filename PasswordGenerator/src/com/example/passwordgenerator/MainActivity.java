package com.example.passwordgenerator;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import java.util.Formatter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    private static final int _ReqCreateLockPattern = 0;
    
    private Button gen_button;
    private Button out_button;
    private EditText passwrd_container;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		gen_button = (Button) findViewById(R.id.gen_button);
		out_button = (Button) findViewById(R.id.out_button);
		passwrd_container = (EditText) findViewById(R.id.passwrd_container);
		
		gen_button.setOnClickListener(gen_buttonOnClickListener);
		out_button.setOnClickListener(out_buttonOnClickListener);
	}

    private final View.OnClickListener gen_buttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(LockPatternActivity._ActionCreatePattern,
                    null, MainActivity.this, LockPatternActivity.class);
            i.putExtra(LockPatternActivity._Theme,
            		R.style.Alp_Theme_Dialog_Dark);
            i.putExtra(LockPatternActivity._StealthMode,
                    false);
            i.putExtra(LockPatternActivity._EncrypterClass, LPEncrypter.class);
            i.putExtra(LockPatternActivity._AutoSave, true);
            i.putExtra(LockPatternActivity._MinWiredDots,
                    1);
            startActivityForResult(i, _ReqCreateLockPattern);
        }// onClick()
    };	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK)
        	passwrd_container.setText(data.getStringExtra(LockPatternActivity._Pattern));
	}
    
    private final View.OnClickListener out_buttonOnClickListener = new View.OnClickListener() {
 
    	@Override
        public void onClick(View v) {
    		String Password = encryptPassword(passwrd_container.getText().toString());
    		setTitle("Password: "+Password);
    		passwrd_container.setText(Password);
        }
    };
    
    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
	
}
