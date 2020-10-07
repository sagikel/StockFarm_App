package com.example.stockfarm_app.ui.tutorial;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stockfarm_app.R;
import com.example.stockfarm_app.data.TutorialData;

import java.util.ArrayList;
import java.util.Collections;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class TutorialFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final long TIME_COUNTDOWN = 30050;

    Button buttonBack;
    ListView listView;
    String[] options;
    TutorialData tutorialData;
    ScrollView scrollView;
    TextView def;
    TextView defText;
    Button test;
    String defName;
    boolean windowOpen;
    TextView countDown;
    CountDownTimer countDownTimer;
    boolean timerRunning;
    long timeLeft = TIME_COUNTDOWN;
    TextView headline;
    TextView question;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    KonfettiView konfettiView;
    TextView filter;
    ArrayAdapter<String> arrayAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        buttonBack = view.findViewById(R.id.button_back);
        listView = view.findViewById(R.id.list_view);
        scrollView = view.findViewById(R.id.scroll_view_window);
        def = view.findViewById(R.id.definition);
        defText = view.findViewById(R.id.definition_text);
        test = view.findViewById(R.id.test_button);
        countDown = view.findViewById(R.id.countdown);
        headline = view.findViewById(R.id.title);
        question = view.findViewById(R.id.question);
        buttonA = view.findViewById(R.id.buttonA);
        buttonB = view.findViewById(R.id.buttonB);
        buttonC = view.findViewById(R.id.buttonC);
        buttonD = view.findViewById(R.id.buttonD);
        konfettiView = view.findViewById(R.id.viewKonfetti);
        filter = view.findViewById(R.id.editTextFilter);

        tutorialData = new TutorialData();
        options = new String[tutorialData.getMap().size()];
        defName = "";
        windowOpen = false;
        timerRunning = false;
        int counter = 0;
        ArrayList<String> d = new ArrayList<>(tutorialData.getMap().keySet());
        Collections.sort(d); // אפשר למנוע את זה אם אסדר את המידע באופן אלפבתי בmap
        for (String string : d) {

            options[counter++] = string;
        }

        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, options);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(this);

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {(TutorialFragment.this).arrayAdapter.getFilter().filter(s);}
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windowOpen) {
                   closeWindow();
                }
                else {
                    stopTimer();
                    countDown.setVisibility(View.INVISIBLE);
                    buttonBack.setVisibility(View.INVISIBLE);
                    timeOut();
                    listView.setVisibility(View.VISIBLE);
                    filter.setVisibility(View.VISIBLE);
                    countDown.setTextColor(Color.BLACK);
                }
            }
        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
                buttonBack.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                filter.setVisibility(View.INVISIBLE);
                countDown.setVisibility(View.VISIBLE);
                startTimer();
            }
        });
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("A");
                }
            }
        });
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("B");
                }
            }
        });
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("C");
                }
            }
        });
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                    checkChoice("D");
                }
            }
        });
        return view;
    }

    private void checkChoice(String answer) {
        switch (answer) {
            case "A":
                feedback("A");
                buttonB.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "B":
                feedback("B");
                buttonA.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "C":
                feedback("C");
                buttonB.setVisibility(View.INVISIBLE);
                buttonA.setVisibility(View.INVISIBLE);
                buttonD.setVisibility(View.INVISIBLE);
                break;
            case "D":
                feedback("D");
                buttonB.setVisibility(View.INVISIBLE);
                buttonC.setVisibility(View.INVISIBLE);
                buttonA.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void feedback(String string) {
        String rightAnswer = tutorialData.getMap().get(defName).get(6);
        if (rightAnswer.equals(string)) {
            question.setText("Correct!");
            konfettiView.build()
                    .addColors(0xFF0C6325, Color.BLACK, Color.GRAY)
                    .setDirection(0.0, 359.0)
                    .setSpeed(0f, 6f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(5000L)
                    .addShapes(Shape.RECT,Shape.CIRCLE)
                    .addSizes(new Size(8, 20))
                    .setPosition(konfettiView.getX()+konfettiView.getWidth()/2f,
                            konfettiView.getY()+konfettiView.getHeight()/2f)
                    .burst(5000);

        } else {
            question.setText("Wrong..\nTry Again");
        }
    }

    private void timeOut() {
        headline.setVisibility(View.INVISIBLE);
        question.setVisibility(View.INVISIBLE);
        buttonA.setVisibility(View.INVISIBLE);
        buttonB.setVisibility(View.INVISIBLE);
        buttonC.setVisibility(View.INVISIBLE);
        buttonD.setVisibility(View.INVISIBLE);
    }


    private void startTimer() {
        headline.setText(defName);
        question.setText(tutorialData.getMap().get(defName).get(1));
        buttonA.setText(tutorialData.getMap().get(defName).get(2));
        buttonB.setText(tutorialData.getMap().get(defName).get(3));
        buttonC.setText(tutorialData.getMap().get(defName).get(4));
        buttonD.setText(tutorialData.getMap().get(defName).get(5));

        headline.setVisibility(View.VISIBLE);
        question.setVisibility(View.VISIBLE);
        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);

        timerRunning = true;
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                int sec = (int) (timeLeft/1000) % 60;
                countDown.setText(String.valueOf(sec));
                if (sec == 5) {
                    countDown.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                countDown.setText("Time out!");
                timerRunning = false;
                timeOut();
            }
        }.start();
    }
    private void stopTimer() {
        countDownTimer.cancel();
        timeLeft = TIME_COUNTDOWN;
        timerRunning = false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (windowOpen) {
            closeWindow();
        } else {
            closeKeyboard();
            openWindow(String.valueOf(parent.getItemAtPosition(position)));
        }
        Log.d("list - item clicked", String.valueOf(parent.getItemAtPosition(position)));
    }

    private void openWindow(String definition) {
        windowOpen = true;
        defName = definition;
        def.setText(defName);
        defText.setText(tutorialData.getMap().get(defName).get(0));

        scrollView.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);
    }

    private void closeWindow() {
        scrollView.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);
        windowOpen = false;
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}


/**
 * konfettiView.build()
 *                     .addColors(0xFF0C6325, Color.BLACK, Color.WHITE)
 *                     .setDirection(0.0, 359.0)
 *                     .setSpeed(0f, 6f)
 *                     .setFadeOutEnabled(true)
 *                     .setTimeToLive(5000L)
 *                     .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
 *                     .addSizes(new Size(8,20f))
 *                     .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
 *                     .burst(5000);
 */
