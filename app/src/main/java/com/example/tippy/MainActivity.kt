package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.LogPrinter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlin.math.log
import kotlin.math.round

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        sbTipPercent.progress = INITIAL_TIP_PERCENT
        tbPercent.text = "$INITIAL_TIP_PERCENT%"
        UpdateDescription(INITIAL_TIP_PERCENT)


        sbTipPercent.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tbPercent.text = "$progress%"
                calculateTipandTotal()
                UpdateDescription(progress)
                SplitExpense()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })



        etBasevalue.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                calculateTipandTotal()
                SplitExpense()

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        etMembers.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG,"afterTextChanged $p0")
                SplitExpense()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })



    }

    private fun UpdateDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }

        etDescription.text = tipDescription

        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / sbTipPercent.max,
            ContextCompat.getColor(this,R.color.Worst_tip),
            ContextCompat.getColor(this,R.color.Best_tip)
        ) as Int

        etDescription.setTextColor(color)
    }

    private fun SplitExpense(){
        if(etMembers.text.isEmpty() or etBasevalue.text.isEmpty()){
            etEach.text = ""
            return
        }
        val total = etTotal.text.toString().toDouble()
        val member = round(etMembers.text.toString().toDouble())


        val split = total / member
        etEach.text = "%.2f".format(split)
    }

    private fun calculateTipandTotal(){
        if (etBasevalue.text.isEmpty()){
            etTipAmout.text = ""
            etTotal.text = ""
            return

        }

        val baseAmount = etBasevalue.text.toString().toDouble()
        val tipPercent = sbTipPercent.progress


        val tipAmount = round(( tipPercent * baseAmount ) / 100)
        val totalAmount = round(baseAmount + tipAmount)


        etTipAmout.text = "%.2f".format(tipAmount)
        etTotal.text = "%.2f".format(totalAmount)
    }


}


