package mcteam08.assign.task01final;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button zero;
    private Button add;
    private Button sub;
    private Button mul;
    private Button div;
    private Button equ;
    private Button dot;
    private Button ac;
    private Button del;
    private TextView control;
    private TextView display;
    private final char ADDITION = '+';
    private final char SUBTRACTION = '-';
    private final char MULTIPLICATION = '*';
    private final char DIVISION = '/';
    private final char EQU = 0;
    private double val1 = Double.NaN;
    private double val2;
    private char ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + "9");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                control.setText(control.getText().toString() + "0");
            }
        });
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.setText(control.getText().toString() + ".");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                compute();
                ACTION = ADDITION;
                display.setText(String.valueOf(val1) + "+");
                control.setText(null);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                compute();
                ACTION = SUBTRACTION;
                display.setText(String.valueOf(val1) + "-");
                control.setText(null);
            }
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                compute();
                ACTION = MULTIPLICATION;
                display.setText(String.valueOf(val1) + "*");
                control.setText(null);
            }
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                compute();
                ACTION = DIVISION;
                display.setText(String.valueOf(val1) + "/");
                control.setText(null);
            }
        });
        equ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                compute();
                ACTION = EQU;
                display.setText(display.getText().toString() + String.valueOf(val2) + "=" + String.valueOf(val1)); //the answer is stored in val1
                control.setText(null);
            }
        });
        ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                val1 = Double.NaN;
                val2 = Double.NaN;
                control.setText(null);
                display.setText(null);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                if(control.getText().length() > 0) {
                    CharSequence name = control.getText().toString();
                    control.setText(name.subSequence(0, name.length()-1));
                }
            }
        });
    }

    private void setupUIViews() {
        one = (Button)findViewById(R.id.button1);
        two = (Button)findViewById(R.id.button2);
        three = (Button)findViewById(R.id.button3);
        four = (Button)findViewById(R.id.button4);
        five = (Button)findViewById(R.id.button5);
        six = (Button)findViewById(R.id.button6);
        seven = (Button)findViewById(R.id.button7);
        eight = (Button)findViewById(R.id.button8);
        nine = (Button)findViewById(R.id.button9);
        zero = (Button)findViewById(R.id.button0);
        add = (Button)findViewById(R.id.buttonAdd);
        sub = (Button)findViewById(R.id.buttonSub);
        mul = (Button)findViewById(R.id.buttonMul);
        div = (Button)findViewById(R.id.buttonDiv);
        equ = (Button)findViewById(R.id.buttonEqu);
        dot = (Button)findViewById(R.id.buttonDot);
        ac = (Button)findViewById(R.id.buttonAC);
        del = (Button)findViewById(R.id.buttonDel);
        control = (TextView) findViewById(R.id.tvControl);
        display = (TextView) findViewById(R.id.tvDisplay);
    }

    private void compute() {
        if(!Double.isNaN(val1)) {
            //value 1 is already a number, get value 2 from control
            val2 = Double.parseDouble(control.getText().toString());

            //do the corresponding operation
            switch(ACTION) {
                case ADDITION:
                    val1 = val1 + val2;
                    break;
                case SUBTRACTION:
                    val1 = val1 - val2;
                    break;
                case MULTIPLICATION:
                    val1 = val1 * val2;
                    break;
                case DIVISION:
                    val1 = val1 / val2;
                    break;
                case EQU:
                    break;
            }
        }
        else {
            //value 1 is still not a number, get value 1 from control
            val1 = Double.parseDouble(control.getText().toString());
        }
    }
}
