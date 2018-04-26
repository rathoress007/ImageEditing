package nnk.com.imageproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;


public class TextActivity extends AppCompatActivity {

    private Button saveButton, colorButton, fontButton;
    private EditText editText;
    private String text;
    private int currentColor;
    public  static Typeface current;
    public static float count = 31;
    SeekBar seekBar;
    String name;
  //  Typeface font[] = new Typeface[15];
    private String style[] = {"DEFAULT","DEFAULT-BOLD","ITALIC","MONOSPACE","ITALIC-BOLD","MINICRAFT","INSOMNIA","ATMOSTPHERE"
  ,"MELATI","ROCK","PATTERN"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_text);
        saveButton = (Button) findViewById(R.id.corner_button);
        colorButton = (Button) findViewById(R.id.color_button);
        fontButton = (Button) findViewById(R.id.font_button);
        editText = (EditText) findViewById(R.id.edit_text);
        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        currentColor = ContextCompat.getColor(this, R.color.colorAccent);
        count = editText.getTextSize();
        editText.setTextSize(count);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                if (progressValue <= 31) {
                    editText.setTextSize(31);
                } else {
                    count = progressValue;
                    editText.setTextSize(count);
                    //    Toast.makeText(getApplicationContext(), "Changing seekBar's progress", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
              //  Toast.makeText(getApplicationContext(), "Started tracking seekBar", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //editText.setText("Covered: " + progress + "/" + seekBar.getMax());


            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text =  editText.getText().toString().trim();
                if(text.isEmpty())
                {
                    Toast.makeText(TextActivity.this, "Please input text here", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(TextActivity.this,ImageActivity.class);
                    intent.putExtra("text",text);
                    intent.putExtra("color",currentColor);
                    startActivity(intent);
                }
            }
        });

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(true);
            }
        });


        fontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             AlertDialog dialog =  new AlertDialog.Builder(TextActivity.this)
                       .setTitle("Select your Font Style")
                       .setCancelable(false)
                     .setSingleChoiceItems(style,0, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             if(which == 0)
                             {
                                 editText.setTypeface(Typeface.DEFAULT);
                                 current = editText.getTypeface();
                             }
                             else if(which == 1)
                             {
                                 editText.setTypeface(Typeface.DEFAULT_BOLD);
                                 current = editText.getTypeface();
                             }
                             else if(which == 2)
                             {
                                 editText.setTypeface(null,Typeface.ITALIC);
                                 current = editText.getTypeface();
                             }
                             else if(which == 3)
                             {
                                 editText.setTypeface(Typeface.MONOSPACE);
                                 current = editText.getTypeface();
                             }
                             else if(which == 4)
                             {
                                 editText.setTypeface(null,Typeface.BOLD_ITALIC);
                                 current = editText.getTypeface();
                             }
                             else if(which == 5)
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Fonty.ttf");

                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }
                             else if(which == 6)
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Android Insomnia Regular.ttf");
                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }
                             else if(which == 7)
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/atmostsphere.ttf");
                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }
                             else if(which == 8)
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/bunga melati putih.ttf");
                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }
                             else if(which == 9)
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/post rock.ttf");
                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }
                             else
                             {
                                 Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/CROCHET PATTERN.ttf");
                                 editText.setTypeface(custom_font);
                                 current = editText.getTypeface();
                             }

                         }
                     })
                     .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }).create();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
                dialog.show();
              //  Toast.makeText(TextActivity.this, "font button is clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, currentColor, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                text = editText.getText().toString().trim();
                editText.setTextColor(currentColor);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
              //  Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
