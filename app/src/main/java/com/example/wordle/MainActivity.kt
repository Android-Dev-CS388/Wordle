package com.example.wordle

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    var counter = 1
    var guessStreak = 0
    var wordToGuess = FourLetterWordList.getRandomFourLetterWord();

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Streak View
        val streakView = findViewById<TextView>(R.id.streakView)

        //Answer
        val answer = findViewById<TextView>(R.id.answer)

        //Guess Captions
        val guess1 = findViewById<TextView>(R.id.textGuess1)
        val guess1Check = findViewById<TextView>(R.id.textGuessCheck1)
        val guess2 = findViewById<TextView>(R.id.textGuess2)
        val guess2Check = findViewById<TextView>(R.id.textGuessCheck2)
        val guess3 = findViewById<TextView>(R.id.textGuess3)
        val guess3Check = findViewById<TextView>(R.id.textGuessCheck3)

        //answer Field
        val guess1Cross = findViewById<TextView>(R.id.textGuess1Cross)
        val guess1Answer = findViewById<TextView>(R.id.textGuess1Answer)
        val guess2Cross = findViewById<TextView>(R.id.textGuess2Cross)
        val guess2Answer = findViewById<TextView>(R.id.textGuess2Answer)
        val guess3Cross = findViewById<TextView>(R.id.textGuess3Cross)
        val guess3Answer = findViewById<TextView>(R.id.textGuess3Answer)

        val button = findViewById<Button>(R.id.button)
        val resetButton = findViewById<Button>(R.id.resetButton)

        val input = findViewById<EditText>(R.id.editText)


        button.setOnClickListener{
            Log.d("TAG", "${wordToGuess}")
            val value = input.text.toString().uppercase()
            input.setText("")

            if(value.length != 4 || !isLetters(value)){
                Toast.makeText(this, "Invalid Input(Incorrect Input Size or Contains Numbers)", Toast.LENGTH_SHORT).show()
            }
            else if(counter == 1) {
                val correction = checkGuess(value)
                guess1.visibility = View.VISIBLE
                guess1Check.visibility = View.VISIBLE
                guess1Answer.text = value
                guess1Cross.text = correction
                counter++
                if(value == wordToGuess) {
                    Toast.makeText(this, "Congrats, You Won!!!", Toast.LENGTH_SHORT).show()
                    resetButton.visibility = View.VISIBLE
                    button.visibility = View.INVISIBLE
                    answer.text = wordToGuess
                    streakView.visibility = View.VISIBLE
                    guessStreak++
                    streakView.text = "Streak: ${guessStreak}"
                }
            }
            else if(counter == 2){
                val correction = checkGuess(value)
                guess2.visibility = View.VISIBLE
                guess2Check.visibility = View.VISIBLE
                guess2Answer.text = value
                guess2Cross.text = correction
                counter++
                if(value == wordToGuess) {
                    Toast.makeText(it.context, "Congrats, You Won!!!", Toast.LENGTH_SHORT).show()
                    button.visibility = View.INVISIBLE
                    resetButton.visibility = View.VISIBLE
                    answer.text = wordToGuess
                    streakView.visibility = View.VISIBLE
                    guessStreak++
                    streakView.text = "Streak: ${guessStreak}"
                }
            }
            else if(counter == 3){
                val correction = checkGuess(value)
                guess3.visibility = View.VISIBLE
                guess3Check.visibility = View.VISIBLE
                guess3Answer.text = value
                guess3Cross.text = correction
                counter++
                answer.visibility = View.VISIBLE
                answer.text = wordToGuess
                resetButton.visibility = View.VISIBLE
                button.visibility = View.INVISIBLE
                if(value == wordToGuess) {
                    Toast.makeText(it.context, "Congrats You Won!!!", Toast.LENGTH_SHORT).show()
                    answer.text = wordToGuess
                    streakView.visibility = View.VISIBLE
                    guessStreak++
                    streakView.text = "Streak: ${guessStreak}"
                }
                else{
                    Toast.makeText(it.context, "You lost! Press Reset to play again", Toast.LENGTH_SHORT).show()
                    guessStreak = 0
                    streakView.visibility = View.VISIBLE
                    streakView.text = "Streak: ${guessStreak}"
                }
            }

        }
        resetButton.setOnClickListener{
            guess1.visibility = View.INVISIBLE
            guess1Check.visibility = View.INVISIBLE
            guess1Answer.text = ""
            guess1Cross.text = ""
            guess2.visibility = View.INVISIBLE
            guess2Check.visibility = View.INVISIBLE
            guess2Answer.text = ""
            guess2Cross.text = ""
            guess3.visibility = View.INVISIBLE
            guess3Check.visibility = View.INVISIBLE
            guess3Answer.text = ""
            guess3Cross.text = ""
            answer.visibility = View.INVISIBLE
            answer.text = ""
            streakView.visibility = View.INVISIBLE
            streakView.text = ""
            wordToGuess = FourLetterWordList.getRandomFourLetterWord();
            resetButton.visibility = View.INVISIBLE
            button.visibility = View.VISIBLE
            counter = 1
        }

    }

    private fun isLetters(string: String): Boolean {
        for (c in string)
        {
            if (c !in 'A'..'Z' && c !in 'a'..'z') {
                return false
            }
        }
        return true
    }

    private fun checkGuess(guess: String) : SpannableString {
        val spannableString = SpannableString(guess)
        for (i in 0..3) {
            if (guess[i] == wordToGuess[i]) {
                val foregroundSpan = ForegroundColorSpan(Color.GREEN)
                spannableString.setSpan(foregroundSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            else if (guess[i] in wordToGuess) {
                val foregroundSpan = ForegroundColorSpan(Color.RED)
                spannableString.setSpan(foregroundSpan, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            else {
            }
        }
        return spannableString
    }

    //Check Guess using X, + and O
//    private fun checkGuess(guess: String) : String {
//        var result = ""
//        for (i in 0..3) {
//            if (guess[i] == wordToGuess[i]) {
//                result += "O"
//            }
//            else if (guess[i] in wordToGuess) {
//                result += "+"
//            }
//            else {
//                result += "X"
//            }
//        }
//        return result
//    }


}