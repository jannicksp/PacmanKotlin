package org.pondar.pacmankotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import java.util.ArrayList
import android.util.Log
import android.widget.Toast
import java.lang.StrictMath.pow
import java.util.*
import kotlin.math.sqrt

/**
 *
 * This class should contain all your game logic
 */

class Game(private var context: Context,view: TextView) {

        private var pointsView: TextView = view
        private var points : Int = 0

        //bitmap of the pacman
        var pacBitmap: Bitmap
        var coinBitmap: Bitmap
        var enemyBitmap: Bitmap
        var pacx: Int = 0
        var pacy: Int = 0
        var enemyx: Int = 400
        var enemyy: Int = 0
        var enemyAlive = true
        var direction = 0
        var directionEnemy = 1
        var counter: Int = 0
        var timer: Int = 30
        var running: Boolean = false
        //did we initialize the coins?
        var coinsInitialized = false
        var enemiesInitialized = false

        //the list of goldcoins - initially empty
        var coins = ArrayList<GoldCoin>()
        var enemies = ArrayList<Enemy>()
        //a reference to the gameview
        private var gameView: GameView? = null
        private var h: Int = 0
        private var w: Int = 0 //height and width of screen


    //The init code is called when we create a new Game class.
    //it's a good place to initialize our images.
    init {
        pacBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pacman)
        coinBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin)
        enemyBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
    }

    fun setGameView(view: GameView) {
        this.gameView = view
    }
    //TODO initialize enemies also here
    fun initializeEnemy() {
        enemies.add(Enemy(false, true, 900, 900))
        enemiesInitialized = true
    }

    //TODO initialize goldcoins also here
    fun initializeGoldcoins()
    {
        var minX: Int = 0
        var maxX: Int = w - coinBitmap.width
        var minY: Int = 0
        var maxY: Int = h - coinBitmap.width
        val random = Random()

        for (i in 0..9) {
            var randomX: Int = random.nextInt(maxX - minX + 1) + minX
            var randomY: Int = random.nextInt(maxY - minY + 1) + minY
            coins.add(GoldCoin(randomX, randomY, false))
        }
        coinsInitialized = true
    }


    fun newGame() {
        pacx = 50
        pacy = 400 //just some starting coordinates - you can change this.
        timer = 30
        counter = 0
        running = false
        coins = ArrayList<GoldCoin>()
        coinsInitialized = false
        points = 0
        pointsView.text = "${context.resources.getString(R.string.points)} $points"
        gameView?.invalidate() //redraw screen
    }
    fun gameOver() {
        if (timer == 1) {
            timer = 0
            running = false
            counter = 0
            Toast.makeText(context, "Time is up, you loose", Toast.LENGTH_LONG).show()
        }
    }

    fun setSize(h: Int, w: Int) {
        this.h = h
        this.w = w
    }

    fun movePacmanRight(pixels: Int) {
        //still within our boundaries?
        if (pacx + pixels + pacBitmap.width < w) {
            pacx = pacx + pixels
            doCollisionCheck()
            direction = 4
            gameView!!.invalidate()
        }
    }

    fun movePacmanLeft(pixels: Int) {
        //still within our boundaries?
        if (pacx - pixels > 0) {
            pacx = pacx - pixels
            doCollisionCheck()
            direction = 3
            gameView!!.invalidate()
        }
    }

    fun movePacmanUp(pixels: Int) {
        //still within our boundaries?
        if (pacy - pixels > 0) {
            pacy = pacy - pixels
            doCollisionCheck()
            direction = 1
            gameView!!.invalidate()
        }
    }

    fun movePacmanDown(pixels: Int) {
        //still within our boundaries?
        if (pacy + pixels + pacBitmap.height < h) {
            pacy = pacy + pixels
            doCollisionCheck()
            direction = 2
            gameView!!.invalidate()
        }
    }
    //Move the enemies
    fun moveEnemy(pixels: Int) {
        //still within our boundaries?
        if (directionEnemy == 2) { //direction is down
            if (enemyy + pixels + enemyBitmap.height < h) {
                enemyy = enemyy + pixels
                directionEnemy = 2
            } else {
                directionEnemy = 1
            }

        } else { // direction is up
            if (enemyy - pixels > 0) {
                enemyy = enemyy - pixels
                directionEnemy = 1
            } else {

                directionEnemy = 2
            }
        }
    }

    fun distance(pacx: Int, pacy: Int, golx: Int, goly: Int): Double {

        // calculate distance and return it
        var cordinatation = (sqrt(((pacx - golx) * (pacx - golx) + (pacy - goly) * (pacy - goly)).toDouble()))

        return cordinatation;
    }

    fun distanceEnemy(pacx: Int, pacy: Int, enemyx: Int, enemyy: Int): Double {

        // calculate distance and return it
        var cordinatation = (sqrt(((pacx - enemyx) * (pacx - enemyx) + (pacy - enemyy) * (pacy - enemyy)).toDouble()))

        return cordinatation;
    }



    //TODO check if the pacman touches a gold coin

    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman

    fun doCollisionCheck() {
        for (coin in coins) {
            if((sqrt(pow((coin.coinX - pacx).toDouble(), 2.0)-pow((coin.coinY - pacy).toDouble(), 2.0)) < 100) && coin.taken == false )
            {
                coin.taken = true
                points++
                pointsView.text = "${context.resources.getString(R.string.points)} $points"
            }
            //check if all goldcoins are taken
            if (points === 10) {
                running = false
                Toast.makeText(context, "You won the game", Toast.LENGTH_SHORT).show()
            }
        }
    }

}