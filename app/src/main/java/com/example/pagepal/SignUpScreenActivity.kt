package com.example.pagepal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pagepal.databinding.ActivitySignupscreen3Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupscreen3Binding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupscreen3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup3.setOnClickListener {
            val name = binding.EditTxtUsername.text.toString()
            val email = binding.EditTxtEmail.text.toString()
            val pass = binding.EditTxtPass.text.toString()
            val cpass = binding.EditTxtCPass.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty()) {
                if (pass == cpass){
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val user = firebaseAuth.currentUser
                            val URef = FirebaseDatabase.getInstance().getReference("users")
                            URef.child(user?.uid?:"").child("name").setValue(name)
                            URef.child(user?.uid?:"").child("email").setValue(email)

                            user?.sendEmailVerification()?.addOnCompleteListener{
                                Toast.makeText(this, "Please Verify your Email.", Toast.LENGTH_SHORT).show()
                            }?.addOnFailureListener{
                                Toast.makeText( this, task.toString(), Toast.LENGTH_SHORT).show()

                            }
                                } else {
                                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                                }




                            }
                    } else {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                    }
             }else{
                Toast.makeText(this, "Please fill in the blanks.", Toast.LENGTH_SHORT).show()
             }
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser!!.isEmailVerified) {
            // If the user is logged in and their email is verified, redirect to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optionally, you may want to finish the SignUpScreenActivity to prevent going back to it with the back button.
        } else {
            // If the user is not logged in or their email is not verified, they stay on SignUpScreenActivity.
            // You may also show a message or UI indication here if needed.
        }

    }

}
