package com.example.oliver

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.oliver.Announcements.ClerkAnnouncementsActivity
import com.example.oliver.FarmerActivity
import com.example.oliver.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signUpTextView = findViewById(R.id.textViewSignUp)

        val loginButton: Button = findViewById(R.id.buttonLogin)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Sign in with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                        // Proceed to the main activity or any other activity after successful login
                        if (email == "clerk@gmail.com") {
                            val intent = Intent(this, ClerkAnnouncementsActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, FarmerActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        signUpTextView.setOnClickListener {
            showCoffeeFarmingDocument()
        }
    }

    private fun showCoffeeFarmingDocument() {
        val documentContent = "Coffee Farming Guide\n\n" +
                "Coffee farming is growing and harvesting coffee beans. Coffee plants are native to tropical regions and are grown in many countries worldwide, including Brazil, Colombia, Ethiopia, and Vietnam. Coffee farming typically involves:\n\n" +
                "•Cultivating the plants.\n" +
                ".Harvesting ripe coffee cherries.\n" +
                ".Processing the beans to remove the outer layer\n" +
                "The beans are then roasted and prepared for consumption.\n\n" +
                "Preparing coffee seedlings\n" +
                "To prepare coffee seedlings, you will need to start by obtaining seeds or seedlings from a reputable source. Before planting, the seeds should be soaked in water for 24 hours to soften the seed coat and encourage germination. \n" +
                "The seedlings should be kept in a warm, moist environment with plenty of light and be watered regularly to keep the soil evenly moist. As the seedlings grow, they should be transplanted into larger pots on the ground. It is important to protect plants from pests and diseases and provide them with the proper nutrients and water to support healthy growth.\n\n" +
                "Transplanting the seedlings to the farm\n" +
                "To transplant coffee seedlings, you must prepare the new planting site by loosening the soil and adding compost or other organic matter to improve the soil structure and fertility. Choose a location that receives plenty of sunlight and has well-drained soil.\n" +
                "When the seedlings are ready, dig a large enough hole to accommodate the seedling’s root system without crowding it. Gently loosen the seedling’s roots and position it in the hole, ensuring that the top of the root ball is level with the surrounding soil. Fill in the hole around the roots, and firm the soil gently to remove any air pockets.\n" +
                "Water the seedling, and continue to water it regularly to keep the soil moist until it becomes established. It is important to protect the seedlings from extreme weather, pests, and diseases and to provide them with the proper care and nutrition to support healthy growth. As the plants mature, they will need regular pruning and maintenance to produce high-quality coffee beans.\n\n" +
                "How to take care of coffee trees\n" +
                "Coffee needs warm temperatures, plenty of sunlight, and well-drained soil.\n" +
                "Coffee trees also require regular watering, especially during the dry season. Adding compost or organic fertilizer to the earth improves its structure and fertility.\n" +
                "In addition to providing proper growing conditions, it is crucial to protect coffee trees from pests and diseases. This can be done through pesticides and other control measures, as well as through proper pruning and maintenance of the trees. Regular pruning promotes healthy growth and increases the yield of coffee beans.\n" +
                "Coffee trees also require regular fertilization to support healthy growth and increase yeilds. This can be done using organic fertilizers or chemical fertilizers, depending on the circumstances.\n\n" +
                "Five common coffee tree diseases\n" +
                "Many diseases affect coffee trees. Below are some of the most common diseases and how to solve them:\n" +
                "1.Coffee rust: This is a fungal disease that attacks the leaves of the coffee tree, causing yellow or orange spots to form on the surface. To control the disease, it is important to prune and remove infected leaves and apply a fungicide.\n" +
                "2.Coffee berry disease: This is a fungal disease that affects the fruit of the coffee tree, causing the berries to turn red and black. To control it, prune and remove infected berries and apply a fungicide./n" +
                "3.Root rot: This is a fungal disease that attacks the roots of the coffee tree, causing them to rot and die. It can be controlled by ensuring proper drainage and applying a suitable fungicide.\n" +
                "4.Leaf blight: This is a bacterial disease that affects the leaves of the coffee tree, causing them to turn yellow and brown. To control this disease, it is important to prune and remove infected leaves and apply a bactericide to the tree.\n" +
                "5.Coffee mosaic virus: This is a viral disease that affects the tree leaves, causing them to develop a mottled or mosaic-like pattern. Coffee mosaics can be controlled by removing and destroying infected plants.\n\n" +
                "Five Common Pests that affect coffee plants/n" +
                "1.Coffee berry borer: This is a small beetle that burrows into the fruit of the coffee tree, causing significant damage to the berries. The beetle can also transmit diseases, which can further harm the trees.\n" +
                "2.Coffee leaf miner: This is a tiny insect that feeds on the leaves of the coffee tree, causing yellow or brown spots to form on the surface. The leaf miner can also transmit diseases, further harming the trees.\n" +
                "3.Coffee mealybug: This is a tiny, sap-sucking insect that feeds on the leaves and stems of the coffee tree. The mealybug can cause the tree to become weakened and less productive.\n" +
                "4.Coffee thrips: This tiny, slender insect feeds on the coffee tree’s leaves and flowers. The thrips can cause the leaves to become distorted and discoloured, reducing the tree’s ability to produce fruit.\n" +
                "5.Coffee mite: This is a tiny, spider-like insect that feeds on the leaves of the coffee tree. The mite can cause the leaves to become yellow and deformed, reducing the tree’s ability to photosynthesize.\n\n" +
                "Things coffee farmers need to avoid when planting coffee\n" +
                "1.Planting in the wrong location: Coffee trees need plenty of sunlight and well-draining soil, so choosing a place that meets these requirements is vital. Avoid planting in areas that are too shady or too wet, as this can lead to poor growth and reduced yields.\n" +
                "2.Planting at the wrong time of year: Coffee trees are sensitive to temperature and rainfall, so choosing the right time of year to grow is crucial. Avoid planting during the rainy season, as this can cause the soil to become waterlogged and can lead to root rot.\n" +
                "3.Using the wrong planting materials: It is essential to use high-quality seeds or seedlings when planting coffee trees, as this will help to ensure robust and healthy growth. Avoid using old or damaged seeds, as these may not germinate or may produce weak plants.\n" +
                "4.Planting the trees too close together: Coffee trees need room to grow and develop, so it is essential to plant them at the proper spacing. Avoid planting the trees too close together, as this can lead to competition for light, water, and nutrients and can reduce the overall yield of the crop.\n" +
                "5.Failing to provide proper care and maintenance: Coffee trees require regular watering, fertilization, pruning, and pest and disease control to grow and produce high-quality coffee beans. \n\n" +
                "Manure, fertilizers and herbicides in coffee growing\n" +
                "Coffee cultivation involves careful management of soil fertility and pest control to ensure optimal plant growth and yield. Manure, fertilizers, and herbicides are commonly used in coffee growing for various reasons:\n" +
                "1.Manure is organic matter that provides essential nutrients to the soil, improving its fertility. It enhances soil structure, water retention, and microbial activity. Farmers apply well-rotted organic manure to coffee plantations to supplement nutrient levels in the soil. This helps in sustaining long-term soil fertility and promoting healthy plant growth.\n" +
                "2.Fertilizers are used to supplement soil nutrients, as coffee plants require a range of essential elements for proper growth and development. Commonly used fertilizers in coffee cultivation include nitrogen (N), phosphorus (P), and potassium (K). Nitrogen is essential for vegetative growth, phosphorus supports root development and flower/fruit formation, while potassium contributes to overall plant health and stress resistance.\n" +
                "3.Herbicides are used to control weeds, which compete with coffee plants for nutrients, water, and sunlight. Weed control is crucial for ensuring optimal growth and yield of coffee plants. Herbicides are applied selectively to control specific types of weeds without harming the coffee plants. Common herbicides used in coffee cultivation include glyphosate, atrazine, diuron, and paraquat. Application methods may include spraying or applying directly to the soil.\n" +
                "4.Specific Herbicides and Fertilizers in Coffee Production:\n" +
                "•Glyphosate: Broad-spectrum herbicide effective against a wide range of weeds.\n" +
                "•Atrazine: Selective herbicide used for pre-emergence and post-emergence weed control.\n" +
                ".Diuron: Pre-emergence herbicide effective against certain grass and broadleaf weeds.\n" +
                "•Paraquat: Non-selective herbicide used for desiccation of weeds.\n" +
                "•Urea: Common nitrogen fertilizer used to promote vegetative growth.\n" +
                "•Triple Superphosphate: Phosphorus fertilizer to support root development and flowering.\n" +
                "•Muriate of Potash: Potassium fertilizer for overall plant health and stress resistance.\n\n" +
                "•Muriate of Potash: Potassium fertilizer for overall plant health and stress resistance."

        // ... (your entire document content)

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.document_dialog_layout)

        val documentTextView: TextView = dialog.findViewById(R.id.documentTextView)
        documentTextView.text = documentContent

        val closeButton: Button = dialog.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
