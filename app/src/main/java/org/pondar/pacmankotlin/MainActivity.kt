package org.pondar.pacmankotlin

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.util.Log
import android.view.View.OnClickListener
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var myTimer: Timer = Timer()
    private var countDown: Timer = Timer()

    //reference to the game class.
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //makes sure it always runs in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        game = Game(this,pointsView)

        //intialize the game view clas and game class
        game?.setGameView(gameView)
        gameView.setGame(game)
        game?.newGame()

        moveRight.setOnClickListener {
            game?.movePacmanRight(0)
        }
        moveLeft.setOnClickListener {
            game?.movePacmanLeft(0)
        }
        moveUp.setOnClickListener {
            game?.movePacmanUp(0)
        }
        moveDown.setOnClickListener {
            game?.movePacmanDown(0)
        }
        pause.setOnClickListener {
            game!!.running = false
        }
        start.setOnClickListener {
            game!!.running = true
        }
        reset.setOnClickListener {
            game!!.newGame() //calling the newGame method
            timer.text = getString(R.string.timer, game!!.counter)
            timerLeft.text = getString(R.string.timeLeft, game!!.counter)
        }

        game!!.running = false //should game be running?

        fun timerMethod() {
            this.runOnUiThread(timerTick);
        }

        //we call the timer 5 times each second - pacman
        myTimer.schedule(
                object : TimerTask() {
                    override fun run() {
                        timerMethod()
                    }
                }, 0, 50)

        //0 indicates we start now,
        //200 is the number of miliseconds between each call


        fun timerMethodCountDown() {
            this.runOnUiThread(timerTickCountDown);
        }

        //we call the timer 5 times each second - time
        countDown.schedule(
                object : TimerTask() {
                    override fun run() {
                        timerMethodCountDown()
                    }
                }, 0, 1000)

        //0 indicates we start now,
        //200 is the number of miliseconds between each call
    }

    override fun onStop() {
        super.onStop()
        myTimer.cancel()
    }

    private val timerTickCountDown = Runnable {
        if (game!!.running) {
            //just to make sure if the app is killed, that we stop the timer.
            game!!.timer--
            timerLeft.text = getString(R.string.timeLeft, game!!.timer)
            game!!.gameOver()
        }
    }
    private val timerTick = Runnable {
        //This method runs in the same thread as the UI.
        // so we can draw
        if (game!!.running) {
            game!!.counter++

            game!!.moveEnemy(10) // move the enemy
            gameView!!.invalidate()

            //update the counter - notice this is NOT seconds in this example
            //you need TWO counters - one for the timer count down that will
            // run every second and one for the pacman which need to run
            //faster than every second
            timer.text = getString(R.string.timer, game!!.counter)
            //moving pacman
            if (game!!.direction == 1) {
                game?.movePacmanUp(50)
            } else if (game!!.direction == 2) {
                game?.movePacmanDown(50)
            } else if (game!!.direction == 3) {
                game?.movePacmanLeft(50)
            } else if (game!!.direction == 4) {
                game?.movePacmanRight(50)
            } else if (game!!.timer < 1 && game!!.running == true) {
                game?.gameOver()

            }
        }


}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            Toast.makeText(this, "settings clicked", Toast.LENGTH_LONG).show()
            return true
        } else if (id == R.id.action_newGame) {
            Toast.makeText(this, "New Game clicked", Toast.LENGTH_LONG).show()
            game?.newGame()
            timer.text = getString(R.string.timer, game!!.counter)
            timerLeft.text = getString(R.string.timeLeft, game!!.counter)

            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
